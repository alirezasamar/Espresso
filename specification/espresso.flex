/*
Espresso Compiler - https://github.com/neevsamar/espresso.git

Alireza Samar (A147053)
Sepideh Sattar (A138894)
*/

package parser;

import java_cup.runtime.*;
import java.io.*;

%%

%class Lexer
%public
%cup
%line
%column
%unicode

%{
    private StringBuffer curString = new StringBuffer();
    private PrintWriter out;

    public Lexer(Reader in, PrintWriter out)
    {
	this(in);
	this.out = out;
    }

    private Symbol symbol(int name)
    {
	return new Symbol(name, yyline+1, yycolumn+1, yytext());
    }

    private Symbol symbol(int name, Object value)
    {
	return new Symbol(name, yyline+1, yycolumn+1, value);
    }
%}

EOL = \r|\n|\r\n
InputChar = [^\r\n]
WhiteSpace = {EOL} | [ \t\f]
Number = [0-9]*
Identifier = [:jletter:][:jletterdigit:]*

%xstate STRING

%%

/* keywords */
"public" {return symbol(sym.PUBLIC);}
"private" {return symbol(sym.PRIVATE);}
"protected" {return symbol(sym.PROTECTED);}
"if" {return symbol(sym.IF);}
"for" {return symbol(sym.FOR);}
"return" {return symbol(sym.RETURN);}
"this" {return symbol(sym.THIS);}
"void" {return symbol(sym.VOID);}
"class" {return symbol(sym.CLASS);}
"extends" {return symbol(sym.EXTENDS);}
"true" {return symbol(sym.TRUE);}
"false" {return symbol(sym.FALSE);}
"super" {return symbol(sym.SUPER);}
"exit()" {return symbol(sym.EXIT);}
"null" {return symbol(sym.NULL);}
"new" {return symbol(sym.NEW);}
"print" {return symbol(sym.PRINT);}

"int" {return symbol(sym.BIT_INT);}
"boolean" {return symbol(sym.BIT_BOOLEAN);}


{Identifier} {return symbol(sym.NAME, yytext());}

{Number} {return symbol(sym.NUMBER, new Integer(yytext()));}

\" {curString.setLength(0); yybegin(STRING);}

; {return symbol(sym.SEMI_COLON);}
, {return symbol(sym.COMMA);}
\. {return symbol(sym.DOT);}

\{ {return symbol(sym.LEFT_BRACE);}
\} {return symbol(sym.RIGHT_BRACE);}
\( {return symbol(sym.LEFT_BRACKET);}
\) {return symbol(sym.RIGHT_BRACKET);}

\/ {return symbol(sym.SLASH);}
\* {return symbol(sym.ASTERISK);}
\+\+ {return symbol(sym.PLUS_PLUS);}
\+ {return symbol(sym.PLUS);}
- {return symbol(sym.MINUS);}

== {return symbol(sym.EQ_EQ);}
\!= {return symbol(sym.NOT_EQ);}

= {return symbol(sym.EQ);}

\! {return symbol(sym.NOT);}
&& {return symbol(sym.AND);}
\|\| {return symbol(sym.OR);}


/* comments */
"//" {InputChar}* {EOL} {}

/* whitespace */
{WhiteSpace} {}

<STRING>
{
  \" {yybegin(YYINITIAL); return symbol(sym.STRING, curString.toString());}
  [^\n\"\\]+ {curString.append(yytext());}
  \\n {curString.append('\n');}
}

/* error fallback */
.|\n {
	if (out != null)
	    out.println("Bad token: " + yytext() + " on row " + (yyline+1) + " and column " + (yycolumn+1));
	throw new LexingException("Bad token: " + yytext() + " on row " + yyline + " and column " + yycolumn);
}
