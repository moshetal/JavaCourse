package com.semulator.engine.program;

import com.semulator.engine.execution.ExecutionContext;
import com.semulator.engine.execution.ExecutionResult;
import com.semulator.engine.instruction.Instruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents an S-Emulator program with instructions and execution capabilities.
 */
public class Program {
    
    private final String name;
    private final List<Instruction> instructions;
    private final Map<String, Integer> labelPositions;
    private final Set<String> inputVariables;
    private final Set<String> usedLabels;
    private int maxDegree;
    
    public Program(String name) {
        this.name = name;
        this.instructions = new ArrayList<>();
        this.labelPositions = new HashMap<>();
        this.inputVariables = new TreeSet<>();
        this.usedLabels = new TreeSet<>();
        this.maxDegree = 0;
    }
    
    /**
     * Add an instruction to the program
     */
    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
        
        // Track label position if instruction has a label
        if (instruction.getLabel() != null && !instruction.getLabel().isEmpty()) {
            labelPositions.put(instruction.getLabel(), instructions.size() - 1);
            usedLabels.add(instruction.getLabel());
        }
        
        // Track input variables
        if (instruction.getVariable() != null && instruction.getVariable().startsWith("x")) {
            inputVariables.add(instruction.getVariable());
        }
        
        // Update max degree
        maxDegree = Math.max(maxDegree, instruction.getDegree());
    }
    
    /**
     * Get the program name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get all instructions
     */
    public List<Instruction> getInstructions() {
        return new ArrayList<>(instructions);
    }
    
    /**
     * Get instruction at specific index
     */
    public Instruction getInstruction(int index) {
        if (index < 0 || index >= instructions.size()) {
            throw new IndexOutOfBoundsException("Instruction index out of bounds: " + index);
        }
        return instructions.get(index);
    }
    
    /**
     * Get the number of instructions
     */
    public int getInstructionCount() {
        return instructions.size();
    }
    
    /**
     * Get all input variables used in the program
     */
    public Set<String> getInputVariables() {
        return new TreeSet<>(inputVariables);
    }
    
    /**
     * Get all labels used in the program
     */
    public Set<String> getUsedLabels() {
        return new TreeSet<>(usedLabels);
    }
    
    /**
     * Get the maximum degree of any instruction in the program
     */
    public int getMaxDegree() {
        return maxDegree;
    }
    
    /**
     * Check if a label exists in the program
     */
    public boolean hasLabel(String label) {
        return labelPositions.containsKey(label);
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
     * Execute the program with given inputs
     */
    public ExecutionResult execute(int[] inputs) {
        ExecutionContext context = new ExecutionContext();
        
        // Initialize input variables
        context.initializeInputVariables(inputs);
        
        // Set up label positions
        for (Map.Entry<String, Integer> entry : labelPositions.entrySet()) {
            context.setLabelPosition(entry.getKey(), entry.getValue());
        }
        
        // Execute instructions
        while (context.getCurrentInstructionIndex() < instructions.size() && !context.shouldExit()) {
            Instruction instruction = instructions.get(context.getCurrentInstructionIndex());
            int currentIndex = context.getCurrentInstructionIndex();
            instruction.execute(context);
            
            // Only advance to next instruction if no jump occurred
            if (!context.shouldExit() && context.getCurrentInstructionIndex() == currentIndex) {
                context.nextInstruction();
            }
        }
        
        return new ExecutionResult(context);
    }
    
    /**
     * Expand the program to a specific degree
     */
    public Program expandToDegree(int targetDegree) {
        if (targetDegree < 0 || targetDegree > maxDegree) {
            throw new IllegalArgumentException("Invalid target degree: " + targetDegree);
        }
        
        Program expandedProgram = new Program(name + "_expanded_" + targetDegree);
        
        for (Instruction instruction : instructions) {
            if (instruction.getType() == com.semulator.engine.instruction.InstructionType.SYNTHETIC && 
                instruction.getDegree() > targetDegree) {
                // Expand synthetic instruction
                List<Instruction> expanded = ((com.semulator.engine.instruction.synthetic.SyntheticInstruction) instruction).expand();
                for (Instruction expandedInstruction : expanded) {
                    expandedInstruction.setParentInstruction(instruction);
                    expandedProgram.addInstruction(expandedInstruction);
                }
            } else {
                // Keep instruction as is
                expandedProgram.addInstruction(instruction);
            }
        }
        
        return expandedProgram;
    }
    
    /**
     * Check if the program is valid (all referenced labels exist)
     */
    public boolean isValid() {
        for (Instruction instruction : instructions) {
            if (instruction instanceof com.semulator.engine.instruction.basic.JumpNotZeroInstruction) {
                String jumpLabel = ((com.semulator.engine.instruction.basic.JumpNotZeroInstruction) instruction).getJumpLabel();
                if (!hasLabel(jumpLabel)) {
                    return false;
                }
            }
        }
        return true;
    }
}
