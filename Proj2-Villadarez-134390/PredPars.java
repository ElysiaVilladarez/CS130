
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
       // String fileName = "sample input file 1.txt";
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
    public void ER(String mes){
        System.out.println("ERROR: " + mes);
       // getNextToken();
        System.exit(0);
    }
    
    public boolean checkParen(int whichParen){
        return (curToken.getId() == whichParen);
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
                T();
                break;
            default:
                ER("NOT INCLUDED IN SCOPE");
                break;
        }
        if(checkParen(Token.SEMICOLON)){
            getNextToken(); //consume semicolon
        } else{
            ER("NO SEMICOLON");
        }
        
        
      //  System.out.println("G(): curtoken=" + curToken.getLexeme());
        
    }
    
    public void Z() { //print
        if(checkParen(Token.LPAREN)) getNextToken(); //consume parenthesis
        else ER("NO LEFT PARENTHESIS");
          
        switch(curToken.getId()){
            case Token.STRING:
                String printS = curToken.getLexeme().trim();
                System.out.println(printS.substring(1, printS.length()-1));
                getNextToken();
                break;
            default:
                System.out.println(A());
                 
                break;
        }
        
        if(checkParen(Token.RPAREN)) getNextToken(); //consume parenthesis
        else ER("NO RIGHT PARENTHESIS"); 
        
    }
//    A -> B + A | B
//    B ->  C - B | C
//    C -> D / C | D % C | D * C | D
//    D -> E ** D | E
//    E -> (A)  | NUM | -NUM | F | -F
//    F -> (A)
    // X -> IDENT
    public double A(){
        double ans = B();
        if(curToken.getId() == Token.PLUS){
            getNextToken();
            ans += A();
        }
        
     //   System.out.println("A(): " + ans + " curtoken=" + curToken.getLexeme());
        return ans;
    }
    public double B(){
        double ans = C();
        if(curToken.getId() == Token.MINUS){
            getNextToken();
            ans -= A();
        }
        
       // System.out.println("B(): " + ans + " curtoken=" + curToken.getLexeme());
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
        
       // System.out.println("C(): " + ans + " curtoken=" + curToken.getLexeme());
        return ans;
        
    }
    public double D(){
        double ans = E();
        if(curToken.getId() == Token.EXP){
            getNextToken();
            ans = Math.pow(ans, D());
        }
        
      //  System.out.println("D(): " + ans + " curtoken=" + curToken.getLexeme());
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
                if(curToken.getId()==Token.NUMBER) {
                    ans = Double.parseDouble("-"+curToken.getLexeme().trim());
                    getNextToken();
                } 
                else {
                    ans = -1 * A();
                }
                break;
            case Token.NUMBER:
                ans = Double.parseDouble(curToken.getLexeme().trim());
                getNextToken(); // consume num
                break;
            case Token.SQRT:
                getNextToken(); //consume SQRT
                ans = F();
                break;
            case Token.IDENT:
                ans = X2();
                break;
            default:
                ER("NOT INCLUDED IN GRAMMAR");
                break;
        }
       
       // System.out.println("E(): " + ans + " curtoken=" + curToken.getLexeme());
        return ans;
    }
    public double F(){
        double ans = 0;
        
        if(checkParen(Token.LPAREN)) getNextToken(); //consume parenthesis
        else ER("NO LEFT PARENTHESIS");
        
        switch(curToken.getId()){
//            case Token.IDENT:
//                ans = Math.sqrt(X2());
//                break;
            default:
                ans = Math.sqrt(A());
                break;
        }
        
        if(checkParen(Token.RPAREN)) getNextToken(); //consume parenthesis
        else ER("NO RIGHT PARENTHESIS");
        //System.out.println("F(): " + ans + " curtoken=" + curToken.getLexeme());
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
        getNextToken(); //consume ident
       return val;
    }
    
    public void Y(){ // IDENT statement
        Variable v = new Variable();
        v.setName(curToken.getLexeme());
        getNextToken(); //consume ident
        if(checkParen(Token.EQUALS)) getNextToken(); // must be equals
        else ER("WRONG SIGN");
        v.setValue(A());
        vars.add(v);
    }
    
   // T -> (A==A) G | (A!=A) G  | (A<=A) G | 
    // (A<A) G | (A>=A) G | (A>A) G | (A==A) | (A!=A) | (A<=A) | (A<A) | (A>=A) | (A>A) 

    public void T(){ // IF statement
        
       
        if(checkParen(Token.LPAREN)) getNextToken(); //consume parenthesis
        else ER("NO LEFT PARENTHESIS");
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
                ER("NOT INCLUDED IN SCOPE");
                break;
        }
        
        
        if(checkParen(Token.RPAREN)) getNextToken(); //consume parenthesis
        else ER("NO RIGHT PARENTHESIS");
                
        if(isTrue){
            G2();
        } else{
            while(curToken.getId()!=Token.SEMICOLON){ //consume until end
                getNextToken();
            }
        }
        
        
    }
    
//    public double T2(){
//        double ans = 0;
//          System.out.println("T2():  curtoken=" + curToken.getLexeme());
//      
//        switch(curToken.getId()){
//            case Token.IDENT:
//                
//                ans = X2();
//               System.out.println("ident:  curtoken=" + curToken.getLexeme());
//      
//                break;
//            default:
//                return A();
//        }
//        
//        return ans;
//    }
    
    public void G2(){
        switch(curToken.getId()){
            case Token.PRINT:
                getNextToken(); // consume PRINT
                Z();
                break;
            case Token.IDENT:
                Y();
                break;
          
            default:
                break;
        }
        
        
      
    }
}
