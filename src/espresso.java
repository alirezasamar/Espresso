import java.io.*;
import parser.*;
import compiler.*;
import java_cup.runtime.Symbol;
import ast.Node;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class espresso
{
    //processes the arguments and sets up the output
    public static void main(String[] args)
    {
        //PrintWriter to take the output, in this case send it all to stdout
        PrintWriter err = new PrintWriter(System.out, true);

        //source file
        String fileName = null;
        //heap size, -1 = use default
        int heapSize = -1;
        //string pool size, -1 = use default
        int poolSize = -1;
        //if true output assembly code, if false output a class file
        boolean asm = false;

        if (args.length == 0)
        {
            //no arg specified
            err.println("Java-- compiler. Usage: java espresso [-asm] [-heap <x>] [-pool <y>] <filename>");

            System.exit(-1);
        }
        else
        {
            //go through the arguments
            try
            {
                int i = 0;
                for (; i < args.length-1; ++i)
                {
                    if (args[i].equals("-asm"))
                        asm = true;
                    else if (args[i].startsWith("-heap"))
                        heapSize = Integer.parseInt(args[++i]);
                    else if (args[i].startsWith("-pool"))
                        poolSize = Integer.parseInt(args[++i]);
                }
                //last argument should be the source file
                fileName = args[i];
            }
            catch (Exception e)
            {
                err.println("Java-- compiler. Usage: java espresso [-asm] [-heap <x>] [-pool <y>] <filename>");
                System.exit(-1);
            }
        }

        try
        {
            //open the file
            File file = new File(fileName);
            BufferedReader buf = new BufferedReader(new FileReader(file));

            String sourceName = file.getName();
            String output = compile(err, buf, sourceName, utils.StringUtils.removeExtension(sourceName) + ".class", null, !asm, heapSize, poolSize);
            if (asm)
            {
                System.out.println("\nOutput:\n\n");
                System.out.println(output);
            }

            //close the file
            buf.close();
        }
        catch (IOException e)
        {
            //io exception
            err.println("Could not open file: " + args[0]);
        }
    }

    /**
     * compiles the given program
     */
    public static String compile(PrintWriter err, Reader input, String sourceName, String className, String outputPath, boolean toClass, int heapSize, int poolSize)
    {
        try
        {
            //parse the file using the cup/flex generated parser
            err.println("Parsing...");
            parser parser = new parser(new Lexer(input, err), err);
            Symbol result = null;
            try
            {
                result = parser.parse();
            }
            catch (LexingException e)
            {
                err.println("Parsing failed during lexing.");
            }
            catch (Exception e)
            {
                //An exception occured during parsing, however we will know this
                //anyway from the error count so do nothing
            }

            if (parser.getErrorCount() == 0)
                err.println("Finished parsing - no errors.");
            else
            {
                err.println("Parsing failed: " + parser.getErrorCount() + " error(s).");
                result = null;
            }

            //if parsing succeeded then compile the ast
            if (result != null)
            {
                //compile the program
                err.println("Beginning semantic analysis...");
                Node root = (Node)result.value;
                compiler.Compiler compiler = new compiler.Compiler(root, err);
                String output = compiler.compile(utils.StringUtils.removeExtension(className), sourceName, heapSize, poolSize);

                if (output != null)
                {
                    if (toClass)
                    {
                        String asmFile = utils.StringUtils.removeExtension(sourceName) + ".j";
                        //output a class file

                        //we need the source in a file for jasmin
                        File sourceFile = new File(asmFile);
                        BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile));
                        writer.write(output);
                        writer.close();

                        //try to assemble
                        String finalResult = null;
                        if(compiler.assemble(asmFile, outputPath, err))
                        {
                            finalResult = "";
                        }

                        sourceFile.delete();
                        return finalResult;
                    }
                    else
                    {
                        //return the assembly code
                        return output;
                    }
                }
            }
        }
        catch (Exception e)
        {
            //non io exception
            err.println("An unknown error occured during compilation: " + e.toString());
            e.printStackTrace();
        }

        return null;
    }
}