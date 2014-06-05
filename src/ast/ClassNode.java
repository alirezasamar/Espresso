package ast;

import compiler.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class ClassNode extends Node
{
    private String name, superClass;
    private ListNode slots = null;

    private ClassSymbol symbol;

    public ClassNode(String name, String superClassName, ListNode slots, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.name = name;
        this.superClass = superClassName;
        this.slots = slots;
    }

    public ClassNode(String name, ListNode slots, int lineNumber, int colNumber)
    {
        this(name, "Object",  slots, lineNumber, colNumber);
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }
    public String getName()
    {
        return name;
    }
    public String getSuperClass()
    {
        return superClass;
    }

    public void visitSlots(Visitor v)
    {
        if (slots != null)
            slots.accept(v);
    }

    public String toString()
    {
        String result = "class " + name;
        if (!(superClass == null || superClass.equals("Object")))
            result += " extends " + superClass;

        return result;
    }

    public void setSymbol(ClassSymbol symbol)
    {
        this.symbol = symbol;
    }
    public ClassSymbol getSymbol()
    {
        return symbol;
    }
}