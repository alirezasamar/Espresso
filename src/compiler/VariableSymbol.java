package compiler;

import ast.Node;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class VariableSymbol extends VarSymbol
{
    /**
     * create a new symbol table entry of kind variable
     * @param name the variable's id
     * @param type the name of the type of the variable
     * @param decl the node where the variable is declared
     */
    public VariableSymbol(String name, String type, Node decl)
    {
        super(name, type, decl);
    }

    public String getKind()
    {
        return "variable";
    }
}