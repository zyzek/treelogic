package logic.conn;

import logic.*;

public class Predicate extends WFF
{
    private char sym;
    private int arity;
    private Term[] args;
    
    public Predicate (char sym, int arity, Term[] args) {
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

/*    public String toString() {
        if (this.arity == 0) { return Character.toString(this.sym); }

        StringBuilder sb = new StringBuilder();

        sb.append(this.sym);
        sb.append('(');
        
        for (int i = 0; i < this.args.length; ++i) {
            sb.append(this.args[i]);
            if (i < this.args.length - 1) {
                sb.append(", ");
            }
        }
        
        sb.append(')');
        
        return sb.toString();
    }*/

    public String toString() {
        if (this.arity == 0) return Character.toString(this.sym);

        StringBuilder sb = new StringBuilder();
        sb.append(this.sym);

        for (Term t : this.args) {
            sb.append(t);
        }

        return sb.toString();

    }
}

