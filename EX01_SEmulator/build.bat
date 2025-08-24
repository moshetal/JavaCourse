@echo off
echo Building S-Emulator...

REM Create output directories
if not exist "Engine\out" mkdir Engine\out
if not exist "UI\out" mkdir UI\out

REM Compile Engine module in dependency order
echo Compiling Engine module...

REM Step 1: Compile basic classes first
echo Step 1: Compiling basic classes...
javac -d Engine\out Engine\src\main\java\com\semulator\engine\instruction\InstructionType.java
if %ERRORLEVEL% neq 0 goto :error

javac -d Engine\out Engine\src\main\java\com\semulator\engine\execution\ExecutionContext.java
if %ERRORLEVEL% neq 0 goto :error

javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\instruction\Instruction.java
if %ERRORLEVEL% neq 0 goto :error

javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\execution\ExecutionResult.java
if %ERRORLEVEL% neq 0 goto :error

REM Step 2: Compile basic instructions
echo Step 2: Compiling basic instructions...
javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\instruction\basic\BasicInstruction.java
if %ERRORLEVEL% neq 0 goto :error

javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\instruction\basic\IncreaseInstruction.java
if %ERRORLEVEL% neq 0 goto :error

javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\instruction\basic\DecreaseInstruction.java
if %ERRORLEVEL% neq 0 goto :error

javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\instruction\basic\JumpNotZeroInstruction.java
if %ERRORLEVEL% neq 0 goto :error

javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\instruction\basic\NeutralInstruction.java
if %ERRORLEVEL% neq 0 goto :error

REM Step 3: Compile synthetic instructions
echo Step 3: Compiling synthetic instructions...
javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\instruction\synthetic\SyntheticInstruction.java
if %ERRORLEVEL% neq 0 goto :error

javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\instruction\synthetic\ZeroVariableInstruction.java
if %ERRORLEVEL% neq 0 goto :error

javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\instruction\synthetic\GotoLabelInstruction.java
if %ERRORLEVEL% neq 0 goto :error

REM Step 4: Compile instruction factory
echo Step 4: Compiling instruction factory...
javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\instruction\InstructionFactory.java
if %ERRORLEVEL% neq 0 goto :error

REM Step 5: Compile program and parser
echo Step 5: Compiling program and parser...
javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\program\Program.java
if %ERRORLEVEL% neq 0 goto :error

javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\parser\ProgramParser.java
if %ERRORLEVEL% neq 0 goto :error

REM Step 6: Compile main engine
echo Step 6: Compiling main engine...
javac -d Engine\out -cp Engine\out Engine\src\main\java\com\semulator\engine\SemulatorEngine.java
if %ERRORLEVEL% neq 0 goto :error

REM Compile UI module
echo Compiling UI module...
javac -d UI\out -cp "UI\src\main\java;Engine\out" UI\src\main\java\com\semulator\ui\ConsoleUI.java
if %ERRORLEVEL% neq 0 goto :error

echo Compilation successful!
echo.
echo To run the application:
echo java -cp "UI\out;Engine\out" com.semulator.ui.ConsoleUI
echo.
goto :end

:error
echo Compilation failed!
pause
exit /b 1

:end
pause
