package parse;

import parse.TokenType;

public class Token
{
    public TokenType token;
    public char sym;

    public Token(char sym) {
        this.token = TokenType.SYM;
        this.sym = sym;
    }

    public Token(TokenType token) {
        this.token = token;
        this.sym = '#';
    }

    public String toString()
    {
        if (this.token == TokenType.SYM) {
            return Character.toString(this.sym);
        }

        return this.token.toString();
    }
}
