/* Abstract WFF classes:
 *
 * A well-formed formula is as follows:
 *  A basic proposition, such as p, q, etc., is a WFF.
 *  A pair of WFFs with a two-place connective between them, parens around the pair, is a WFF.
 *  A WFF preceded by a one-place connective is a WFF.
 *  Nothing else is a WFF.
 *
 *  We will also allow True and False values as special cases of basic propositions whose
 *  value is predetermined.
*/

public abstract class WFF
{
    public abstract String toString();
}

abstract class BinaryConn extends WFF
{
    public final WFF left, right;
    
    public BinaryConn(WFF left, WFF right) {
        this.left = left;
        this.right = right;
    }

    public abstract String getConn();

    public String toString() {
        return "(" + this.left.toString() + this.getConn() + this.right.toString() + ")";
    }
}

abstract class UnaryConn extends WFF
{
    public final WFF sub;
    
    public UnaryConn(WFF sub) {
        this.sub = sub;
    }

    public abstract String getConn();

    public String toString() {
        return this.getConn() + this.sub.toString();
    }
}



// Basic Proposition

class BasicProp extends WFF
{
    private char sym;
    
    public BasicProp (char sym) {
        if ((sym >= 'a' && sym <= 'z') || sym == 'T' || sym == 'F') {
            this.sym = sym;
        } else {
            this.sym = '!';
        }
    }

    public boolean equals(BasicProp prop) {
        return this.sym == prop.sym;
    }

    public String toString() {
        return Character.toString(this.sym);
    }
}

class Verum extends BasicProp
{
    public Verum() {
        super('T');
    }
}

class Falsum extends BasicProp
{
    public Falsum() {
        super('F');
    }
}



// The sole unary connective is negation.

class Negation extends UnaryConn
{
    public String getConn() { return "~"; }

    public Negation(WFF sub) {
        super(sub);
    }
}



// Binary connectives.

class Conjunction extends BinaryConn
{
    public String getConn() { return " & "; }
    
    public Conjunction(WFF left, WFF right) {
        super(left, right);
    }
}

class Disjunction extends BinaryConn
{
    public String getConn() { return " | "; }
    
    public Disjunction(WFF left, WFF right) {
        super(left, right);
    }
}

class Conditional extends BinaryConn
{
    public String getConn() { return " -> "; }
    
    public Conditional(WFF left, WFF right) {
        super(left, right);
    }
}

class Biconditional extends BinaryConn
{
    public String getConn() { return " <-> "; }
    
    public Biconditional(WFF left, WFF right) {
        super(left, right);
    }
}


