package compiler;

import ast.*;
import java.io.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class SymbolTableConstructor extends Visitor
{
    //pointer to the current symbol table
    private SymbolTable sTable = new SymbolTable();

    //the class that we are currently checking
    private ClassSymbol curClass = null;

    /**
     * Create a new Symbol table constructor
     * @param out PrintWriter to be used for output
     */
    public SymbolTableConstructor(PrintWriter out)
    {
        super(out);

        //add the built in types to the type table
        sTable.put(new TypeSymbol("unknown"));
        sTable.put(new TypeSymbol("void"));
        sTable.put(new TypeSymbol("int"));
        sTable.put(new TypeSymbol("boolean"));
        sTable.put(new TypeSymbol("null"));

        //Object
        SymbolTable objectST = new SymbolTable(sTable, true);

        MethodSymbol objectInit = new MethodSymbol("Object", "void", new Symbol[0], Modifier.PUBLIC, null, null, true);
        objectInit.setMethodNumber(NumberGenerator.getInstance().getMethodNumber(objectInit));
        objectST.put(objectInit);

        MethodSymbol objectToString = new MethodSymbol("toString", "String", new Symbol[0], Modifier.PUBLIC, null, null, false);
        objectToString.setMethodNumber(NumberGenerator.getInstance().getMethodNumber(objectToString));
        objectST.put(objectToString);

        ClassSymbol object = new ClassSymbol("Object", null, objectST);
        objectInit.setOwner(object);
        objectToString.setOwner(object);
        sTable.put(object);

        //String
        SymbolTable stringST = new SymbolTable(sTable, true);


        MethodSymbol stringInit = new MethodSymbol("String", "void", new Symbol[0], Modifier.PUBLIC, null, null, true);
        stringInit.setMethodNumber(NumberGenerator.getInstance().getMethodNumber(stringInit));
        stringST.put(stringInit);

        MethodSymbol stringInitString = new MethodSymbol("String", "void", new Symbol[] {new ParamSymbol("str", "String", null)}, Modifier.PUBLIC, null, null, true);
        stringInitString.setMethodNumber(NumberGenerator.getInstance().getMethodNumber(stringInitString));
        stringST.put(stringInitString);

        MethodSymbol stringToString = new MethodSymbol("toString", "String", new Symbol[0], Modifier.PUBLIC, null, null, false);
        stringToString.setMethodNumber(NumberGenerator.getInstance().getMethodNumber(stringToString));
        stringST.put(stringToString);

        MethodSymbol stringLength = new MethodSymbol("length", "int", new Symbol[0], Modifier.PUBLIC, null, null, false);
        stringLength.setMethodNumber(NumberGenerator.getInstance().getMethodNumber(stringLength));
        stringST.put(stringLength);

        MethodSymbol stringCharAt = new MethodSymbol("charAt", "String", new Symbol[] {new ParamSymbol("index", "int", null)}, Modifier.PUBLIC, null, null, false);
        stringCharAt.setMethodNumber(NumberGenerator.getInstance().getMethodNumber(stringCharAt));
        stringST.put(stringCharAt);

        stringST.put(new StringFieldSymbol());

        ClassSymbol string = new ClassSymbol("String", object, stringST);
        stringInit.setOwner(string);
        stringInitString.setOwner(string);
        stringToString.setOwner(string);
        stringLength.setOwner(string);
        stringCharAt.setOwner(string);

        sTable.put(string);
    }

    /**
     * returns the current symbol table. Should be called once the visit is complete
     * to return the constructed symbol table
     * @return
     */
    public SymbolTable getSymbolTable()
    {
        return sTable;
    }

    //visit a class node (defines a class and therefore a type)
    public Object visit(ClassNode node)
    {
        if (!sTable.canBeDeclared(node.getName()))
            error("Duplicate class definition: ", node);
        else
        {
            //the class's symbol table
            sTable = new SymbolTable(sTable, true);
            //create a new type to put in the table
            ClassSymbol newClass = new ClassSymbol(node.getName(), node.getSuperClass(), node, sTable);

            curClass = newClass;
            //visit the slots
            node.visitSlots(this);
            curClass = null;

            //restore the symbol table
            sTable = sTable.getParent();

            //add the class to the symbol table
            sTable.put(newClass);

            //node should remember the symbol
            node.setSymbol(newClass);
        }

        return null;
    }

    //visit a list node, visit its children
    public Object visit(ListNode node)
    {
        node.visitChildren(this);
        return null;
    }

    //visit a compound statement node and it to the symbol table
    public Object visit(CompoundNode node)
    {
        //create a symbol table for the compound statement
        sTable = new SymbolTable(sTable);

        //create an entry in the symbol table for the cs
        ScopeSymbol scope = ScopeSymbol.getInstance(sTable, node);
        //visit the statements in the compound statement
        node.visitChildren(this);

        //restore the symbol table
        sTable = sTable.getParent();

        //add the compound statement
        sTable.put(scope);

        node.setSymbol(scope);

        return null;
    }

    /**
     * visit a variable declaration node in the ast, add the variable to the symbol table
     * @param node
     * @return
     */
    public Object visit(DeclNode node)
    {
        VariableSymbol var = new VariableSymbol(node.getName(), node.getType(), node);

        if (canBeDeclared(var, node))
            node.setSymbol(var);

        return null;
    }

    /**
     * visits a field definition node in the ast and adds it to the symbol table
     * @param node a FieldNode to visit
     * @return null
     */
    public Object visit(FieldNode node)
    {
        FieldSymbol field = new FieldSymbol(node.getName(), node.getType(), node.getAccess(), node);

        if (canBeDeclared(field, node))
        {
            field.setOwner(curClass);
            field.setFieldNumber(NumberGenerator.getInstance().getFieldNumber(field));
            node.setSymbol(field);
        }

        return null;
    }

    //checks if a field or variable is a duplicate, if not adds it to the symbol table
    //used when visiting FieldNodes and DeclNodes
    private boolean canBeDeclared(Symbol symbol, Node node)
    {
        if (!sTable.canBeDeclared(symbol.getKey()))
        {
            error(symbol.getKind() + " has already been declared: ", node);
            return false;
        }
        else
        {
            //put the symbol in the symbol table
            sTable.put(symbol);
            return true;
        }
    }

    /**
     * visit a method definition node and add the method, its parameters and any variables in its body
     * to the symbol table
     * @param node a MethodNode to visit
     * @return null
     */
    public Object visit(MethodNode node)
    {
        visitMethod(node, "method", false);

        return null;
    }

    /**
     * Add a constructor to the symbol table and also its parameters and any declarations in its body
     * @param node The node in the ast representing the constructor
     * @return null
     */
    public Object visit(ConstructorNode node)
    {
        visitMethod(node, "constructor", true);

        return null;
    }

    //visit a method or constructor definition
    private void visitMethod(MethodNode node, String methodType, boolean constructor)
    {
        //change to the constructor's symbol table
        sTable = new SymbolTable(sTable, true);

        node.visitParams(this);
        MethodSymbol meth = new MethodSymbol(node.getName(), node.getType(), sTable.toArray(), node.getAccess(), node, sTable, constructor);
        node.visitBody(this);

        //restore the symbol table
        sTable = sTable.getParent();

        //check if the method is already defined, if not add it to the symbol table
        if (!sTable.canBeDeclared(meth.getKey()))
            error("Duplicate " + methodType + " definition: ", node);
        else
        {
            meth.setOwner(curClass);
            meth.setMethodNumber(NumberGenerator.getInstance().getMethodNumber(meth));
            sTable.put(meth);
            node.setSymbol(meth);
        }
    }

    /**
     * visits a node representing a single parameter to a method, adding it to the symbol table
     * @param node an ArgNode to visit
     * @return null
     */
    public Object visit(ArgNode node)
    {
        ParamSymbol param = new ParamSymbol(node.getName(), node.getType(), node);

        //check if the parameter is a duplicate, if not add it to the symbol table
        if (!sTable.canBeDeclared(param.getKey()))
            error("Duplicate parameter: ", node);
        else
        {
            sTable.put(param);
            node.setSymbol(param);
        }

        return null;
    }

    public Object visit(ForNode node)
    {
        //create a symbol table for the for statement
        sTable = new SymbolTable(sTable);

        //create an entry in the symbol table for the cs
        ScopeSymbol scope = ScopeSymbol.getInstance(sTable, node);
        //visit the initialisation and loop statements
        node.visitInitExpr(this);
        node.visitLoopStmt(this);

        //restore the symbol table
        sTable = sTable.getParent();

        //add the for statement
        sTable.put(scope);

        node.setSymbol(scope);

        return null;
    }

    public Object visit(IfNode node)
    {
        node.visitThen(this);
        return null;
    }



    //all the below methods do nothing, since none of these nodes define new symbols
    /////////////////////////////////////////////////////////////////////////////////////////////////
    public Object visit(StringNode node)
    {
        return null;
    }
    public Object visit(OrNode node)
    {
        return null;
    }
    public Object visit(PrintNode node)
    {
        return null;
    }
    public Object visit(VarNode node)
    {
        return null;
    }
    public Object visit(AssNode node)
    {
        return null;
    }
    public Object visit(FieldAccessNode node)
    {
        return null;
    }
    public Object visit(ConcatNode node)
    {
        return null;
    }
    public Object visit(DivNode node)
    {
        return null;
    }
    public Object visit(AddNode node)
    {
        return null;
    }
    public Object visit(SuperCallNode node)
    {
        return null;
    }
    public Object visit(ExitNode node)
    {
        return null;
    }
    public Object visit(NullNode node)
    {
        return null;
    }
    public Object visit(SubNode node)
    {
        return null;
    }
    public Object visit(MethodInvocNode node)
    {
        return null;
    }
    public Object visit(AndNode node)
    {
        return null;
    }
    public Object visit(NumberNode node)
    {
        return null;
    }
    public Object visit(EqNode node)
    {
        return null;
    }
    public Object visit(ReturnNode node)
    {
        return null;
    }
    public Object visit(MultNode node)
    {
        return null;
    }
    public Object visit(BooleanNode node)
    {
        return null;
    }
    public Object visit(NotEqNode node)
    {
        return null;
    }
    public Object visit(NegNode node)
    {
        return null;
    }
    public Object visit(NotNode node)
    {
        return null;
    }
    public Object visit(NewNode node)
    {
        return null;
    }
}