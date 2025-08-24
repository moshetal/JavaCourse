package com.semulator.engine.execution;

import java.util.Map;
import java.util.Set;

/**
 * Represents the result of executing an S-Emulator program.
 */
public class ExecutionResult {
    
    private final int output;
    private final int totalCycles;
    private final Map<String, Integer> finalVariableValues;
    private final Set<String> usedVariables;
    
    public ExecutionResult(ExecutionContext context) {
        this.output = context.getOutput();
        this.totalCycles = context.getTotalCycles();
        this.usedVariables = context.getUsedVariables();
        
        // Create a map of final variable values
        this.finalVariableValues = new java.util.HashMap<>();
        for (String variable : usedVariables) {
            finalVariableValues.put(variable, context.getVariable(variable));
        }
    }
    
    /**
     * Get the output value (y variable)
     */
    public int getOutput() {
        return output;
    }
    
    /**
     * Get the total cycles consumed
     */
    public int getTotalCycles() {
        return totalCycles;
    }
    
    /**
     * Get all variables that were used during execution
     */
    public Set<String> getUsedVariables() {
        return usedVariables;
    }
    
    /**
     * Get the final value of a specific variable
     */
    public int getVariableValue(String variable) {
        return finalVariableValues.getOrDefault(variable, 0);
    }
    
    /**
     * Get all final variable values
     */
    public Map<String, Integer> getFinalVariableValues() {
        return new java.util.HashMap<>(finalVariableValues);
    }
}
