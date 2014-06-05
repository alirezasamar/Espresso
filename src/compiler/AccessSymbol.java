package compiler;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public interface AccessSymbol
{
    Modifier getAccessModifier();
    String getName();
    String getKind();
    ClassSymbol getOwner();
}