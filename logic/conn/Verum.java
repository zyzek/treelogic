package logic.conn;
import logic.*;

public class Verum extends Predicate
{
    public Verum()
    {
        super('T');
    }

    public boolean contradicts(WFF prop)
    {
        return (prop instanceof Falsum) || 
               (prop instanceof Negation && ((Negation)prop).sub instanceof Verum);
    }
}


