package vmtranslator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {
	List<String> fileContents;
	List<String> parsedContents;
	String errorMessage;
	int max, index, firstSpace, secondSpace;
	String currentCommand;
	String argType, argOp, argSpec, addrStr;
	int argTarget, addrInt;
	
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
		String line;
		parsedContents = new ArrayList<>();
		
		while (hasMoreCommands(index)) {
			// set current command to value in fileContents associated by index
			currentCommand = fileContents.get(index);
			// eliminate whitespace and comments
			currentCommand = trimString();
			
			if (!currentCommand.equals("")) {
				initialize();
				argType = commandType();
				
				if (argType != "C_RETURN") {
					argOp = arg1();
				}
				
				if (arg2Applicable()) {
					argSpec = arg2();
					argTarget = arg3();
				}
				
				

				switch (argSpec) {
					case "local":
						addrInt = 1 + argTarget;
						addrStr = Integer.toString(addrInt);
					case "argument":
						
					case "this":
						
					case "that":
						
					case "static":
						
					case "constant":
						
					case "temp":
						
					case "pointer":
						
					case "push":
						
					case "pop":
						
					case "label":
						
					case "goto":
						
					case "if":
						
					case "function":
						
					case "call":
						
				default:
					
						
				}
				
				line = "";
				line = argOp + argSpec;
				parsedContents.add(line);
			}
			index++;
		}
		
		return parsedContents;
	}
	
	private void initialize() {
		argOp = "";
		argSpec = "";
		argTarget = -1;
		addrInt = 0;
		addrStr = "";
	}
	
	public String trimString() {
		// detect and remove lines containing only comments as well as trailing comments
		int commentIndex = currentCommand.indexOf("/");

		// detect and remove lines containing only comments
		if (commentIndex == 0) {
			return "";
		}
		// detect and remove lines containing trailing comments
		else if (commentIndex > 0) {
			return currentCommand.substring(0, commentIndex).trim();
		}

		// remove empty lines
		if (currentCommand.charAt(0) == ' ') {
			return "";
		}
		return currentCommand.trim();
	}
	
	private boolean hasMoreCommands(int index) {
		return index < fileContents.size();
	}
	
	public String commandType() {
		// return operation type
		String opType;
		firstSpace = currentCommand.indexOf(" ");
		
		if (firstSpace == -1) {
			opType = currentCommand;
		}
		else {
			opType = currentCommand.substring(0, firstSpace);			
		}
		
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
			case "and":
				return Operation.C_ARITHMETIC.toString();
			case "neg":
				return Operation.C_ARITHMETIC.toString();
			case "or":
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
	
	public String arg1() {
		// return operation type in plain text
		switch (argType) {
			case "C_ARITHMETIC":
				return currentCommand;
			case "C_PUSH":
				return "push";
			case "C_POP":
				return "pop";
			case "C_LABEL":
				return "label";
			case "C_GOTO":
				return "goto";
			case "C_IF":
				return "if";
			case "C_FUNCTION":
				return "function";
			case "C_CALL":
				return "call";
		default:
			return "";
		}
	}
	
	public String arg2() {
		/* if present, return specifier for operation (ie. constant, temp, static, etc.)
		if first space not present, return null case
		check for presence of second space character
		if present, return characters between spaces 1 and 2
		otherwise, return everything after first space */
		firstSpace = currentCommand.indexOf(" ");
		secondSpace = currentCommand.indexOf(" ", firstSpace + 1);
		
		if (firstSpace == -1) {
			return "";
		}
		
		if (secondSpace == -1) {
			return currentCommand.substring(firstSpace + 1);
		}
		
		return currentCommand.substring(firstSpace + 1, secondSpace);
	}
	
	private boolean arg2Applicable() {
		return (argType == "C_PUSH" || argType == "C_POP" || argType == "C_FUNCTION" || argType == "C_CALL");
	}

	public int arg3() {
		// if present, return target pointer for operation
		firstSpace = currentCommand.indexOf(" ");
		secondSpace = currentCommand.indexOf(" ", firstSpace + 1);
		
		if (firstSpace == -1) {
			return -1;
		}
		
		if (secondSpace == -1) {
			return -1;
		}
		
		return Integer.parseInt(currentCommand.substring(secondSpace + 1));
	}
	
	private void getLengthOfLongestLine() {
		max = 0;
		for (String line : fileContents) {
			if (line.length() > max) {
				max = line.length();
			}
		}
	}

	public List<String> getFileContents() { 
		return this.fileContents;
	}

	public void setCurrentCommand(String command) {
		currentCommand = command;
	}

	public String getCurrentCommand() {
		return currentCommand;
	}
}
