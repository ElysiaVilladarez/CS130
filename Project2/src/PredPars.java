
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elysi
 */
public class PredPars {
    public static Token curToken;
    private static LexAny program;
    private static ArrayList<Variable> vars;
    private static boolean found;
    public static void main(String args[]){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the absolute filepath of the file to analyze:\n");
        String fileName = in.nextLine();
        PredPars p = new PredPars(new LexAny(fileName));
        while(p.curToken.getId() != Token.EOF){
            p.G();
        }
    }
    
    public PredPars(LexAny program){
        this.program = program;
        vars = new ArrayList<>();
        curToken = program.getToken();
    }
    public static void getNextToken(){
        curToken = program.getToken();
    }
    public static double findValue(String name){
        double value = 0;
        for(Variable a: vars){
            if (a.getName().equals(name)){
                value = a.getValue();
                found = true;
            }
        }
        return value;
    }
    
    public void G(){
        switch(curToken.getId()){
            case Token.PRINT:
                getNextToken(); // consume PRINT
                Z();
                break;
            case Token.IDENT:
                Y();
                break;
            case Token.IF:
                getNextToken(); //consume IF
                break;
            default:
                
                break;
        }
        getNextToken();
    }
    
    public void Z() { //print
        
        getNextToken(); //consume parenthesis
        
        switch(curToken.getId()){
            case Token.STRING:
                System.out.println(curToken.getLexeme());
                break;
            case Token.IDENT:
                X();
                break;
            default:
                 System.out.println(A());
                 
                break;
        }
        getNextToken(); //consume closing paren
    }
//    A -> B + A | B
//    B ->  C - B | C
//    C -> D / C | D % C | D * C | D
//    D -> E ** D | E
//    E -> (A)  | NUM | -NUM | F
//    F -> (A) | (X)
    // X -> IDENT
    public double A(){
        double ans = B();
        if(curToken.getId() == Token.PLUS){
            getNextToken();
            ans += A();
        }
        return ans;
    }
    public double B(){
        double ans = C();
        if(curToken.getId() == Token.MINUS){
            getNextToken();
            ans -= A();
        }
        return ans;
    }
    public double C(){
        double ans = D();
        switch(curToken.getId()){  
            case Token.DIVIDE:
                getNextToken();
                ans /= C();
                break;
            case Token.MULT:
                getNextToken();
                ans *= C();
                break;
            case Token.MODULO:
                getNextToken();
                ans %= C();
                break;
            default:
                break;
        }
        return ans;
        
    }
    public double D(){
        double ans = E();
        if(curToken.getId() == Token.EXP){
            getNextToken();
            ans = Math.pow(ans, D());
        }
        return ans;
    }
    public double E(){
        double ans = 0;
        switch(curToken.getId()){
            case Token.LPAREN:
                getNextToken(); // consume lparen
                ans = A();
                getNextToken(); // consume rparen
                break;
            case Token.MINUS: //negative num
                getNextToken(); // consume negative sign
                ans = -1 * A();
                break;
            case Token.NUMBER:
                ans = Double.parseDouble(curToken.getLexeme().trim());
                getNextToken(); // consume num
                break;
            case Token.SQRT:
                getNextToken(); //consume SQRT
                F();
                break;
            default:
                break;
        }
        return ans;
    }
    public double F(){
        double ans = 0;
        getNextToken(); // consume lparen
        switch(curToken.getId()){
            case Token.IDENT:
                ans = Math.sqrt(X2());
                break;
            default:
                ans = Math.sqrt(A());
                break;
        }
        getNextToken(); //consume rparen
        return ans;
        
    }
    
    public double X(){
        double val = findValue(curToken.getLexeme());
        if(found){
              System.out.println(val);
              found = false;
        } else{
              System.out.println("Unbound identifier: " + curToken.getLexeme());  
        }
       return val;
    }
    
     public double X2(){
        double val = findValue(curToken.getLexeme());
        if(found){
              found = false;
        } else{
              System.out.println("Unbound identifier: " + curToken.getLexeme());  
        }
       return val;
    }
    
    public void Y(){ // IDENT statement
        Variable v = new Variable();
        v.setName(curToken.getLexeme());
        getNextToken(); //consume ident
        getNextToken(); //consume equal sign
        v.setValue(A());
        vars.add(v);
    }
    
   // T -> (A==A) G | (A!=A) G  | (A<=A) G | 
    // (A<A) G | (A>=A) G | (A>A) G | (A==A) | (A!=A) | (A<=A) | (A<A) | (A>=A) | (A>A) 

    public void T(){ // IF statement
        
        getNextToken(); // consume lparen
        boolean isTrue = false;
        double comp1 = A();
        switch(curToken.getId()){
            case Token.EQUALTO:
                getNextToken();
                isTrue = (comp1 == A());
                break;
            case Token.NOTEQUATO:
                getNextToken();
                isTrue = (comp1 != A());
                break;
            case Token.LESSTHANEQUALS:
                getNextToken();
                isTrue = (comp1 <= A());
                break;
            case Token.LESSTHAN:
                getNextToken();
                isTrue = (comp1 < A());
                break;
            case Token.GREATERTHANEQUALS:
                getNextToken();
                isTrue = (comp1 >= A());
                break;
            case Token.GREATERTHAN:
                getNextToken();
                isTrue = (comp1 > A());
                break;
            default:
                break;
        }
        
        getNextToken(); //consume rparen
                
        if(isTrue){
            G();
        } else{
            while(curToken.getId() != Token.SEMICOLON){ //consume until end
                getNextToken();
            }
        }
    }
}
