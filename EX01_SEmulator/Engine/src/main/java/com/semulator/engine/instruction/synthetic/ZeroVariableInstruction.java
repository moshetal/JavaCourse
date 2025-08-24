package com.semulator.engine.instruction.synthetic;

import com.semulator.engine.execution.ExecutionContext;
import com.semulator.engine.instruction.Instruction;
import com.semulator.engine.instruction.basic.DecreaseInstruction;
import com.semulator.engine.instruction.basic.JumpNotZeroInstruction;
import com.semulator.engine.instruction.basic.NeutralInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Synthetic instruction: V ← 0
 * Sets a variable to 0 by repeatedly decrementing it until it reaches 0.
 */
public class ZeroVariableInstruction extends SyntheticInstruction {
    
    public ZeroVariableInstruction(String variable, String label) {
        super("ZERO_VARIABLE", variable, label, 1);
    }
    
    @Override
    protected void executeInstruction(ExecutionContext context) {
        // Simple implementation: just set to 0
        context.setVariable(variable, 0);
    }
    
    @Override
    public List<Instruction> expand() {
        List<Instruction> expanded = new ArrayList<>();
        
        // Create a temporary label for the loop
        String tempLabel = "L" + System.identityHashCode(this);
        
        // Add the label to the first instruction if this instruction has a label
        String firstLabel = (label != null && !label.isEmpty()) ? label : null;
        
        // Loop: decrement until 0
        expanded.add(new DecreaseInstruction(variable, firstLabel));
        expanded.add(new JumpNotZeroInstruction(variable, null, tempLabel));
        
        // Set the temp label position
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
        return variable + " <- 0";
    }
}
