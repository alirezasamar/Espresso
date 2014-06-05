package compiler;

import ast.Node;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class FieldSymbol extends Symbol implements AccessSymbol
{
    //the fields access modifier (eg private)
    private Modifier access;

    //the field's unique id used in code generation
    protected int fieldNumber;

    //the class where the method is defined
    private ClassSymbol owner = null;

    /**
     * create a new symbol table entry of kind field
     * @param name the field's name
     * @param type the name of the field's type
     * @param access the fields access modifier (eg private)
     * @param decl the node where the field is declared
     */
    public FieldSymbol(String name, String type, Modifier access, Node decl)
    {
        super(name, type, decl);
        this.access = access;
    }

    public String getKind()
    {
        return "field";
    }

    public Modifier getAccessModifier()
    {
        return access;
    }
    public void setFieldNumber(int fieldNumber)
    {
        this.fieldNumber = fieldNumber;
    }
    public int getFieldNumber()
    {
        return fieldNumber;
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