package logic.conn;

import logic.*;

public abstract class UnaryConn extends WFF
{
    public final WFF sub;
    
    public UnaryConn(WFF sub) {
        this.sub = sub;
    }

    public abstract String getConn();

    public String toString() {
        return this.getConn() + this.sub.toString();
    }

    public boolean equals(WFF prop) {
        if (prop instanceof UnaryConn) {
            if (((UnaryConn)prop).getConn().equals(this.getConn())) {
                return this.sub.equals(((UnaryConn)prop).sub);
            }
        }
        return false;
    }

    public boolean contradicts(WFF prop) {
        if (prop instanceof Negation) {
            return ((Negation)prop).sub.equals(this);
        } else if (this instanceof Negation) {
            return this.sub.equals(prop);
        } else {
            return false;
        }
    }
}

