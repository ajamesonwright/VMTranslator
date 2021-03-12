package com.jwright.vmtranslator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Parser {
	List<String> fileContents;
	List<String> parsedContents;
	String errorMessage;
	int max, index;
	String currentCommand;
	
	private enum Operation {
		C_ARITHMETIC,
		C_PUSH, C_POP,
		C_LABEL, C_GOTO,
		C_IF, C_FUNCTION,
		C_RETURN, C_CALL
	}
	
	public Parser(File file) {
		// capture file extension
		String fileExt = file.getName();
		fileExt = fileExt.substring(fileExt.indexOf(".")+1);
		
		try {
			fileContents = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.US_ASCII);
			getLengthOfLongestLine();
		}
		catch (IOException e1) {
			errorMessage = e1.toString();
		}
		
	}
	
	public List<String> parseFile() {
		index = 0;
		
		while (hasMoreCommands(index)) {
			// set current command to value in fileContents associated by index
			currentCommand = fileContents.get(index);
			String argType = arg1();
			int argLocation;
			
			if (argType == "C_PUSH" || argType == "C_POP" || argType == "C_FUNCTION" || argType == "C_CALL")
				argLocation = arg2();
			
			index++;
		}
		
		return parsedContents;
	}
	
	private boolean hasMoreCommands(int index) {
		return index < fileContents.size();
	}
	
	private String arg1() {
		String opType = currentCommand.substring(0, currentCommand.indexOf(" "));
		
		switch (opType) {
			case "add":
				return Operation.C_ARITHMETIC.toString();
			case "sub":
				return Operation.C_ARITHMETIC.toString();
			case "eq":
				return Operation.C_ARITHMETIC.toString();
			case "lt":
				return Operation.C_ARITHMETIC.toString();
			case "gt":
				return Operation.C_ARITHMETIC.toString();
			case "push":
				return Operation.C_PUSH.toString();
			case "pop":
				return Operation.C_POP.toString();
			case "label":
				return Operation.C_LABEL.toString();
			case "goto":
				return Operation.C_GOTO.toString();
			case "if":
				return Operation.C_IF.toString();
			case "function":
				return Operation.C_FUNCTION.toString();
			case "return":
				return Operation.C_RETURN.toString();
			case "call":
				return Operation.C_CALL.toString();
		default:
			return "";
		}
	}
	
	private int arg2() {
		return Integer.parseInt(currentCommand.substring(currentCommand.indexOf(" ") + 1));
	}
	
	private void getLengthOfLongestLine() {
		max = 0;
		for (String line : fileContents) {
			if (line.length() > max) {
				max = line.length();
			}
		}
	}
}
