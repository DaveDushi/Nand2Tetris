import java.io.BufferedWriter;
import java.io.IOException;

public class CodeWriter {

    private String fileName;
    private int num = 0;
    private BufferedWriter bw; 

    /**
     * Constructer gets ready to write to a file 
     * @param out
     * @param fileName
     */
    public CodeWriter(java.io.Writer out, String fileName) {
        this.bw = new BufferedWriter(out);
        this.fileName = fileName;
    }

     /**
     * writes to the output file the assembly code 
     * that implements the given arithmetic-logical command 
     * @throws IOException
     * */
    public void writeArithmetic(String command) throws IOException{
        String n = null; 

        switch (command) {
            case "add":
                writeLine("//ADD"); 
                writeLine("@SP"); //D=RAM[SP-1], SP--
                writeLine("AM=M-1");
                writeLine("D=M");
                writeLine("A=A-1"); //RAM[SP-1]=D+RAM[SP-1]
                writeLine("M=D+M");
                break;
            case "sub":
                writeLine("//SUB");
                writeLine("@SP"); //RAM[SP-1], SP--
                writeLine("AM=M-1");
                writeLine("D=M");
                writeLine("A=A-1"); //RAM[SP-1]=RAM[SP-1]-D
                writeLine("M=M-D");
                break;
            case "and":
                writeLine("//AND");
                writeLine("@SP"); //RAM[SP-1], SP--
                writeLine("AM=M-1");
                writeLine("D=M");
                writeLine("A=A-1"); //RAM[SP-1]=D&RAM[SP-1]
                writeLine("M=D&M");
                break;
            case "or":
                writeLine("//OR");
                writeLine("@SP"); //RAM[SP-1], SP--
                writeLine("AM=M-1");
                writeLine("D=M");
                writeLine("A=A-1");//RAM[SP-1]=D|RAM[SP-1]
                writeLine("M=D|M");
                break;
            case "neg":
                writeLine("//NEG");
                writeLine("@SP"); //SP--
                writeLine("A=M-1");
                writeLine("M=-M"); //RAM[SP]=-RAM[SP]
                break;
            case "not":
                writeLine("//NOT");
                writeLine("@SP"); //SP--
                writeLine("A=M-1");
                writeLine("M=!M"); //RAM[SP]=!RAM[SP]
                break;
            case "eq":
                n = increment(); //change n
                writeLine("//EQ");
                writeLine("@SP"); //SP--
                writeLine("AM=M-1");
                writeLine("D=M"); //D=RAM[SP]
                writeLine("A=A-1"); //SP--
                writeLine("D=M-D"); //D=RAM[SP]-D
                writeLine("@isEq" + n); //if D=0 JUMP to isEq'n'
                writeLine("D;JEQ");
                writeLine("@SP"); //else RAM[SP]=0
                writeLine("A=M-1");
                writeLine("M=0");
                writeLine("@notEq" + n);
                writeLine("0;JMP"); //JUMP to notEq'n' 
                writeLine("(isEq" + n + ")");
                writeLine("@SP"); //set RAM[SP]=-1
                writeLine("A=M-1");
                writeLine("M=-1");
                writeLine("(notEq" + n + ")");
                break;
            case "gt":
                n = increment();
                writeLine("//GT");
                writeLine("@SP"); //SP--
                writeLine("AM=M-1"); 
                writeLine("D=M"); //D=RAM[SP]
                writeLine("A=A-1"); //SP--
                writeLine("D=M-D"); //D=RAM[SP]-D
                writeLine("@isGt" + n); //if D>0 JUMP to isGt'n'
                writeLine("D;JGT");
                writeLine("@SP"); //else RAM[SP]=0
                writeLine("A=M-1");
                writeLine("M=0");
                writeLine("@notGt" + n);
                writeLine("0;JMP"); //JUMP to notGT'n'
                writeLine("(isGt" + n + ")");
                writeLine("@SP"); //set RAM[SP]=-1
                writeLine("A=M-1");
                writeLine("M=-1");
                writeLine("(notGt" + n + ")");
                break;
            case "lt":
                n = increment();
                writeLine("//LT");
                writeLine("@SP"); //SP--
                writeLine("AM=M-1");
                writeLine("D=M"); //D=RAM[SP]
                writeLine("A=A-1"); //SP--
                writeLine("D=M-D"); //D=RAM[SP]-D
                writeLine("@isLt" + n); //if D<0 JUMP to isLT'n'
                writeLine("D;JLT");
                writeLine("@SP"); //else RAM[SP]=0
                writeLine("A=M-1");
                writeLine("M=0");
                writeLine("@notLt" + n);
                writeLine("0;JMP"); //JUMP to notLt'n'
                writeLine("(isLt" + n + ")");
                writeLine("@SP"); //set RAM[SP]=-1
                writeLine("A=M-1");
                writeLine("M=-1");
                writeLine("(notLt" + n + ")");
                break;
            default:
                break;
        }


    }

    /**
     * Writes to the output file the assembly code 
     * that implememts the given push or pop command. 
     * @throws IOException
     * */
    public void writePushPop(Parser.Command command, String segment, int index) throws IOException{
        //set the segmant loacation
        String segName;

        switch (segment) {
            case "local":
                segName = "LCL";
                break;
            case "argument":
                segName = "ARG";
                break;
           case "this":
                segName = "THIS";
                break;
            case "that":
                segName = "THAT";
                break;
            default:
                segName = null;
                break;
        }

       if(command == Parser.Command.C_PUSH){
            switch (segment) {
                case "constant":
                    writeLine("//Push constant " + index);
                    writeLine("@"+index); //D=i
                    writeLine("D=A");
                    push();
                    break;
                case "local":
                case "argument":
                case "this":
                case "that":
                    writeLine("//Push " + segment + " " + index);
                    writeLine("@" + index); //D=i
                    writeLine("D=A");
                    writeLine("@" + segName); //D=seg+i
                    writeLine("D=D+M");
                    writeLine("A=D"); //D=RAM[addr]
                    writeLine("D=M");
                    push();
                    break;
                case "static":
                    writeLine("//Push static " + index);
                    writeLine("@" + fileName.replaceFirst("vm", Integer.toString(index))); //D=RAM[file.i]
                    writeLine("D=M");
                    push();
                    break;
                case "temp":
                    writeLine("//Push temp " + index);
                    writeLine("@R5"); //D=5
                    writeLine("D=A");
                    writeLine("@" + index); //D=RAM[5+i]
                    writeLine("A=D+A");
                    writeLine("D=M");
                    push();
                    break;
                case "pointer": 
                    writeLine("//Push pointer " + index);
                    if(index == 0){
                        writeLine("@THIS"); //D=RAM[THIS]
                        writeLine("D=M");
                        push();
                    }else{
                        writeLine("@THAT"); //D=RAM[THAT]
                        writeLine("D=M");
                        push();
                    }
                    break;
                default:
                    break;
            }
        }else if(command == Parser.Command.C_POP){
            switch (segment) {
                case "local":
                case "argument":
                case "this":
                case "that":
                    writeLine("//Pop " + segment + " " + index);
                    writeLine("@" + index); //D=i
                    writeLine("D=A");
                    writeLine("@" + segName); //D=seg+i
                    writeLine("D=D+M");
                    pop();
                    break;
                case "static":
                    writeLine("//Pop static " + index);
                    writeLine("@" + fileName.replaceFirst("vm", Integer.toString(index))); //D=RAM[file.i]
                    writeLine("D=A");
                    pop();
                    break;
                case "temp":
                    writeLine("//Pop temp " + index);
                    writeLine("@R5"); //D=5+i
                    writeLine("D=A");
                    writeLine("@" + index);
                    writeLine("D=D+A");
                    pop();
                    break;
                case "pointer": 
                    writeLine("//Pop pointer " + index);
                    if(index == 0){
                        writeLine("@THIS"); 
                        writeLine("D=A");
                        pop();;
                    }else{
                        writeLine("@THAT"); 
                        writeLine("D=A");
                        pop();;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Adds the generic POP assembly code to the file
     * @throws IOException
     */
    private void pop() throws IOException {
        writeLine("@R13"); //addr <- D
        writeLine("M=D");
        writeLine("@SP"); //SP--
        writeLine("AM=M-1");
        writeLine("D=M"); //D=RAM[SP]
        writeLine("@R13"); //RAM[addr]=D
        writeLine("A=M");
        writeLine("M=D");
    }

     /**
     * Adds the generic PUSH assembly code to the file
     * @throws IOException
     */
    private void push() throws IOException {
        writeLine("@SP"); //RAM[SP] = D
        writeLine("A=M");
        writeLine("M=D");
        writeLine("@SP"); //SP++
        writeLine("M=M+1");
    }

    /**
     * Closes the writer
     * @throws IOException
     */
    public void close() throws IOException {
        bw.close();
    }

    /**
     * Writes a line and adds a new line after it
     * @throws IOException
     */
    private void writeLine(String string) throws IOException {
        bw.write(string);
        bw.newLine();
        
    }

    /**
     * Increments num each time its called 
     * @return
     */
    private String increment() {
        return Integer.toString(this.num++);
    }
}
