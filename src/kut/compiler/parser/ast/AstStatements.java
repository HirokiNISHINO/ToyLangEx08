package kut.compiler.parser.ast;

import java.util.Vector;

import kut.compiler.compiler.CodeGenerator;
import kut.compiler.exception.CompileErrorException;

public class AstStatements extends AstNode 
{

	protected Vector<AstNode> statements;
	
	/**
	 * @param node
	 * @param platform
	 */
	public AstStatements()
	{
		this.statements = new Vector<AstNode>();
	}


	/**
	 *
	 */
	public void printTree(int indent) {
		this.println(indent, "statements:");
		for (AstNode s: statements) {
			s.printTree(indent + 1);
		}
	}
	
	
	/**
	 * @param statement
	 */
	public void addStatement(AstNode statement)
	{
		this.statements.add(statement);
	}
	
	/**
	 *
	 */
	@Override
	public void cgen(CodeGenerator gen) throws CompileErrorException
	{
		for (AstNode n: statements) {
			n.cgen(gen);
		}
		return;
	}

}
