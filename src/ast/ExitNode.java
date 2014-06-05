package ast;

import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class ExitNode extends Node
{
    public ExitNode(int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public String toString()
    {
        return "exit()";
    }
}