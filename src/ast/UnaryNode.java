package ast;

import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public abstract class UnaryNode extends ExprNode
{
    protected Node child = null;

    public UnaryNode(Node child, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.child = child;
    }

    public Object visitChild(Visitor v)
    {
        return child.accept(v);
    }
}