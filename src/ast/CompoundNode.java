package ast;

import compiler.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class CompoundNode extends Node
{
    private ListNode stmts;

    private ScopeSymbol symbol = null;

    public CompoundNode(ListNode stmts, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.stmts = stmts;
    }

    public String toString()
    {
        return "{\n" + stmts + "\n}";
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public void visitChildren(Visitor v)
    {
        if (stmts != null)
            stmts.accept(v);
    }

    public void setSymbol(ScopeSymbol symbol)
    {
        this.symbol = symbol;
    }
    public ScopeSymbol getSymbol()
    {
        return symbol;
    }
}
