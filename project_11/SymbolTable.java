import java.util.Hashtable;

public class SymbolTable {
    public enum Kind { STATIC, FIELD, ARG, VAR, NONE; }

    private Hashtable<String,Symbol> classTable;
    private Hashtable<String,Symbol> subroutineTable;
    private Hashtable<Kind, Integer> indexTable;

    /**creates new symbol table */
    public SymbolTable () {
        classTable = new Hashtable<>();
        subroutineTable = new Hashtable<>();
        indexTable = new Hashtable<>();
        indexTable.put(Kind.ARG, 0);
        indexTable.put(Kind.FIELD, 0);
        indexTable.put(Kind.STATIC, 0);
        indexTable.put(Kind.VAR, 0);
    }

    /**resets the symbol table, and resets the four
     * indexes to 0. Should be called when starting 
     * to compile a subroutine declaration
      */
    public void reset() {
        subroutineTable.clear();
        indexTable.put(Kind.ARG, 0);
        //indexTable.put(Kind.FIELD, 0);
        //indexTable.put(Kind.STATIC, 0);
        indexTable.put(Kind.VAR, 0);
    }

    /**
     * Defines (adds to the table) a new variable of
     * the given name, type, and kind. Assigns to it
     * the index value of that kind, and adds 1 to the index.
     * @param name
     * @param type
     * @param kind
     */
    public void define(String name, String type, Kind kind) {
        int index = indexTable.get(kind);
        Symbol symbol = new Symbol(type, kind, index);
        //increment the index and update the table
        index++;
        indexTable.put(kind, index);
        //put the symbol in the right table 
        if(kind == Kind.ARG || kind == Kind.VAR){
            subroutineTable.put(name, symbol);
        }else if(kind == Kind.FIELD || kind == Kind.STATIC){
            classTable.put(name, symbol);
        }
    }

    /**
     * Returns the number of variables of the given 
     * kind already defied in the table.
     * @param kind
     * @return
     */
    public int varCount(Kind kind) {
        return indexTable.get(kind);
    }

    /**
     * Returns the kind of the named identifier.
     * If the identifier is not found, returns NONE.
     * @param name
     * @return
     */
    public Kind kindOf(String name) {
        //check if in classTable or subroutineTable
        if(classTable.containsKey(name)){
            return classTable.get(name).getKind();
        }else if(subroutineTable.containsKey(name)){
            return  subroutineTable.get(name).getKind();
        }
        return Kind.NONE;
    }

    /**
     * Returns the type of the named variable.
     * @param name
     * @return
     */
    public String typeOf(String name) {
        //check if in classTable or subroutineTable
        if(classTable.containsKey(name)){
            return classTable.get(name).getType();
        }else if(subroutineTable.containsKey(name)){
            return  subroutineTable.get(name).getType();
        }
        return null;
    }

    /**
     * Returns the index of the named variable.
     * @param name
     * @return
     */
    public int indexOf(String name) {
        //check if in classTable or subroutineTable
        if(classTable.containsKey(name)){
            return classTable.get(name).getIndex();
        }else if(subroutineTable.containsKey(name)){
            return  subroutineTable.get(name).getIndex();
        }

        return -1;
    }
}