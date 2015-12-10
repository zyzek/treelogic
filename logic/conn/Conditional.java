package logic.conn;

import logic.*;

public class Conditional extends BinaryConn
{
    public String getConn() { return " -> "; }
    
    public Conditional(WFF left, WFF right) {
        super(left, right);
    }
}
