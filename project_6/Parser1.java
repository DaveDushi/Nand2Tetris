import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class Parser1 {
    public enum Instruction { A_INSTRUCTION, C_INSTRUCTION, L_INSTRUCTION;}
    private File file;
    private LineNumberReader br;
    public String instruction; 

/**
 * Opens the input file/stream and
 * gets ready to parse it.
 * @param asmFile
 */
    public Parser1(File asmFile) throws IOException{
        this.file = asmFile;
        this.br = new LineNumberReader(new FileReader(file));
        this.instruction = null; 
    }

/**
 * Are there more lines in the input
 * @return
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
            String nextInstruction;
            //find the next instruction that isn't empty line or a comment
            do {
                nextInstruction = br.readLine();
            
            }while (nextInstruction.isEmpty() || nextInstruction.trim().startsWith("//"));

            //find the index of the beginning of a comment if there is one cut it out of the line 
            int comment = nextInstruction.indexOf("//");
            if(comment != -1){
                this.instruction = nextInstruction.trim().substring(0, comment-3);
            
            }else{
                this.instruction = nextInstruction.trim();
            }
        }else{
            br.close();
        }
    }

/**
 * Returns the type of the current instruction:
 * A_INSTRUCTION for @Xxx where Xxx is either a symbol or a decimal number
 * C_INSTRUCTION for dest=comp;jump
 * L_INSTRUCTION (actually, pseudo-INSTRUCTION) for (Xxx) where Xxx is a symbol
 * @return
 */
    public Instruction instructionType(){
        if(this.instruction.startsWith("@")){
            return Instruction.A_INSTRUCTION;
        }else if(this.instruction.startsWith("(")){
            return Instruction.L_INSTRUCTION;
        }else{
            return Instruction.C_INSTRUCTION;
        }
    }

/**
 * Returns the symbol or decimal Xxx of the current instruction @Xxx 
 * or (Xxx). Should be called only when instructionType() 
 * is A_INSTRUCTION or L_INSTRUCTION
 * @return
 */
    public String symbol(){
        if (instructionType() == Instruction.A_INSTRUCTION ){
            return instruction.substring(1, instruction.length());
            }
        if(instructionType() == Instruction.L_INSTRUCTION){
            return instruction.substring(1, instruction.length()-1);
        }
        else return "";
    }

/**
 * Returns the dest mnemonic in the current C-INSTRUCTION (8 possibilities). 
 * Should be called only when INSTRUCTIONType() is C_INSTRUCTION.
 * @return

 */
    public String dest() {
        int destI = this.instruction.indexOf("=");
        if (destI == 0 || destI == -1){
            return null;
        }else{
            return this.instruction.substring(0,destI);
        }
    }

/**
 * Returns the comp mnemonic in the current C-INSTRUCTION (28 possibilities). 
 * Should be called only when INSTRUCTIONType() is C_INSTRUCTION
 * @return
 */
    public String comp() {
        int startI = this.instruction.indexOf("=") + 1;
        int endI = this.instruction.indexOf(";");
        if (endI == -1){
            return this.instruction.substring(startI);
        }else if (startI == -1) {
            return this.instruction.substring(0,endI);
        }else{
            return this.instruction.substring(startI,endI);
        }
    }

/**
 * Returns the jump mnemonic in the current C-INSTRUCTION (8 possibilities). 
 * Should be called only when INSTRUCTIONType() is C_INSTRUCTION
 * @return
 */
    public String jump(){
        if(instruction.contains(";")){
            return instruction.substring(instruction.indexOf(";")+1);
            
        }
        return "null";
                 
    }

}

