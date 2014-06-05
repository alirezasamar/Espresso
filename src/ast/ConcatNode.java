package ast;

import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class ConcatNode extends BinaryNode
{
    private String leftType = null;
    private String rightType = null;

    public ConcatNode(ExprNode e1, ExprNode e2, int lineNumber, int colNumber)
    {
        super(e1, e2, lineNumber, colNumber);
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public String toString()
    {
        return left + " ++ " + right;
    }
    public String getLeftType()
    {
        return leftType;
    }
    public String getRightType()
    {
        return rightType;
    }
    public void setLeftType(String leftType)
    {
        this.leftType = leftType;
    }
    public void setRightType(String rightType)
    {
        this.rightType = rightType;
    }
}