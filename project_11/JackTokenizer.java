import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class JackTokenizer { 
    public enum TokenType { 
        KEYWORD, 
        SYMBOL,
        IDENTIFIER,
        INT_CONST,
        STRING_CONST;
    }
    public enum KeyWord {
        CLASS,
        METHOD,
        FUNCTION,
        CONSTRUCTOR,
        INT,
        BOOLEAN,
        CHAR,
        VOID,
        VAR,
        STATIC,
        FIELD,
        LET,
        DO,
        IF,
        ELSE,
        WHILE,
        RETURN,
        TRUE,
        FALSE,
        NULL,
        THIS;

        @Override 
        public String toString() {
            return name().toLowerCase();
        }
    }
      
    private Scanner scanner;
    private String line;
    private String preparedString;
    int pointer;
    private String curToken;
    private static String symbols = "{}()[].,;+-*/&|<>=~";
    private static ArrayList<String> keyWords = new ArrayList<String>();
    private  ArrayList<String> tokenList = new ArrayList<String>();


    /**
     * Opens the input .jack file and gets ready to tokenize it 
     */
    public JackTokenizer(File file){
        //put all the key words into the list
        keyWords.add("class");
        keyWords.add("constructor");
        keyWords.add("function");
        keyWords.add("method");
        keyWords.add("field");
        keyWords.add("static");
        keyWords.add("var");
        keyWords.add("int");
        keyWords.add("char");
        keyWords.add("boolean");
        keyWords.add("void");
        keyWords.add("true");
        keyWords.add("false");
        keyWords.add("null");
        keyWords.add("this");
        keyWords.add("do");
        keyWords.add("if");
        keyWords.add("else");
        keyWords.add("while");
        keyWords.add("return");
        keyWords.add("let");

        // intilize 
        preparedString = "";
        curToken = "";
        pointer = 0;
        //read the jack file
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //put the file into one big string without any of the comments or whitespace
        line = "";
        while(scanner.hasNext()){
            //get rid of // comments
            line =  removeComment(scanner.nextLine().trim());
            if (line.length() > 0) {
                 preparedString += line + "\n";
                 // get rid of /* */ comments
                    if(line.startsWith("/*") || line.startsWith("*/") || line.endsWith("*/")){
                        preparedString =  remove_Slash_Commet(preparedString).trim();
                    }
            }
        }
        //split the string up into tokens and add the token to the tokenList
        while(preparedString.length() > 0){
            for (int i = 0; i < keyWords.size(); i++) {
                if(preparedString.startsWith(keyWords.get(i))){
                    String word = keyWords.get(i).toString();
                    tokenList.add(word);
                    preparedString = preparedString.substring(word.length());
                }
            }
            if (symbols.contains(preparedString.substring(0, 1))) { 
                char symbol = preparedString.charAt(0);
                tokenList.add(Character.toString(symbol));
                preparedString = preparedString.substring(1);
            // ift here is a number
             }else if (Character.isDigit(preparedString.charAt(0))) { 
                String value = preparedString.substring(0, 1);
                preparedString = preparedString.substring(1);
                while (Character.isDigit(preparedString.charAt(0))) {
                    value += preparedString.substring(0, 1);
                    preparedString = preparedString.substring(1);

                }
                tokenList.add(value);
                // if is there is a \ 
            }else if (preparedString.substring(0,1).equals("\"")){
                
                preparedString = preparedString.substring(1);
                String str = "\"";

                while(preparedString.charAt(0) != '\"'){
                    str += preparedString.charAt(0);
                    preparedString = preparedString.substring(1);
                }
                str = str+ "\"";
                tokenList.add(str);
                preparedString = preparedString.substring(1);
            //if there is a letter
            }else if(Character.isLetter(preparedString.charAt(0)) ||  preparedString.substring(0, 1).equals("_")){
                String id = preparedString.substring(0, 1);
                preparedString = preparedString.substring(1);
                while (Character.isLetter(preparedString.charAt(0)) || preparedString.substring(0, 1).equals("_")){
                    id += preparedString.charAt(0);
                    preparedString = preparedString.substring(1);
                }
               tokenList.add(id);
            
            }else preparedString =   preparedString.substring(1);
        }
    }

    /**
     * Are there more tokens in the input?
     * @return
     */
    public boolean hasMoreTokens() {
        return pointer < tokenList.size();
    }

    /**
     * Gets the token from the input, and makes it the current token.
     * This method should only be called if hasMoreTokens is true. 
     * Initially there is no current token
     */
    public void advance() {
        if (hasMoreTokens()) {
            curToken = tokenList.get(pointer);
            pointer++;
            
        }
    }

    /**
     * returns the type of the current token, as a constant
     */
    public TokenType tokenType() {
        if (keyWords.contains(curToken)) {
            return TokenType.KEYWORD;
        } else if (symbols.contains(curToken)) {
            return TokenType.SYMBOL;
        } else if (Character.isDigit(curToken.charAt(0))) {
            return TokenType.INT_CONST;
        } else if (curToken.charAt(0) == '"') {
            return TokenType.STRING_CONST;
        } else {
            return TokenType.IDENTIFIER;
        }            
    }
    
    /**
     * Returns the keyword which is the current token, as a constant.
     * This method shold be called only if tokenType is KEYWORD
     */
    public KeyWord keyWord() {
        switch (curToken) {
            case "class":
                return KeyWord.CLASS;
            case "constructor":
                return KeyWord.CONSTRUCTOR;
            case "function":
                return KeyWord.FUNCTION;
            case "method":
                return KeyWord.METHOD;
            case "field":
                return KeyWord.FIELD;
            case "static":
                return KeyWord.STATIC;
            case "var":
                return KeyWord.VAR;
            case "int":
                return KeyWord.INT;
            case "char":
                return KeyWord.CHAR;
            case "boolean":
                return KeyWord.BOOLEAN;
            case "void":
                return KeyWord.VOID;
            case "true":
                return KeyWord.TRUE;
            case "false":
                return KeyWord.FALSE;
            case "null":
                return KeyWord.NULL;
            case "this":
                return KeyWord.THIS;
            case "let":
                return KeyWord.LET;
            case "do":
                return KeyWord.DO;
            case "if":
                return KeyWord.IF;
            case "else":
                return KeyWord.ELSE;
            case "while":
                return KeyWord.WHILE;
            case "return":
                return KeyWord.RETURN;
            default:
                return null;
        }
    }
    
    /**
     * Returns the character which is the current token.
     * Should only be called if tokenType is SYMBOl
     * @return
     */
    public char symbol() {
        if (tokenType() == TokenType.SYMBOL) {
            return curToken.charAt(0);
        }else {
            return '\0';
        }
    }

    /**
     * Returns the string which is the current token.
     * Should only be called if tokenType is IDENTIFIER
     * @return
     */
    public String identifier() {
        if (tokenType() == TokenType.IDENTIFIER) {
            return curToken;
        } else {
            return null;
        }
    }

    /**
     * Returns the integer value of the current token.
     * Should only be called if tokenType is INT_CONST
     * @return
     */
    public int intVal() {
        if(tokenType() == TokenType.INT_CONST){
            return Integer.parseInt(curToken);
        }else{
            return -1;
        } 
    }

    /**
     * Returns the string value of the current token, without the 
     * opening and closing double quotes.
     * Should only be called if tokenType is STRING_CONST
     * @return
     */
    public String stringVal() {
        if (tokenType() == TokenType.STRING_CONST) {
            int openQuote = curToken.indexOf("\"") + 1;
            int closeQuote = curToken.lastIndexOf("\"");
            return curToken.substring(openQuote, closeQuote);
        } else {
            return null;
        }
    }
    
    /**removes comments from the string */
    private String removeComment(String st){
        int index = -1;
        index = st.indexOf("//");
        if(index == 0 ){
            st = "";
        }
        else  if(index != -1 ){
            st =  st.substring(0, index);
            
        }
        return st;
    }

    /**removes the slash* type comments from the string */
    private String remove_Slash_Commet(String st){
        int indexbegining = -1;
        int index_end = -1;
        indexbegining = st.indexOf("/*");
        index_end = st. indexOf("*/");
        if(index_end != -1 && indexbegining != -1){
            st = st.substring(0, indexbegining) + st.substring(index_end+2);
        }
        return st;
    }
    public void goBack_pionter(){
        pointer= pointer-2;
        advance();
        
        
    }
    
    //+-*/&|<>="
    public boolean isSpecialop() {
        return(curToken.charAt(0)=='<' || curToken.charAt(0)=='>' || curToken.charAt(0)=='&' || curToken.charAt(0)=='*' || curToken.charAt(0)=='|' || curToken.charAt(0)=='=' ||  curToken.charAt(0)=='-' || curToken.charAt(0)=='+' || curToken.charAt(0)=='/');
        }
}



