package ast;

import compiler.*;
import java.util.Vector;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class ConstructorNode extends MethodNode
{
    public ConstructorNode(String name, ListNode params, Modifier access, CompoundNode body, int lineNumber, int colNumber)
    {
        super(name, params, access, "void", body, lineNumber, colNumber);
    }

    public Object accept(Visitor v)
    {
        return v.visit(this);
    }

    public String toString()
    {
        return access + " " + name + "(" + ParamUtils.makeSourceList(params) + ")";
    }

    public String getType()
    {
        return "void";
    }
}