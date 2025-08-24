package com.semulator.engine.parser;

import com.semulator.engine.instruction.Instruction;
import com.semulator.engine.instruction.InstructionFactory;
import com.semulator.engine.program.Program;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser for S-Emulator program XML files.
 */
public class ProgramParser {
    
    /**
     * Parse an XML file and create a Program object
     */
    public static Program parseProgram(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + filePath);
        }
        
        if (!filePath.toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("File must be an XML file: " + filePath);
        }
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        
        Element rootElement = document.getDocumentElement();
        if (!"S-Program".equals(rootElement.getTagName())) {
            throw new IllegalArgumentException("Root element must be 'S-Program'");
        }
        
        String programName = rootElement.getAttribute("name");
        if (programName == null || programName.trim().isEmpty()) {
            throw new IllegalArgumentException("Program name is required");
        }
        
        Program program = new Program(programName.trim());
        
        // Parse instructions
        NodeList instructionElements = rootElement.getElementsByTagName("S-Instruction");
        for (int i = 0; i < instructionElements.getLength(); i++) {
            Element instructionElement = (Element) instructionElements.item(i);
            Instruction instruction = parseInstruction(instructionElement);
            program.addInstruction(instruction);
        }
        
        // Validate program
        if (!program.isValid()) {
            throw new IllegalArgumentException("Program is invalid: referenced labels do not exist");
        }
        
        return program;
    }
    
    /**
     * Parse a single instruction element
     */
    private static Instruction parseInstruction(Element element) throws Exception {
        String type = element.getAttribute("type");
        String name = element.getAttribute("name");
        
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Instruction type is required");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Instruction name is required");
        }
        
        // Parse variable
        String variable = "";
        NodeList variableElements = element.getElementsByTagName("S-Variable");
        if (variableElements.getLength() > 0) {
            variable = variableElements.item(0).getTextContent().trim();
        }
        
        // Parse label
        String label = null;
        NodeList labelElements = element.getElementsByTagName("S-Label");
        if (labelElements.getLength() > 0) {
            label = labelElements.item(0).getTextContent().trim();
        }
        
        // Parse arguments
        Map<String, String> arguments = new HashMap<>();
        NodeList argumentElements = element.getElementsByTagName("S-Instruction-Argument");
        for (int i = 0; i < argumentElements.getLength(); i++) {
            Element argElement = (Element) argumentElements.item(i);
            String argName = argElement.getAttribute("name");
            String argValue = argElement.getAttribute("value");
            if (argName != null && argValue != null) {
                arguments.put(argName.trim(), argValue.trim());
            }
        }
        
        return InstructionFactory.createInstruction(name.trim(), variable, label, arguments);
    }
}
