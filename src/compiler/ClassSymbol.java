package compiler;

import ast.*;
import java.io.PrintWriter;
import java.util.Vector;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class ClassSymbol extends Symbol
{
    //the class's super class, null if the super class has not been resolved or the class is Object
    private ClassSymbol superClass = null;
    //the name of the super class if the superclass has not been resolved
    private String unresolvedSuperClass = null;

    private int classNumber = -1;

    //the class's symbol table
    private SymbolTable sTable;

    //offset tables, used in code generation
    private int[] fieldOffsets = null;
    private int[] methodOffsets = null;

    /**
     * create a new class entry in the symbol table
     * @param className name of the class
     * @param superClass name of the superclass
     * @param decl the ast node where the class is declared
     * @param st the class's symbol table
     */
    public ClassSymbol(String className, String superClass, Node decl, SymbolTable st)
    {
        super(className, "void", decl);
        unresolvedSuperClass = superClass;
        sTable = st;
        sTable.setParentEntry(this);
    }

    /**
     * create a new class symbol for the predefined classes (Object and String)
     * @param className name of the class
     * @param superClass the superclass or null if there is none
     */
    public ClassSymbol(String className, ClassSymbol superClass, SymbolTable st)
    {
        super(className, "void", null);
        this.superClass = superClass;
        sTable = st;
        if (sTable != null)
            sTable.setParentEntry(this);
    }

    /**
     * resolve the superclass, ie check that it is in the symbol table and is valid
     * @param st the symbol table that the superclass should be in
     * @return true if the superclass could be  resolved
     */
    public boolean resolveSuperClass(SymbolTable st, PrintWriter out)
    {
        if (isResolved())
            return true;
        if (mark == 2)
            return false;

        //we are investigating this symbol
        mark = 1;

        Symbol superClass = st.get(unresolvedSuperClass);
        if (superClass == null)
        {
            //super class does not exist
            out.println("Attempt to extend undefined class: '" + symbolDecl.toString() + "' on line " + symbolDecl.getLineNumber() + ", column " + symbolDecl.getColumnNumber());
            mark = 2;  //bad
            return false;
        }
        else if (!(superClass instanceof ClassSymbol))
        {
            //super class is not a class
            out.println("Attempt to extend non-class symbol: '" + symbolDecl.toString() + "' on line " + symbolDecl.getLineNumber() + ", column " + symbolDecl.getColumnNumber());
            mark = 2;  //bad
            return false;
        }
        else if (superClass.mark == 1)
        {
            //circular inheritance
            out.println("Circular inheritance: '" + symbolDecl.toString() + "' on line " + symbolDecl.getLineNumber() + ", column " + symbolDecl.getColumnNumber());
            mark = 2;  //bad
            return false;
        }
        else
        {
            //if the super class is OK, then this class is OK

            boolean success = ((ClassSymbol)superClass).resolveSuperClass(st, out);
            if (success)
            {
                mark = 0;
                this.superClass = (ClassSymbol)superClass;
            }
            else
                mark = 2;

            return success;
        }
    }

    /**
     * returns true if the superclass has been resolved
     * @return true if the superclass has been resolved
     */
    public boolean isResolved()
    {
        return superClass != null || name.equals("Object");
    }

    /**
     * true if an object of this class can be used in place of an object of class other
     * eg for a method call or passed to a method
     * ie if b extends a
     * a.symbolEquals(a) = true
     * b.symbolEquals(a) = true
     * a.symbolEquals(b) = false
     * @param other another ClassSymbol
     * @return true if the classes are equal or this class may be used in place of class other
     */
    public boolean symbolEquals(Symbol other)
    {
        if (other.getName().equals("null") && other instanceof TypeSymbol)
            return true;
        if (!(other instanceof ClassSymbol))
            return false;

        ClassSymbol otherClass = (ClassSymbol)other;
        if (name.equals(otherClass.name))
            return true;

        if (superClass == null)
            return false;
        else
            return superClass.symbolEquals(other);
    }

    /**
     * if this class or its super classes contain the specified method then return it's
     * symbol table entry, otherwise return null
     * @return the method symbol if it can be found, null otherwise
     */
    public MethodSymbol getMethod(String name, String[] params)
    {
        MethodSymbol firstGo = sTable.getMethod(name, params);
        if (firstGo != null || superClass == null)
            return firstGo;

        return superClass.getMethod(name, params);
    }

    /**
     * returns the FieldSymbol for the specified field in this class (or superclass)
     * @param name the name of the field
     * @return The FieldSymbol or null if the field is not present in this class (or its superclasses)
     */
    public FieldSymbol getField(String name)
    {
        Symbol firstGo = sTable.get(name);
        if ((firstGo != null && firstGo instanceof FieldSymbol) || superClass == null)
            return (FieldSymbol) firstGo;

        return superClass.getField(name);
    }

    /**
     * returns the symbol table entry for the specifed constructor, or null if it doesn't exist
     * @return
     */
    public MethodSymbol getConstructor(String[] params)
    {
        Symbol firstGo = sTable.get(name + " (" + ParamUtils.makeList(params) + ")");
        if (firstGo != null)
            return (MethodSymbol)firstGo;

        //try type subsumtion to get the method
        MethodSymbol secondGo = sTable.getMethod(name, params);
        if (secondGo != null)
            return secondGo;

        return null;
    }

    /**
     * converts an offset to a method number
     * @param offset
     * @return the method number of the method with the given offset
     */
    public int getMethodNumber(int offset)
    {
        //the method's static number
        int methodNumber = methodOffsets[offset];

        //get the symbol table key for the method
        MethodSymbol staticMethod = NumberGenerator.getInstance().getMethod(methodNumber);

        //get the correct dynamic method
        MethodSymbol dynamicMethod = getMethod(staticMethod.getName(), staticMethod.getParamTypeArray());

        return dynamicMethod.getMethodNumber();
    }

    /**
     * converts a field number to an offset
     * @param fieldNumber
     * @return the offset of the given fiels, or -1 if it is not present in this class
     */
    public int getFieldOffset(int fieldNumber)
    {
        for (int i = 0; i < fieldOffsets.length; ++i)
            if (fieldOffsets[i] == fieldNumber)
                return i;

        return -1;
    }

    /**
     * converts a field offset to a field number
     * @param offset
     * @return
     */
    public int getFieldNumber(int offset)
    {
        return fieldOffsets[offset];
    }

    /**
     * converts a method number to an offset
     * @param methodNumber
     * @return -1 if the method is not in this class
     */
    public int getMethodOffset(int methodNumber)
    {
        for (int i = 0; i < methodOffsets.length; ++i)
            if (methodOffsets[i] == methodNumber)
                return i;

        return -1;
    }

    /**
     * creates mappings from the method and field numbers of all the methods and
     * fields in the class to offsets in the class or object descriptors created
     * by code generation
     */
    public void createOffsetTables()
    {
        //fields
        Vector fields = new Vector();
        getFields(fields);
        fieldOffsets = new int[fields.size()];
        for (int i = 0; i < fields.size(); ++i)
            fieldOffsets[i] = ((FieldSymbol)fields.get(i)).getFieldNumber();

        //methods
        Vector methods = new Vector();
        getMethods(methods);
        getConstructors(methods);
        methodOffsets = new int[methods.size()];
        for (int i = 0; i < methods.size(); ++i)
        {
            methodOffsets[i] = ((MethodSymbol)methods.get(i)).getMethodNumber();
        }
    }

    /**
     * adds all the fields in this class and its superclasses into the given Vector
     * @param result
     */
    private void getFields(Vector result)
    {
        if (superClass != null)
            superClass.getFields(result);

        Symbol[] symbols = sTable.toArray();
        for (int i = 0; i < symbols.length; ++i)
        {
            if (symbols[i] instanceof FieldSymbol)
                result.add(symbols[i]);
        }
    }

    /**
     * adds all the methods (non constructors) in this class and its superclasses into the
     * given Vector
     * @param result
     */
    private void getMethods(Vector result)
    {
        if (superClass != null)
            superClass.getMethods(result);

        Symbol[] symbols = sTable.toArray();
        for (int i = 0; i < symbols.length; ++i)
        {
            if (symbols[i] instanceof MethodSymbol)
            {
                if (!((MethodSymbol)symbols[i]).isConstructor())
                    result.add(symbols[i]);
            }
        }
    }

    /**
     * adds all the constructors in this class into the given Vector
     * @param result
     */
    private void getConstructors(Vector result)
    {
        Symbol[] symbols = sTable.toArray();
        for (int i = 0; i < symbols.length; ++i)
        {
            if (symbols[i] instanceof MethodSymbol)
            {
                if (((MethodSymbol)symbols[i]).isConstructor())
                    result.add(symbols[i]);
            }
        }
    }

    /**
     * checks that any methods in this class that over ride methods in its super class have the
     * same return type as the methods they over ride.
     */
    public boolean checkReturnTypes(PrintWriter err)
    {
        //if there is no superclass then there is nothing wrong
        if (superClass == null)
            return true;

        boolean result = true;

        //go through all the slots
        Symbol[] symbols = sTable.toArray();
        for (int i = 0; i < symbols.length; ++i)
        {
            //look at methods only
            if (symbols[i] instanceof MethodSymbol)
            {
                //look at non-constrcutors only
                MethodSymbol mSymbol = (MethodSymbol)symbols[i];
                if (!mSymbol.isConstructor())
                {
                    //check if there is a method in the super class that we are over riding
                    MethodSymbol sMethod = superClass.getMethod(mSymbol.getName(), mSymbol.getParamTypeArray());
                    if (sMethod != null && !sMethod.getType().equals(mSymbol.getType()))
                    {
                        err.println("  Method '" + mSymbol.getKey() + "' in class '" + getName() + "' over rides method with different return type.");
                        result = false;
                    }
                }
            }
        }

        return result;
    }

    public int getNumberOfMethods()
    {
        return methodOffsets.length;
    }

    public int getNumberOfFields()
    {
        return fieldOffsets.length;
    }

    public String getKind()
    {
        return "class";
    }

    public SymbolTable getSymbolTable()
    {
        return sTable;
    }
    public ClassSymbol getSuperClass()
    {
        return superClass;
    }
    public int getClassNumber()
    {
        return classNumber;
    }
    public void setClassNumber(int classNumber)
    {
        this.classNumber = classNumber;
    }
}