package compiler;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class StringFieldSymbol extends FieldSymbol
{
    public StringFieldSymbol()
    {
        super("StringStorage", "Ljava.lang.String", Modifier.PRIVATE, null);
        fieldNumber = NumberGenerator.getInstance().getFieldNumber(this);
    }

    public void setFieldNumber(int fieldNumber)
    {
        throw new RuntimeException("Method should not be called!!!");
    }
    public int getFieldNumber()
    {
        return fieldNumber;
    }
}