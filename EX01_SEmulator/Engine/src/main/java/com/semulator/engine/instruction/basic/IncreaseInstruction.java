package com.semulator.engine.instruction.basic;

import com.semulator.engine.execution.ExecutionContext;

/**
 * Basic instruction: V ← V + 1
 * Increments the value of a variable by 1.
 */
public class IncreaseInstruction extends BasicInstruction {
    
    public IncreaseInstruction(String variable, String label) {
        super("INCREASE", variable, label, 1);
    }
    
    @Override
    protected void executeInstruction(ExecutionContext context) {
        context.incrementVariable(variable);
    }
    
    @Override
    protected String getInstructionDisplay() {
        return variable + " <- " + variable + " + 1";
    }
}
