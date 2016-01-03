package logic.conn;

import logic.*;
import logic.conn.*;

public class BinaryConn extends WFF
{
    public final WFF left, right;
    public final ConnType conn;
    
    public BinaryConn(WFF left, WFF right, ConnType conn)
    {
        this.left = left;
        this.right = right;
        this.conn = conn;
    }

    public ConnType getConn()
    {
        return this.conn;
    }
    
    public boolean isBasic()
    {
        return false;
    }

    public String toString()
    {
        return "(" + this.left.toString() + this.getConn() + this.right.toString() + ")";
    }

    public boolean equals(WFF prop)
    {
        if (prop instanceof BinaryConn)
        {
            if (((BinaryConn)prop).getConn().equals(this.getConn()))
            {
                return this.left.equals(((BinaryConn)prop).left) && 
                       this.right.equals(((BinaryConn)prop).right);
            }
        }

        return false;
    }

    public boolean contradicts(WFF prop)
    {
        return (prop instanceof Negation) && (((Negation)prop).sub.equals(this));
    }
}


