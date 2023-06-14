import java.util.HashMap;

public class SymbolTable extends HashMap<String, Integer> {
    private static final long serialVersionUID = 1L; // (because we're extending a serializable class)
   // HashMap<String, Integer> table;

/**
 * Constructer
 * puts the default keys:values into the table
 */
    public SymbolTable(){
        put("SCREEN", 16384);
        put("KBD", 24576);
        put("SP", 0);
        put("LCL", 1);
        put("ARG", 2);
        put("THIS", 3);
        put("THAT", 4);

        for (int i = 0; i < 16; i++) {
            put("R" + i, i);
        }



    }

/**
 * Adds the pair (symbol, address) to the table 
 * @param symbol
 * @param address
 */    
    public void addEntry(String symbol, int address){
    put(symbol, address);
    }

/**
 * Does the symbol table contain the given symbol
 * @param symbol
 * @return
 */
    public boolean contains(String symbol){
        return containsKey(symbol);
    }
/**
 * Returns the address associated with the symbol
 * @param symbol
 * @return
 */
    public int getAddress(String symbol){
        return get(symbol);
    }
}
