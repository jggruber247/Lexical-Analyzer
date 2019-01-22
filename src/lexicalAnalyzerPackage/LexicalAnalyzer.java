package lexicalAnalyzerPackage;
/* --------------------------------------------------------------------------------
 * Garrett Gruber
 * CSCI 4200 DB
 * Dr. Abi Salimi
 *
 * Lexical Analyzer Program
 * Purpose: This program is a Java translation of the lexical analysis program on
 * pages 166-170 of the textbook. Unlike the textbook, this program uses String 
 * names to identify tokens rather than numbers. This program also recognizes the 
 * assignment operator '=' when the original did not. The program takes statements 
 * from an input file (titled "lexInput.txt") and outputs each statement's tokens
 * and lexemes.
 * --------------------------------------------------------------------------------
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LexicalAnalyzer {

/**Global Declarations**/
	//Declaring Variables
	int charClass;
	char nextChar;
	int lexLength = 0;
	int token;
	String nextToken; 
	int ind= 0;
	
	//Initializing an Array for storing Lexemes
	final static int MAX_LEXEME = 100;
	char [] lexeme = new char[MAX_LEXEME];
	
	//Initializing the different character class constants
	final static int LETTER = 0;
	final static int DIGIT = 1;
	final static int UNKNOWN = 99;
	final static int IEOF = -1; 
	
	//Initializing token codes
	final static String INT_LIT = "INT_LIT";
	final static String IDENT = "IDENT";
	final static String ASSIGN_OP = "ASSIGN_OP";
	final static String ADD_OP = "ADD_OP";
	final static String SUB_OP = "SUB_OB";
	final static String MULT_OP = "MULT_OP";
	final static String DIV_OP = "DIV_OP";
	final static String LEFT_PAREN = "LEFT_PAREN";
	final static String RIGHT_PAREN = "RIGHT_PAREN";
	final static String EOF = "END_OF_FILE";
	
/**Functions and Methods**/
	//*******************************************************************************************
	//Checks the parameter (char ch) to determine if it is an operator or parentheses character 
		//and returns its token type if it is.
	private String lookUp(char ch) {
		switch(ch) {
		case '(':
			addChar();
			nextToken = LEFT_PAREN;
			break;
		case ')':
			addChar();
			nextToken = RIGHT_PAREN;
			break;
		case '+':
			addChar();
			nextToken = ADD_OP;
			break;
		case '-':
			addChar();
			nextToken = SUB_OP;
			break;
		case '*':
			addChar();
			nextToken = MULT_OP;
			break;
		case '/':
			addChar();
			nextToken = DIV_OP;
			break;
		case '=':
			addChar();
			nextToken = ASSIGN_OP;
			break;
		default:
			addChar();
			nextToken = EOF;
		}
		return nextToken;
	}
	
	//*******************************************************************************************
	//Adds nextChar to the char Array lexeme, throws an error if lexeme is full (100 characters
		//in this case).
	private void addChar() {
		if(lexLength <= 98) {
			lexeme[lexLength++] = nextChar;
			lexeme[lexLength] = 0;
		}
		else
			System.out.println("Error - lexeme is too long");
	}
	
	//*******************************************************************************************
	//Gets the next character from the input file and determines its character class.
	private void getChar() throws IOException {
		String content = new String(Files.readAllBytes(Paths.get("C:/Users/jggru/Documents/Eclipse/LexicalAnalysis/src/lexicalAnalyzerPackage/lexInput.txt")));
		content = content + " ";
		if(ind == content.length()) {
			charClass = IEOF;
		}
		else if(nextToken != EOF) {
			nextChar = content.charAt(ind);
    		if(Character.isAlphabetic(nextChar)) {
    			charClass = LETTER;
    		}
    		else if(Character.isDigit(nextChar)) {
    			charClass = DIGIT;
    		}
    		else {
    			charClass = UNKNOWN;
    		}
    		ind++;
    	}
    	else {
    		charClass = IEOF;
    	}
	}
	
	//*******************************************************************************************
	//Calls the getChar() method until it returns a character that isn't whitespace, essentially
		//skipping blank spaces.
	private void getNonBlank() throws IOException {
		String content = new String(Files.readAllBytes(Paths.get("C:/Users/jggru/Documents/Eclipse/LexicalAnalysis/src/lexicalAnalyzerPackage/lexInput.txt")));
		content = content + " ";
		while(Character.isWhitespace(nextChar) && (ind < content.length())){
			getChar();
		}
	}
	
	//*******************************************************************************************
	//This is the main lexical analyzer method.
	private String lex() throws IOException {
		lexLength=0;
		getNonBlank();
		if (charClass == LETTER) { //Parse identifiers
			addChar();
			getChar();
			while(charClass == LETTER || charClass == DIGIT) {
				addChar();
				getChar();
			}
			nextToken = IDENT;
		}
		else if (charClass == DIGIT) { //Parse integer literals
			addChar();
			getChar();
			while(charClass == DIGIT) {
				addChar();
				getChar();
			}
			nextToken = INT_LIT;
		}
		else if (charClass == UNKNOWN) { //Parse parentheses and operators
			lookUp(nextChar);
			getChar();
		}
		if (charClass == IEOF){ //End Of File
			System.out.println("********************************************************************************");
			nextToken = EOF;
			lexeme[0] = 'E';
			lexeme[1] = 'O';
			lexeme[2] = 'F';
			lexeme[3] = 0;
		}
		System.out.printf("Next token is: %-11s Next lexeme is %s%n", nextToken, printString(lexeme));
		if(nextToken == "END_OF_FILE") {
			System.out.println("Lexical analysis of the program is complete!");
		}
		return nextToken;
	}	
	
	//*******************************************************************************************
	//Neatly converts the contents of a character Array into a printable String.
	private String printString(char[] lexArray) {
		String lexString = "";
		for (int i=0; i<lexArray.length; i++) {
			if(lexArray[i] != ' ') {
				lexString = lexString + lexArray[i];
				lexArray[i] = ' ';
			}
		}
		return lexString.trim();
	}
	
	
	//*******************************************************************************************
	//Blank constructor method that allows a LexicalAnalyzer object to be used for running methods
	public LexicalAnalyzer() {
		
	}
	
/**Main Method**/
	public static void main(String[] args) throws IOException {
		
	//Programmer Information 
		System.out.println("Garrett Gruber, CSCI4200-DB, Fall 2018, Lexical Analyzer");
		System.out.println("********************************************************************************");

	//Initializing a LexicalAnalyzer object
		LexicalAnalyzer lexi = new LexicalAnalyzer();
			
	//Retrieving the input file, printing the content, and running the lexical analyzer
		String content = new String(Files.readAllBytes(Paths.get("C:/Users/jggru/Documents/Eclipse/LexicalAnalysis/src/lexicalAnalyzerPackage/lexInput.txt")));
		System.out.println ("Input: " +  content);
		while (lexi.ind < content.length()) {
			lexi.getChar();
			do{
	        	lexi.lex();
			} while (lexi.nextToken != EOF);
		}
	}

}