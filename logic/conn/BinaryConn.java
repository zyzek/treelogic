package logic.conn;

import logic.*;

public abstract class BinaryConn extends WFF
{
    public final WFF left, right;
    
    public BinaryConn(WFF left, WFF right) {
        this.left = left;
        this.right = right;
    }

    public abstract String getConn();
    
    public boolean isBasic() { return false; }

    public String toString() {
        return "(" + this.left.toString() + this.getConn() + this.right.toString() + ")";
    }

    public boolean equals(WFF prop) {
        if (prop instanceof BinaryConn) {
            if (((BinaryConn)prop).getConn().equals(this.getConn())) {
                return this.left.equals(((BinaryConn)prop).left) && 
                       this.right.equals(((BinaryConn)prop).right);
            }
        }

        return false;
    }

    public boolean contradicts(WFF prop) {
        return (prop instanceof Negation) && (((Negation)prop).sub.equals(this));
    }
}


