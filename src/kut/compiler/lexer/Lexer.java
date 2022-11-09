package kut.compiler.lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

import kut.compiler.exception.CompileErrorException;


/**
 * @author hnishino
 *
 */
public class Lexer 
{
	/**
	 * the filename of a program to load.
	 */
	protected File				file	;	
	
	protected FileReader 		reader	;
	protected Stack<Integer>	unreadCharacters;
	
	protected int				lineNo	;
	/**
	 * @param program
	 */
	public Lexer(String filename) throws CompileErrorException
	{
		this.unreadCharacters = new Stack<Integer>();
		
		this.file = new File(filename);
		
		reader = null;
		try {
			reader = new FileReader(file);
		}
		catch (FileNotFoundException e) {
			throw new CompileErrorException("file not found: " + this.file.getAbsolutePath());
		}
		
		this.lineNo = 0;
		
		return;
	}
	


	/**
	 * @return
	 * @throws IOException
	 */
	protected int read() throws IOException
	{
		int i = 0;
		if (!unreadCharacters.isEmpty()) {
			i = unreadCharacters.pop();
		}
		else {
			i = reader.read();
		}
		
		if (i == '\n') {
			lineNo++;
		}
		
		return i;
	}
	
	/**
	 * @param i
	 * @throws IOException
	 */
	protected void unread(int i) throws IOException
	{
		if (i == '\n') {
			lineNo--;
		}
		unreadCharacters.push(i);
	}
	/**
	 * @return
	 */
	public Token getNextToken() throws IOException
	{
		if (this.reader == null) {
			return null;
		}
		
		do {
			int i = this.read();
			
			if (i < 0) {
				return new Token(-1, "EOF", lineNo);
			}
			
			char c = (char)i;

			
			//skip the white space character.
			if (Character.isWhitespace(c)) {
				continue;
			}
			
			//if it is a digit, then get a integer number token.
			if (Character.isDigit(c)) {
				this.unread(i); 
				return this.getNextTokenInteger();
			}
			
			//otherwise, tokenize one character as a token.
			return new Token(i, "" + c, lineNo);
		} while(true);
	}
	
	/**
	 * @return
	 * @throws IOException
	 */
	public Token getNextTokenInteger() throws IOException
	{
		StringBuffer sb = new StringBuffer();
		
		int i;
		do {
			i = this.read();
			if (i < 0) {
				break;
			}
			char c = (char)i;
			if (!Character.isDigit(c)) {
				break;
			}
			
			sb.append(c);
		} while(true);

		this.unread(i);

		Token t = new Token(TokenClass.IntLiteral, sb.toString(), lineNo);
		
		return t;
	}
}
