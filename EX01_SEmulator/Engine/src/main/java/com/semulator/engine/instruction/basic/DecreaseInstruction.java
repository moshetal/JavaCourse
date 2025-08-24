package com.semulator.engine.instruction.basic;

import com.semulator.engine.execution.ExecutionContext;

/**
 * Basic instruction: V ← V - 1
 * Decrements the value of a variable by 1 (minimum 0).
 */
public class DecreaseInstruction extends BasicInstruction {
    
    public DecreaseInstruction(String variable, String label) {
        super("DECREASE", variable, label, 1);
    }
    
    @Override
    protected void executeInstruction(ExecutionContext context) {
        context.decrementVariable(variable);
    }
    
    @Override
    protected String getInstructionDisplay() {
        return variable + " <- " + variable + " - 1";
    }
}
