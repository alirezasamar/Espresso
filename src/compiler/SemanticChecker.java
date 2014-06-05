package compiler;

import ast.*;
import java.io.PrintWriter;
import utils.ParamUtils;
import java.util.Vector;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class SemanticChecker extends Visitor
{
    //pointer to the global symbol table
    private SymbolTable sTable;
    private SymbolTable curTable;

    private boolean superExpected = false;

    //the class that we are currently checking
    private ClassSymbol curClass = null;

    public SemanticChecker(SymbolTable sTable, PrintWriter out)
    {
        super(out);
        this.sTable = sTable;
        this.curTable = sTable;
    }


    //if we are visiting the value part of a declaration statement then this is
    //the variable being declared, otherwise its null
    private Symbol curDeclVar = null;

    public Object visit(DeclNode node)
    {
        checkSuperCall(node);

        visitFieldOrDecl("variable", node);

        return sTable.get("void");
    }

    public Object visit(FieldNode node)
    {
        visitFieldOrDecl("field", node);

        return sTable.get("void");
    }

    private void visitFieldOrDecl(String kind, NodeWithValue node)
    {
        //check that the parameter's type exists
        Symbol type = sTable.get(node.getType());
        Symbol var = curTable.get(node.getName());

        //variable should have been added to symbol table in the first pass
        if (var == null)
            throw new RuntimeException(kind + " should be in symbol table: " + node.getName());

        //check that the type exists
        if (type == null)
        {
            error("Type '" + node.getType() + "' does not exist: ", (Node)node);
            var.resolveType(sTable.get("unknown"));

            visitDeclValue(var, node);
        }
        else
        {
            //check the types match
            var.resolveType(type);

            Symbol valueType = visitDeclValue(var, node);
            if (!valueType.symbolEquals(type))
                error("Type mismatch in " + kind + " declaration. Found: '" + valueType.getName() + "', expected: '" + type.getName() + "'. ", (Node)node);

        }
    }

    private Symbol visitDeclValue(Symbol var, NodeWithValue node)
    {
        //work around to make sure a variable being declared isn't used in the value;
        curDeclVar = var;
        //check the value and its type
        Symbol valueType = (Symbol)node.visitValue(this);
        curDeclVar = null;

        return valueType;
    }

    //true if we are checking the lhs of an assignment
    private boolean inAssignment = false;
    public Object visit(AssNode node)
    {
        checkSuperCall(node);

        inAssignment = true;
        Symbol targetType = (Symbol)node.visitTarget(this);
        inAssignment = false;
        Symbol valueType = (Symbol)node.visitValue(this);

        if (targetType == null || targetType == sTable.get("unknown"))
            return curTable.get("unknown");

        //check the types match
        if (!valueType.symbolEquals(targetType))
            error("Type mismatch in declaration. Expected: '" + targetType.getName() + "', found: '" + valueType.getName() + "'. ", node);

        return targetType;
    }

    public Object visit(FieldAccessNode node)
    {
        //so we don't check for assignment to 'this' if we assign to one of this's field
        inAssignment = false;
        //check the target
        Symbol target = (Symbol)node.visitTarget(this);

        //check the target is OK
        if (target == sTable.get("unknown"))
            return sTable.get("unknown");

        //check the target has a method with the given parameters
        ClassSymbol targetClass = (ClassSymbol)target;
        FieldSymbol field = targetClass.getField(node.getName());
        if (field == null)
        {
            error("Field not defined: ", node);
            return sTable.get("unknown");
        }

        checkAccess(field, node);
        Symbol type = sTable.get(field.getType());

        node.setSymbol(field);
        node.setTargetSymbol(targetClass);

        if (type == null)
            return sTable.get("unknown"); //the error will be caught when the field declaration is visited

        return type;
    }

    //check's the field's access modifier to see if have access
    private void checkAccess(AccessSymbol slot, Node node)
    {
        //check we have access
        if (slot.getAccessModifier() == Modifier.PRIVATE && curClass != slot.getOwner())
            error(slot.getKind() + " '" + slot.getName() + "' has private access in class '" + slot.getOwner().getName() + "'. ", node);
        else if (slot.getAccessModifier() == Modifier.PROTECTED && !curClass.symbolEquals(slot.getOwner()))
            error(slot.getKind() + " '" + slot.getName() + "' has protected access in class '" + slot.getOwner().getName() + "'. ", node);
    }

    public Object visit(VarNode node)
    {
        //check for 'this'
        if (node.isThis())
        {
            if (inAssignment)
                error("Attempt to assign to 'this': ", node);

            node.setType(curClass);
            return curClass;
        }

        //check the variable is declared (in the symbol table)
        Symbol symbol = curTable.get(node.getName());
        if (symbol == null)
        {
            //check if its an inherited field
            symbol = curClass.getField(node.getName());
            if (symbol == null)
            {
                error("Variable has not been declared: ", node);
                return sTable.get("unknown");
            }
        }

        if (symbol instanceof FieldSymbol)
        {
            //if the variable is a field

            //check the target has a field with the given parameters
            FieldSymbol field = curClass.getField(node.getName());
            if (field == null)
            {
                error("Variable has not been declared: ", node);
                return sTable.get("unknown");
            }

            checkAccess(field, node);
            Symbol type = sTable.get(field.getType());

            if (type == null)
                return sTable.get("unknown");

            node.setType(type);
            node.setFieldSymbol(field);

            return type;
        }

        //check a variable being declared is not used in its own declaration
        if (symbol == curDeclVar)
        {
            error("Variable has not been declared: ", node);
            return sTable.get(symbol.getType());
        }
        //if it is a local variable check it is not a forward reference
        if (symbol instanceof VariableSymbol)
        {
            if (symbol.getLineNumber() > node.getLineNumber())
            {
                error("Variable has not been declared: ", node);
                return sTable.get("unknown");
            }
            else if (symbol.getLineNumber() == node.getLineNumber() && symbol.getColumnNumber() >= node.getColumnNumber())
            {
                error("Variable has not been declared: ", node);
                return sTable.get("unknown");
            }
        }
        //check the kind
        if (!(symbol instanceof VarSymbol || symbol instanceof FieldSymbol))
        {
            error("Variable has incorrect kind: '" + symbol.getKind() + "'. Variable: ", node);
            return sTable.get("unknown");
        }

        //store the variable's static type in the node
        //won't be null since the type has already been checked when the var was declared
        node.setType(sTable.get(symbol.getType()));
        node.setSymbol((VarSymbol)symbol);

        //return the variable's static type
        return node.getTypeSymbol();
    }

    public Object visit(CompoundNode node)
    {
        //move scope
        curTable = node.getSymbol().getSymbolTable();

        node.visitChildren(this);

        //restore the scope
        curTable = curTable.getParent();

        return sTable.get("void");
    }

    public Object visit(NewNode node)
    {
        checkSuperCall(node);

        Symbol className = sTable.get(node.getClassName());

        //check the class exists
        if (className == null || !(className instanceof ClassSymbol))
        {
            error("No such class: '" + node.getClassName() + "'. ", node);
            return sTable.get("unknown");
        }
        ClassSymbol classSymbol = (ClassSymbol)className;

        //check the parameters
        String[] typeNames = ParamUtils.makeTypeArray(node.visitParams(this));

        //check an approriate constructor exists
        MethodSymbol methodSymbol = classSymbol.getConstructor(typeNames);
        if (methodSymbol == null)
        {
            error("Constructor not found: '" + classSymbol.getName() + " (" + ParamUtils.makeList(typeNames) + ")'. ", node);
            return(className);
        }

        //check we have access to it
        checkAccess(methodSymbol, node);

        node.setSymbol(methodSymbol);
        node.setClassSymbol(classSymbol);

        return className;
    }

    public Object visit(SuperCallNode node)
    {
        if (!superExpected)
            error("Method super() must be the first statement in a constructor. ", node);
        superExpected = false;

        //check the super class exists
        ClassSymbol targetClass = curClass.getSuperClass();
        if (targetClass == null)
        {
            error("No declared super class in '" + curClass.getName() + "'. ", node);
            return sTable.get("void");
        }

        //check the parameters
        String[] typeNames = ParamUtils.makeTypeArray(node.visitParams(this));

        //check an appropriate constructor exists
        MethodSymbol methodSymbol = targetClass.getConstructor(typeNames);
        if (methodSymbol == null)
        {
            error("Constructor not found: '" + targetClass.getName() + " (" + ParamUtils.makeList(typeNames) + ")'. ", node);
            return sTable.get("void");
        }

        //check we have access
        checkAccess(methodSymbol, node);

        node.setSymbol(methodSymbol);

        return sTable.get("void");
    }

    //only checks the method exists statically, the actual method to call is
    //determined at run time
    public Object visit(MethodInvocNode node)
    {
        checkSuperCall(node);

        //check the parameters
        String[] typeNames = ParamUtils.makeTypeArray(node.visitParams(this));

        //check the target
        Symbol target = (Symbol)node.visitTarget(this);
        if (!(target instanceof ClassSymbol))
        {
            error("Method not defined: '" + node.getName() + " (" + ParamUtils.makeList(typeNames) + ")'. ", node);
            return sTable.get("unknown");
        }

        //check the target has a method with the given parameters
        ClassSymbol targetClass = (ClassSymbol)target;
        MethodSymbol method = targetClass.getMethod(node.getName(), typeNames);
        if (method == null)
        {
            error("Method not defined: '" + node.getName() + " (" + ParamUtils.makeList(typeNames) + ")'. ", node);
            return sTable.get("unknown");
        }

        //check we have access
        checkAccess(method, node);

        //set the static method in the node
        node.setSymbol(method);

        //return the method's return type
        Symbol returnType = sTable.get(method.getType());
        if (returnType == null)
            return sTable.get("unknown"); //the error will be caught when the method is resolved
        else
            return returnType;
    }

    public Object visit(ArgNode node)
    {
        //check that the parameter's type exists
        Symbol type = sTable.get(node.getType());
        Symbol arg = curTable.get(node.getName());

        //variable should have been added to symbol table in the first pass
        if (arg == null)
            throw new RuntimeException("Parameter should be in symbol table: " + node.getName());

        //check that the type exists
        if (type == null)
        {
            error("Type '" + node.getType() + "' does not exist: ", node);
            arg.resolveType(sTable.get("unknown"));
        }
        else
            arg.resolveType(type);

        return sTable.get("void");
    }

    public Object visit(ClassNode node)
    {
        //change scope to the class's scope
        curTable = node.getSymbol().getSymbolTable();

        curClass = node.getSymbol();

        //visit the methods and fields of the class
        node.visitSlots(this);

        curClass = null;

        //restore the global scope
        curTable = sTable;

        return sTable.get("void");
    }

    //the return type for the current node
    private Symbol returnType = null;
    //weather there is a return type in the method
    private boolean returned = false;

    public Object visit(MethodNode node)
    {
        //get the symbol and set up the symbol table
        MethodSymbol symbol = node.getSymbol();
        curTable = symbol.getSymbolTable();

        //check the return type exists
        Symbol returnType = sTable.get(node.getType());
        if (returnType == null)
        {
            error("Type '" + node.getType() + "' does not exist: ", node);
            symbol.resolveType(sTable.get("unknown"));
        }
        else
            symbol.resolveType(returnType);


        visitParamsAndBody(node, returnType);

        //make sure there's at elast one return statement if the type is not void
        if (!returned && returnType != sTable.get("void"))
            error("Method does not return. ", node);

        return sTable.get("void");
    }

    public Object visit(ConstructorNode node)
    {
        MethodSymbol symbol = node.getSymbol();
        curTable = symbol.getSymbolTable();

        //check the constructor is correctly named
        if(!node.getName().equals(curClass.getName()))
        {
            error("Method does not declare a return type: ", node);
            return sTable.get("void");
        }

        //resolve the type - trivial
        symbol.resolveType(sTable.get("void"));

        superExpected = true;
        visitParamsAndBody(node, sTable.get("void"));
        checkSuperCall(node);

        superExpected = false;

        return sTable.get("void");
    }

    //visits the parameters and body of a method or constructor
    private void visitParamsAndBody(MethodNode node, Symbol returnType)
    {
        //check the parameters
        node.visitParams(this);

        //setup return type checking
        returned = false;
        this.returnType = returnType;

        node.visitBody(this);

        curTable = curTable.getParent();
    }

    //checks that super call is made as the first statement in a constructor
    //and not elsewhere
    private void checkSuperCall(Node node)
    {
        if (superExpected)
            error("First statement in constructor must be super call. ", node);

        superExpected = false;
    }

    public Object visit(ListNode node)
    {
        node.visitChildren(this);
        return sTable.get("void");
    }

    public Object visit(ForNode node)
    {
        checkSuperCall(node);

        curTable = node.getSymbol().getSymbolTable();

        node.visitInitExpr(this);
        Symbol testType = (Symbol) node.visitTestExpr(this);
        node.visitIncExpr(this);

        if (testType != sTable.get("boolean"))
            error("Test expression in for statement must be of type boolean, found: '" + testType.getKey() + "' in expression: ", node);

        node.visitLoopStmt(this);
        curTable = curTable.getParent();

        return sTable.get("void");
    }

    public Object visit(IfNode node)
    {
        checkSuperCall(node);

        Symbol testType = (Symbol) node.visitTest(this);

        if (testType != sTable.get("boolean"))
            error("Test expression in if statement must be of type boolean, found: '" + testType.getKey() + "' in expression: ", node);

        node.visitThen(this);

        return sTable.get("void");
    }

    public Object visit(PrintNode node)
    {
        checkSuperCall(node);

        Symbol valueType = (Symbol)node.visitValue(this);

        //type of the expression in the print statement must not be void
        if (valueType == sTable.get("void"))
            error("Argument to print statement must have type: found 'void'. ", node);

        node.setType(valueType.getName());

        return sTable.get("void");
    }

    public Object visit(ReturnNode node)
    {
        checkSuperCall(node);

        Object valueType = node.visitValue(this);

        if (valueType == null)
        {
            if (returnType != sTable.get("void"))
                error("Return type does not match method declaration expected: '" + returnType.getName() + "' found: 'void'. ", node);
        }
        else
        {
            if (valueType instanceof TypeSymbol)
            {
                if (returnType instanceof ClassSymbol && valueType == sTable.get("null"))
                {
                    //this is OK - expects an object, but gets null
                }
                else if (valueType != returnType)
                    error("Return type does not match method declaration expected: '" + returnType.getName() + "' found: '" + ((TypeSymbol)valueType).getName() + "'. ", node);
            }
            else if (!((ClassSymbol)valueType).symbolEquals(returnType))
                error("Return type does not match method declaration expected: '" + returnType.getName() + "' found: '" + ((ClassSymbol)valueType).getName() + "'. ", node);
        }

        returned = true;

        return sTable.get("void");
    }

    public Object visit(ExitNode node)
    {
        checkSuperCall(node);

        return sTable.get("void");
    }

    public Object visit(ConcatNode node)
    {
        Symbol leftType = (Symbol) node.visitLeft(this);
        Symbol rightType = (Symbol) node.visitRight(this);

        if (leftType == sTable.get("void"))
            error("Left argument to operator ++ must have type, found: 'void' in expression: ", node);
        if (rightType == sTable.get("void"))
            error("Right argument to operator ++ must have type, found: 'void' in expression: ", node);

        node.setLeftType(leftType.getName());
        node.setRightType(rightType.getName());

        return sTable.get("String");
    }

    public Object visit(DivNode node)
    {
        return visitBinaryOp("/", "int", node);
    }

    public Object visit(AddNode node)
    {
        return visitBinaryOp("+", "int", node);
    }

    public Object visit(SubNode node)
    {
        return visitBinaryOp("-", "int", node);
    }

    public Object visit(MultNode node)
    {
        return visitBinaryOp("*", "int", node);
    }

    public Object visit(OrNode node)
    {
        return visitBinaryOp("||", "boolean", node);
    }

    public Object visit(AndNode node)
    {
        return visitBinaryOp("&&", "boolean", node);
    }

    //shared functionality for &&, ||, +, -, / and * operators
    private Symbol visitBinaryOp(String op, String type, BinaryNode node)
    {
        Symbol leftType = (Symbol) node.visitLeft(this);
        Symbol rightType = (Symbol) node.visitRight(this);

        if (leftType != sTable.get(type))
            error("Left argument to operator " + op + " must be of type " + type + ", found: '" + leftType.getKey() + "' in expression: ", node);
        if (rightType != sTable.get(type))
            error("Right argument to operator " + op + " must be of type " + type + ", found: '" + rightType.getKey() + "' in expression: ", node);

        return sTable.get(type);
    }

    public Object visit(EqNode node)
    {
        return visitEqStyleNode("==", node);
    }

    public Object visit(NotEqNode node)
    {
        return visitEqStyleNode("!=", node);
    }

    //common functionality for == and != operators
    private Symbol visitEqStyleNode(String op, BinaryNode node)
    {
        Symbol leftType = (Symbol) node.visitLeft(this);
        Symbol rightType = (Symbol) node.visitRight(this);

        if (leftType == sTable.get("void") || rightType == sTable.get("void"))
            error("Arguments to operator " + op + " must have type, found: 'void' in expression: ", node);
        if (!((leftType == sTable.get("int") && rightType == sTable.get("int"))
            || (leftType == sTable.get("boolean") && rightType == sTable.get("boolean"))
            || (leftType.symbolEquals(sTable.get("Object")) && rightType.symbolEquals(sTable.get("Object")))))
            error("Type mis-match in operator " + op + ", found: '" + leftType.getKey() + "' and '" + rightType.getKey() + "' in expression: ", node);

        return sTable.get("boolean");
    }

    public Object visit(NegNode node)
    {
        return visitUnaryOp("-", "int", node);
    }

    public Object visit(NotNode node)
    {
        return visitUnaryOp("!", "boolean", node);
    }

    //shared functionality for - and ! operators
    private Symbol visitUnaryOp(String op, String type, UnaryNode node)
    {
        Symbol childType = (Symbol)node.visitChild(this);

        if (childType != sTable.get(type))
            error("Argument to operator " + op + " must be of type " + type + ", found: '" + childType.getKey() + "' in expression: ", node);

        return sTable.get(type);
    }

    public Object visit(BooleanNode node)
    {
        return sTable.get("boolean");
    }

    public Object visit(NumberNode node)
    {
        return sTable.get("int");
    }

    public Object visit(StringNode node)
    {
        return sTable.get("String");
    }

    public Object visit(NullNode node)
    {
        return sTable.get("null");
    }
}