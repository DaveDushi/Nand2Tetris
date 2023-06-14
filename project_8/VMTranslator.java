import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMTranslator {
    public static void main(String[] args) throws IOException {
        String projName = args[0];
        File vmFiles = new File(projName);
        File[] files;

        //create the file name for the output file
        String outputFile = outputFile(projName);  
        File asmFile = new File(outputFile);
        FileWriter fw = new FileWriter(asmFile, false); 
        CodeWriter writer = new CodeWriter(fw); //Construct a Writer to write to the output file 

        //if its a directory add the bootstrap code 
        if(vmFiles.isDirectory()){
            files = vmFiles.listFiles();
            writer.bootstrap();
        }else{
            files = new File[1];
            files[0] = vmFiles;
        }


        for(File file : files){
            if(file.getName().endsWith(".vm")){
                String fileName = file.toString();
                
                //set the file name for static calls 
                int x = fileName.lastIndexOf("/");
                if(x != -1){
                    fileName = fileName.substring(x+1, fileName.length());
                }
                writer.SetFileName(fileName);

                Parser parser = new Parser(file); //Construct a Parser to parse the file 
                
                String funcName = null;
                //go over all the lines and write to the output the respected lines 
                while (parser.hasMoreLines()) {
                    parser.advance();
    
                    if(parser.commandType() == Parser.Command.C_POP){
                        writer.writePushPop(Parser.Command.C_POP, parser.arg1(), parser.arg2());
    
                    }else if(parser.commandType() == Parser.Command.C_PUSH){
                        writer.writePushPop(Parser.Command.C_PUSH, parser.arg1(), parser.arg2());
    
                    }else if(parser.commandType() == Parser.Command.C_ARITHMETIC){
                        writer.writeArithmetic(parser.arg1());
                    }else if(parser.commandType() == Parser.Command.C_GOTO){
                        writer.writeGoTo(funcName+"$"+parser.arg1());
                    }else if(parser.commandType() == Parser.Command.C_LABEL){
                        writer.writeLabel(funcName+"$"+parser.arg1());
                    }else if(parser.commandType() == Parser.Command.C_IF){
                        writer.writeIf(funcName+"$"+parser.arg1());
                    }else if(parser.commandType() == Parser.Command.C_FUNCTION){
                        funcName = parser.arg1();
                        writer.writeFunction(funcName, parser.arg2());
    
                    }else if(parser.commandType() == Parser.Command.C_RETURN){
                        writer.writeReturn();
                        
                    }else if(parser.commandType() == Parser.Command.C_CALL){
                        writer.writeCall(parser.arg1(), parser.arg2());
                        
                    }   
                }
            }
        }
        writer.close();
    }

    /**
     * Creates a file name for the output 
     * @param fileName
     * @return
     */
    private static String outputFile(String fileName){
        int index = 0;
        if(fileName.lastIndexOf("/") != -1) index = fileName.lastIndexOf("/") + 1;
        String file = fileName.substring(index);

        StringBuilder output = new StringBuilder(fileName);
        if(output.indexOf(".vm") == -1){
            output.append("/" + file + ".asm");
        }else{
            output.replace(output.lastIndexOf("vm"),output.length(), "asm");
        }
        return output.toString();
        
    }
}
