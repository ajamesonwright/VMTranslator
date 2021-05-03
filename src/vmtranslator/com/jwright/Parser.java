package com.jwright;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
	Scanner fileReader;
	String[] segments;
	String fileOpenErrorMessage;
	String currentCommand;
	String opType;
	
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
		fileExt = fileExt.substring(fileExt.indexOf("."));

		// create scanner with file name and prepare for parsing if applicable file type selected
		if (fileExt.equals(".vm")) {
			try {
				fileReader = new Scanner(file);
			}
			catch (IOException e1) {
				fileOpenErrorMessage = e1.toString();
			}
		}
		else {
			System.out.println("failed");
		}
	}

	void advance() {
		// assign currentCommand to next line in file
		if (fileReader.hasNext()) {
			currentCommand = fileReader.nextLine();
			currentCommand = trimString();
		}
	}
	
	String trimString() {
		// trim leading and trailing whitespace
		currentCommand = currentCommand.trim();
		
		int commentIndex = currentCommand.indexOf("//");
		// returns "" for all-comment line, returns useful text for trailing comment line
		if (commentIndex > 0) {
			return currentCommand.substring(0, commentIndex).trim();
		}
		else if (commentIndex < 0) {
			return currentCommand;
		}
		else {
			return "";
		}
	}
	
	boolean hasMoreCommands() {
		return fileReader.hasNext();
	}
	
	String commandType() {
		// split command into each component (type (argument 1), argument 2 (arg specifier), argument 3 (memory target))
		segments = currentCommand.split(" ");
		
		if (segments.length == 1) {
			opType = currentCommand;
		}
		else {
			opType = segments[0];
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
			case "not":
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
			case "if-goto":
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

	String arg1() {
		return segments[0];
	}
	
	String arg2() {
		// if segments[1] present, return specifier for operation (ie. constant, temp, static, etc.)
		
		if (segments.length == 1 ) {
			return "";
		}
		
		return segments[1];
	}

	int arg3() {
		// if present, return target pointer for operation
		int target = -1;

		if (segments.length != 3) {
			return -1;
		}
		else {
			try {
				target = Integer.parseInt(segments[2]);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Target argument is not an integer.");
			}

			return target;
		}
	}
}
