package logic.conn;

import logic.*;

public class Biconditional extends BinaryConn
{
    public String getConn() { return " <-> "; }
    
    public Biconditional(WFF left, WFF right) {
        super(left, right);
    }
}


