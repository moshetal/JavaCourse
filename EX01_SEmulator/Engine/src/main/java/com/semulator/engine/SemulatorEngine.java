package com.semulator.engine;

import com.semulator.engine.execution.ExecutionResult;
import com.semulator.engine.parser.ProgramParser;
import com.semulator.engine.program.Program;

import java.util.ArrayList;
import java.util.List;

/**
 * Main engine class for the S-Emulator system.
 * Manages program loading, execution, and history.
 */
public class SemulatorEngine {
    
    private Program currentProgram;
    private final List<ExecutionHistory> executionHistory;
    
    public SemulatorEngine() {
        this.currentProgram = null;
        this.executionHistory = new ArrayList<>();
    }
    
    /**
     * Load a program from an XML file
     */
    public LoadResult loadProgram(String filePath) {
        try {
            Program program = ProgramParser.parseProgram(filePath);
            this.currentProgram = program;
            return new LoadResult(true, "Program loaded successfully: " + program.getName());
        } catch (Exception e) {
            return new LoadResult(false, "Failed to load program: " + e.getMessage());
        }
    }
    
    /**
     * Get the currently loaded program
     */
    public Program getCurrentProgram() {
        return currentProgram;
    }
    
    /**
     * Check if a program is currently loaded
     */
    public boolean hasProgram() {
        return currentProgram != null;
    }
    
    /**
     * Execute the current program with given inputs
     */
    public ExecutionResult executeProgram(int[] inputs, int expansionDegree) {
        if (currentProgram == null) {
            throw new IllegalStateException("No program loaded");
        }
        
        Program programToExecute = currentProgram;
        if (expansionDegree > 0) {
            programToExecute = currentProgram.expandToDegree(expansionDegree);
        }
        
        ExecutionResult result = programToExecute.execute(inputs);
        
        // Record execution history
        executionHistory.add(new ExecutionHistory(
            executionHistory.size() + 1,
            expansionDegree,
            inputs,
            result.getOutput(),
            result.getTotalCycles()
        ));
        
        return result;
    }
    
    /**
     * Get execution history
     */
    public List<ExecutionHistory> getExecutionHistory() {
        return new ArrayList<>(executionHistory);
    }
    
    /**
     * Clear execution history
     */
    public void clearHistory() {
        executionHistory.clear();
    }
    
    /**
     * Result of loading a program
     */
    public static class LoadResult {
        private final boolean success;
        private final String message;
        
        public LoadResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    /**
     * Record of program execution
     */
    public static class ExecutionHistory {
        private final int runNumber;
        private final int expansionDegree;
        private final int[] inputs;
        private final int output;
        private final int cycles;
        
        public ExecutionHistory(int runNumber, int expansionDegree, int[] inputs, int output, int cycles) {
            this.runNumber = runNumber;
            this.expansionDegree = expansionDegree;
            this.inputs = inputs.clone();
            this.output = output;
            this.cycles = cycles;
        }
        
        public int getRunNumber() {
            return runNumber;
        }
        
        public int getExpansionDegree() {
            return expansionDegree;
        }
        
        public int[] getInputs() {
            return inputs.clone();
        }
        
        public int getOutput() {
            return output;
        }
        
        public int getCycles() {
            return cycles;
        }
    }
}
