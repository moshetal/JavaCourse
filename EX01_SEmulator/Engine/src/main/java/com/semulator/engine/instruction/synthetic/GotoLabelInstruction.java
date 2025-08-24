package com.semulator.engine.instruction.synthetic;

import com.semulator.engine.execution.ExecutionContext;
import com.semulator.engine.instruction.Instruction;
import com.semulator.engine.instruction.basic.IncreaseInstruction;
import com.semulator.engine.instruction.basic.JumpNotZeroInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Synthetic instruction: GOTO L
 * Performs an unconditional jump to a label.
 */
public class GotoLabelInstruction extends SyntheticInstruction {
    
    private final String gotoLabel;
    
    public GotoLabelInstruction(String variable, String label, String gotoLabel) {
        super("GOTO_LABEL", variable, label, 1);
        this.gotoLabel = gotoLabel;
    }
    
    @Override
    protected void executeInstruction(ExecutionContext context) {
        // Simple implementation: just jump to the label
        int jumpPosition = context.getLabelPosition(gotoLabel);
        context.jumpTo(jumpPosition);
    }
    
    @Override
    public List<Instruction> expand() {
        List<Instruction> expanded = new ArrayList<>();
        
        // Create a temporary work variable
        String tempVar = "z" + System.identityHashCode(this);
        
        // Add the label to the first instruction if this instruction has a label
        String firstLabel = (label != null && !label.isEmpty()) ? label : null;
        
        // Set temp variable to 1 (guaranteed non-zero)
        expanded.add(new IncreaseInstruction(tempVar, firstLabel));
        
        // Jump to the target label (temp variable is guaranteed to be non-zero)
        expanded.add(new JumpNotZeroInstruction(tempVar, null, gotoLabel));
        
        // Set parent instructions
        expanded.get(0).setParentInstruction(this);
        expanded.get(1).setParentInstruction(this);
        
        return expanded;
    }
    
    @Override
    public int getDegree() {
        return 1; // This instruction expands to basic instructions
    }
    
    @Override
    protected String getInstructionDisplay() {
        return "GOTO " + gotoLabel;
    }
    
    public String getGotoLabel() {
        return gotoLabel;
    }
}
