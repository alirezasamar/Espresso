package compiler;

import ast.Node;
import utils.ParamUtils;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class MethodSymbol extends Symbol implements AccessSymbol
{
    //the method's access modifier
    private Modifier access;
    //the method's parameters
    private Symbol[] params;

    //the method's symbol table
    private SymbolTable sTable;

    private int methodNumber;

    //true if the method is a constructor
    private boolean constructor;

    //the class where the method is defined
    private ClassSymbol owner = null;

    /**
     * create a new symbol table entry of kind method
     * @param name the method's name
     * @param returnType the name of the method's return type
     * @param params the symbol table entries for the method's parameters
     * @param access the method's access modifier (eg private)
     * @param decl the ast node where the method is declared
     * @param st the symbol table for the method's scope
     */
    public MethodSymbol(String name, String returnType, Symbol[] params, Modifier access, Node decl, SymbolTable st, boolean constructor)
    {
        super(name, returnType, decl);
        this.params = params;
        this.access = access;
        sTable = st;
        if (sTable != null)
            sTable.setParentEntry(this);

        this.constructor = constructor;
    }

    /**
     * produces a key that identifies the method
     * @return String
     */
    public String getKey()
    {
        return name + " (" + ParamUtils.makeTypeList(params) + ")";
    }

    public String getKind()
    {
        return "method";
    }

    public SymbolTable getSymbolTable()
    {
        return sTable;
    }

    public Modifier getAccessModifier()
    {
        return access;
    }

    /**
     * see if the parameters supplied match the signature of this method, taking into
     * account type subsumtion
     * @param params the parameters to test
     * @param st The target class's symbol table
     * @return true if the parameters match this method symbol's parameters
     */
    public boolean matchParams(String[] params, SymbolTable st)
    {
        //check the number of parameters match
        if (params.length != this.params.length)
            return false;

        for (int i = 0; i < params.length; ++i)
        {
            if (!st.get(params[i]).symbolEquals(st.get(this.params[i].getType())))
                if (!(params[i].equals("null") && st.get(this.params[i].getType()) instanceof ClassSymbol))
                    return false;
        }

        return true;
    }

    public String[] getParamTypeArray()
    {
        String[] result = new String[params.length];

        for (int i = 0; i < result.length; ++i)
            result[i] = params[i].getType();

        return result;
    }

    public boolean isConstructor()
    {
        return constructor;
    }

    public void setMethodNumber(int methodNumber)
    {
        this.methodNumber = methodNumber;
    }
    public int getMethodNumber()
    {
        return methodNumber;
    }
    public ClassSymbol getOwner()
    {
        return owner;
    }
    public void setOwner(ClassSymbol owner)
    {
        this.owner = owner;
    }
}