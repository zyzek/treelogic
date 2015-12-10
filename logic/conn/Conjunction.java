package logic.conn;

import logic.*;

public class Conjunction extends BinaryConn
{
    public String getConn() { return " & "; }
    
    public Conjunction(WFF left, WFF right) {
        super(left, right);
    }
}

