import java.io.File;
import java.io.IOException;

public class CompilationEngine {
    File inputFile;
    File outputFile;
    JackTokenizer token;
    SymbolTable table;
    VMWriter vm;
    String className;
    
    /**
     * Creates a new compilation engine with the
     * given input and output
     * @param inputFile
     * @param outputFile
     */
    public CompilationEngine(File inputFile, File outputFile)throws IOException {
        token = new JackTokenizer(inputFile);
        vm = new VMWriter(outputFile);
        table = new SymbolTable();



    }

    /**
     * Compiles a complete class
     * @throws IOException
     */
    public void compileClass() throws IOException {
        token.advance();
        token.advance();
        className = token.identifier();
        System.out.println(className);
        token.advance();
        compileClassVarDec();
        compileSubroutine();
        vm.close();

    }

    /**
     * Compiles a static variable declaration,
     * or a field declaration
     */
    public void compileClassVarDec() {
      SymbolTable.Kind kind;
      String type;
      token.advance();
      System.out.println(token.tokenType());
      while(token.keyWord() == JackTokenizer.KeyWord.STATIC || token.keyWord() == JackTokenizer.KeyWord.FIELD){
             
        if(token.keyWord() == JackTokenizer.KeyWord.FIELD){
          kind = SymbolTable.Kind.FIELD;

        }else{
          kind = SymbolTable.Kind.STATIC;
        }
        token.advance();
        if(token.tokenType() == JackTokenizer.TokenType.IDENTIFIER){
          type = token.identifier(); 
        } else{
          type = token.keyWord().toString();
        }
        token.advance();
        table.define(token.identifier(), type, kind);
        token.advance();
        while(token.symbol() == ','){
          token.advance();
          table.define(token.identifier(), type, kind);
          token.advance();
        }
        //skip the ;
        token.advance();
        if(token.keyWord() == JackTokenizer.KeyWord.FUNCTION || token.keyWord() == JackTokenizer.KeyWord.CONSTRUCTOR ||token.keyWord() == JackTokenizer.KeyWord.METHOD){
          token.goBack_pionter();
          return;
        }
      }
    }

    /**
     * Compiles a complete method, function, or constructor.
     */
    public void compileSubroutine() {

    }

    /**
     * Compiles a (possibly empty) parameter list.
     * Doesn't handle the enclosing parentheses tokens.
     */
    public void compileParameterList() {

    }

    /**
     * Compiles a subroutine's body.
     */
    public void compileSubroutineBody() {

    }

    /**
     * Compiles a var declaration
     */
    public void compileVarDec() {

    }

    /**
     * Compiles a sequence of statements.
     * Does't handle the enclosing curly bracket tokens.
     */
    public void compileStatements() {
        // we might need to advance here
        if (token.symbol() == '}' && token.tokenType() == JackTokenizer.TokenType.SYMBOL){
      return;
    }
    //do statment
    else if (token.keyWord()== JackTokenizer.KeyWord.DO && (token.tokenType() == JackTokenizer.TokenType.KEYWORD)) {
      compileDo();
    //let
    } else if (token.keyWord()== JackTokenizer.KeyWord.LET && (token.tokenType() == JackTokenizer.TokenType.KEYWORD)) {
      compileLet();
      //if
    } else if (token.keyWord()== JackTokenizer.KeyWord.IF && (token.tokenType() == JackTokenizer.TokenType.KEYWORD)) {
      compileIf();
      //while
    } else if (token.keyWord()== JackTokenizer.KeyWord.WHILE && (token.tokenType() == JackTokenizer.TokenType.KEYWORD)) {
      compileWhile();
      //return
    } else if (token.keyWord()== JackTokenizer.KeyWord.RETURN && ((token.tokenType() == JackTokenizer.TokenType.KEYWORD))) {
      compileReturn();
      
    }
    //we might need to advance here

    compileStatements();
  }



    /**
     * Compiles a let statement
     */
    public void compileLet() {

    }

    /**
     * Compiles an if statement, possibly with a 
     * trailing else clause.
     */
    public void compileIf() {

    }

    /**
     * Compiles a while statement
     */
    public void compileWhile() {

    }

    /**
     * Compiles a do statement
     */
    public void compileDo(){

    }

    /**
     * Compiles a return statement
     */
    public void compileReturn() {

    }

    /**
     * Compiles an expression
     */
    public void compileExpression() {

    }

    /**
     * Compiles a term. If the current token is an
     * identifier, the routine must resolve it into
     * a variable, an array entry, or a subroutine call.
     * A single lookahead token, which may be [, (, or .,
     * suffieces to distunguish between the possibilities.
     * Any other token is not part of this term ans should
     * not be advanced over.
     */
    public void compileTerm() {

    }

    /**
     * Compiles a (possibly empty) comma-seperated
     * list of expressions. 
     * Returns the number of expressions in the list.
     * @return
     */
    public int compileExpressionList() {
        return 0;
    }

    public static void main(String[] args) throws IOException {
        File input = new File("Main.jack");
        File output = new File("output.vm");
        CompilationEngine engine = new CompilationEngine(input, output);
        engine.compileClass();
        System.out.println("x: ");
        System.out.println(engine.table.kindOf("x"));
        System.out.println(engine.table.indexOf("x"));
        System.out.println(engine.table.typeOf("x"));
        System.out.println("y: ");
        System.out.println(engine.table.kindOf("y"));
        System.out.println(engine.table.indexOf("y"));
        System.out.println(engine.table.typeOf("y"));
        System.out.println("z: ");
        System.out.println(engine.table.kindOf("z"));
        System.out.println(engine.table.indexOf("z"));
        System.out.println(engine.table.typeOf("z"));
        System.out.println("s: ");
        System.out.println(engine.table.kindOf("s"));
        System.out.println(engine.table.indexOf("s"));
        System.out.println(engine.table.typeOf("s"));
        System.out.println("a: ");
        System.out.println(engine.table.kindOf("a"));
        System.out.println(engine.table.indexOf("a"));
        System.out.println(engine.table.typeOf("a"));
    }
}
