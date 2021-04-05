package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import vmtranslator.*;

class ParserTest {
	
	File testFile = new File("C:\\Users\\Jamie\\git\\VMTranslator\\src\\vmtranslator\\SimpleAdd.vm");
	Parser testObject = new Parser(testFile);
	
	@Test
	void testParser() {
		List<String> fileContents = testObject.getFileContents();

		assertEquals("// Pushes and adds two constants.", fileContents.get(0));
		assertEquals("add", fileContents.get(fileContents.size()-1));
	}

	@Test
	void testParseFile() {
		List<String> parsedContents = testObject.parseFile();

		assertEquals("pushconstant", parsedContents.get(0));
		assertEquals("add", parsedContents.get(2));
	}
	
	@Test
	void testCommandType() {
		testObject.setCurrentCommand("push constant 1");
		assertEquals("C_PUSH", testObject.commandType());
		
		testObject.setCurrentCommand("lt");
		assertEquals("C_ARITHMETIC", testObject.commandType());
		
		testObject.setCurrentCommand("test");
		assertEquals("", testObject.commandType());
	}
	
	@Test
	void testArg2() {
		testObject.setCurrentCommand("push constant 1");
		assertEquals("constant", testObject.arg2());
		
		testObject.setCurrentCommand("test case");
		assertEquals("case", testObject.arg2());
		
		testObject.setCurrentCommand("testcase");
		assertEquals("", testObject.arg2());
	}
	
	@Test
	void testArg3() {
		testObject.setCurrentCommand("push constant 1");
		assertEquals(1, testObject.arg3());
		
		testObject.setCurrentCommand("push constant 11");
		assertEquals(11, testObject.arg3());
		
		testObject.setCurrentCommand("test case2");
		assertEquals(-1, testObject.arg3());
		
		testObject.setCurrentCommand("testcase2");
		assertEquals(-1, testObject.arg3());
	}

	@Test
	void testTrim() {
		testObject.setCurrentCommand("// test whole line comment");
		assertEquals("", testObject.trimString());

		testObject.setCurrentCommand("test // trailing comment");
		assertEquals("test", testObject.trimString());

		testObject.setCurrentCommand(" test blank line ");
		assertEquals("", testObject.trimString());
	}
}
