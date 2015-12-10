package logic.conn;

import logic.*;

import java.util.ArrayList;

public class Predicate extends WFF
{
    private char sym;
    private int arity;
    private ArrayList<Term> args;
    
    public Predicate (char sym, int arity, ArrayList<Term> args) {
        this.sym = sym;
        this.arity = Math.max(0, arity);
        this.args = args;
    }

    public Predicate (char sym) {
        this(sym, 0, null);
    }

    public boolean equals(WFF prop) {
        if (prop instanceof Predicate) {
            return this.sym == ((Predicate)prop).sym && 
                   this.args.equals(((Predicate)prop).args);
        } else {
            return false;
        }
    }

    public boolean contradicts(WFF prop) {
        if (prop instanceof Negation) {
            return ((Negation)prop).sub.equals(this);
        } else {
            return false;
        }
    }
    
    public boolean isBasic() { return true; }

    public String toString() {
        if (this.arity == 0) { return Character.toString(this.sym); }

        StringBuilder sb = new StringBuilder();

        sb.append(this.sym);
        sb.append('(');
        
        for (int i = 0; i < this.args.size(); ++i) {
            sb.append(this.args.get(i));
            if (i < this.args.size() - 1) {
                sb.append(", ");
            }
        }
        
        sb.append(')');
        
        return sb.toString();
    }
}

