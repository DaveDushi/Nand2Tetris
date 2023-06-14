import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class HackAssembler {
    private static int newFile = 0;
    public static void main(String[] args) throws IOException {
        String fileName = args[0];
        File asmFile = new File(fileName);
        Code code = new Code();
        SymbolTable table = new SymbolTable();
        
        //read the file for the first time
        Parser1 firstPass = new Parser1(asmFile);
        int numLines = 0;
        
        while(firstPass.hasMoreLines()){
            firstPass.advance();

            if(firstPass.instructionType() == Parser1.Instruction.L_INSTRUCTION){
                table.addEntry(firstPass.symbol(), numLines); //add the new keys to the table 
            }else{
                numLines++; //count the lines of A_INSTRUCTIONs and C_INSTRUCTIONs
            }
        }
        
        //read the file for the second time 
        Parser1 secondPass = new Parser1(asmFile);
        int i = 16; //access registers after R15 
        int address;
        String dest, comp, jump;
        String binary;
        File output = new File(args[0].replaceFirst("asm", "hack")); //output to a file called prog.hack
        
        while(secondPass.hasMoreLines()){
            secondPass.advance();
            
            //if A_INSTRUCTION 
            if(secondPass.instructionType() == Parser1.Instruction.A_INSTRUCTION){
                //if the symbol is not a number and is not in the table add it 
                if(!table.containsKey(secondPass.symbol()) && !isNum(secondPass.symbol())){
                    table.addEntry(secondPass.symbol(), i);
                    i++;
                }
                // if the symbol is a number convert it to binary else get the address from the table and convert it to binary 
                if(isNum(secondPass.symbol())){
                    binary = String.format("%16s", Integer.toBinaryString(Integer.parseInt(secondPass.symbol()))).replace(" ", "0");
                }else{
                    address = table.getAddress(secondPass.symbol());
                    binary = String.format("%16s", Integer.toBinaryString(address)).replace(" ", "0");
                }

                //add the INSTRUCTION to the output file 
                writeLine(output, binary);
            
            //if C_INSTRUCTION
            }else if(secondPass.instructionType() == Parser1.Instruction.C_INSTRUCTION){
                //put together the 3 parts and write it to output file
                dest = code.dest(secondPass.dest());
                comp = code.comp(secondPass.comp().trim());
                jump = code.jump(secondPass.jump().trim());
                binary = "111" + comp + dest + jump;
                writeLine(output, binary);
            }
        }
    }

    /**
     * writes a line to a file 
     * @param file
     * @param line
     */
    public static void writeLine(File file, String line) {
        FileWriter fw;

        try {
            if(newFile == 0){
                fw = new FileWriter(file, false);
                newFile = 1;
            }else{
                fw = new FileWriter(file, true);
            }
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(line);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * checks if a String is an Integer 
     * @param s
     * @return
     */
    public static boolean isNum(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}

