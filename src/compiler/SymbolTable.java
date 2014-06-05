package compiler;

import java.util.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class SymbolTable
{
    //storage for the entries
    private Hashtable storage = new Hashtable();
    //if this symbol table is for a nested scope then parent is the symbol table
    //for the parent scope
    private SymbolTable parent = null;
    //if this symbol table is for a nested scope then parentEntry is the symbol table entry
    //that owns this scope
    private Symbol parentEntry = null;

    //if this is true then the scope can hide variables from higher scope
    private boolean hidingScope = false;


    /**
     * create a symbol table for the global (top level) scope
     */
    public SymbolTable()
    {
        this.hidingScope = true;
    }

    /**
     * create a symbol table for a nested scope
     * @param parent the symbol table for the parent scope
     * @param hidingScope, true if this scope can hide variables in more global scopes
     */
    public SymbolTable(SymbolTable parent, boolean hidingScope)
    {
        this.parent = parent;
        this.hidingScope = hidingScope;
    }

    /**
     * create a symbol table for a nested scope
     * @param parent the symbol table for the parent scope
     */
    public SymbolTable(SymbolTable parent)
    {
        this.parent = parent;
    }

    /**
     * put a symbol in the symbol table
     * @param entry
     */
    public void put(Symbol entry)
    {
        storage.put(entry.getKey(), entry);
    }

    public MethodSymbol getMethod(String name, String[] params)
    {
        Symbol[] elements = toArray();
        for (int i = 0 ; i < elements.length; ++i)
        {
            if (elements[i].getName().equals(name) && elements[i] instanceof MethodSymbol)
            {
                MethodSymbol symbol = (MethodSymbol)elements[i];
                if (symbol.matchParams(params, this))
                    return symbol;
            }
        }

        return null;
    }

    /**
     * get a Symbol from the symbol table
     * @param key the symbol's key
     * @return null if the symbol is not in theis symbol table
     */
    public Symbol get(String key)
    {
        Symbol symbol = getStrict(key);
        if (symbol != null)
            return symbol;

        //if the symbol is not in this symbol table try the parent symbol table
        if (parent == null)
            return null;
        else
            return parent.get(key);
    }

    /**
     * get a Symbol from the symbol table, only looks in this symbol table and not any parent
     * symbol tables
     * @param key the symbol's key
     * @return null if the symbol is not in theis symbol table
     */
    public Symbol getStrict(String key)
    {
        return (Symbol)storage.get(key);
    }

    /**
     * returns an array of all the symbols in this symbol table
     * @return
     */
    public Symbol[] toArray()
    {
        Collection collect = storage.values();
        return (Symbol[])collect.toArray(new Symbol[0]);
    }

    public boolean canBeDeclared(String key)
    {
        if (hidingScope)
            return getStrict(key) == null;
        else
        {
            Symbol symbol = getStrict(key);
            if (symbol != null)
                return false;
            else if (parent == null)
                return true;
            else
                return parent.canBeDeclared(key);
        }
    }
    public SymbolTable getParent()
    {
        return parent;
    }
    public Symbol getParentEntry()
    {
        return parentEntry;
    }
    public void setParentEntry(Symbol parentEntry)
    {
        this.parentEntry = parentEntry;
    }
}