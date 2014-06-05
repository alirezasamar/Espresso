package ast;

import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public interface InvocNode
{
    Object[] visitParams(Visitor v);
    Node[] getParams();
    int getNumberOfParams();
}