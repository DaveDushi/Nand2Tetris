import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMTranslator {
    public static void main(String[] args) throws IOException {
        String fileName = args[0];

        File vmFile = new File(fileName);
        Parser parser = new Parser(vmFile); //Construct a Parser to parse the file 
        
        //create the file name for the output file
        int index = 0;
        if(fileName.lastIndexOf("/") != -1) index = fileName.lastIndexOf("/") + 1;
        File asmFile = new File(fileName.replaceFirst("vm", "asm"));

        FileWriter fw = new FileWriter(asmFile, false); 
        CodeWriter writer = new CodeWriter(fw, fileName.substring(index)); //Construct a Writer to write to the output file 


        //go over all the lines and write to the output the respected lines 
        while (parser.hasMoreLines()) {
            parser.advance();

            if(parser.commandType() == Parser.Command.C_POP){
                writer.writePushPop(Parser.Command.C_POP, parser.arg1(), parser.arg2());

            }else if(parser.commandType() == Parser.Command.C_PUSH){
                writer.writePushPop(Parser.Command.C_PUSH, parser.arg1(), parser.arg2());

            }else if(parser.commandType() == Parser.Command.C_ARITHMETIC){
                writer.writeArithmetic(parser.arg1());
            }
            
        }
        writer.close();
    }
}
