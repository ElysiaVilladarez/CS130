
/**
 *
 * @author Elysia Jelena Villadarez
 * March 3, 2017
 */
public class Token {
    //id list
    public static final int PLUS = 0;
    public static final int MINUS = 1;
    public static final int MULT = 2;
    public static final int DIVIDE = 3;
    public static final int MODULO = 4;
    public static final int EXP = 5;
    public static final int LPAREN = 6;
    public static final int RPAREN = 7;
    public static final int COMMA = 8;
    public static final int PERIOD = 9;
    public static final int EQUALS = 10;
    public static final int NUMBER = 11;
    public static final int IDENT = 12;
    public static final int STRING = 13;
    public static final int EOF = 14;
    
    public static final int IF = 15;
    public static final int PRINT = 16;
    public static final int SQRT = 17;
    public static final int LESSTHAN = 18;
    public static final int LESSTHANEQUALS = 19;
    public static final int GREATERTHAN = 20;
    public static final int GREATERTHANEQUALS = 21;
    public static final int SEMICOLON = 22;
    public static final int EQUALTO = 23;
    public static final int NOTEQUATO = 24;
    
    
    private int id;
    private String lexeme;
    private String category;

    public Token(){
        
    }
   public Token(int id, String lexeme, String category){
       this.id = id;
       this.lexeme = lexeme;
       this.category = category;
   }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    
}
