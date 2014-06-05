package compiler;

import ast.Node;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public abstract class Symbol
{
    //name of the entry
    protected String name;
    //the node where the symbol is declared
    protected Node symbolDecl;

    //the symbol's type, return type if a method
    protected Symbol type = null;
    //the type as a string if it has not been resolved yet
    protected String unresolvedType = null;

    //can be used by external classes to mark a symbol table entry, obviously no guarantee
    //it will stay marked, in fact this is pretty unsafe, but its a small project etc.
    public int mark = 0;

    /**
     * create a new Symbol table entry with unresolved type
     * @param name name of the entry
     * @param type name of the type
     * @param decl node where the entry is declared
     */
    public Symbol(String name, String type, Node decl)
    {
        this.name = name;
        this.symbolDecl = decl;
        this.unresolvedType = type;
    }

    /**
     * create a symbol table entry with resolved type
     * @param name name of the entry
     * @param type SymbolTableEntry that defines the symbol's type
     * @param decl node where the entry is declared
     */
    public Symbol(String name, Symbol type, Node decl)
    {
        this.name = name;
        this.symbolDecl = decl;
        this.type = type;
    }

    /**
     * produces a key that identifies the symbol
     * @return String
     */
    public String getKey()
    {
        return name;
    }

    /**
     * returns a String describing the type of the entry
     * @return a String the name of the entry's type
     */
    public String getType()
    {
        if (type == null)
            return unresolvedType;
        else
            return type.getName();
    }

    /**
     * returns the name of the entry
     * @return
     */
    public final String getName()
    {
        return name;
    }

    /**
     * returns the line in the source code that this node starts on
     * @return int a line number in the source code, 0 if the node has no direct representation in the
     * source code
     */
    public final int getLineNumber()
    {
        if (symbolDecl == null)
            return 0;
        else
            return symbolDecl.getLineNumber();
    }

    /**
     * returns the column number of the node in the source code
     * @return int the column in the source code, 0 if the node has no direct representation in the
     * source code
     */
    public final int getColumnNumber()
    {
        if (symbolDecl == null)
            return 0;
        else
            return symbolDecl.getColumnNumber();
    }

    /**
     * true if the symbol table entry has been totally resolved, eg type resolution
     * @return true if no more resolution is required for this node, false otherwise
     */
    public boolean isResolved()
    {
        return type != null;
    }

    /**
     * set the type to an entry in the symbol table
     * @param type a Symbol, the entry in the symbol table that represents this symbol's type
     */
    public void resolveType(Symbol type)
    {
        this.type = type;
    }

    /**
     * tests for symbol equality
     * we do not over ride equals() since this is not the same as object equality, in particular
     * it may not be symetric (see ClassSymbol)
     * @param other some other Symbol
     * @return true if the symbols may be regarded as equals
     */
    public boolean symbolEquals(Symbol other)
    {
        if (other == this)
            return true;
        return getKey().equals(other.getKey());
    }


    /**
     * returns the kind of the symbol (eg variable, parameter etc)
     * @return the symbol's kind
     */
    public abstract String getKind();
}