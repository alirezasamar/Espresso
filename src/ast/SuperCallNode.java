package ast;

import compiler.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class SuperCallNode extends ExprNode implements InvocNode
{
    private ListNode params;

    //the constructor to call
    private MethodSymbol symbol = null;

    public SuperCallNode(ListNode params, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.params = params;
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public String toString()
    {
        return "super(" + ParamUtils.makeSourceList(params) + ")";
    }

    public Object[] visitParams(Visitor v)
    {
        if (params == null)
            return new Object[0];

        Node[] paramList = params.toArray();
        Object[] result = new Object[paramList.length];
        for (int i = 0; i < paramList.length; ++i)
            result[i] = paramList[i].accept(v);

        return result;
    }

    public Node[] getParams()
    {
        if (params == null)
            return new Node[0];

        return params.toArray();
    }

    public int getNumberOfParams()
    {
        if (params == null)
            return 0;

        return params.toArray().length;
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