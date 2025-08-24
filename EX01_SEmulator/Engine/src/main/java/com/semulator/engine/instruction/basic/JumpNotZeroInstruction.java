package com.semulator.engine.instruction.basic;

import com.semulator.engine.execution.ExecutionContext;

/**
 * Basic instruction: IF V != 0 GOTO L
 * Jumps to a label if the variable is not zero.
 */
public class JumpNotZeroInstruction extends BasicInstruction {
    
    private final String jumpLabel;
    
    public JumpNotZeroInstruction(String variable, String label, String jumpLabel) {
        super("JUMP_NOT_ZERO", variable, label, 2);
        this.jumpLabel = jumpLabel;
    }
    
    @Override
    protected void executeInstruction(ExecutionContext context) {
        int value = context.getVariable(variable);
        if (value != 0) {
            int jumpPosition = context.getLabelPosition(jumpLabel);
            context.jumpTo(jumpPosition);
        }
    }
    
    @Override
    protected String getInstructionDisplay() {
        return "IF " + variable + " != 0 GOTO " + jumpLabel;
    }
    
    public String getJumpLabel() {
        return jumpLabel;
    }
}
