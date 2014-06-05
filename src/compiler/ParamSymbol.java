package compiler;

import ast.Node;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class ParamSymbol extends VarSymbol
{

    /**
     * create a new symbol table entry for a parameter
     * @param name the parameter's id
     * @param type the name of the parameter's type
     * @param decl the node where the parameter is declared
     */
    public ParamSymbol(String name, String type, Node decl)
    {
        super(name, type, decl);
    }

    public String getKind()
    {
        return "parameter";
    }
}