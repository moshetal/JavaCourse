package com.semulator.engine.execution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Execution context for S-Emulator programs.
 * Manages variables, labels, and execution state during program execution.
 */
public class ExecutionContext {
    
    private final Map<String, Integer> variables;
    private final Map<String, Integer> labelPositions;
    private int currentInstructionIndex;
    private int totalCycles;
    private boolean shouldExit;
    
    public ExecutionContext() {
        this.variables = new HashMap<>();
        this.labelPositions = new HashMap<>();
        this.currentInstructionIndex = 0;
        this.totalCycles = 0;
        this.shouldExit = false;
    }
    
    /**
     * Initialize input variables with provided values
     */
    public void initializeInputVariables(int[] inputs) {
        for (int i = 0; i < inputs.length; i++) {
            setVariable("x" + (i + 1), inputs[i]);
        }
    }
    
    /**
     * Get the value of a variable (returns 0 if not set)
     */
    public int getVariable(String variableName) {
        return variables.getOrDefault(variableName, 0);
    }
    
    /**
     * Set the value of a variable
     */
    public void setVariable(String variableName, int value) {
        variables.put(variableName, Math.max(0, value)); // Ensure non-negative
    }
    
    /**
     * Increment a variable by 1
     */
    public void incrementVariable(String variableName) {
        int currentValue = getVariable(variableName);
        setVariable(variableName, currentValue + 1);
    }
    
    /**
     * Decrement a variable by 1 (minimum 0)
     */
    public void decrementVariable(String variableName) {
        int currentValue = getVariable(variableName);
        setVariable(variableName, currentValue - 1);
    }
    
    /**
     * Set a label position
     */
    public void setLabelPosition(String label, int position) {
        labelPositions.put(label, position);
    }
    
    /**
     * Get the position of a label
     */
    public int getLabelPosition(String label) {
        Integer position = labelPositions.get(label);
        if (position == null) {
            throw new IllegalArgumentException("Label not found: " + label);
        }
        return position;
    }
    
    /**
     * Check if a label exists
     */
    public boolean hasLabel(String label) {
        return labelPositions.containsKey(label);
    }
    
    /**
     * Get current instruction index
     */
    public int getCurrentInstructionIndex() {
        return currentInstructionIndex;
    }
    
    /**
     * Set current instruction index
     */
    public void setCurrentInstructionIndex(int index) {
        this.currentInstructionIndex = index;
    }
    
    /**
     * Move to next instruction
     */
    public void nextInstruction() {
        currentInstructionIndex++;
    }
    
    /**
     * Jump to a specific instruction index
     */
    public void jumpTo(int index) {
        currentInstructionIndex = index;
    }
    
    /**
     * Add cycles to the total
     */
    public void addCycles(int cycles) {
        this.totalCycles += cycles;
    }
    
    /**
     * Get total cycles consumed
     */
    public int getTotalCycles() {
        return totalCycles;
    }
    
    /**
     * Check if program should exit
     */
    public boolean shouldExit() {
        return shouldExit;
    }
    
    /**
     * Set exit flag
     */
    public void setExit(boolean exit) {
        this.shouldExit = exit;
    }
    
    /**
     * Get the output value (y variable)
     */
    public int getOutput() {
        return getVariable("y");
    }
    
    /**
     * Get all variables that have been used
     */
    public Set<String> getUsedVariables() {
        return new TreeSet<>(variables.keySet());
    }
    
    /**
     * Get all input variables (x1, x2, ...)
     */
    public Set<String> getInputVariables() {
        Set<String> inputVars = new TreeSet<>();
        for (String var : variables.keySet()) {
            if (var.startsWith("x")) {
                inputVars.add(var);
            }
        }
        return inputVars;
    }
    
    /**
     * Get all work variables (z1, z2, ...)
     */
    public Set<String> getWorkVariables() {
        Set<String> workVars = new TreeSet<>();
        for (String var : variables.keySet()) {
            if (var.startsWith("z")) {
                workVars.add(var);
            }
        }
        return workVars;
    }
    
    /**
     * Reset the execution context
     */
    public void reset() {
        variables.clear();
        currentInstructionIndex = 0;
        totalCycles = 0;
        shouldExit = false;
    }
}
