package ast;

import compiler.*;


/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class MethodNode extends SlotNode
{
    protected ListNode params;
    private CompoundNode body;

    private MethodSymbol symbol = null;

    public MethodNode(String name, ListNode params, Modifier access, String type, CompoundNode body, int lineNumber, int colNumber)
    {
        super(name, access, type, lineNumber, colNumber);
        this.params = params;
        this.body = body;
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public void visitParams(Visitor v)
    {
        if (params != null)
            params.accept(v);
    }

    public String toString()
    {
        return access + " " + name + "(" + ParamUtils.makeSourceList(params) + ")";
    }

    public void visitBody(Visitor v)
    {
        if (body != null)
            body.accept(v);
    }

    public String getType()
    {
        return type;
    }

    public void setParams(ListNode params)
    {
        this.params = params;
    }

    public void setSymbol(MethodSymbol symbol)
    {
        this.symbol = symbol;
    }

    public MethodSymbol getSymbol()
    {
        return symbol;
    }
}