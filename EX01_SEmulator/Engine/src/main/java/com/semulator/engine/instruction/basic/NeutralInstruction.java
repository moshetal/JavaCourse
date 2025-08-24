package com.semulator.engine.instruction.basic;

import com.semulator.engine.execution.ExecutionContext;

/**
 * Basic instruction: V ← V
 * No-operation instruction that does nothing.
 */
public class NeutralInstruction extends BasicInstruction {
    
    public NeutralInstruction(String variable, String label) {
        super("NEUTRAL", variable, label, 0);
    }
    
    @Override
    protected void executeInstruction(ExecutionContext context) {
        // Do nothing - this is a no-op instruction
    }
    
    @Override
    protected String getInstructionDisplay() {
        return variable + " <- " + variable;
    }
}
