package com.semulator.engine.instruction.basic;

import com.semulator.engine.execution.ExecutionContext;
import com.semulator.engine.instruction.Instruction;
import com.semulator.engine.instruction.InstructionType;

/**
 * Abstract base class for basic instructions in the S-Emulator.
 * Basic instructions are atomic and cannot be expanded further.
 */
public abstract class BasicInstruction implements Instruction {
    
    protected final String name;
    protected final String variable;
    protected final String label;
    protected final int cycles;
    protected Instruction parentInstruction;
    
    protected BasicInstruction(String name, String variable, String label, int cycles) {
        this.name = name;
        this.variable = variable;
        this.label = label;
        this.cycles = cycles;
        this.parentInstruction = null;
    }
    
    @Override
    public InstructionType getType() {
        return InstructionType.BASIC;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getVariable() {
        return variable;
    }
    
    @Override
    public String getLabel() {
        return label;
    }
    
    @Override
    public int getCycles() {
        return cycles;
    }
    
    @Override
    public Instruction getParentInstruction() {
        return parentInstruction;
    }
    
    @Override
    public void setParentInstruction(Instruction parent) {
        this.parentInstruction = parent;
    }
    
    @Override
    public int getDegree() {
        return 0; // Basic instructions have degree 0
    }
    
    @Override
    public void execute(ExecutionContext context) {
        context.addCycles(cycles);
        executeInstruction(context);
    }
    
    /**
     * Execute the specific instruction logic
     */
    protected abstract void executeInstruction(ExecutionContext context);
    
    @Override
    public String getDisplayString() {
        StringBuilder sb = new StringBuilder();
        
        // Add label if present
        if (label != null && !label.isEmpty()) {
            sb.append(String.format("[%s]", label));
        } else {
            sb.append("[     ]");
        }
        
        // Add instruction representation
        sb.append(" ").append(getInstructionDisplay());
        
        // Add cycles
        sb.append(" (").append(cycles).append(")");
        
        return sb.toString();
    }
    
    /**
     * Get the instruction-specific display string
     */
    protected abstract String getInstructionDisplay();
}
