package com.semulator.engine.instruction;

/**
 * Enum representing the type of instruction in the S-Emulator.
 */
public enum InstructionType {
    BASIC("basic"),
    SYNTHETIC("synthetic");
    
    private final String xmlValue;
    
    InstructionType(String xmlValue) {
        this.xmlValue = xmlValue;
    }
    
    public String getXmlValue() {
        return xmlValue;
    }
    
    public static InstructionType fromXmlValue(String xmlValue) {
        for (InstructionType type : values()) {
            if (type.xmlValue.equals(xmlValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + xmlValue);
    }
}
