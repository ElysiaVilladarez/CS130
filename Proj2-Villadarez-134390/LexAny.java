
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elysia Jelena Villadarez
 * March 3, 2017
 */
public class LexAny {

    private int[][] nextState = {
       {100,101,102,103,104,105,14,107,108,1,2,2,3,109,4,10,0,0,11,0,12,13,119,15},
{1,1,1,1,1,1,1,201,1,110,1,1,1,1,1,1,201,1,1,1,1,1,1,1},
{112,112,112,112,112,112,112,112,112,112,2,2,112,112,2,112,112,112,112,112,112,112,112,112},
{113,113,113,113,113,113,113,113,113,113,113,113,111,113,113,113,113,113,113,113,113,113,200,200},
{114,114,114,114,114,5,114,114,114,114,7,114,114,114,4,114,114,114,114,114,114,114,114,114},
{200,200,200,200,200,200,200,200,200,200,200,200,200,200,6,200,150,150,200,150,150,150,150,150},
{114,114,114,114,114,200,114,114,114,114,7,114,114,114,6,114,114,114,114,114,114,114,114,114},
{200,200,200,200,200,200,200,200,8,200,200,200,200,8,9,200,200,200,200,200,200,200,200,200},
{200,200,200,200,200,200,200,200,200,200,200,200,200,200,9,200,200,200,200,200,200,200,200,200},
{114,114,114,114,114,114,114,114,114,114,114,114,114,114,9,114,114,114,114,114,114,114,114,114},
{10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,0,10,10,10,10,10,10,10},
{11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,201,11,110,11,11,11,11,11},
{115,115,115,115,115,115,116,115,115,115,115,115,115,115,115,115,115,115,115,115,115,115,115,115},
{117,117,117,117,117,117,118,117,117,117,117,117,117,117,117,117,117,117,117,117,117,117,117,117},
{106,106,106,106,106,106,120,106,106,106,106,106,106,106,106,106,106,106,106,106,106,106,106,106},
{200,200,200,200,200,200,121,200,200,200,200,200,200,200,200,200,200,200,200,200,200,200,200,200}
    };
    private final String fileName;
    private final String alphaCheck = "abcdefghijklmnopqrstuvwxyz";
    private final String digitCheck = "1234567890";
        
    private BufferedReader r;
    private static int pos = 0;
    private char cur;
    private int prevState;
    private int currentState;
    private String compile;
    private boolean isComment;
    private boolean isString1;
    private boolean isString2;
    private boolean isIllegal;
    public LexAny(String fileName) {
        this.fileName = fileName;
    }
    
    
    private int curToken(String t) {
        String text = t.toLowerCase();
        if(isComment && !(text.equals("\n") || text.equals("\r") || text.equals("\r\n"))){
            return 15;
        } else{
            isComment = false;
        }
        
//        if(isString1 && !text.equals("\"")){
//            System.out.println("WHY LOOP?? " + text);
//            return 9;
//        } else{
//            isString1 = false;
//        }
//         if(isString2 && !text.equals("'")){
//            return 18;
//        } else{
//            isString2 = false;
//        }
        
        
        switch (text){
            case "(":
                return 0;
            case ")":
                return 1;
            case "/":
                return 2;
            case "%":
                return 3;
            case ",":
                return 4;
            case ".":
                return 5;
            case "=":
                return 6;
            case "$":
                return 7;
            case "+":
                return 8;
            case "\"":
                isString1 = true;
                return 9;
            case "e":
                return 10;
            case "*":
                return 12;
            case "-":
                return 13;
            case "#":
                isComment = true;
                return 15;
//            case "\n":
//                return 16;
            case "'":
                isString2 = true;
                return 18;
                
            case "<":
                return 20;
            case ">":
                return 21;
            case ";":
                return 22;
            case "!":
                return 23;
                
            default:
                if(alphaCheck.indexOf(text)!= -1){
                    return 11;
                } else if(digitCheck.indexOf(text)!= -1){
                    return 14;
                } else if(text.equals("\n") || text.equals("\r") || text.equals("\r\n")){
                    return 16;
                }else if(text.contains(" ")){
                    return 17;
                }else{
                    return 19;
                }
              
        }
    }
    
    public int parseID(int state){
        switch(state){
            case 150:
                return Token.NUMBER;        
            case 100:
                return Token.LPAREN;
            case 101:
                return Token.RPAREN;
            case 102:
                return Token.DIVIDE;
            case 103:
                return Token.MODULO;
            case 104:
                return Token.COMMA;
            case 105:
                return Token.PERIOD;
            case 106:
                return Token.EQUALS;
            case 107:
                return Token.EOF;
            case 108:
                return Token.PLUS;
            case 109:
                return Token.MINUS;
            case 110:
                return Token.STRING;
            case 111:
                return Token.EXP;
            case 112:
                if(compile.equals("IF")) return Token.IF;
                else if(compile.equals("PRINT")) return Token.PRINT;
                else if(compile.equals("SQRT")) return Token.SQRT;
                else return Token.IDENT;
            case 113:
                return Token.MULT;
            case 114:
                return Token.NUMBER;
            
            
            case 115:
                return Token.LESSTHAN;
            case 116:
                return Token.LESSTHANEQUALS;
            case 117:
                return Token.GREATERTHAN;
            case 118:
                return Token.GREATERTHANEQUALS;
            case 119:
                return Token.SEMICOLON;
            case 120:
                return Token.EQUALTO;
            case 121:
                return Token.NOTEQUATO;
            default:
                return -1;
        }
    }
    public String errorParse(int state){
        switch(state){
            case 200:
                return "BADLY FORMED NUMBER";        
            case 201:
                return "UNTERMINATED STRING";
            case 19:
                return "ILLEGAL CHARACTER";
            default:
                return "UNKNOWN ERROR";
        }
    }
    public String parseCategory(int state){
        switch(state){
            case 150:
                return "NUMBER";
            case 100:
                return "LPAREN";
            case 101:
                return "RPAREN";
            case 102:
                return "DIVIDE";
            case 103:
                return "MODULO";
            case 104:
                return "COMMA";
            case 105:
                return "PERIOD";
            case 106:
                return "EQUALS";
            case 107:
                return "EOF";
            case 108:
                return "PLUS";
            case 109:
                return "MINUS";
            case 110:
                return "STRING";
            case 111:
                return "EXP";
            case 112:
                if(compile.equals("IF")) return "IF";
                else if(compile.equals("PRINT")) return "PRINT";
                else if(compile.equals("SQRT")) return "SQRT";
                else return "IDENT";
            case 113:
                return "MULT";
            case 114:
                return "NUMBER";
                
            case 115:
                return "LESSTHAN";
            case 116:
                return "LESSTHANEQUALS";
            case 117:
                return "GREATERTHAN";
            case 118:
                return "GREATERTHANEQUALS";
            case 119:
                return "SEMICOLON";
            case 120:
                return "EQUALTO";
            case 121:
                return "NOTEQUALTO";
            default:
                return "UNKNOWN";
        }
    }
    
    public Token getToken() {
        //   File file = new File(fileName);
        try {
            InputStream is = new FileInputStream(fileName);
            r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            r.skip(pos);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LexAny.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("FILE DOES NOT EXIST");
            System.exit(0);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LexAny.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LexAny.class.getName()).log(Level.SEVERE, null, ex);
        }

        Token t = null;
        currentState = 0;
        compile = "";
                
        try {
            while (t == null) {
                int temp = r.read();
               // if(temp == -1) return null;
                cur = (char)temp;
                int charIndex = curToken(Character.toString(cur));
                
                prevState = currentState;
                
               // System.out.println("CurState: " + currentState);
                currentState = nextState[currentState][charIndex];
                
              //  System.out.println("CharIndex: " + charIndex + " Char: " + cur + " CurState: " + currentState);
                if(isIllegal){
                    isIllegal = false;
                    System.out.println("lexical error: ILLEGAL CHARACTER");
                    cur = (char)r.read();
                    charIndex = curToken(Character.toString(cur));
                     prevState = currentState;
                    //  System.out.println("CharIndex: " + charIndex + " Char: " + cur);
                    currentState = nextState[currentState][charIndex];
                    compile = "";
                    ++pos;
                }
                
                if(currentState>=200){ // error
                    System.out.println("lexical error: " + errorParse(currentState));
                   // System.exit(0);
                    currentState = 0;
                    compile = "";
                    if(currentState != 200)++pos;
                }else if(currentState>=100){ //finishedStates
                    if(currentState == 150 ){
                        --pos;
                        compile = compile.substring(0, compile.length()-1);
                    } else if(currentState == 106 || currentState == 115 || currentState == 117){
                        
                    } else if(currentState < 112 || currentState >114){
                         compile+=cur;
                        ++pos;
                    }
                    t = new Token(parseID(currentState), compile, parseCategory(currentState));
                } else if(currentState == 0){
                    compile = "";
                    ++pos;
                    t = null;
                }
                else{
                    compile += cur;
                    t = null;
                    ++pos;
                }
                
//                if(charIndex == 19){
//                    isIllegal = true;
//                }
            }
            r.close();
        } catch (IOException ex) {
            Logger.getLogger(LexAny.class.getName()).log(Level.SEVERE, null, ex);
        }

        return t;
    }

//    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        System.out.println("Enter the absolute filepath of the file to analyze:\n");
//        String fileName = in.nextLine();
//        LexAny program = new LexAny(fileName);
//        Token t;
//        t = program.getToken();
//        while (t.getId() != Token.EOF) {
//            System.out.println(t.getCategory() + "\t" + t.getLexeme());
//            t = program.getToken();
//        }
//    }
}
