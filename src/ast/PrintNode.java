package ast;

import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class PrintNode extends Node
{
    private ExprNode value;
    private String type = null; //the type of the value (either int, boolean or a class)

    public PrintNode(ExprNode value, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.value = value;
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public Object visitValue(Visitor v)
    {
        return value.accept(v);
    }

    public String toString()
    {
        return "print(" + value + ")";
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}