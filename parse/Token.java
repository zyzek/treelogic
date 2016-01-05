package parse;

import parse.TokenType;

public class Token
{
    public TokenType type;
    public char sym;

    public Token(char sym) {
        this.type = TokenType.SYM;
        this.sym = sym;
    }

    public Token(TokenType type) {
        this.type = type;
        this.sym = '#';
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || this.getClass() != obj.getClass())
        {
            return false;
        }

        Token that = (Token)obj;
        if (this.type == TokenType.SYM && that.type == TokenType.SYM)
        {
            return this.sym == that.sym;
        }
        else
        {
            return this.type == that.type;
        }
    }

    @Override
    public int hashCode()
    {
        if (this.type == TokenType.SYM)
        {
            return Character.hashCode(this.sym);
        }
        else
        {
            return this.type.hashCode();
        }
    }

    public String toString()
    {
        if (this.type == TokenType.SYM) {
            return Character.toString(this.sym);
        }

        return this.type.toString();
    }
}
