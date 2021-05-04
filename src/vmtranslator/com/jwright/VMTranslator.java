package com.jwright;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.SwingUtilities;

public class VMTranslator {
	static GUI gui;
	static Parser parser;
	static CodeWriter codeWriter;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui = new GUI();

			}
		});
	}

	static void openFile(File file) {
		// create parser, open file, and store file contents
		parser = new Parser(file);

		// if file was opened successfully, append each line to display in input log pane
		if (parser.fileOpenErrorMessage ==  null) {
			while (parser.fileReader.hasNextLine()) {
				gui.logI.append(parser.fileReader.nextLine() + "\n");
			}
		}

		parser = new Parser(file);
	}

	static void triggerParser() {
		String argType, argument1, argument2;
		int argument3;

		codeWriter = new CodeWriter();

		while (parser.hasMoreCommands()) {
			argType = "";
			argument1 = "";
			argument2 = "";
			argument3 = -1;

			parser.advance();

			// if currentCommand is whitespace or was removed by trimString(), step to next iteration
			if (parser.currentCommand.equals("")) {
				continue;
			}

			argType = parser.commandType();
			// illegal argument (length) guard
			if (parser.segments.length > 3) {
				throw new IllegalArgumentException("Too many arguments");
			}

			// return first word of command, plain English
			if (!argType.equals("C_RETURN")) {
				argument1 = parser.arg1();
			}
			// return memory segment and memory target if applicable
			if (argType == "C_PUSH" || argType == "C_POP" || argType == "C_FUNCTION" || argType == "C_CALL") {
				argument2 = parser.arg2();
				argument3 = parser.arg3();
			}
			if (argType == "C_LABEL" || argType == "C_GOTO" || argType == "C_IF") {
				argument2 = parser.arg2();
			}

			// store each input command 
			if (argType == "C_ARITHMETIC") {
				codeWriter.storeArithmetic(parser.currentCommand);
			}
			if (argType == "C_PUSH" || argType == "C_POP") {
				codeWriter.storePushPop(argument1, argument2, argument3);
			}
			if (argType == "C_LABEL") {
				codeWriter.storeLabel(argument1, argument2);
			}
			if (argType == "C_GOTO") {
				codeWriter.storeGoTo(argument1, argument2);
			}
			if (argType == "C_IF") {
				codeWriter.storeIf(argument1, argument2);
			}
			if (argType == "C_FUNCTION") {
				codeWriter.storeFunction(argument1, argument2, argument3);
			}
			if (argType == "C_CALL") {
				codeWriter.storeCall(argument1, argument2, argument3);
			}
			if (argType == "C_RETURN") {
				codeWriter.storeReturn("return");
			}

		}
		gui.logO.append("Parsing complete");
	}

	static void triggerCodeWriter(List<File> fileIn) {
		for (File f : fileIn) {
			// extract destination file name
			String fileInName = f.getPath();
			// exchange .vm extension for .asm extension
			String fileOutName = fileInName.substring(0, fileInName.indexOf(".")) + ".asm";
			
			try {
				// create new file for parsed output
				File outputFile = new File(fileOutName);
				outputFile.createNewFile();
				codeWriter.fw = new FileWriter(outputFile);
	
				String writerOutput = "";
				gui.logO.setText("");
				// step through all arrays stored in parsed list
				for (String[] sa : codeWriter.outputQueue) {
					if (sa.length == 1 && !sa[0].equals("return")) {
						writerOutput = codeWriter.writeArithmetic(sa);
					}
					else if (sa[0].equals("push") || sa[0].equals("pop")) {
						writerOutput = codeWriter.writePushPop(sa);
					}
					else if (sa[0].equals("label")) {
						writerOutput = codeWriter.writeLabel(sa);
					}
					else if (sa[0].equals("goto")) {
						writerOutput = codeWriter.writeGoTo(sa);
					}
					else if (sa[0].equals("if-goto")) {
						writerOutput = codeWriter.writeIf(sa);
					}
					else if (sa[0].equals("function")) {
						writerOutput = codeWriter.writeFunction(sa);
					}
					else if (sa[0].equals("call")) {
						writerOutput = codeWriter.writeCall(sa);
					}
					else {
						writerOutput = codeWriter.writeReturn();
					}

					gui.logO.append(writerOutput);
				}
				codeWriter.closeFileWriter();
			}
			catch (IOException e) {
				System.out.println(e.toString());
			}
		}
		
	}

	static void triggerFileChange(String fileName) {
		codeWriter.setFileName(fileName);
	}
}
