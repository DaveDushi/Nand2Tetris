public class Symbol {
    String type;
    SymbolTable.Kind kind;
    int index;
    
 public Symbol(String strType, SymbolTable.Kind strKind, int nIndex) {
        type = strType;
        kind = strKind;
        index = nIndex;
        
        }
    public String getType() {
        return type;
    }
    public SymbolTable.Kind getKind() {
        return kind;
    }
   
    
  public int getIndex() {
      return index;
  }
}

