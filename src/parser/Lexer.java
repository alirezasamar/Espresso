/* The following code was generated by JFlex 1.3.5 on 11/05/14 10:18 */

package parser;

import java_cup.runtime.*;
import java.io.*;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.3.5
 * on 11/03/04 10:18 from the specification file
 * <tt>file:/D:/Nick/UCL/Programming/mini-project/files/jmm.flex</tt>
 */
public class Lexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  final public static int YYEOF = -1;

  /** initial size of the lookahead buffer */
  final private static int YY_BUFFERSIZE = 16384;

  /** lexical states */
  final public static int STRING = 1;
  final public static int YYINITIAL = 0;

  /** 
   * Translates characters to character classes
   */
  final private static String yycmap_packed = 
    "\11\6\1\3\1\2\1\0\1\3\1\1\16\6\4\0\1\3\1\47"+
    "\1\34\1\0\1\5\1\0\1\50\1\0\1\31\1\32\1\43\1\44"+
    "\1\36\1\45\1\37\1\42\12\4\1\0\1\35\1\0\1\46\3\0"+
    "\32\5\1\0\1\52\2\0\1\5\1\0\1\17\1\11\1\14\1\23"+
    "\1\21\1\24\1\5\1\26\1\13\2\5\1\12\1\5\1\25\1\22"+
    "\1\7\1\5\1\15\1\27\1\20\1\10\1\16\1\33\1\30\2\5"+
    "\1\40\1\51\1\41\1\0\41\6\2\0\4\5\4\0\1\5\12\0"+
    "\1\5\4\0\1\5\5\0\27\5\1\0\37\5\1\0\u0128\5\2\0"+
    "\22\5\34\0\136\5\2\0\11\5\2\0\7\5\16\0\2\5\16\0"+
    "\5\5\11\0\1\5\21\0\117\6\21\0\3\6\27\0\1\5\13\0"+
    "\1\5\1\0\3\5\1\0\1\5\1\0\24\5\1\0\54\5\1\0"+
    "\10\5\2\0\32\5\14\0\202\5\1\0\4\6\5\0\71\5\2\0"+
    "\2\5\2\0\2\5\3\0\46\5\2\0\2\5\67\0\46\5\2\0"+
    "\1\5\7\0\47\5\11\0\21\6\1\0\27\6\1\0\3\6\1\0"+
    "\1\6\1\0\2\6\1\0\1\6\13\0\33\5\5\0\3\5\56\0"+
    "\32\5\5\0\13\5\13\6\12\0\12\6\6\0\1\6\143\5\1\0"+
    "\1\5\7\6\2\0\6\6\2\5\2\6\1\0\4\6\2\0\12\6"+
    "\3\5\22\0\1\6\1\5\1\6\33\5\3\0\33\6\65\0\46\5"+
    "\13\6\u0150\0\3\6\1\0\65\5\2\0\1\6\1\5\20\6\2\0"+
    "\1\5\4\6\3\0\12\5\2\6\2\0\12\6\21\0\3\6\1\0"+
    "\10\5\2\0\2\5\2\0\26\5\1\0\7\5\1\0\1\5\3\0"+
    "\4\5\2\0\1\6\1\0\7\6\2\0\2\6\2\0\3\6\11\0"+
    "\1\6\4\0\2\5\1\0\3\5\2\6\2\0\12\6\4\5\16\0"+
    "\1\6\2\0\6\5\4\0\2\5\2\0\26\5\1\0\7\5\1\0"+
    "\2\5\1\0\2\5\1\0\2\5\2\0\1\6\1\0\5\6\4\0"+
    "\2\6\2\0\3\6\13\0\4\5\1\0\1\5\7\0\14\6\3\5"+
    "\14\0\3\6\1\0\7\5\1\0\1\5\1\0\3\5\1\0\26\5"+
    "\1\0\7\5\1\0\2\5\1\0\5\5\2\0\1\6\1\5\10\6"+
    "\1\0\3\6\1\0\3\6\2\0\1\5\17\0\1\5\5\0\12\6"+
    "\21\0\3\6\1\0\10\5\2\0\2\5\2\0\26\5\1\0\7\5"+
    "\1\0\2\5\2\0\4\5\2\0\1\6\1\5\6\6\3\0\2\6"+
    "\2\0\3\6\10\0\2\6\4\0\2\5\1\0\3\5\4\0\12\6"+
    "\22\0\2\6\1\0\6\5\3\0\3\5\1\0\4\5\3\0\2\5"+
    "\1\0\1\5\1\0\2\5\3\0\2\5\3\0\3\5\3\0\10\5"+
    "\1\0\3\5\4\0\5\6\3\0\3\6\1\0\4\6\11\0\1\6"+
    "\17\0\11\6\21\0\3\6\1\0\10\5\1\0\3\5\1\0\27\5"+
    "\1\0\12\5\1\0\5\5\4\0\7\6\1\0\3\6\1\0\4\6"+
    "\7\0\2\6\11\0\2\5\4\0\12\6\22\0\2\6\1\0\10\5"+
    "\1\0\3\5\1\0\27\5\1\0\12\5\1\0\5\5\4\0\7\6"+
    "\1\0\3\6\1\0\4\6\7\0\2\6\7\0\1\5\1\0\2\5"+
    "\4\0\12\6\22\0\2\6\1\0\10\5\1\0\3\5\1\0\27\5"+
    "\1\0\20\5\4\0\6\6\2\0\3\6\1\0\4\6\11\0\1\6"+
    "\10\0\2\5\4\0\12\6\22\0\2\6\1\0\22\5\3\0\30\5"+
    "\1\0\11\5\1\0\1\5\2\0\7\5\3\0\1\6\4\0\6\6"+
    "\1\0\1\6\1\0\10\6\22\0\2\6\15\0\60\5\1\6\2\5"+
    "\7\6\4\0\10\5\10\6\1\0\12\6\47\0\2\5\1\0\1\5"+
    "\2\0\2\5\1\0\1\5\2\0\1\5\6\0\4\5\1\0\7\5"+
    "\1\0\3\5\1\0\1\5\1\0\1\5\2\0\2\5\1\0\4\5"+
    "\1\6\2\5\6\6\1\0\2\6\1\5\2\0\5\5\1\0\1\5"+
    "\1\0\6\6\2\0\12\6\2\0\2\5\42\0\1\5\27\0\2\6"+
    "\6\0\12\6\13\0\1\6\1\0\1\6\1\0\1\6\4\0\2\6"+
    "\10\5\1\0\42\5\6\0\24\6\1\0\2\6\4\5\4\0\10\6"+
    "\1\0\44\6\11\0\1\6\71\0\42\5\1\0\5\5\1\0\2\5"+
    "\1\0\7\6\3\0\4\6\6\0\12\6\6\0\6\5\4\6\106\0"+
    "\46\5\12\0\47\5\11\0\132\5\5\0\104\5\5\0\122\5\6\0"+
    "\7\5\1\0\77\5\1\0\1\5\1\0\4\5\2\0\7\5\1\0"+
    "\1\5\1\0\4\5\2\0\47\5\1\0\1\5\1\0\4\5\2\0"+
    "\37\5\1\0\1\5\1\0\4\5\2\0\7\5\1\0\1\5\1\0"+
    "\4\5\2\0\7\5\1\0\7\5\1\0\27\5\1\0\37\5\1\0"+
    "\1\5\1\0\4\5\2\0\7\5\1\0\47\5\1\0\23\5\16\0"+
    "\11\6\56\0\125\5\14\0\u026c\5\2\0\10\5\12\0\32\5\5\0"+
    "\113\5\225\0\64\5\40\6\7\0\1\5\4\0\12\6\41\0\4\6"+
    "\1\0\12\6\6\0\130\5\10\0\51\5\1\6\u0556\0\234\5\4\0"+
    "\132\5\6\0\26\5\2\0\6\5\2\0\46\5\2\0\6\5\2\0"+
    "\10\5\1\0\1\5\1\0\1\5\1\0\1\5\1\0\37\5\2\0"+
    "\65\5\1\0\7\5\1\0\1\5\3\0\3\5\1\0\7\5\3\0"+
    "\4\5\2\0\6\5\4\0\15\5\5\0\3\5\1\0\7\5\17\0"+
    "\4\6\32\0\5\6\20\0\2\5\51\0\6\6\17\0\1\5\40\0"+
    "\20\5\40\0\15\6\4\0\1\6\40\0\1\5\4\0\1\5\2\0"+
    "\12\5\1\0\1\5\3\0\5\5\6\0\1\5\1\0\1\5\1\0"+
    "\1\5\1\0\4\5\1\0\3\5\1\0\7\5\46\0\44\5\u0e81\0"+
    "\3\5\31\0\11\5\6\6\1\0\5\5\2\0\3\5\6\0\124\5"+
    "\4\0\2\6\2\0\2\5\2\0\136\5\6\0\50\5\4\0\136\5"+
    "\21\0\30\5\u0248\0\u19b6\5\112\0\u51a6\5\132\0\u048d\5\u0773\0\u2ba4\5"+
    "\u215c\0\u012e\5\322\0\7\5\14\0\5\5\5\0\1\5\1\6\12\5"+
    "\1\0\15\5\1\0\5\5\1\0\1\5\1\0\2\5\1\0\2\5"+
    "\1\0\154\5\41\0\u016b\5\22\0\100\5\2\0\66\5\50\0\14\5"+
    "\44\0\4\6\17\0\2\5\30\0\3\5\31\0\1\5\6\0\3\5"+
    "\1\0\1\5\1\0\207\5\2\0\1\6\4\0\1\5\13\0\12\6"+
    "\7\0\32\5\4\0\1\5\1\0\32\5\12\0\132\5\3\0\6\5"+
    "\2\0\6\5\2\0\6\5\2\0\3\5\3\0\2\5\3\0\2\5"+
    "\22\0\3\6\4\0";

  /** 
   * Translates characters to character classes
   */
  final private static char [] yycmap = yy_unpack_cmap(yycmap_packed);

  /** 
   * Translates a state to a row index in the transition table
   */
  final private static int yy_rowMap [] = { 
        0,    43,    86,   129,    86,   172,   215,   258,   301,   344, 
      387,   430,   473,   516,   559,   602,   645,   688,    86,    86, 
       86,    86,    86,    86,    86,    86,   731,    86,   774,    86, 
      817,   860,   903,   946,   989,    86,  1032,  1075,  1118,  1161, 
      215,  1204,  1247,  1290,  1333,  1376,  1419,  1462,  1505,  1548, 
     1591,  1634,  1677,  1720,    86,    86,    86,    86,    86,    86, 
     1763,  1806,  1849,  1892,   215,  1935,  1978,  2021,  2064,  2107, 
     2150,  2193,  2236,   215,  2279,   215,  2322,  2365,  2408,  2451, 
     2494,  2537,  2580,  2623,   215,   215,   215,  2666,  2709,  2752, 
      215,  2795,  2838,  2881,   215,  2924,  2967,   215,  3010,  3053, 
     3096,   215,   215,   215,  3139,  3182,  3225,   215,    86,  3268, 
      215,  3311,   215,   215,  3354,   215
  };

  /** 
   * The packed transition table of the DFA (part 0)
   */
  final private static String yy_packed0 = 
    "\1\3\1\4\2\5\1\6\1\7\1\3\1\10\1\7"+
    "\1\11\1\7\1\12\1\13\1\14\1\15\1\7\1\16"+
    "\1\17\2\7\1\20\1\21\1\7\1\22\1\7\1\23"+
    "\1\24\1\7\1\25\1\26\1\27\1\30\1\31\1\32"+
    "\1\33\1\34\1\35\1\36\1\37\1\40\1\41\1\42"+
    "\1\3\2\43\1\0\31\43\1\44\15\43\1\45\55\0"+
    "\1\5\54\0\1\6\52\0\25\7\2\0\1\7\23\0"+
    "\4\7\1\46\4\7\1\47\13\7\2\0\1\7\23\0"+
    "\16\7\1\50\6\7\2\0\1\7\23\0\20\7\1\51"+
    "\1\52\3\7\2\0\1\7\23\0\6\7\1\53\16\7"+
    "\2\0\1\7\23\0\15\7\1\54\7\7\2\0\1\7"+
    "\23\0\16\7\1\55\6\7\2\0\1\7\23\0\11\7"+
    "\1\56\10\7\1\57\2\7\2\0\1\7\23\0\24\7"+
    "\1\60\2\0\1\7\23\0\13\7\1\61\2\7\1\62"+
    "\6\7\2\0\1\7\23\0\4\7\1\63\10\7\1\64"+
    "\7\7\2\0\1\7\23\0\4\7\1\65\20\7\2\0"+
    "\1\7\61\0\1\66\54\0\1\67\54\0\1\70\52\0"+
    "\1\71\54\0\1\72\53\0\1\73\1\0\2\43\1\0"+
    "\31\43\1\0\15\43\26\0\1\74\31\0\5\7\1\75"+
    "\17\7\2\0\1\7\23\0\7\7\1\76\6\7\1\77"+
    "\6\7\2\0\1\7\23\0\16\7\1\100\6\7\2\0"+
    "\1\7\23\0\14\7\1\101\10\7\2\0\1\7\23\0"+
    "\13\7\1\102\11\7\2\0\1\7\23\0\14\7\1\103"+
    "\10\7\2\0\1\7\23\0\7\7\1\104\15\7\2\0"+
    "\1\7\23\0\4\7\1\105\20\7\2\0\1\7\23\0"+
    "\7\7\1\106\15\7\2\0\1\7\23\0\7\7\1\107"+
    "\4\7\1\110\10\7\2\0\1\7\23\0\6\7\1\111"+
    "\16\7\2\0\1\7\23\0\11\7\1\112\13\7\2\0"+
    "\1\7\23\0\6\7\1\113\16\7\2\0\1\7\23\0"+
    "\25\7\2\0\1\114\23\0\3\7\1\115\21\7\2\0"+
    "\1\7\17\0\1\66\1\4\1\5\50\66\4\0\6\7"+
    "\1\116\16\7\2\0\1\7\23\0\12\7\1\117\6\7"+
    "\1\120\3\7\2\0\1\7\23\0\14\7\1\121\10\7"+
    "\2\0\1\7\23\0\6\7\1\122\16\7\2\0\1\7"+
    "\23\0\23\7\1\123\1\7\2\0\1\7\23\0\4\7"+
    "\1\124\20\7\2\0\1\7\23\0\17\7\1\125\5\7"+
    "\2\0\1\7\23\0\15\7\1\126\7\7\2\0\1\7"+
    "\23\0\23\7\1\127\1\7\2\0\1\7\23\0\14\7"+
    "\1\130\10\7\2\0\1\7\23\0\15\7\1\131\7\7"+
    "\2\0\1\7\23\0\23\7\1\132\1\7\2\0\1\7"+
    "\23\0\6\7\1\133\16\7\2\0\1\7\23\0\15\7"+
    "\1\134\7\7\2\0\1\7\23\0\7\7\1\135\15\7"+
    "\2\0\1\7\23\0\13\7\1\136\11\7\2\0\1\7"+
    "\23\0\14\7\1\137\10\7\2\0\1\7\23\0\15\7"+
    "\1\140\7\7\2\0\1\7\23\0\15\7\1\141\7\7"+
    "\2\0\1\7\23\0\23\7\1\142\1\7\2\0\1\7"+
    "\23\0\11\7\1\143\13\7\2\0\1\7\23\0\25\7"+
    "\1\144\1\0\1\7\23\0\21\7\1\145\3\7\2\0"+
    "\1\7\23\0\15\7\1\146\7\7\2\0\1\7\23\0"+
    "\11\7\1\147\13\7\2\0\1\7\23\0\10\7\1\150"+
    "\14\7\2\0\1\7\23\0\14\7\1\151\10\7\2\0"+
    "\1\7\23\0\10\7\1\152\14\7\2\0\1\7\23\0"+
    "\13\7\1\153\11\7\2\0\1\7\23\0\21\7\1\154"+
    "\3\7\2\0\1\7\51\0\1\155\24\0\17\7\1\156"+
    "\5\7\2\0\1\7\23\0\15\7\1\157\7\7\2\0"+
    "\1\7\23\0\14\7\1\160\10\7\2\0\1\7\23\0"+
    "\21\7\1\161\3\7\2\0\1\7\23\0\23\7\1\162"+
    "\1\7\2\0\1\7\23\0\15\7\1\163\7\7\2\0"+
    "\1\7\23\0\17\7\1\164\5\7\2\0\1\7\17\0";

  /** 
   * The transition table of the DFA
   */
  final private static int yytrans [] = yy_unpack();


  /* error codes */
  final private static int YY_UNKNOWN_ERROR = 0;
  final private static int YY_ILLEGAL_STATE = 1;
  final private static int YY_NO_MATCH = 2;
  final private static int YY_PUSHBACK_2BIG = 3;

  /* error messages for the codes above */
  final private static String YY_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Internal error: unknown state",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * YY_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private final static byte YY_ATTRIBUTE[] = {
     1,  0,  9,  1,  9,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 
     1,  1,  9,  9,  9,  9,  9,  9,  9,  9,  1,  9,  1,  9,  1,  1, 
     1,  1,  1,  9,  0,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 
     1,  1,  1,  1,  1,  0,  9,  9,  9,  9,  9,  9,  1,  1,  1,  1, 
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 
     1,  1,  1,  0,  1,  1,  1,  1,  1,  1,  1,  1,  9,  1,  1,  1, 
     1,  1,  1,  1
  };

  /** the input device */
  private java.io.Reader yy_reader;

  /** the current state of the DFA */
  private int yy_state;

  /** the current lexical state */
  private int yy_lexical_state = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char yy_buffer[] = new char[YY_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int yy_markedPos;

  /** the textposition at the last state to be included in yytext */
  private int yy_pushbackPos;

  /** the current text position in the buffer */
  private int yy_currentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int yy_startRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int yy_endRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn; 

  /** 
   * yy_atBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean yy_atBOL = true;

  /** yy_atEOF == true <=> the scanner is at the EOF */
  private boolean yy_atEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean yy_eof_done;

  /* user code: */
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


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
    this.yy_reader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public Lexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the split, compressed DFA transition table.
   *
   * @return the unpacked transition table
   */
  private static int [] yy_unpack() {
    int [] trans = new int[3397];
    int offset = 0;
    offset = yy_unpack(yy_packed0, offset, trans);
    return trans;
  }

  /** 
   * Unpacks the compressed DFA transition table.
   *
   * @param packed   the packed transition table
   * @return         the index of the last entry
   */
  private static int yy_unpack(String packed, int offset, int [] trans) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do trans[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] yy_unpack_cmap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 1626) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   IOException  if any I/O-Error occurs
   */
  private boolean yy_refill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (yy_startRead > 0) {
      System.arraycopy(yy_buffer, yy_startRead, 
                       yy_buffer, 0, 
                       yy_endRead-yy_startRead);

      /* translate stored positions */
      yy_endRead-= yy_startRead;
      yy_currentPos-= yy_startRead;
      yy_markedPos-= yy_startRead;
      yy_pushbackPos-= yy_startRead;
      yy_startRead = 0;
    }

    /* is the buffer big enough? */
    if (yy_currentPos >= yy_buffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[yy_currentPos*2];
      System.arraycopy(yy_buffer, 0, newBuffer, 0, yy_buffer.length);
      yy_buffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = yy_reader.read(yy_buffer, yy_endRead, 
                                            yy_buffer.length-yy_endRead);

    if (numRead < 0) {
      return true;
    }
    else {
      yy_endRead+= numRead;  
      return false;
    }
  }


  /**
   * Closes the input stream.
   */
  final public void yyclose() throws java.io.IOException {
    yy_atEOF = true;            /* indicate end of file */
    yy_endRead = yy_startRead;  /* invalidate buffer    */

    if (yy_reader != null)
      yy_reader.close();
  }


  /**
   * Closes the current stream, and resets the
   * scanner to read from a new input stream.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>YY_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  final public void yyreset(java.io.Reader reader) throws java.io.IOException {
    yyclose();
    yy_reader = reader;
    yy_atBOL  = true;
    yy_atEOF  = false;
    yy_endRead = yy_startRead = 0;
    yy_currentPos = yy_markedPos = yy_pushbackPos = 0;
    yyline = yychar = yycolumn = 0;
    yy_lexical_state = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  final public int yystate() {
    return yy_lexical_state;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  final public void yybegin(int newState) {
    yy_lexical_state = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  final public String yytext() {
    return new String( yy_buffer, yy_startRead, yy_markedPos-yy_startRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  final public char yycharat(int pos) {
    return yy_buffer[yy_startRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  final public int yylength() {
    return yy_markedPos-yy_startRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void yy_ScanError(int errorCode) {
    String message;
    try {
      message = YY_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = YY_ERROR_MSG[YY_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  private void yypushback(int number)  {
    if ( number > yylength() )
      yy_ScanError(YY_PUSHBACK_2BIG);

    yy_markedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void yy_do_eof() throws java.io.IOException {
    if (!yy_eof_done) {
      yy_eof_done = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int yy_input;
    int yy_action;

    // cached fields:
    int yy_currentPos_l;
    int yy_startRead_l;
    int yy_markedPos_l;
    int yy_endRead_l = yy_endRead;
    char [] yy_buffer_l = yy_buffer;
    char [] yycmap_l = yycmap;

    int [] yytrans_l = yytrans;
    int [] yy_rowMap_l = yy_rowMap;
    byte [] yy_attr_l = YY_ATTRIBUTE;

    while (true) {
      yy_markedPos_l = yy_markedPos;

      boolean yy_r = false;
      for (yy_currentPos_l = yy_startRead; yy_currentPos_l < yy_markedPos_l;
                                                             yy_currentPos_l++) {
        switch (yy_buffer_l[yy_currentPos_l]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          yy_r = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          yy_r = true;
          break;
        case '\n':
          if (yy_r)
            yy_r = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          yy_r = false;
          yycolumn++;
        }
      }

      if (yy_r) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean yy_peek;
        if (yy_markedPos_l < yy_endRead_l)
          yy_peek = yy_buffer_l[yy_markedPos_l] == '\n';
        else if (yy_atEOF)
          yy_peek = false;
        else {
          boolean eof = yy_refill();
          yy_markedPos_l = yy_markedPos;
          yy_buffer_l = yy_buffer;
          if (eof) 
            yy_peek = false;
          else 
            yy_peek = yy_buffer_l[yy_markedPos_l] == '\n';
        }
        if (yy_peek) yyline--;
      }
      yy_action = -1;

      yy_startRead_l = yy_currentPos_l = yy_currentPos = 
                       yy_startRead = yy_markedPos_l;

      yy_state = yy_lexical_state;


      yy_forAction: {
        while (true) {

          if (yy_currentPos_l < yy_endRead_l)
            yy_input = yy_buffer_l[yy_currentPos_l++];
          else if (yy_atEOF) {
            yy_input = YYEOF;
            break yy_forAction;
          }
          else {
            // store back cached positions
            yy_currentPos  = yy_currentPos_l;
            yy_markedPos   = yy_markedPos_l;
            boolean eof = yy_refill();
            // get translated positions and possibly new buffer
            yy_currentPos_l  = yy_currentPos;
            yy_markedPos_l   = yy_markedPos;
            yy_buffer_l      = yy_buffer;
            yy_endRead_l     = yy_endRead;
            if (eof) {
              yy_input = YYEOF;
              break yy_forAction;
            }
            else {
              yy_input = yy_buffer_l[yy_currentPos_l++];
            }
          }
          int yy_next = yytrans_l[ yy_rowMap_l[yy_state] + yycmap_l[yy_input] ];
          if (yy_next == -1) break yy_forAction;
          yy_state = yy_next;

          int yy_attributes = yy_attr_l[yy_state];
          if ( (yy_attributes & 1) == 1 ) {
            yy_action = yy_state; 
            yy_markedPos_l = yy_currentPos_l; 
            if ( (yy_attributes & 8) == 8 ) break yy_forAction;
          }

        }
      }

      // store back cached position
      yy_markedPos = yy_markedPos_l;

      switch (yy_action) {

        case 6: 
        case 7: 
        case 8: 
        case 9: 
        case 10: 
        case 11: 
        case 12: 
        case 13: 
        case 14: 
        case 15: 
        case 16: 
        case 17: 
        case 37: 
        case 38: 
        case 39: 
        case 41: 
        case 42: 
        case 43: 
        case 44: 
        case 45: 
        case 46: 
        case 47: 
        case 48: 
        case 49: 
        case 50: 
        case 51: 
        case 52: 
        case 60: 
        case 61: 
        case 62: 
        case 63: 
        case 65: 
        case 66: 
        case 67: 
        case 68: 
        case 69: 
        case 70: 
        case 71: 
        case 72: 
        case 74: 
        case 76: 
        case 77: 
        case 78: 
        case 79: 
        case 80: 
        case 81: 
        case 82: 
        case 83: 
        case 87: 
        case 88: 
        case 89: 
        case 91: 
        case 92: 
        case 93: 
        case 95: 
        case 96: 
        case 98: 
        case 100: 
        case 104: 
        case 105: 
        case 106: 
        case 109: 
        case 111: 
        case 114: 
          { return symbol(sym.NAME, yytext()); }
        case 117: break;
        case 0: 
        case 5: 
          { return symbol(sym.NUMBER, new Integer(yytext())); }
        case 118: break;
        case 113: 
          { return symbol(sym.EXTENDS); }
        case 119: break;
        case 110: 
          { return symbol(sym.PRIVATE); }
        case 120: break;
        case 107: 
          { return symbol(sym.RETURN); }
        case 121: break;
        case 103: 
          { return symbol(sym.PUBLIC); }
        case 122: break;
        case 56: 
          { return symbol(sym.NOT_EQ); }
        case 123: break;
        case 64: 
          { return symbol(sym.BIT_INT); }
        case 124: break;
        case 20: 
          { curString.setLength(0); yybegin(STRING); }
        case 125: break;
        case 35: 
          { yybegin(YYINITIAL); return symbol(sym.STRING, curString.toString()); }
        case 126: break;
        case 112: 
          { return symbol(sym.BIT_BOOLEAN); }
        case 127: break;
        case 25: 
          { return symbol(sym.RIGHT_BRACE); }
        case 128: break;
        case 24: 
          { return symbol(sym.LEFT_BRACE); }
        case 129: break;
        case 21: 
          { return symbol(sym.SEMI_COLON); }
        case 130: break;
        case 115: 
          { return symbol(sym.PROTECTED); }
        case 131: break;
        case 27: 
          { return symbol(sym.ASTERISK); }
        case 132: break;
        case 54: 
          { return symbol(sym.PLUS_PLUS); }
        case 133: break;
        case 2: 
        case 32: 
        case 33: 
          { 
	if (out != null)
	    out.println("Bad token: " + yytext() + " on row " + (yyline+1) + " and column " + (yycolumn+1));
	throw new LexingException("Bad token: " + yytext() + " on row " + yyline + " and column " + yycolumn);
 }
        case 134: break;
        case 30: 
          { return symbol(sym.EQ); }
        case 135: break;
        case 23: 
          { return symbol(sym.DOT); }
        case 136: break;
        case 31: 
          { return symbol(sym.NOT); }
        case 137: break;
        case 40: 
          { return symbol(sym.IF); }
        case 138: break;
        case 57: 
          { return symbol(sym.AND); }
        case 139: break;
        case 58: 
          { return symbol(sym.OR); }
        case 140: break;
        case 73: 
          { return symbol(sym.FOR); }
        case 141: break;
        case 75: 
          { return symbol(sym.NEW); }
        case 142: break;
        case 18: 
          { return symbol(sym.LEFT_BRACKET); }
        case 143: break;
        case 19: 
          { return symbol(sym.RIGHT_BRACKET); }
        case 144: break;
        case 108: 
          { return symbol(sym.EXIT); }
        case 145: break;
        case 102: 
          { return symbol(sym.SUPER); }
        case 146: break;
        case 101: 
          { return symbol(sym.FALSE); }
        case 147: break;
        case 97: 
          { return symbol(sym.CLASS); }
        case 148: break;
        case 29: 
          { return symbol(sym.MINUS); }
        case 149: break;
        case 28: 
          { return symbol(sym.PLUS); }
        case 150: break;
        case 26: 
          { return symbol(sym.SLASH); }
        case 151: break;
        case 22: 
          { return symbol(sym.COMMA); }
        case 152: break;
        case 55: 
          { return symbol(sym.EQ_EQ); }
        case 153: break;
        case 84: 
          { return symbol(sym.VOID); }
        case 154: break;
        case 85: 
          { return symbol(sym.TRUE); }
        case 155: break;
        case 86: 
          { return symbol(sym.THIS); }
        case 156: break;
        case 90: 
          { return symbol(sym.NULL); }
        case 157: break;
        case 94: 
          { return symbol(sym.PRINT); }
        case 158: break;
        case 59: 
          { curString.append('\n'); }
        case 159: break;
        case 34: 
          { curString.append(yytext()); }
        case 160: break;
        case 3: 
        case 4: 
          {  }
        case 161: break;
        default: 
          if (yy_input == YYEOF && yy_startRead == yy_currentPos) {
            yy_atEOF = true;
            yy_do_eof();
              { return new java_cup.runtime.Symbol(sym.EOF); }
          } 
          else {
            yy_ScanError(YY_NO_MATCH);
          }
      }
    }
  }


}
