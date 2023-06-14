public class Code {

    public Code() {
    }
    
    /**
     * Returns the binary representation of the parsed dest field (string)
     * @param des
     * @return
     */
    public String dest(String des){
      StringBuilder b = new StringBuilder();
        if (des != null && des.contains("A")){
            b.append("1");
        }
        else{
            b.append("0");

        }
        if (des != null && des.contains("D")){
            b.append("1");
        }
        else{
            b.append("0");

        }
         if (des != null && des.contains("M")){
            b.append("1");
        }
        else{
            b.append("0");
        }
        return b.toString();

    }
    /**
     * Returns the binary representation of the parsed comp field (string)
     * @param com
     * @return
     */
    public String comp(String com){
        String bitrep = null;
        switch (com) {
            case "0":
                bitrep = "0101010";
                break;
            case "1":
                bitrep = "0111111";
                break;
            case "-1":
                bitrep = "0111010"; 
                break;
            case "D":
                bitrep = "0001100";
                break;
            case "A":
                bitrep = "0110000";
                break;
            case "M":
                bitrep = "1110000";
                break;
            case "!D":
                bitrep = "0001100";
                break;
            case "!A":
                bitrep = "0110000";
                break;
            case "!M":
                bitrep = "1110001";
                break;
            case "-D":
                bitrep = "0001111";
                break;
            case "-A":
                bitrep = "0110011";
                break;
            case "-M":
                bitrep = "1110011";
                break;
            case "D+1":
                bitrep = "0011111";
                break;
            case "A+1":
                bitrep = "0110111";
                break;
            case "M+1":
                bitrep = "1110111";
                break;
            case "D-1":
                bitrep = "0001110";
                break;
            case "A-1":
                bitrep = "0110010";
                break;
            case "M-1":
                bitrep = "1110010";
                break;
            case "D+A":
                bitrep = "0000010";
                break;
            case "D+M":
                bitrep = "1000010";
                break;
            case "D-A":
                bitrep = "0010011";
                break;
            case "D-M":
                bitrep = "1010011";
                break;
            case "A-D":
                bitrep = "0000111";
                break;
            case "M-D":
                bitrep = "1000111";
                break;
            case "D&M":
                bitrep = "1000000";
                break;
            case "D&A":
                bitrep = "0000000";
                break;
            case "D|A":
                bitrep = "0010101";
                break;
            case "D|M":
                bitrep = "1010101";
                break;
        }
        return bitrep;
        
    }
   /**
    * Returns the binary representation of the parsed jump field (string)
    * @param jum
    * @return
    */ 
    
   public String jump(String jum) {
    String jumpBit;
        switch (jum) {
            case "JGT":
                jumpBit = "001";            
                break;
            case "JEQ":
                jumpBit = "010";
                break;
            case "JGE":
                jumpBit = "011";
                break;
            case "JLT":
                jumpBit = "100";
                break;
            case "JNE":
                jumpBit = "101";
                break;
            case "JLE":
                jumpBit = "110";
                break;
            case "JMP":
                jumpBit = "111";
                break;
            default:
                jumpBit = "000";
                break;
        }
    return jumpBit;
    }   
}

