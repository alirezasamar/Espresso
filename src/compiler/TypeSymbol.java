package compiler;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class TypeSymbol extends Symbol
{
    /**
     * create a new entry in the symbol table for a built in type
     * @param name the type's name
     */
    public TypeSymbol(String name)
    {
        super(name, "void", null);
    }

    //is always OK
    public boolean isResolved()
    {
        return true;
    }

    public boolean symbolEquals(Symbol other)
    {
        if (name.equals("null"))
        {
            if (other instanceof ClassSymbol)
                return true;
            return false;
        }

        if (!(other instanceof TypeSymbol))
            return false;
        return name.equals(((TypeSymbol)other).name);
    }

    public String getKind()
    {
        return "built in type";
    }
}