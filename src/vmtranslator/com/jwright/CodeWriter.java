package com.jwright;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeWriter {
    List<String[]> outputQueue;
    FileWriter fw;
    int lineJump;

    public CodeWriter() {
        outputQueue = new ArrayList<>();
        lineJump = 0;
    }
    
    void setFileName(String fileName) {
        
    }
    
    void writeInit() {

    }

    void storeArithmetic(String currentCommand) {
        String[] newArithmetic = new String[1];
        newArithmetic[0] = currentCommand;
        outputQueue.add(newArithmetic);
    }

    void storePushPop(String arg1, String arg2, int arg3) {
        String[] newPushPop = new String[3];
        newPushPop[0] = arg1;
        newPushPop[1] = arg2;
        newPushPop[2] = "" + arg3;
        outputQueue.add(newPushPop);
    }

    void storeLabel(String arg1, String arg2) {
        String[] newLabel = new String[2];
        newLabel[0] = arg1;
        newLabel[1] = arg2;
        outputQueue.add(newLabel);
    }
    
    void storeGoTo(String arg1, String arg2) {
        String[] newGoTo = new String[2];
        newGoTo[0] = arg1;
        newGoTo[1] = arg2;
        outputQueue.add(newGoTo);
    }

    void storeIf(String arg1, String arg2) {
        String[] newIf = new String[2];
        newIf[0] = arg1;
        newIf[1] = arg2;
        outputQueue.add(newIf);
    }

    void storeFunction(String arg1, String arg2, int arg3) {
        String[] newFunction = new String[3];
        newFunction[0] = arg1;
        newFunction[1] = arg2;
        newFunction[2] = "" + arg3;
        outputQueue.add(newFunction);
    }

    void storeCall(String arg1, String arg2, int arg3) {
        String[] newCall = new String[3];
        newCall[0] = arg1;
        newCall[1] = arg2;
        newCall[2] = "" + arg3;
        outputQueue.add(newCall);
    }

    void storeReturn(String arg1) {
        String[] newReturn = new String[1];
        newReturn[0] = arg1;
    }

    String writeArithmetic(String[] arithmeticCommand) throws IOException {
        // needs to translate each VM command into component assembly commands
        String writeCommand = "// " + arithmeticCommand[0] + "\n";
        if (arithmeticCommand[0].equals("add")) {
            writeCommand += """
            @SP
            AM=M-1
            D=M
            A=A-1
            M=M+D
            """;
        }
        else if (arithmeticCommand[0].equals("sub")) {
            writeCommand += """
            @SP
            AM=M-1
            D=M
            A=A-1
            M=M-D
            """;
        }
        else if (arithmeticCommand[0].equals("eq")) {
            writeCommand += "@SP\n" +
            "AM=M-1\n" + 
            "D=M\n" + 
            "A=A-1\n" + 
            "D=M-D\n" + 
            "@FALSE" + lineJump + "\n" + 
            "D;JNE\n" + 
            "@SP\n" + 
            "A=M-1\n" + 
            "M=-1\n" + 
            "@CONTINUE" + lineJump + "\n" + 
            "0;JMP\n" + 
            "(FALSE" + lineJump + ")\n" + 
            "@SP\n" + 
            "A=M-1\n" + 
            "M=0\n" + 
            "(CONTINUE" + lineJump + ")\n";
            lineJump++;
        }
        else if (arithmeticCommand[0].equals("lt")) {
            writeCommand += "@SP\n" +
            "AM=M-1\n" + 
            "D=M\n" + 
            "A=A-1\n" + 
            "D=M-D\n" + 
            "@FALSE" + lineJump + "\n" + 
            "D;JGE\n" + 
            "@SP\n" + 
            "A=M-1\n" + 
            "M=-1\n" + 
            "@CONTINUE" + lineJump + "\n" + 
            "0;JMP\n" + 
            "(FALSE" + lineJump + ")\n" + 
            "@SP\n" + 
            "A=M-1\n" + 
            "M=0\n" + 
            "(CONTINUE" + lineJump + ")\n";
            lineJump++;
        }
        else if (arithmeticCommand[0].equals("gt")) {
            writeCommand += "@SP\n" +
            "AM=M-1\n" + 
            "D=M\n" + 
            "A=A-1\n" + 
            "D=M-D\n" + 
            "@FALSE" + lineJump + "\n" + 
            "D;JLE\n" + 
            "@SP\n" + 
            "A=M-1\n" + 
            "M=-1\n" + 
            "@CONTINUE" + lineJump + "\n" + 
            "0;JMP\n" + 
            "(FALSE" + lineJump + ")\n" + 
            "@SP\n" + 
            "A=M-1\n" + 
            "M=0\n" + 
            "(CONTINUE" + lineJump + ")\n";
            lineJump++;
        }
        else if (arithmeticCommand[0].equals("and")) {
            writeCommand += """
            @SP
            AM=M-1
            D=M
            A=A-1
            M=M&D
            """;
        }
        else if (arithmeticCommand[0].equals("neg")) {
            writeCommand += """
            @SP
            A=M-1
            D=0
            M=D-M
            """;
        }
        else if (arithmeticCommand[0].equals("not")) {
            writeCommand +=  """
            @SP
            A=M-1
            M=!M
            """;
        }
        else if (arithmeticCommand[0].equals("or")) {
            writeCommand +=  """
            @SP
            AM=M-1
            D=M
            A=A-1
            M=M|D
            """;
        }
        fw.write(writeCommand);

        return writeCommand;
    }

    String writePushPop(String[] pushPopCommand) throws IOException {
        // comment line being translated
        String writeCommand = "// " + pushPopCommand[0] + " " + pushPopCommand[1] + " " + pushPopCommand[2] + "\n";
        int target = Integer.parseInt(pushPopCommand[2]);

        // needs to translate each VM command into component assembly commands
        if (pushPopCommand[0].equals("push")) {
            if (pushPopCommand[1].equals("local")) {
                writeCommand += """
                @LCL
                D=M
                @""" + target + "\n" + """
                A=D+A
                D=M
                @SP
                A=M
                M=D
                @SP
                M=M+1
                """;
            }
            else if (pushPopCommand[1].equals("argument")){
                writeCommand += """
                @ARG
                D=M
                @""" + target + "\n" + """
                A=D+A
                D=M
                @SP
                A=M
                M=D
                @SP
                M=M+1
                """;
            }
            else if (pushPopCommand[1].equals("this")) {
                writeCommand += """
                @THIS
                D=M
                @""" + target + "\n" + """
                A=D+A
                D=M
                @SP
                A=M
                M=D
                @SP
                M=M+1
                """;
            }
            else if (pushPopCommand[1].equals("that")) {
                writeCommand += """
                @THAT
                D=M
                @""" + target + "\n" + """
                A=D+A
                D=M
                @SP
                A=M
                M=D
                @SP
                M=M+1
                """;
            }
            else if (pushPopCommand[1].equals("constant")) {
                writeCommand += "@" + target + "\n" + """
                D=A
                @SP
                A=M
                M=D
                @SP
                M=M+1
                """;
            }
            else if (pushPopCommand[1].equals("static")) {
                int addr = 16 + target;
                writeCommand += "@" + addr + "\n" + """
                D=M
                @SP
                A=M
                M=D
                @SP
                M=M+1
                """;
            }
            else if (pushPopCommand[1].equals("temp")) {
                int addr = 5 + target;
                writeCommand += """
                @R5
                D=M
                @""" + addr + "\n" + """
                A=D+A
                D=M
                @SP
                A=M
                M=D
                @SP
                M=M+1
                """;
            }
            else if (pushPopCommand[1].equals("pointer")) {
                if (target == 0) {
                    writeCommand = "@THIS\n";
                }
                else if (target == 1) {
                    writeCommand = "@THAT\n";
                }
                else {
                    throw new IllegalArgumentException("Illegal pointer argument");
                }
                writeCommand += """
                D=M
                @SP
                A=M
                M=D
                @SP
                M=M+1
                """;
            }
            else {
                throw new IllegalArgumentException("Illegal memory segment argument");
            }
        }
        else if (pushPopCommand[0].equals("pop")) {
            if (pushPopCommand[1].equals("local")) {
                writeCommand += """
                @LCL
                D=M
                @""" + target + "\n" +  """
                D=D+A
                @R13
                M=D
                @SP
                AM=M-1
                D=M
                @R13
                A=M
                M=D
                """;
            }
            else if (pushPopCommand[1].equals("argument")){
                writeCommand += """
                @ARG
                D=M
                @""" + target + "\n" + """
                D=D+A
                @R13
                M=D
                @SP
                AM=M-1
                D=M
                @R13
                A=M
                M=D
                """;
            }
            else if (pushPopCommand[1].equals("this")) {
                writeCommand += """
                @THIS
                D=M
                @""" + target + "\n" + """
                D=D+A
                @R13
                M=D
                @SP
                AM=M-1
                D=M
                @R13
                A=M
                M=D
                """;
            }
            else if (pushPopCommand[1].equals("that")) {
                writeCommand += """
                @THAT
                D=M
                @""" + target + "\n" + """
                D=D+A
                @R13
                M=D
                @SP
                AM=M-1
                D=M
                @R13
                A=M
                M=D
                """;
            }
            else if (pushPopCommand[1].equals("constant")) {
                throw new IllegalArgumentException("Illegal pop destination");
            }
            else if (pushPopCommand[1].equals("static")) {
                int addr = 16 + target;
                writeCommand += "@" + addr + "\n" + """
                D=A
                @R13
                M=D
                @SP
                AM=M-1
                D=M
                @R13
                A=M
                M=D
                """;
            }
            else if (pushPopCommand[1].equals("temp")) {
                int addr = 5 + target;
                writeCommand += """
                @R5
                D=M
                @""" + addr + "\n" +  """
                D=D+A
                @R13
                M=D
                @SP
                AM=M-1
                D=M
                @R13
                A=M
                M=D
                """;
            }
            else if (pushPopCommand[1].equals("pointer")) {
                if (target == 0) {
                    writeCommand += "@THIS\n";
                }
                else if (target == 1) {
                    writeCommand += "@THAT\n";
                }
                else {
                    throw new IllegalArgumentException("Illegal pointer argument");
                }
                writeCommand += """
                D=A
                @R13
                M=D
                @SP
                AM=M=1
                D=M
                @R13
                A=M
                M=D
                """;
            }
            else {
                throw new IllegalArgumentException("Illegal memory segment argument");
            }
        }
        fw.write(writeCommand);

        return writeCommand;
    }

    String writeLabel(String[] labelCommand) throws IOException {
        String writeCommand = "// " + labelCommand[0] + " " + labelCommand[1] + "\n";
        writeCommand += "(" + labelCommand[1] + ")\n";

        fw.write(writeCommand);

        return writeCommand;
    }

    String writeGoTo(String[] goToCommand) throws IOException {
        String writeCommand = "// " + goToCommand[0] + " " + goToCommand[1] + "\n";

        fw.write(writeCommand);

        return writeCommand;
    }

    String writeIf(String[] ifCommand) throws IOException {
        String writeCommand = "// " + ifCommand[0] + " " + ifCommand[1] + "\n";

        fw.write(writeCommand);

        return writeCommand;
    }

    String writeFunction(String[] functionCommand) throws IOException {
        String writeCommand = "// " + functionCommand[0] + " " + functionCommand[1] + " " + functionCommand[2] + "\n";

        fw.write(writeCommand);

        return writeCommand;
    }

    String writeCall(String[] callCommand) throws IOException {
        String writeCommand = "// " + callCommand[0] + " " + callCommand[1] + " " + callCommand[2] + "\n";

        fw.write(writeCommand);

        return writeCommand;
    }

    String writeReturn() throws IOException {
        String writeCommand = "// return\n";
        writeCommand += """
        @LCL
        D=M
        @R12
        M=D
        @5
        A=D-A
        D=M
        @R11
        M=D
        @ARG
        D=M
        @R13
        M=D
        @SP
        AM=M-1
        D=M
        @R13
        A=M
        M=D
        @ARG
        D=M
        @SP
        M=D+1
        @R12
        D=M-1
        AM=D
        D=M
        @THAT
        M=D
        @R12
        D=M-1
        AM=D
        D=M
        @THIS
        M=D
        @R12
        D=M-1
        AM=D
        D=M
        @ARG
        M=D
        @R12
        D=M-1
        AM=D
        D=M
        @LCL
        M=D
        @R11
        A=M
        0;JMP
        """;

        fw.write(writeCommand);

        return writeCommand;
    }

    void closeFileWriter() throws IOException {
        fw.close();
    }
}
