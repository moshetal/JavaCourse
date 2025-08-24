package com.semulator.ui;

import com.semulator.engine.SemulatorEngine;
import com.semulator.engine.execution.ExecutionResult;
import com.semulator.engine.program.Program;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Console user interface for the S-Emulator.
 * Handles user input/output and menu navigation.
 */
public class ConsoleUI {
    
    private final SemulatorEngine engine;
    private final Scanner scanner;
    private boolean running;
    
    public ConsoleUI() {
        this.engine = new SemulatorEngine();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    /**
     * Start the console application
     */
    public void start() {
        System.out.println("Welcome to S-Emulator Console Application");
        System.out.println("==========================================");
        
        while (running) {
            displayMenu();
            int choice = getMenuChoice();
            processMenuChoice(choice);
        }
        
        System.out.println("Thank you for using S-Emulator!");
        scanner.close();
    }
    
    /**
     * Display the main menu
     */
    private void displayMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Load XML file");
        System.out.println("2. Display program");
        System.out.println("3. Expand program");
        System.out.println("4. Run program");
        System.out.println("5. Show execution history");
        System.out.println("6. Exit");
        System.out.print("Please select an option (1-6): ");
    }
    
    /**
     * Get user's menu choice
     */
    private int getMenuChoice() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 6) {
                    return choice;
                } else {
                    System.out.println("Please enter a number between 1 and 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Process the user's menu choice
     */
    private void processMenuChoice(int choice) {
        switch (choice) {
            case 1:
                loadXmlFile();
                break;
            case 2:
                displayProgram();
                break;
            case 3:
                expandProgram();
                break;
            case 4:
                runProgram();
                break;
            case 5:
                showExecutionHistory();
                break;
            case 6:
                running = false;
                break;
        }
    }
    
    /**
     * Load an XML file
     */
    private void loadXmlFile() {
        System.out.print("Enter the full path to the XML file: ");
        String filePath = scanner.nextLine().trim();
        
        SemulatorEngine.LoadResult result = engine.loadProgram(filePath);
        System.out.println(result.getMessage());
    }
    
    /**
     * Display the current program
     */
    private void displayProgram() {
        if (!engine.hasProgram()) {
            System.out.println("No program is currently loaded. Please load a program first.");
            return;
        }
        
        Program program = engine.getCurrentProgram();
        System.out.println("\nProgram: " + program.getName());
        
        // Display input variables
        Set<String> inputVars = program.getInputVariables();
        if (!inputVars.isEmpty()) {
            System.out.println("Input variables: " + String.join(", ", inputVars));
        }
        
        // Display labels
        Set<String> labels = program.getUsedLabels();
        if (!labels.isEmpty()) {
            System.out.println("Labels: " + String.join(", ", labels));
        }
        
        // Display instructions
        System.out.println("\nInstructions:");
        List<com.semulator.engine.instruction.Instruction> instructions = program.getInstructions();
        for (int i = 0; i < instructions.size(); i++) {
            com.semulator.engine.instruction.Instruction instruction = instructions.get(i);
            String type = instruction.getType() == com.semulator.engine.instruction.InstructionType.BASIC ? "B" : "S";
            System.out.printf("#%d (%s) %s (%d)\n", 
                i + 1, type, instruction.getDisplayString(), instruction.getCycles());
        }
    }
    
    /**
     * Expand the program
     */
    private void expandProgram() {
        if (!engine.hasProgram()) {
            System.out.println("No program is currently loaded. Please load a program first.");
            return;
        }
        
        Program program = engine.getCurrentProgram();
        int maxDegree = program.getMaxDegree();
        
        System.out.println("Maximum degree of the program: " + maxDegree);
        System.out.print("Enter the degree to expand to (0-" + maxDegree + "): ");
        
        int targetDegree = getValidDegree(maxDegree);
        Program expandedProgram = program.expandToDegree(targetDegree);
        
        System.out.println("\nExpanded program:");
        List<com.semulator.engine.instruction.Instruction> instructions = expandedProgram.getInstructions();
        for (int i = 0; i < instructions.size(); i++) {
            com.semulator.engine.instruction.Instruction instruction = instructions.get(i);
            String type = instruction.getType() == com.semulator.engine.instruction.InstructionType.BASIC ? "B" : "S";
            String display = instruction.getDisplayString();
            
            // Add parent instruction info if available
            if (instruction.getParentInstruction() != null) {
                display += " <<< " + instruction.getParentInstruction().getDisplayString();
            }
            
            System.out.printf("#%d (%s) %s (%d)\n", 
                i + 1, type, display, instruction.getCycles());
        }
    }
    
    /**
     * Run the program
     */
    private void runProgram() {
        if (!engine.hasProgram()) {
            System.out.println("No program is currently loaded. Please load a program first.");
            return;
        }
        
        Program program = engine.getCurrentProgram();
        int maxDegree = program.getMaxDegree();
        
        System.out.println("Maximum degree of the program: " + maxDegree);
        System.out.print("Enter the degree to run at (0-" + maxDegree + "): ");
        
        int expansionDegree = getValidDegree(maxDegree);
        
        // Get input variables
        Set<String> inputVars = program.getInputVariables();
        if (inputVars.isEmpty()) {
            System.out.println("No input variables required.");
            int[] inputs = new int[0];
            executeAndDisplayResults(inputs, expansionDegree);
        } else {
            System.out.println("Input variables: " + String.join(", ", inputVars));
            System.out.print("Enter input values (comma-separated): ");
            
            int[] inputs = getInputValues();
            executeAndDisplayResults(inputs, expansionDegree);
        }
    }
    
    /**
     * Execute program and display results
     */
    private void executeAndDisplayResults(int[] inputs, int expansionDegree) {
        try {
            ExecutionResult result = engine.executeProgram(inputs, expansionDegree);
            
            System.out.println("\nExecution completed successfully!");
            System.out.println("Output (y): " + result.getOutput());
            System.out.println("Total cycles: " + result.getTotalCycles());
            
            // Display final variable values
            System.out.println("\nFinal variable values:");
            Set<String> usedVars = result.getUsedVariables();
            for (String var : usedVars) {
                System.out.println(var + " = " + result.getVariableValue(var));
            }
            
        } catch (Exception e) {
            System.out.println("Error during execution: " + e.getMessage());
        }
    }
    
    /**
     * Show execution history
     */
    private void showExecutionHistory() {
        if (!engine.hasProgram()) {
            System.out.println("No program is currently loaded. Please load a program first.");
            return;
        }
        
        List<SemulatorEngine.ExecutionHistory> history = engine.getExecutionHistory();
        if (history.isEmpty()) {
            System.out.println("No execution history available.");
            return;
        }
        
        System.out.println("\nExecution History:");
        System.out.println("==================");
        
        for (SemulatorEngine.ExecutionHistory record : history) {
            System.out.printf("Run #%d:\n", record.getRunNumber());
            System.out.printf("  Expansion degree: %d\n", record.getExpansionDegree());
            System.out.printf("  Inputs: %s\n", formatInputs(record.getInputs()));
            System.out.printf("  Output: %d\n", record.getOutput());
            System.out.printf("  Cycles: %d\n", record.getCycles());
            System.out.println();
        }
    }
    
    /**
     * Get valid degree from user input
     */
    private int getValidDegree(int maxDegree) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int degree = Integer.parseInt(input);
                if (degree >= 0 && degree <= maxDegree) {
                    return degree;
                } else {
                    System.out.printf("Please enter a number between 0 and %d.\n", maxDegree);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Get input values from user
     */
    private int[] getInputValues() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                String[] parts = input.split(",");
                int[] values = new int[parts.length];
                
                for (int i = 0; i < parts.length; i++) {
                    values[i] = Integer.parseInt(parts[i].trim());
                }
                
                return values;
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid numbers separated by commas.");
            }
        }
    }
    
    /**
     * Format input array for display
     */
    private String formatInputs(int[] inputs) {
        if (inputs.length == 0) {
            return "none";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inputs.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(inputs[i]);
        }
        return sb.toString();
    }
    
    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}
