package com.semulator.engine.instruction.synthetic;

import com.semulator.engine.execution.ExecutionContext;
import com.semulator.engine.instruction.Instruction;
import com.semulator.engine.instruction.InstructionType;

import java.util.List;

/**
 * Abstract base class for synthetic instructions in the S-Emulator.
 * Synthetic instructions can be expanded to basic instructions.
 */
public abstract class SyntheticInstruction implements Instruction {
    
    protected final String name;
    protected final String variable;
    protected final String label;
    protected final int cycles;
    protected Instruction parentInstruction;
    
    protected SyntheticInstruction(String name, String variable, String label, int cycles) {
        this.name = name;
        this.variable = variable;
        this.label = label;
        this.cycles = cycles;
        this.parentInstruction = null;
    }
    
    @Override
    public InstructionType getType() {
        return InstructionType.SYNTHETIC;
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
    public void execute(ExecutionContext context) {
        context.addCycles(cycles);
        executeInstruction(context);
    }
    
    /**
     * Execute the specific instruction logic
     */
    protected abstract void executeInstruction(ExecutionContext context);
    
    /**
     * Expand this synthetic instruction to a list of basic instructions
     */
    public abstract List<Instruction> expand();
    
    /**
     * Get the degree of this instruction (how many expansions needed to reach basic instructions)
     */
    @Override
    public abstract int getDegree();
    
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
