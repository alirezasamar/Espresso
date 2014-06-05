package compiler;

import ast.Node;
import java.io.*;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class Compiler
{
    //abstract syntax tree to compile
    private Node root;
    //feedback output
    private PrintWriter err;

    /**
     * create a new compiler to compile a program
     * @param ast the program to compile, supplied as the root node of its ast
     * @param err PrintWriter to log/display feedback (eg errors)
     */
    public Compiler(Node ast, PrintWriter err)
    {
        root = ast;
        this.err = err;

        NumberGenerator.resetInstance();
    }

    /**
     * compile the program to Java assembler code
     */
    public String compile(String className, String sourceName, int heapSize, int stringPoolSize)
    {
        SymbolTable sTable = checkSemantics();
        if (sTable == null)
            return null;

        String asm = produceAsm(sTable, className, sourceName, heapSize, stringPoolSize);
        if (asm == null)
            return null;

        return asm;
    }

    //check the semantics of the given program
    private SymbolTable checkSemantics()
    {
        //1st pass: build the symbol table
        SymbolTableConstructor stc = new SymbolTableConstructor(err);
        root.accept(stc);
        int errors = stc.getErrorCount();
        if (errors > 0)
        {
            err.println("Semantic analysis failed with " + errors + " error(s) during symbol table construction");
            return null;
        }
        SymbolTable symbolTable = stc.getSymbolTable();

        //all classes are now in the super table, resolve any super class references
        boolean success = resolveSuperClasses(symbolTable);
        if (!success)
        {
            err.println("Semantic analysis failed during super class resolution");
            return null;
        }


        //2nd pass: check types and scopes
        SemanticChecker  tsc = new SemanticChecker(symbolTable, err);
        root.accept(tsc);
        errors = tsc.getErrorCount();
        if (errors > 0)
        {
            err.println("Semantic analysis failed with " + errors + " error(s) during semantics checking");
            return null;
        }

        //check that an entry point (class Main with default constructor) exists
        if (!checkEntryPoint(symbolTable))
        {
            return null;
        }

        err.println("Semantic analysis complete - 0 errors");

        return symbolTable;
    }

    //check that an entry point (class Main with default constructor) exists
    private boolean checkEntryPoint(SymbolTable st)
    {
        //check class exists
        Symbol main = st.get("Main");
        if (main == null || !(main instanceof ClassSymbol))
        {
            err.println("Program must contain a class 'Main'");
            return false;
        }

        SymbolTable mainST = ((ClassSymbol) main).getSymbolTable();

        //check for a public default constructor
        Symbol cons = mainST.get("Main ()");
        if (cons == null || !(cons instanceof MethodSymbol) || ((MethodSymbol)cons).getAccessModifier() != Modifier.PUBLIC)
        {
            err.println("Class 'Main' must have a public default constructor");
            return false;
        }

        //check for a main method
        Symbol mainMethod = mainST.get("main ()");
        if (mainMethod == null || !(mainMethod instanceof MethodSymbol) || ((MethodSymbol)mainMethod).getAccessModifier() != Modifier.PUBLIC)
        {
            err.println("Class 'Main' must have a public main method");
            return false;
        }

        return true;
    }

    //resolves the super classes of any class symbols in the symbol table
    private boolean resolveSuperClasses(SymbolTable st)
    {
        Symbol[] symbols = st.toArray();

        //true if all classes so far have resolved super classes
        boolean trip = true;

        for (int i = 0; i < symbols.length; ++i)
        {
            //go through the symbol table attempt to resolve the super classes
            //of any classes in the table
            if (symbols[i] instanceof ClassSymbol)
            {
                ClassSymbol cSymbol = ((ClassSymbol)symbols[i]);
                 boolean success = cSymbol.resolveSuperClass(st, err);
                 if (!success)
                     trip = false;
                 else
                 {
                     success = cSymbol.checkReturnTypes(err);
                     if (!success)
                         trip = false;
                     else
                         cSymbol.createOffsetTables();
                 }
            }
        }

        return trip;
    }

    //produce java assembler for the program
    private String produceAsm(SymbolTable symbolTable, String className, String sourceName, int heapSize, int stringPoolSize)
    {
        CodeGenerator cg = new CodeGenerator(symbolTable, err, heapSize, stringPoolSize);
        String output = cg.generate(className, sourceName, root);
        int errors = cg.getErrorCount();
        if (errors > 0)
        {
            err.println("Code generation failed with " + errors + " error(s).");
            return null;
        }

        err.println("Code generation completed.");
        return output;
    }

    /**
     * assembles a file using jasmin
     * @param sourceName the name of the file to assemble
     * @param err PrintWriter for reporting any feedback
     * @return true if the class was successfully assembled
     */
    public boolean assemble(String sourceName, String destPath, PrintWriter err)
    {
        //save System.out and .err
        PrintStream outBak = System.out;
        PrintStream errBak = System.err;

        //set System.out and .err to the a StringOutput Stream to collect any output
        //from jasmin
        StringOutputStream strStream = new StringOutputStream();
        System.setOut(new PrintStream(strStream));
        System.setErr(new PrintStream(strStream));

        //run jasmin
        jasmin.Main.assemble(destPath, sourceName, false);

        //restore System.out and .err
        System.setOut(outBak);
        System.setErr(errBak);

        //append any jasmin output to the output writer
        String output = strStream.toString();
        err.println(output);

        //return true if a class file was successfully generated
        return output.startsWith("Generated:");
    }
}

//cheap and cheerful little class for streaming data into a String (like java.io.StringWriter
//for streams, but not as robust (or as good...))
class StringOutputStream extends OutputStream
{
    private StringBuffer buf = new StringBuffer();

    //write a char to the stream
    public void write(int c)
    {
        buf.append((char)c);
    }

    //get the text written to the stream
    public String toString()
    {
        return buf.toString();
    }
}
