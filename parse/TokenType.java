package parse;

// These are the possible tokens; SYM represents any character not covered by the other token types.
public enum TokenType {
    CONJ, DISJ, COND, BICOND, NEG, UNIV, EXIST, SYM, LPAREN, RPAREN, COMMA
}
