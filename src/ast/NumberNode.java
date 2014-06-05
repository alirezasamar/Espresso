package ast;

import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class NumberNode extends ExprNode
{
    private int value;

    public NumberNode(Integer value, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.value = value.intValue();
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public String toString()
    {
        return String.valueOf(value);
    }

    public String getType()
    {
        return "int";
    }
}