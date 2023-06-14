import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Parser {
    public enum Command {C_ARITHMETIC, C_PUSH, C_LABEL, C_POP, C_GOTO, C_IF, C_FUNCTION, C_RETURN, C_CALL;}
    private File file;
    private LineNumberReader br;
    public String current_command; 
    private String [] commandList;
   
    /**
     * Constructer 
     * Opens the input file/stream and 
     * gets ready to parse it.
     * @param vmFile
     * @throws IOException
     */
    public Parser(File vmFile) throws IOException{
        this.file = vmFile;
        this.br = new LineNumberReader(new FileReader(file));
        this.current_command = null;
    }

    /**
     * Are there more lines in the input
     * @return
     * @throws IOException
     */
    public boolean hasMoreLines() throws IOException {
        return br.ready();
    } 

/**
 * Skips over whitespace and comments if necesary.
 * Reads the next instruction from the input
 * and makes it the current instruction. 
 * Should be called only if hasMoreLines() is true. 
 * Initially there is no current instruction
 * @throws IOException
 */
    public void advance() throws IOException {
        if(hasMoreLines()) {
            String nextCommand = br.readLine();

            //skip comments and white space
            while (nextCommand.trim().startsWith("//") || nextCommand.isEmpty()){
                nextCommand = br.readLine();
            }

            //set the command to the line without the comments if there are any
            int comment = nextCommand.indexOf("//");
            if(comment != -1){
                this.current_command = nextCommand.substring(0, comment).trim();
                commandList = current_command.split(" ");
            }else{
                this.current_command = nextCommand.trim();
                commandList = current_command.split(" ");
            }
        }else{
            br.close();
        }
    }
            
   
    
    /**
     * returns a constant representing the type of the current command. 
     * if the current command is an arithmetic logical command, return C_ARITHMETIC
     * @return
     */
    public Command commandType(){
        switch (commandList[0]) {
            case "pop":
                return Command.C_POP;
            case "push":
                return Command.C_PUSH;
            case "add":
            case "sub":
            case "neg":
            case "eq":
            case "gt":
            case "lt":
            case "and":
            case "or":
            case "not":
                return Command.C_ARITHMETIC;
            case "goto":
                return Command.C_GOTO;
            case "if-goto":
                return Command.C_IF;
            case "call":
                return Command.C_CALL;
            case "return":
                return Command.C_RETURN;
            case "function":
                return Command.C_FUNCTION;
            case "label":
                return Command.C_LABEL;
            default:
                return null;
        }
    }

    /**Returns the first command of the current command 
     * in the case of the C_ARITHIMETIC the command itself (add, sub, ect) is returned
     *  should not be called if the current command is C_RETURN
     */
    public String arg1(){
        if(commandType() == Command.C_ARITHMETIC) return commandList[0];
        return commandList[1];

    }

    /**
     * returns the second argument of the current command 
     * should be called olny if the current command is C_PUSH, C_POP, C_FUNCTION, or C_CALL
     * @return
     */
  
    public int arg2(){
        return Integer.parseInt(commandList[2]);
    }

}

