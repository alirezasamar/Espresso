package parser;

/*
 Espresso Compiler - https://github.com/neevsamar/espresso.git
 
 Alireza Samar (A147053)
 Sepideh Sattar (A138894)
 */

public class LexingException extends java.io.IOException
{
    public LexingException()
    {
    }
    public LexingException(String msg)
    {
        super(msg);
    }
}