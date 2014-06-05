package compiler;

import ast.*;
import java.io.PrintWriter;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public abstract class Visitor
{
    //output
    protected PrintWriter err;
    //keep track of the number of errors
    protected int errorCount = 0;

    public Visitor(PrintWriter out)
    {
        this.err = out;
    }

    /**
     * returns the number of errors that occured during the visit
     * @return
     */
    public int getErrorCount()
    {
        return errorCount;
    }

    //report and log an error
    protected void error(String msg, Node node)
    {
        err.println("  " + msg + "'" + node.toString() + "' on line " + node.getLineNumber() + ", column " + node.getColumnNumber());
        ++errorCount;
    }

    public abstract Object visit(AddNode node);
    public abstract Object visit(AndNode node);
    public abstract Object visit(ArgNode node);
    public abstract Object visit(AssNode node);
    public abstract Object visit(BooleanNode node);
    public abstract Object visit(ClassNode node);
    public abstract Object visit(CompoundNode node);
    public abstract Object visit(ConcatNode node);
    public abstract Object visit(ConstructorNode node);
    public abstract Object visit(DeclNode node);
    public abstract Object visit(DivNode node);
    public abstract Object visit(EqNode node);
    public abstract Object visit(ExitNode node);
    public abstract Object visit(FieldAccessNode node);
    public abstract Object visit(FieldNode node);
    public abstract Object visit(ForNode node);
    public abstract Object visit(IfNode node);
    public abstract Object visit(ListNode node);
    public abstract Object visit(MethodInvocNode node);
    public abstract Object visit(MethodNode node);
    public abstract Object visit(MultNode node);
    public abstract Object visit(NegNode node);
    public abstract Object visit(NewNode node);
    public abstract Object visit(NotEqNode node);
    public abstract Object visit(NotNode node);
    public abstract Object visit(NullNode node);
    public abstract Object visit(NumberNode node);
    public abstract Object visit(OrNode node);
    public abstract Object visit(PrintNode node);
    public abstract Object visit(ReturnNode node);
    public abstract Object visit(StringNode node);
    public abstract Object visit(SubNode node);
    public abstract Object visit(SuperCallNode node);
    public abstract Object visit(VarNode node);
}