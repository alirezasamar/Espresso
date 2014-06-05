package ast;

import compiler.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class BracketNode extends UnaryNode
{
    public BracketNode(ExprNode expr, int line, int col)
    {
        super(expr, line, col);
    }
    public Object accept(Visitor v)
    {
        return child.accept(v);
    }

    public String toString()
    {
        return "(" + child + ")";
    }
}