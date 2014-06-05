package ast;

import compiler.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class DeclNode extends ExprNode implements NodeWithValue
{
    private String varName;
    private String type;
    private ExprNode value;

    private VariableSymbol symbol = null;

    public DeclNode(String varName, String type, ExprNode value, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.varName = varName;
        this.type = type;
        this.value = value;
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return varName;
    }

    public String toString()
    {
        return type + " " + varName + " = " + value;
    }

    public Object visitValue(Visitor v)
    {
        return value.accept(v);
    }
    public void setSymbol(VariableSymbol symbol)
    {
        this.symbol = symbol;
    }
    public VariableSymbol getSymbol()
    {
        return symbol;
    }
}