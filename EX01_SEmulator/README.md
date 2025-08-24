# S-Emulator Console Application

## Overview

S-Emulator is a console application that implements an emulator for the abstract S programming language. The S language is a theoretical computational model with only 4 basic instructions that can express any computable function.

## Project Structure

The project is organized into two main modules:

### Engine Module (`Engine/`)
Contains the core S-Emulator engine with the following components:

- **Instruction System**: Basic and synthetic instruction implementations
- **Execution Engine**: Program execution and variable management
- **XML Parser**: Parses S-Emulator program files
- **Program Management**: Program representation and expansion

### UI Module (`UI/`)
Contains the console user interface:

- **ConsoleUI**: Main console application with menu system
- **User Input Handling**: Input validation and error handling
- **Output Formatting**: Program display and execution results

## Key Features

### Basic Instructions (4)
1. `INCREASE` - `V ← V + 1` (1 cycle)
2. `DECREASE` - `V ← V - 1` (1 cycle) 
3. `JUMP_NOT_ZERO` - `IF V != 0 GOTO L` (2 cycles)
4. `NEUTRAL` - `V ← V` (0 cycles)

### Synthetic Instructions (7 for Exercise 1)
1. `ZERO_VARIABLE` - `V ← 0` (1 cycle)
2. `GOTO_LABEL` - `GOTO L` (1 cycle)
3. `ASSIGNMENT` - `V' ← V` (4 cycles)
4. `CONSTANT_ASSIGNMENT` - `V ← K` (2 cycles)
5. `JUMP_ZERO` - `IF V = 0 GOTO L` (2 cycles)
6. `JUMP_EQUAL_CONSTANT` - `IF V = K GOTO L` (2 cycles)
7. `JUMP_EQUAL_VARIABLE` - `IF V = V' GOTO L` (2 cycles)

### Console Commands
1. **Load XML file** - Load a program from an XML file
2. **Display program** - Show the current program structure
3. **Expand program** - Expand synthetic instructions to basic instructions
4. **Run program** - Execute the program with user inputs
5. **Show execution history** - Display previous execution records
6. **Exit** - Exit the application

## Building and Running

### Prerequisites
- Java 21 or higher
- Windows operating system (for batch files)

### Build
```bash
build.bat
```

### Run
```bash
run.bat
```

### Manual Compilation
```bash
# Compile Engine module
javac -d Engine/out -cp "Engine/src/main/java" Engine/src/main/java/com/semulator/engine/**/*.java

# Compile UI module
javac -d UI/out -cp "UI/src/main/java;Engine/out" UI/src/main/java/com/semulator/ui/*.java

# Run application
java -cp "UI/out;Engine/out" com.semulator.ui.ConsoleUI
```

## XML File Format

S-Emulator programs are defined in XML files with the following structure:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<S-Program name="Program Name">
    <S-Instructions>
        <S-Instruction type="basic" name="INCREASE">
            <S-Variable>x1</S-Variable>
            <S-Label>L1</S-Label>
        </S-Instruction>
        <S-Instruction type="synthetic" name="ZERO_VARIABLE">
            <S-Variable>y</S-Variable>
        </S-Instruction>
    </S-Instructions>
</S-Program>
```

## Architecture Design

### Core Classes

1. **Instruction Interface**: Base interface for all instructions
2. **BasicInstruction**: Abstract base for basic instructions
3. **SyntheticInstruction**: Abstract base for synthetic instructions
4. **ExecutionContext**: Manages execution state and variables
5. **Program**: Represents a complete S-Emulator program
6. **SemulatorEngine**: Main engine coordinating all operations
7. **ProgramParser**: XML parser for program files
8. **ConsoleUI**: Console user interface

### Design Patterns

- **Factory Pattern**: InstructionFactory creates instruction instances
- **Strategy Pattern**: Different instruction types implement different execution strategies
- **Template Method**: Basic and synthetic instruction base classes define common behavior
- **Command Pattern**: Instructions encapsulate execution logic

## Error Handling

The system includes comprehensive error handling:

- **Input Validation**: All user inputs are validated
- **File Validation**: XML files are checked for proper format
- **Program Validation**: Programs are validated for label references
- **Execution Error Handling**: Runtime errors are caught and reported

## Testing

A test XML file (`test.xml`) is included for basic testing. The system can be tested by:

1. Loading the test file
2. Displaying the program
3. Running with various inputs
4. Testing expansion functionality

## Future Enhancements

For Exercise 2, the following features will be added:
- Additional synthetic instructions (QUOTE, JUMP_EQUAL_FUNCTION)
- JavaFX graphical user interface
- Enhanced program visualization

For Exercise 3, the following features will be added:
- Client-server architecture
- Remote program execution
- Multi-user support

## Technical Notes

- All variables are non-negative integers
- Labels are case-sensitive
- Program expansion preserves instruction relationships
- Execution history is maintained per session
- 1-based indexing is used for user interfaces

## Author

This S-Emulator implementation was created as part of a Java application development course project.
