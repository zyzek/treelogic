package logic;

public class Term 
{
    private char sym;

    public Term(char sym) {
        this.sym = sym;
    }

    public char getSym() {
        return this.sym;
    }

    public String toString() {
        return Character.toString(sym);
    }
}


