package compiler;

import ast.*;
import java.io.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)


 * local variables:
 * 1: the heap
 * 2: the heap pointer
 * 3: the String pool
 * 4: the string pool pointer
 * 5..8: registers
 */

public class CodeGenerator
{
    //the output of the code generator
    StringBuffer out = new StringBuffer();
    private SymbolTable sTable; //the top level symbol table

    private int maxHeapSize; //the size of the heap allocated
    private int maxStringPoolSize; //the size of the string pool
    private int maxStackSize; //the maximum stack depth

    private CodeGenVisitor visitor;


    /**
     * Creates a new CodeGenerator object
     * @param st The global symbol table for the program
     * @param err a PrintWriter to output error messages to
     * @param maxHeapSize the size of the heap to allocate, -1 to use default
     * @param maxStringPoolSize the size of the strign pool to allocate, -1 to use default
     */
    public CodeGenerator(SymbolTable st, PrintWriter err, int maxHeapSize, int maxStringPoolSize)
    {
        visitor = new CodeGenVisitor(st, err, this);

        sTable = st;
        if (maxHeapSize < 0)
            this.maxHeapSize = 10000;
        else
            this.maxHeapSize = maxHeapSize;

        if (maxStringPoolSize < 0)
            this.maxStringPoolSize = 500;
        else
            this.maxStringPoolSize = maxStringPoolSize;

        this.maxStackSize = 50;
    }

    public int getErrorCount()
    {
        return visitor.getErrorCount();
    }

    /**
     * Get the output of code generation
     * @param className the name of the class (eg Main)
     * @param sourceName the name of the source file (eg Main.jmm)
     * @return the empty String if an error occured, otherwise a String representing
     * the program in Jasmin assembler format
     */
    public String generate(String className, String sourceName, Node root)
    {
        assignClassNumbers();

        root.accept(visitor);

        if (visitor.getErrorCount() > 0)
            return "";

        int localsCount = NumberGenerator.getInstance().getMaxLocals();

        //reset the StringBuffer
        out.setLength(0);
        out.append(".source " + sourceName + "\n");
        out.append(".class public " + className + "\n");
        out.append(".super java/lang/Object\n\n");

        out.append(".method public <init>()V\n");
        out.append("aload_0\n");
        out.append("invokenonvirtual java/lang/Object/<init>()V\n");
        out.append("return\n");
        out.append(".end method\n\n");

        out.append(".method public static main([Ljava/lang/String;)V\n");
        out.append(".limit stack 16\n");
        out.append(".limit locals " + localsCount + "\n");
        out.append(".throws java/lang/Exception\n");

        //heap declaration
        out.append("\n;heap---------------------\n");
        out.append("ldc " + maxHeapSize +"\n");     //the array
        out.append("newarray int\n");
        out.append("astore 1\n");

        out.append("iconst_0\n");  //the current index into the array
        out.append("istore 2\n");

        //String pool
        out.append("\n;String pool---------------------\n");
        out.append("ldc " + maxStringPoolSize +"\n");     //the array
        out.append("anewarray java/lang/String\n");
        out.append("astore 3\n");

        out.append("iconst_0\n");  //the current index into the array
        out.append("istore 4\n");

        //the stack
        out.append("\n;stack---------------------\n");
        out.append("ldc " + maxStackSize +"\n");     //the array
        out.append("newarray int\n");
        out.append("astore 9\n");

        out.append("iconst_0\n");  //the stack pointer
        out.append("istore 10\n");

        //the 'registers'
        out.append("\n;'registers'---------------------\n");
        out.append("iconst_0\n");
        out.append("dup\n");
        out.append("dup\n");
        out.append("dup\n");
        out.append("istore 5\n");
        out.append("istore 6\n");
        out.append("istore 7\n");
        out.append("istore 8\n");
        out.append("bipush -1\n");  //this
        out.append("istore 0\n");

        //initialise any local variables
        for (int i = 11; i < localsCount; ++i)
        {
            out.append("iconst_0\n");
            out.append("istore " + i + "\n");
        }

        appendClassDecls();

        //entry point
        appendEntryPoint();

        //String and Object methods
        appendObjectProcs();
        appendStringProcs();
        //user methods
        out.append(visitor.getOutput());

        //jump table
        appendJumpTable();
        //return table
        appendReturnTable();

        //deal with errors
        out.append("\nerrorLabel:\n");  //stack depth = 0
        out.append("new java/lang/Exception\n");
        out.append("dup\n");
        out.append("ldc \"An unknown error occured during execution of " + className + ".class\"\n");
        out.append("invokenonvirtual java/lang/Exception/<init>(Ljava/lang/String;)V\n");
        out.append("athrow\n");

        out.append("\nerrorMsg:\n");   //stack depth = 1
        out.append("astore 5\n");  //expects a String on top of the stack
        out.append("new java/lang/Exception\n");
        out.append("dup\n");
        out.append("aload 5\n");
        out.append("invokenonvirtual java/lang/Exception/<init>(Ljava/lang/String;)V\n");
        out.append("athrow\n");

        out.append("\nreturn\n");
        out.append(".end method\n");

        return out.toString();
    }

    private void assignClassNumbers()
    {
        Symbol[] symbols = sTable.toArray();

        //assign class numbers to each class
        int classNumber = 0;
        for (int i = 0; i < symbols.length; ++i)
            if (symbols[i] instanceof ClassSymbol)
                ((ClassSymbol)symbols[i]).setClassNumber(classNumber++);

        //the first classNumber ints in the heap are the addresses of the class declarations
        //for the class with that class number

        //curIndex is the next index into the heap
        curHeapIndex = classNumber;
    }

    //read all the classes from the symbol table and put code to create their descriptors
    //in the output
    private void appendClassDecls()
    {
        Symbol[] symbols = sTable.toArray();

        //all the user defined classes
        for (int i = 0; i < symbols.length; ++i)
        {
            if (symbols[i] instanceof ClassSymbol)
            {
                 ClassSymbol classSymbol = (ClassSymbol)symbols[i];

                 out.append(";" + classSymbol.getName() + " class descriptor-----------------------------\n");

                 //store the index to the class declaration for this class
                 out.append("aload 1\n");
                 out.append("bipush " + classSymbol.getClassNumber() + "\n");
                 out.append("bipush " + curHeapIndex + "\n");
                 out.append("iastore\n");

                 //store the number of fields
                 storeOnHeap(classSymbol.getNumberOfFields());
                 //store the number of methods
                 storeOnHeap(classSymbol.getNumberOfMethods());
                 //methods (the class's vTable)
                 for (int j = 0; j < classSymbol.getNumberOfMethods(); ++j)
                     storeOnHeap(classSymbol.getMethodNumber(j));

                 //store the super class number
                 if (classSymbol.getSuperClass() == null)
                     storeOnHeap(-1);
                 else
                     storeOnHeap(classSymbol.getSuperClass().getClassNumber());

                 out.append(";End of " + classSymbol.getName() + " class descriptor-----------------------------\n");
            }
        }

        //position the heap index at the end of the class declarations
        out.append("bipush " + curHeapIndex + "\n");
        out.append("istore 2\n");
    }

    private int curHeapIndex = 0;
    //only good when the heap index is statically known, ie at startup
    private void storeOnHeap(int x)
    {
        out.append("aload 1\n");  //push the heap onto the stack
        out.append("bipush " + curHeapIndex++ + "\n");  //push the next available index onto the stack
        out.append("bipush " + x + "\n");  //push the value to store onto the stack
        out.append("iastore\n");  //store it in the heap
    }

    //jump table for the methods, allows us to use 'method pointers' for method despatch
    //and translate to labels
    private void appendJumpTable()
    {
        /*
        0:          Object.init()
        1: String   Object.toString()
        2:          String.init()
        3:          String.init(String)
        4: String   String.toString()
        5: int      String.length()
        6: String   String.charAt(int)
        */

        out.append("\n;Jump table----------------------------------\n");  //stack depth = 1, jump address
        out.append("jumpTable:\n");
        out.append("tableswitch 0\n");
        for (int i = 0; i < NumberGenerator.getInstance().getTotalMethods(); ++i)
            out.append("  " + NumberGenerator.getInstance().makeMethodLabel(i) + "\n");

        out.append("  default : errorLabel\n");

        out.append(";end of jump table---------------------------\n");
    }

    //the return table is used to return to the point of method call
    //the return value is on top of the stack, followed by the
    //the return 'address' and we jump to the appropriate label
    private void appendReturnTable()
    {
        out.append("\n;Return table----------------------------------\n");
        out.append("returnTable:\n");
        popToStack(out);  //pop from the stack to the operand stack the return value
        popToStack(out);   //pop from the stack to the operand stack the return address
        out.append("swap\n"); //swap the return value and return address
        pushFromStack(out);  //put the return value back on the stack
        out.append("tableswitch 0\n");
        for (int i = 0; i < NumberGenerator.getInstance().getTotalRetAdds(); ++i)
            out.append("  " + NumberGenerator.getInstance().makeRetAdd(i) + "\n");

        out.append("  default : errorLabel\n");

        out.append(";end of return table---------------------------\n");
    }


    private void appendObjectProcs()
    {
        /*
        0:          Object.init()
        1: String   Object.toString()
        */

        //Object.init()
        out.append("\n;Object----------------------------------\n");

        out.append(";Object.init()-----------------------------\n");
        out.append("proc0:\n");
        //pop this
        pop(out);
        //do nothing, return 0
        pushConst(0, out);
        out.append("goto returnTable\n");
        out.append(";end of Object.init()-----------------------------\n");

        //Object.toString()
        out.append("\n;Object.toString()-----------------------------\n");
        out.append("proc1:\n");
        //pop this
        pop(out);

        //make the String classNumber@address
        out.append("new java/lang/StringBuffer\n");
        out.append("dup\n");
        out.append("invokenonvirtual java/lang/StringBuffer/<init>()V\n");
        out.append("aload 1\n");  //push the 'this' pointer
        out.append("iload 0\n");
        out.append("iaload\n");  //get the class number
        out.append("invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;\n");
        out.append("ldc \"@\"\n");
        out.append("invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;\n");
        out.append("iload 0\n");
        out.append("invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;\n");
        out.append("invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;\n");

        newString(out);

        pushFromStack(out);

        out.append("goto returnTable\n");
        out.append(";end of Object.toString()-----------------------------\n");

        out.append(";end of Object---------------------------\n");

    }

    private void appendStringProcs()
    {
        out.append("\n;String----------------------------------\n");
        out.append(";field 0-----------------------------\n");
        out.append("fieldInit0:\n");
        //doesn't do alot, just leaves -1 on the stack, ie null pointer
        pushConst(-1, out);
        out.append("goto returnTable\n");
        out.append(";end of String.fieldInit0-----------------------------\n");

        /*
        2:          String.init()
        3:          String.init(String)
        4: String   String.toString()
        5: int      String.length()
        6: String   String.charAt(int)
        */

        //default constructor
        out.append(";String.init()-----------------------------\n");
        out.append("proc2:\n");
        //pop this
        pop(out);
        //do nothing
        pushConst(0, out);
        out.append("goto returnTable\n");
        out.append(";end of String.init()-----------------------------\n");

        //copy constructor
        out.append("\n;String.init(String)-----------------------------\n");
        out.append("proc3:\n");

        //pop the param
        popToStack(out);
        //pop this
        popToStack(out);
        out.append("istore 0\n");

        out.append("aload 1\n");
        out.append("swap\n");
        out.append("iconst_2\n"); //TODO field fetch, don't assume its in position 2
        out.append("iadd\n");
        out.append("iaload\n"); //load the String pointer from the heap (the other String's field)
        out.append("istore 7\n");

        out.append("aload 1\n");
        out.append("iload 0\n");
        out.append("iconst_2\n");
        out.append("iadd\n");
        out.append("iload 7\n");
        out.append("iastore\n"); //store it again in this String's field

        pushConst(0, out); //return 0
        out.append("goto returnTable\n");
        out.append(";end of String.init(String)-----------------------------\n");


        //toString
        out.append("\n;String.toString()-----------------------------\n");
        out.append("proc4:\n");
        //return this - we just leave this on top of the stack
        out.append("goto returnTable\n");
        out.append(";end of String.toString()-----------------------------\n");

        //length
        /*
        if (storage == null)
            return 0;
        else
            return storage.length;
        */
        out.append("\n;String.length()-----------------------------\n");
        out.append("proc5:\n");

        popToStack(out);
        //get the string pointer
        out.append("iconst_2\n");
        out.append("iadd\n");
        out.append("aload 1\n");
        out.append("swap\n");
        out.append("iaload\n");

        //check for null field
        out.append("dup\n");
        out.append("istore 7\n"); //save the string pool pointer
        out.append("bipush -1\n");
        String notEqLabel = NumberGenerator.getInstance().getLabel();
        String endLabel = NumberGenerator.getInstance().getLabel();
        out.append("if_icmpne " + notEqLabel + "\n"); //jump if not equal
        pushConst(0, out);  //null                                     |

        out.append("goto " + endLabel + "\n");  //jump to end label ---+--+
        out.append(notEqLabel + ":\n");   //       <-------------------+  |
        //non-null pointer
        //get the string pointer
        out.append("aload 3\n");
        out.append("iload 7\n");
        out.append("aaload\n");

        //call length and leave the result on the stack
        out.append("invokevirtual java/lang/String/length()I\n");

        pushFromStack(out);                                            // |
        out.append(endLabel + ":\n");        //    <----------------------+
        out.append("goto returnTable\n");
        out.append(";end of String.length()-----------------------------\n");

        //charAt
        /*
        return new String(new StringBuffer(storage.charAt(i)).toString());
        */
        out.append("\n;String.charAt(int)-----------------------------\n");
        out.append("proc6:\n");

        //save the parameter
        popToStack(out);
        out.append("istore 7\n");

        popToStack(out);


        //get the string pointer
        out.append("iconst_2\n");
        out.append("iadd\n");
        out.append("aload 1\n");
        out.append("swap\n");
        out.append("iaload\n");
        //check for null field
        out.append("dup\n");
        out.append("istore 8\n"); //save the string pool pointer
        out.append("bipush -1\n");
        notEqLabel = NumberGenerator.getInstance().getLabel();
        endLabel = NumberGenerator.getInstance().getLabel();
        out.append("if_icmpne " + notEqLabel + "\n"); //jump if not equal
        out.append("bipush -1\n"); //null                           //  |
        out.append("goto " + endLabel + "\n");  //jump to end label ---+--+
        out.append(notEqLabel + ":\n");   //       <-------------------+  |
        //non-null pointer
        //get the string pointer
        out.append("aload 3\n");
        out.append("iload 8\n");
        out.append("aaload\n");
        out.append("iload 7\n");  //reload the parameter
        //call char and leave the result on the stack
        out.append("invokevirtual java/lang/String/charAt(I)C\n");
        //create a new StringBuffer
        out.append("new java/lang/StringBuffer\n");
        out.append("dup\n");
        out.append("invokenonvirtual java/lang/StringBuffer/<init>()V\n"); //call its constructor
        //add the char
        out.append("swap\n");
        out.append("invokevirtual java/lang/StringBuffer/append(C)Ljava/lang/StringBuffer;\n");
        //call toString
        out.append("invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;\n");
        //make a new String around the String
        newString(out);

                                                                       // |
        out.append(endLabel + ":\n");        //    <----------------------+
        pushFromStack(out);
        out.append("goto returnTable\n");
        out.append(";end of String.charAt(int)-----------------------------\n");

        out.append(";end of String---------------------------\n");
    }

    //assumes a java.lang.String is on top of the operand stack
    //creates a new String object in the heap and leaves a reference to it on the stack
    public void newString(StringBuffer out)
    {
        out.append("; create a new String object\n");

        //store the string in the string pool
        out.append("aload 3\n");
        out.append("swap\n");
        out.append("iload 4\n");
        out.append("swap\n");
        out.append("aastore\n");
        out.append("iload 4\n");
        out.append("istore 7\n");  //keep the address of the new String in register 5
        out.append("iinc 4 1\n"); //increment the string pool pointer

        //make a new String object and leave it on the stack
        ClassSymbol string = (ClassSymbol)sTable.get("String");

        //save the heap index, this will become the address for the new object
        out.append("iload 2\n");
        out.append("istore 8\n");

        //class descriptor index
        out.append("aload 1\n");  //push the heap onto the stack
        out.append("iload 2\n");  //push the heap index onto the stack
        out.append("ldc " + string.getClassNumber() + "\n");  //push the value to store onto the stack
        out.append("iastore\n");  //store it in the heap
        out.append("iinc 2 1\n"); //increment the heap index

        //number of fields
        out.append("aload 1\n");  //push the heap onto the stack
        out.append("iload 2\n");  //push the heap index onto the stack
        out.append("iconst_1\n");  //push the value to store onto the stack
        out.append("iastore\n");  //store it in the heap
        out.append("iinc 2 1\n"); //increment the heap index

        //the field
        out.append("aload 1\n");
        out.append("iload 2\n");
        out.append("iload 7\n");
        out.append("iastore\n");
        out.append("iinc 2 1\n");

        out.append("iload 8\n");
    }

    public static void newObject(ClassSymbol classSymbol, StringBuffer out)
    {
        pushVar(5, out);

        //create the object descriptor on the stack

        //save the heap index, this will become the address for the new object
        out.append("iload 2\n");
        out.append("istore 5\n");

        //class descriptor index
        out.append("aload 1\n");  //push the heap onto the stack
        out.append("iload 2\n");  //push the heap index onto the stack
        out.append("ldc " + classSymbol.getClassNumber() + "\n");  //push the value to store onto the stack
        out.append("iastore\n");  //store it in the heap
        out.append("iinc 2 1\n"); //increment the heap index
        //number of fields
        out.append("aload 1\n");  //push the heap onto the stack
        out.append("iload 2\n");  //push the heap index onto the stack
        out.append("ldc " + classSymbol.getNumberOfFields() + "\n");  //push the value to store onto the stack
        out.append("iastore\n");  //store it in the heap
        out.append("iinc 2 1\n"); //increment the heap index

        //the fields----------------------------
        //store the current heap index (becomes the local heap pointer)
        pushVar(2, out);
        //point to after the object record so any field initialisers write in the proper
        //place on the heap
        out.append("iinc 2 " + classSymbol.getNumberOfFields() + "\n");

        //goto the initialiser for the field
        for (int i = 0; i < classSymbol.getNumberOfFields(); ++i)
        {
            //jump to the field initialiser
            int retAdd = NumberGenerator.getInstance().getRetAddress();
            pushConst(retAdd, out);
            out.append("goto fieldInit" + classSymbol.getFieldNumber(i) + "\n");
            out.append(NumberGenerator.getInstance().makeRetAdd(retAdd) + ":\n"); //return label

            popToStack(out);
            out.append("istore 7\n"); //store the return value in a register
            popToStack(out);
            out.append("istore 8\n"); //store the local heap pointer in another register
            out.append("aload 1\n");  //push the heap onto the stack
            out.append("iload 8\n");  //push the local heap index onto the stack
            out.append("iload 7\n");  //push the value to store onto the stack
            out.append("iastore\n");  //store it in the heap

            out.append("iinc 8 1\n"); //increment the local heap pointer
            pushVar(8, out); //and put the local heap pointer back on the stack
        }
        //pop the local heap pointer back off the stack
        pop(out);

        out.append("iload 5\n"); //and put the index to the object on the stack
        popToVar(5, out);
    }


    private void appendEntryPoint()
    {
        ClassSymbol classSymbol = (ClassSymbol)sTable.get("Main");
        MethodSymbol constructor = (MethodSymbol)classSymbol.getSymbolTable().get("Main ()");
        MethodSymbol method = (MethodSymbol)classSymbol.getSymbolTable().get("main ()");

        out.append("\n;Entry point------------------------------------------\n");
        newObject(classSymbol, out);  //create an instance of Main

        out.append("istore 5\n");


        //call the constructor

        //push the return address
        int retAdd = NumberGenerator.getInstance().getRetAddress();
        pushConst(retAdd, out);

        //push the target, to become 'this'
        pushVar(5, out);

        //figure out the method number (trivial)
        out.append("ldc " + constructor.getMethodNumber() + "\n");  //now have the method number

        //use the jump table
        out.append("goto jumpTable\n");

        //the return label
        out.append(NumberGenerator.getInstance().makeRetAdd(retAdd) + ":\n");

        //pop the result (void so we can ignore it)
        pop(out);

        //call main(), we don't need to do virtual dispatch here
        //push the return address
        retAdd = NumberGenerator.getInstance().getRetAddress();
        pushConst(retAdd, out);

        //push the target, to become 'this'
        pushVar(5, out);

        //figure out the method number (trivial)
        out.append("ldc " + method.getMethodNumber() + "\n");  //now have the method number

        //use the jump table
        out.append("goto jumpTable\n");

        //the return label
        out.append(NumberGenerator.getInstance().makeRetAdd(retAdd) + ":\n");

        //ignore the result
        pop(out);

        // exit using System.exit(0);
        out.append("iconst_0\n");
        out.append("invokestatic java/lang/System/exit(I)V\n");
        out.append(";end of entry point------------------------------------\n");
    }

    public static void popToStack(StringBuffer out)
    {
        out.append("iinc 10 -1\n");
        out.append("aload 9\n");
        out.append("iload 10\n");
        out.append("iaload\n");
    }

    public static void popToVar(int v, StringBuffer out)
    {
        out.append("iinc 10 -1\n");
        out.append("aload 9\n");
        out.append("iload 10\n");
        out.append("iaload\n");
        out.append("istore " + v + "\n");
    }

    public static void pop(StringBuffer out)
    {
        out.append("iinc 10 -1\n");
    }

    public static void pushVar(int i, StringBuffer out)
    {
        out.append("aload 9\n");
        out.append("iload 10\n");
        out.append("iload " + i + "\n");
        out.append("iastore\n");
        out.append("iinc 10 1\n");
    }

    //public static void pushConst(String s, StringBuffer out)
    //{
    //}

    public static void pushConst(int i, StringBuffer out)
    {
        out.append("aload 9\n");
        out.append("iload 10\n");
        out.append("ldc " + i + "\n");
        out.append("iastore\n");
        out.append("iinc 10 1\n");
    }

    public static void pushFromStack(StringBuffer out)
    {
        out.append("aload 9\n");
        out.append("swap\n");
        out.append("iload 10\n");
        out.append("swap\n");
        out.append("iastore\n");
        out.append("iinc 10 1\n");
    }

    //public static void pushFromHeap(int i, StringBuffer out)
    //{
    //}
}