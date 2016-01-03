package parse;

import parse.TokenType;

public class Token {

    public TokenType token;
    public char sym;

    public Token(TokenType token, char sym) {
        this.token = token;
        this.sym = sym;
    }
}
