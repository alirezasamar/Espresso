package ast;
import compiler.Visitor;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public interface NodeWithValue
{
    public Object visitValue(Visitor v);
    public String getType();
    public String getName();
}