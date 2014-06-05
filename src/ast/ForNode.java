package ast;

import compiler.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class ForNode extends Node
{
    private ExprNode initExpr, testExpr, incExpr;
    private Node loopStmt;

    //the symbol table entry for the new scope created by the for statement
    private ScopeSymbol symbol = null;

    public ForNode(ExprNode initExpr, ExprNode testExpr, ExprNode incExpr, Node loopStmt, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.initExpr = initExpr;
        this.testExpr = testExpr;
        this.incExpr = incExpr;
        this.loopStmt = loopStmt;
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public Object visitInitExpr(Visitor v)
    {
        if (initExpr == null)
            return null;
        else
            return initExpr.accept(v);
    }

    public Object visitTestExpr(Visitor v)
    {
        return testExpr.accept(v);
    }

    public Object visitIncExpr(Visitor v)
    {
        if (incExpr == null)
            return null;
        else
            return incExpr.accept(v);
    }

    public Object visitLoopStmt(Visitor v)
    {
        return loopStmt.accept(v);
    }

    public String toString()
    {
        return "for (" + initExpr + "; " + testExpr + "; " + incExpr + ")";
    }
    public ScopeSymbol getSymbol()
    {
        return symbol;
    }
    public void setSymbol(ScopeSymbol symbol)
    {
        this.symbol = symbol;
    }
}