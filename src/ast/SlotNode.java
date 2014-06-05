package ast;

import compiler.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public abstract class SlotNode extends Node
{
    protected Modifier access;
    protected String type;
    protected String name;

    public SlotNode(String name, Modifier access, String type, int lineNumber, int colNumber)
    {
        super(lineNumber, colNumber);
        this.access = access;
        this.type = type;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Modifier getAccess()
    {
            return access;
    }
}