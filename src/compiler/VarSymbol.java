package compiler;

import ast.Node;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public abstract class VarSymbol extends Symbol
{
    private int localVar = -1;

    /**
     * @param name the variable's id
     * @param type the name of the type of the variable
     * @param decl the node where the variable is declared
     */
    public VarSymbol(String name, String type, Node decl)
    {
        super(name, type, decl);
    }

    public int getLocalVar()
    {
        return localVar;
    }

    public void setLocalVar(int localVar)
    {
        this.localVar = localVar;
    }
}