package compiler;

import java.util.Vector;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class NumberGenerator
{
    private static NumberGenerator instance = new NumberGenerator();

    public static void resetInstance()
    {
        instance = new NumberGenerator();
    }

    public static NumberGenerator getInstance()
    {
        return instance;
    }

    private Vector methods = new Vector();
    private Vector fields = new Vector();

    private NumberGenerator()
    {
    }

    private int methodNumber = 0;  //String and Object methods
    public int getMethodNumber(MethodSymbol key)
    {
        methods.add(key);
        return methodNumber++;
    }
    public int getTotalMethods()
    {
        return methodNumber;
    }
    public String makeMethodLabel(int i)
    {
        StringBuffer result = new StringBuffer("proc");
        result.append(i);

        return result.toString();
    }
    public MethodSymbol getMethod(int i)
    {
        return (MethodSymbol)methods.get(i);
    }

    private int fieldNumber = 0;
    public int getFieldNumber(FieldSymbol key)
    {
        fields.add(key);
        return fieldNumber++;
    }
    public FieldSymbol getField(int i)
    {
        return (FieldSymbol)fields.get(i);
    }


    private int offset = 0;
    public int getOffset()
    {
        return offset++;
    }
    public void resetOffset()
    {
        offset = 0;
    }


    private int nextLocal = 11;
    private int maxLocals = 11;
    public int getLocal()
    {
        if (nextLocal >= maxLocals)
        {
            maxLocals = nextLocal + 1;
        }
        return nextLocal++;
    }
    public int getMaxLocals()
    {
        return maxLocals;
    }
    public int getTotalLocals()
    {
        return nextLocal;
    }
    public void resetLocals()
    {
        nextLocal = 11;
    }


    private int nextLabel = 0;
    public String getLabel()
    {
        StringBuffer result = new StringBuffer("lbl");
        result.append(nextLabel++);

        return result.toString();
    }
    public int getTotalLabels()
    {
        return nextLabel;
    }

    private int nextRetAdd = 0;
    public int getRetAddress()
    {
        return nextRetAdd++;
    }
    public int getTotalRetAdds()
    {
        return nextRetAdd;
    }
    public String makeRetAdd(int i)
    {
        StringBuffer result = new StringBuffer("retadd");
        result.append(i);

        return result.toString();
    }
}