package ast;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public abstract class ExprNode extends Node
{
    ExprNode(int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
    }
}