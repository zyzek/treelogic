package logic.conn;
import logic.*;

public class Falsum extends Predicate
{
    public Falsum() {
        super('F');
    }

    public boolean contradicts(WFF prop) {
        return (prop instanceof Verum) || 
               (prop instanceof Negation && ((Negation)prop).sub instanceof Falsum);
    }
}

