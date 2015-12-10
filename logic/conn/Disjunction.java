package logic.conn;

import logic.*;

public class Disjunction extends BinaryConn
{
    public String getConn() { return " | "; }
    
    public Disjunction(WFF left, WFF right) {
        super(left, right);
    }
}

