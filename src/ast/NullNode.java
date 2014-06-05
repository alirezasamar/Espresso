package ast;

import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class NullNode extends ExprNode
{
    public NullNode(int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public String toString()
    {
        return "null";
    }

    public String getType()
    {
        return "null";
    }
}