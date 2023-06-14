import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {
    
    public enum Segmant { 
        CONSTANT, ARGUMENT, LOCAL, STATIC, THIS, THAT, POINTER, TEMP; 
        @Override 
        public String toString() {
            return name().toLowerCase();
        }
    }
    public enum Command { 
        ADD, SUB, NEG, EQ, GT, LT, AND, OR, NOT; 
        @Override 
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    private FileWriter fw;
    private BufferedWriter bw;

    /**
     * Creates a new output.vm file
     * and prepares it for writing
     * @param outputFile
     * @throws IOException
     */
    public VMWriter(File outputFile) throws IOException {
        fw = new FileWriter(outputFile);
        bw = new BufferedWriter(fw);
        
    }

    /**
     * Writes a VM push command.
     * @param segmant
     * @param index
     * @throws IOException
     */
    public void writePush(Segmant segmant, int index) throws IOException {
        writeLine("push " + segmant + " " + index);
    }

    /**
     * Writes a VM pop command.
     * @param segmant
     * @param index
     * @throws IOException
     */
    public void writePop(Segmant segmant, int index) throws IOException {
        writeLine("pop " + segmant + " " + index);
    }

    /**
     * Writes a VM arithmetic-logical command.
     * @param command
     * @throws IOException
     */
    public void writeArithmetic(Command command) throws IOException{
        writeLine(command.toString());
    }

    /**
     * Writes a VM label command.
     * @param label
     * @throws IOException
     */
    public void writeLabel(String label) throws IOException{
        writeLine("label " + label);
    }

    /**
     * Writes a VM goto command.
     * @param label
     * @throws IOException
     */
    public void writeGoTo(String label) throws IOException{
        writeLine("goto " + label);
    }

    /**
     * Writes a VM if-goto command.
     * @param label
     * @throws IOException
     */
    public void writeIf(String label) throws IOException{
        writeLine("if-goto " + label);
    }

    /**
     * Writes a VM call command.
     * @param name
     * @param nArgs
     * @throws IOException
     */
    public void writeCall(String name, int nArgs) throws IOException{
        writeLine("call " + name + " " + nArgs);
    }

    /**
     * Writes a VM function command.
     * @param name
     * @param nVars
     * @throws IOException
     */
    public void writeFunction(String name, int nVars) throws IOException{
        writeLine("function " + name + " " + nVars);
    }

    /**
     * Writes a VM return command.
     * @throws IOException
     */
    public void writeReturn() throws IOException{
        writeLine("return");
    }

    /**
     * Closes the output file
     * @throws IOException
     */
    public void close() throws IOException{
        bw.close();
        fw.close();
    }

    private void writeLine(String string) throws IOException {
        bw.write(string);
        bw.newLine();
    }
}
