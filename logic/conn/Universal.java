package logic.conn;

import logic.*;

// Quantifiers are treated here as unary connectives;
public class Universal extends UnaryConn 
{
    
    private char var;
    
    public String getConn() { return "∀" + Character.toString(var); }

    public Universal(WFF sub, char var) {
        super(sub);
        this.var = var;
    }

    public boolean isBasic() {
        return false;
    }
}

