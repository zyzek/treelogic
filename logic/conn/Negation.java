package logic.conn;

import logic.*;

public class Negation extends UnaryConn
{
    public String getConn() { return "¬"; }

    public Negation(WFF sub) {
        super(sub);
    }

    public boolean isBasic() {
        return this.sub instanceof Predicate;
    }

    public boolean contradicts(WFF prop) {
        if (prop instanceof Predicate) {
            return prop.equals(this.sub);
        } else if (prop instanceof Negation) {
            return ((Negation)prop).sub.equals(this);
        } else {
            return false;
        }
    }
}

