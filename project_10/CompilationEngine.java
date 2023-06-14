import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/*
 CompilationEngine
 */

public class CompilationEngine {

  File inFile;
  File outFile;
  JackTokenizer token;
  FileWriter out ;
  private boolean isFirstSubroutine;

  /**
  * Creates a new compilation engine with the given input out put The next routine called (by the JackAnalyzer module) must be compileClass
  * @throws IOException
  */
  public CompilationEngine(File inFile, File outFile)throws IOException{
    token = new JackTokenizer(inFile);
    out  = new FileWriter(outFile);
    isFirstSubroutine = true;
  }

  /**
  * Compiles a complete class
  * @throws IOException
  */
  public void compileClass() throws IOException{
    token.advance();
    out.write("<class>\n");
    out.write("<keyword> class </keyword>\n");
    token.advance();
    out.write("<identifier> " + token.identifier() + " </identifier>\n");
    token.advance();
    out.write("<symbol> { </symbol>\n");
    token.advance();
    compileClassVarDec();
    compileSubroutine(); 
    out.write("<symbol> } </symbol>\n");
    out.write("</class>\n");
    out.close();
  }

  /**
  * Compiles a static variable declaration or a field declaration
  */
  public void compileClassVarDec() throws IOException{
    //token.advance();
    while(token.keyWord() == JackTokenizer.KeyWord.STATIC || token.keyWord() == JackTokenizer.KeyWord.FIELD){
      out.write("<classVarDec>\n");
      out.write("<keyword> " + token.keyWord().toString() +" </keyword>\n");
      //move to the next token
      token.advance();

      if(token.tokenType() == JackTokenizer.TokenType.IDENTIFIER){ //if type is a classname
        out.write("<identifier> " + token.identifier() + " </identifier>\n");
      }else{
        out.write("<keyword> " + token.keyWord().toString() + " </keyword>\n");
      }
      
      //move to the next token
      token.advance();
      
      //write the varName
      out.write("<identifier> " + token.identifier() + " </identifier>\n");
      //move to the next token
      token.advance();
      //check for more var names if there are 
      while(token.symbol() == ','){
        out.write("<symbol> , </symbol>\n");
        token.advance();
        //write the varName
        out.write("<identifier> " + token.identifier() + " </identifier>\n");
        token.advance();
      }
      //add the semi-colon
      out.write("<symbol> ; </symbol>\n");
      //close the classVarDec
      out.write("</classVarDec>\n");
      
      //move to the next token
      token.advance();
    }
  }
  
  /**
  * Compiles a complete method, function or constructor.
  * @throws IOException
  */
  public void compileSubroutine() throws IOException{
    boolean moreSubroutines = false;
    if(token.symbol() == '}'){
      return;
    }
    //open the subroutine 
    if(isFirstSubroutine && (token.keyWord() == JackTokenizer.KeyWord.CONSTRUCTOR || token.keyWord() == JackTokenizer.KeyWord.FUNCTION || token.keyWord() == JackTokenizer.KeyWord.METHOD)){
      out.write("<subroutineDec>\n");
      moreSubroutines = true;
    }else if(token.keyWord() == JackTokenizer.KeyWord.CONSTRUCTOR || token.keyWord() == JackTokenizer.KeyWord.FUNCTION || token.keyWord() == JackTokenizer.KeyWord.METHOD){
      //write the Constructor|Function|Method
      out.write("<keyword> " + token.keyWord() + " </keyword>");
      moreSubroutines  = true;
      token.advance();
    }
    //if function or method write its type 
    if(token.tokenType() == JackTokenizer.TokenType.KEYWORD){
      out.write("<keyword> " + token.keyWord() + " </keyword>\n");
      token.advance();
      
    }else{ //constructor 
      out.write("<identifier> " + token.identifier() + " </identifier>\n");
      token.advance();
    }
    //write the name of the subroutine 
    if(token.tokenType() == JackTokenizer.TokenType.IDENTIFIER){
      out.write("<identifier> " + token.identifier() + " </identifier>\n");
      token.advance();
    }
    // if the is a int or void ect
    else if (token.tokenType() == JackTokenizer.TokenType.KEYWORD) {
      out.write("<keyword> " + token.keyWord() + " </keyword>\n");
      token.advance();
    }
    if (token.tokenType()== JackTokenizer.TokenType.IDENTIFIER) {
      out.write("<identifier> " + token.identifier() + " </identifier>\n");
      token.advance();
    }
    
    //write the param list 
    if(token.symbol() == '('){
      out.write("<symbol> " + token.symbol() + " </symbol>\n");
      compileParameterList();
      out.write("<symbol> " + token.symbol() + " </symbol>\n");
      token.advance();
    }

    compileSubroutineBody();
    
    if (moreSubroutines) {
      out.write("</subroutineDec>\n");
      isFirstSubroutine = true;
    }
    
    compileSubroutine();

  }
  
  /**
  * Compiles compiles a (possibly empty) parameter list.
  * does not handle the enclosing parentheses token( and)
    */
  public void compileParameterList()throws IOException{
    out.write("<parameterList>\n");
    token.advance();
    while(!(token.symbol() == ')' && token.tokenType() == JackTokenizer.TokenType.SYMBOL )){
      if(token.tokenType() == JackTokenizer.TokenType.KEYWORD ){
        out.write("<keyword> " + token.keyWord().toString() + " </keyword>\n");
        token.advance();
      }
      else if(token.tokenType() == JackTokenizer.TokenType.IDENTIFIER ){
        out.write("<identifier> " + token.identifier() + " </identifier>\n");
        token.advance();
      }

      else if(token.tokenType() == JackTokenizer.TokenType.SYMBOL && token.symbol() == ','){
        out.write("<symbol> " + token.symbol() + " </symbol>\n");
        token.advance();

      }

    }
    out.write("</parameterList>\n");
  }

  /*
  * compiles a subroutine body.
  */
  public void compileSubroutineBody() throws IOException{
    if(token.symbol() == '{') {
      out.write("<subroutineBody>\n");
      out.write("<symbol> " + token.symbol() + " </symbol>\n");
      token.advance();
    }
    //varDec*
    while(token.keyWord() == JackTokenizer.KeyWord.VAR){
      compileVarDec();
    }
    //statemetents
    out.write("<statements>\n");
    compileStatements();
    out.write("</statements>\n");

    //closing bracket '}'
    if(token.symbol() == '}') {
      out.write("<symbol> " + token.symbol() + " </symbol>\n");
      out.write("</subroutineBody>\n");
      token.advance();
    }
    
  }

  /**
  * Compiles a var decleration 
  * @throws IOException
  */
  public void compileVarDec() throws IOException{
    //'var'
    out.write("<varDec>\n");
    out.write("<keyword> " +  token.keyWord().toString() +  " </keyword>\n");
    //move the token
    token.advance();
    //write the type 
    if(token.tokenType() == JackTokenizer.TokenType.IDENTIFIER){ //if type is a classname
        out.write("<identifier> " + token.identifier() + " </identifier>\n");
    }else{
        out.write("<keyword> " + token.keyWord().toString() + " </keyword>\n"); //type keyword
    }
      
    //move to the next token
    token.advance();
      
    //write the varName
    out.write("<identifier> " + token.identifier() + " </identifier>\n");
    //move to the next token
    token.advance();
    //check for more var names if there are 
    while(token.symbol() == ','){
      out.write("<symbol> , </symbol>\n");
      token.advance();
      //write the varName
      out.write("<identifier> " + token.identifier() + " </identifier>\n");
      token.advance();
    }
    //add the semi-colon
    out.write("<symbol> " + token.symbol() + " </symbol>\n");
    //close 
    out.write("</varDec>\n");
    token.advance();
  }

  /**
  * compiles a sequence of statments
  * Does not handle the enclosing curly bracket token { and }
  * @throws IOException
  */
  public void compileStatements() throws IOException{
    //token.advance();
    if (token.symbol() == '}' && token.tokenType() == JackTokenizer.TokenType.SYMBOL){
      return;
    }
    //do statment
    else if (token.keyWord()== JackTokenizer.KeyWord.DO && (token.tokenType() == JackTokenizer.TokenType.KEYWORD)) {
      out.write("<doStatement>\n");
      compileDo();
      out.write(("</doStatement>\n"));
    //let
    } else if (token.keyWord()== JackTokenizer.KeyWord.LET && (token.tokenType() == JackTokenizer.TokenType.KEYWORD)) {
      out.write("<letStatement>\n");
      compileLet();
      out.write(("</letStatement>\n"));
      //if
    } else if (token.keyWord()== JackTokenizer.KeyWord.IF && (token.tokenType() == JackTokenizer.TokenType.KEYWORD)) {
      out.write("<ifStatement>\n");
      compileIf();
      out.write(("</ifStatement>\n"));
      //while
    } else if (token.keyWord()== JackTokenizer.KeyWord.WHILE && (token.tokenType() == JackTokenizer.TokenType.KEYWORD)) {
      out.write("<whileStatement>\n");
      compileWhile();
      out.write(("</whileStatement>\n"));
      //return
    } else if (token.keyWord()== JackTokenizer.KeyWord.RETURN && ((token.tokenType() == JackTokenizer.TokenType.KEYWORD))) {
      out.write("<returnStatement>\n");
      compileReturn();
      out.write(("</returnStatement>\n"));
    }

    compileStatements();
  }


  /**
  * Compiles a let statment.
  * @throws IOException
  */
  public void compileLet() throws IOException{
    out.write("<keyword> " + token.keyWord() + " </keyword>\n");
    token.advance();
    out.write("<identifier> " + token.identifier() + " </identifier>\n");
    token.advance();
    if ((token.symbol() == '[') && token.tokenType() == JackTokenizer.TokenType.SYMBOL){
      out.write("<symbol> " + token.symbol() + " </symbol>\n");
      compileExpression();
      if (((token.symbol() == ']') && token.tokenType() == JackTokenizer.TokenType.SYMBOL)){
        out.write("<symbol> " + token.symbol() + " </symbol>\n");
      }
      token.advance();

    }
    out.write("<symbol> " + token.symbol() + " </symbol>\n");
    compileExpression();
    if(token.symbol() != ';') token.advance();
    out.write("<symbol> " + token.symbol() +" </symbol>\n");
    token.advance();
    
  }

  /**
  * Compiles an if statsment possibly with a trailing else clause.
  * @throws IOException
  */
  public void compileIf() throws IOException{
    out.write("<keyword> " + token.keyWord().toString() + " </keyword>\n");
    token.advance();
    //open (
    out.write("<symbol> " + token.symbol() +" </symbol>\n");
    compileExpression();
    //<symbol> ) </symbol>
    out.write("<symbol> " + token.symbol() +" </symbol>\n");
    token.advance();
    //<symbol> { </symbol>
    out.write("<symbol> " + token.symbol() +" </symbol>\n");  
    token.advance();
    out.write("<statements>\n");
    compileStatements();
    out.write("</statements>\n");
    //<symbol> } </symbol>
    out.write("<symbol> " + token.symbol() +" </symbol>\n");
    token.advance();

    // else clause
    if(token.keyWord() == JackTokenizer.KeyWord.ELSE){
      out.write("<keyword> " + token.keyWord() + " </keyword>\n");
      token.advance();
      //<symbol> { </symbol>
      out.write("<symbol> " + token.symbol() +" </symbol>\n");
      token.advance();
      out.write("<statements>\n");
      compileStatements();
      out.write("</statements>\n");
      //<symbol> } </symbol>
      out.write("<symbol> " + token.symbol() +" </symbol>\n");
      token.advance();
    }
  }

  /**
  * compiles a  While statment.
  * @throws IOException
  */
  public void compileWhile() throws IOException{
    //System.out.println(token.keyWord());
    out.write("<keyword> " + token.keyWord() + " </keyword>\n");
    token.advance();
    //open (
    out.write("<symbol> " + token.symbol() + " </symbol>\n");
    compileExpression();
    //token.advance();
    //<symbol> ) </symbol>\n
    out.write("<symbol> " + token.symbol() + " </symbol>\n");
    token.advance();
    //<symbol> { </symbol>\n
    out.write("<symbol> " + token.symbol() + " </symbol>\n");
    out.write("<statements>\n");
    token.advance();
    compileStatements();
    out.write("</statements>\n");
    //"<symbol> } </symbol>
    out.write("<symbol> " + token.symbol() + " </symbol>\n");
    token.advance();

  } 
  //compile expression
  public void compileExpression() throws IOException {
    out.write("<expression>\n");
    compileTerm();
    while (true){
      //System.out.println("guess : "+ token.symbol());
      //token.advance();
      //op
      if (token.tokenType() == JackTokenizer.TokenType.SYMBOL &&  token.isSpecialop()){
          if (token.symbol() == '>'){
              out.write("<symbol> &gt; </symbol>\n");
          }else if (token.symbol() == '<'){
              out.write("<symbol> &lt; </symbol>\n");
          }else if (token.symbol() == '&') {
              out.write("<symbol> &amp; </symbol>\n");
          }else {
              out.write("<symbol> " + token.symbol() + " </symbol>\n");
          }
            
          //term
          compileTerm();
      }else {
        break;
      }
    }

    out.write("</expression>\n");
  }
  

  private void compileTerm() throws IOException {
    out.write("<term>\n");
    token.advance();

    if((token.tokenType() != JackTokenizer.TokenType.IDENTIFIER) ){
      //when we have a String
      if(token.tokenType() == JackTokenizer.TokenType.STRING_CONST){
        out.write("<stringConstant> " + token.stringVal() + " </stringConstant>\n");

      }
      //when we have an Int
      else if(token.tokenType() == JackTokenizer.TokenType.INT_CONST){
        out.write("<integerConstant> " + token.intVal() + " </integerConstant>\n");

      }
      //when we have Parenthesis
      else if(token.tokenType() == JackTokenizer.TokenType.SYMBOL && token.symbol() == '('){
        out.write("<symbol> " + token.symbol() + " </symbol>\n");
        compileExpression();
        //token.advance(); //bug-fix
        out.write("<symbol> " + token.symbol() + " </symbol>\n");

      }
      //when we have a THIS NULL TRUE FALSE
      else if(token.tokenType() == JackTokenizer.TokenType.KEYWORD && ( token.keyWord() == JackTokenizer.KeyWord.THIS || token.keyWord() == JackTokenizer.KeyWord.NULL || token.keyWord() == JackTokenizer.KeyWord.FALSE || token.keyWord() == JackTokenizer.KeyWord.TRUE) ){
        out.write("<keyword> " + token.keyWord() + " </keyword>\n");

      }
      //when we have a unary opirator
      else if(token.tokenType() == JackTokenizer.TokenType.SYMBOL && (token.symbol() =='~' || token.symbol() =='-' )){
        out.write("<symbol> " + token.symbol() + " </symbol>\n");
        compileTerm();
        token.goBack_pionter(); //fixed bug?

      }
      token.advance();
    }
    //subroutine call | varName | varName '['expression']' 
    else{
      String saveIdn = token.identifier();
      System.out.println("id: " + saveIdn);
      token.advance();
      //if the is a []
      if (token.tokenType() == JackTokenizer.TokenType.SYMBOL && token.symbol() == '[') {
        out.write("<identifier> " + saveIdn + " </identifier>\n");
        out.write("<symbol> " + token.symbol() + " </symbol>\n");
        compileExpression();
        out.write("<symbol> " + token.symbol() + " </symbol>\n");
      }
      else if(token.tokenType() == JackTokenizer.TokenType.SYMBOL && (token.symbol() == '.') || token.symbol() == '('){
        out.write("<identifier> " + saveIdn + " </identifier>\n");
        compileCall();
       // token.goBack_pionter();
      }
      else {
        out.write("<identifier> " + saveIdn + " </identifier>\n");
      }
    }
    out.write("</term>\n");
  }

  /**
  * compiles a do statment.
  * @throws IOException
  */
  public void compileDo() throws IOException{
    if(token.keyWord() == JackTokenizer.KeyWord.DO){
      out.write("<keyword> " + token.keyWord() + " </keyword>\n");
      //move to identier
      token.advance(); //bug-fix1
      out.write("<identifier> " + token.identifier() + " </identifier>\n");
      //move to symbol '.' | '('
      token.advance();
      compileCall();
      token.advance();
      out.write("<symbol> " + token.symbol() + " </symbol>\n");
      token.advance();
    }

  }
  private void compileCall() throws IOException {
    //took out possibly same code twice more efficient like this 
    // if(token.tokenType() == JackTokenizer.TokenType.SYMBOL && token.symbol() =='('){
    //   //<symbol> ( </symbol>
    //   out.write("<symbol> "+ token.symbol() +" </symbol>\n");
    //   out.write("<expressionList>\n");
    //   compileExpressionList();
    //   out.write("</expressionList>\n");
    //   // parentheses ) shouuld go here maybe gonna need to be fixed 
    //   token.advance();
    // }
    //else 
    if (token.tokenType() == JackTokenizer.TokenType.SYMBOL && token.symbol() =='.'){
      out.write("<symbol> "+ token.symbol() +" </symbol>\n");
      token.advance();
      out.write("<identifier> " + token.identifier() + " </identifier>\n");
      token.advance();
    }
    //open (
    out.write("<symbol> "+ token.symbol() +" </symbol>\n");
    //expression list
    out.write("<expressionList>\n");
    compileExpressionList();
    System.out.println("after compile list: " + token.symbol());
    out.write("</expressionList>\n");
    //close )
    out.write("<symbol> "+ token.symbol() +" </symbol>\n");
    //}
  }
  
  /*
  * Compiles a possiably empty) commasepartaed list of expressinons
  * returns the number of expretions;
  */
  private void compileExpressionList() throws IOException {

    token.advance();
    
    if(token.tokenType() == JackTokenizer.TokenType.SYMBOL && token.symbol() ==')'){
      return;
    }
    else{
      token.goBack_pionter();
      //count++;
      compileExpression();
      System.out.println("token after compiling exprssion in list: " + token.tokenType() + " : " + token.symbol());
    }
    while(true){
      if(token.symbol() ==',' && token.tokenType() == JackTokenizer.TokenType.SYMBOL){
        out.write("<symbol> "+ token.symbol() +" </symbol>\n");
        //count++;
        compileExpression();
      }else{
        break;
      }
    }
  }


  /**
  * Compile return statment 
  * @throws IOException
  */
  public void compileReturn() throws IOException{
    //keyword return 
    out.write("<keyword> " + token.keyWord() + " </keyword>\n");
    token.advance();
    if((token.symbol() == ';'&& token.tokenType() == JackTokenizer.TokenType.SYMBOL)){
      out.write("<symbol> "+ token.symbol() +" </symbol>\n");
      token.advance();
    }
    else{
      token.goBack_pionter();
      compileExpression();
      out.write("<symbol> "+ token.symbol() +" </symbol>\n");
      token.advance();
    }
  }
  public static void main(String[] args) throws IOException {
 File output = new File("output.xml");
 File file = new File("Main.jack");
CompilationEngine com = new CompilationEngine(file, output);
com.compileClass();;

  }

}
