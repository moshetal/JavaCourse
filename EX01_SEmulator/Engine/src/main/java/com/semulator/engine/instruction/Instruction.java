package com.semulator.engine.instruction;

/**
 * Base interface for all S-Emulator instructions.
 * Defines the contract that all instructions must implement.
 */
public interface Instruction {
    
    /**
     * Get the instruction type (BASIC or SYNTHETIC)
     */
    InstructionType getType();
    
    /**
     * Get the instruction name
     */
    String getName();
    
    /**
     * Get the variable this instruction operates on (can be null)
     */
    String getVariable();
    
    /**
     * Get the label associated with this instruction (can be null)
     */
    String getLabel();
    
    /**
     * Get the number of cycles this instruction takes to execute
     */
    int getCycles();
    
    /**
     * Execute this instruction in the given execution context
     */
    void execute(com.semulator.engine.execution.ExecutionContext context);
    
    /**
     * Get the display representation of this instruction for user output
     */
    String getDisplayString();
    
    /**
     * Get the instruction that created this instruction (for expansion tracking)
     */
    Instruction getParentInstruction();
    
    /**
     * Set the instruction that created this instruction
     */
    void setParentInstruction(Instruction parent);
    
    /**
     * Get the degree of this instruction (0 for basic, >0 for synthetic)
     */
    int getDegree();
}
