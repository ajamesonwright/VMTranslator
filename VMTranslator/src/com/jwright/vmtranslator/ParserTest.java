package com.jwright.vmtranslator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

class ParserTest {
	
	File testFile = new File("C:\\Users\\Jameson\\eclipse-workspace\\VMTranslator\\src\\com\\jwright\\vmtranslator\\SimpleAdd.VM");
	Parser testObject = new Parser(testFile);

	@Test
	void testParser() {
		fail("Not yet implemented");
	}

	@Test
	void testParseFile() {
		fail("Not yet implemented");
	}
	
	@Test
	void testArg1() {
		testObject.currentCommand = "push constant 1";
		assertEquals("C_PUSH", testObject.arg1());
		
		testObject.currentCommand = "lt";
		assertEquals("C_ARITHMETIC", testObject.arg1());		
	}

}
