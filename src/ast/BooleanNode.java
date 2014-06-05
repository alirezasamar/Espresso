package ast;

import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class BooleanNode extends ExprNode
{
    private boolean value;

    public BooleanNode(boolean value, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.value = value;
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public String toString()
    {
        if (value)
            return "true";
        else
            return "false";
    }

    public boolean getValue()
    {
        return value;
    }

    public String getType()
    {
        return "boolean";
    }
}