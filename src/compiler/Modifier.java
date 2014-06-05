package compiler;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class Modifier
{
    private String value;

    private Modifier(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return value;
    }

    public static final Modifier PUBLIC = new Modifier("public");
    public static final Modifier PROTECTED = new Modifier("protected");
    public static final Modifier PRIVATE = new Modifier("private");
}