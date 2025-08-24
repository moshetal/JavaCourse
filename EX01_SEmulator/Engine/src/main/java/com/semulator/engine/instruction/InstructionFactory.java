package com.semulator.engine.instruction;

import com.semulator.engine.instruction.basic.*;
import com.semulator.engine.instruction.synthetic.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for creating S-Emulator instructions from XML data.
 */
public class InstructionFactory {
    
    private static final Map<String, InstructionCreator> creators = new HashMap<>();
    
    static {
        // Basic instructions
        creators.put("NEUTRAL", (name, variable, label, args) -> 
            new NeutralInstruction(variable, label));
        creators.put("INCREASE", (name, variable, label, args) -> 
            new IncreaseInstruction(variable, label));
        creators.put("DECREASE", (name, variable, label, args) -> 
            new DecreaseInstruction(variable, label));
        creators.put("JUMP_NOT_ZERO", (name, variable, label, args) -> 
            new JumpNotZeroInstruction(variable, label, args.get("JNZLabel")));
        
        // Synthetic instructions
        creators.put("ZERO_VARIABLE", (name, variable, label, args) -> 
            new ZeroVariableInstruction(variable, label));
        creators.put("GOTO_LABEL", (name, variable, label, args) -> 
            new GotoLabelInstruction(variable, label, args.get("gotoLabel")));
        // Add more synthetic instructions as they are implemented
    }
    
    /**
     * Create an instruction from XML data
     */
    public static Instruction createInstruction(String name, String variable, String label, Map<String, String> arguments) {
        InstructionCreator creator = creators.get(name);
        if (creator == null) {
            throw new IllegalArgumentException("Unknown instruction: " + name);
        }
        return creator.create(name, variable, label, arguments);
    }
    
    /**
     * Check if an instruction name is supported
     */
    public static boolean isSupported(String name) {
        return creators.containsKey(name);
    }
    
    /**
     * Functional interface for instruction creation
     */
    @FunctionalInterface
    private interface InstructionCreator {
        Instruction create(String name, String variable, String label, Map<String, String> arguments);
    }
}
