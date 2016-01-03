package logic.conn;

import logic.*;

// Quantifiers are treated as unary connectives.
public class Existential extends UnaryConn
{
    private char var;

    public String getConn()
    {
        return "∃" + Character.toString(var);
    }

    public Existential(WFF sub, char var)
    {
        super(sub);
        this.var = var;
    }

    public boolean isBasic()
    {
        return false;
    }
}
