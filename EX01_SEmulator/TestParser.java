import com.semulator.engine.parser.ProgramParser;
import com.semulator.engine.program.Program;

public class TestParser {
    public static void main(String[] args) {
        try {
            System.out.println("Testing XML parser...");
            Program program = ProgramParser.parseProgram("C:\\Users\\moshe.DIRECT-EX\\Downloads\\badic.xml");
            System.out.println("Program loaded successfully: " + program.getName());
            System.out.println("Number of instructions: " + program.getInstructionCount());
            System.out.println("Max degree: " + program.getMaxDegree());
            
            // Test execution
            System.out.println("\nTesting execution with x1=3...");
            int[] inputs = {3};
            var result = program.execute(inputs);
            System.out.println("Execution completed!");
            System.out.println("Output (y): " + result.getOutput());
            System.out.println("Total cycles: " + result.getTotalCycles());
            System.out.println("Used variables: " + result.getUsedVariables());
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
