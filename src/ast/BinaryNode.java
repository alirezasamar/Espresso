package ast;

import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public abstract class BinaryNode extends ExprNode
{
    //the left child of the node in the AST
    protected Node left;
    //the right child of the node in the AST
    protected Node right;

    public BinaryNode(Node left, Node right, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.left = left;
        this.right = right;
    }

    public Object visitLeft(Visitor v)
    {
        return left.accept(v);
    }

    public Object visitRight(Visitor v)
    {
        return right.accept(v);
    }
}