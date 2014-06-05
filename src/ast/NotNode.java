package ast;

import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class NotNode extends UnaryNode
{
    public NotNode(ExprNode e, int lineNumber, int colNumber)
    {
        super(e, lineNumber, colNumber);
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public String toString()
    {
        return "!" + child;
    }
}