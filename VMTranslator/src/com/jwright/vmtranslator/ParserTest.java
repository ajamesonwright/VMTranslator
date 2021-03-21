package com.jwright.vmtranslator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

class ParserTest {
	
	File testFile = new File("C:\\Users\\Jamie\\git\\VMTranslator\\VMTranslator\\src\\com\\jwright\\vmtranslator\\SimpleAdd.VM");
	Parser testObject = new Parser(testFile);
	
	@Test
	void testParser() {
		assertEquals("// Pushes and adds two constants.", testObject.fileContents.get(0));
		assertEquals("add", testObject.fileContents.get(testObject.fileContents.size()-1));
	}

	@Test
	void testParseFile() {
		List<String> parsedContents = testObject.parseFile();

		assertEquals("pushconstant", parsedContents.get(0));
		assertEquals("add", parsedContents.get(2));
	}
	
	@Test
	void testCommandType() {
		testObject.currentCommand = "push constant 1";
		assertEquals("C_PUSH", testObject.commandType());
		
		testObject.currentCommand = "lt";
		assertEquals("C_ARITHMETIC", testObject.commandType());
		
		testObject.currentCommand = "test";
		assertEquals("", testObject.commandType());
	}
	
	@Test
	void testArg2() {
		testObject.currentCommand = "push constant 1";
		assertEquals("constant", testObject.arg2());
		
		testObject.currentCommand = "test case";
		assertEquals("case", testObject.arg2());
		
		testObject.currentCommand = "testcase";
		assertEquals("", testObject.arg2());
	}
	
	@Test
	void testArg3() {
		testObject.currentCommand = "push constant 1";
		assertEquals(1, testObject.arg3());
		
		testObject.currentCommand = "push constant 11";
		assertEquals(11, testObject.arg3());
		
		testObject.currentCommand = "test case2";
		assertEquals(-1, testObject.arg3());
		
		testObject.currentCommand = "testcase2";
		assertEquals(-1, testObject.arg3());
	}

}
