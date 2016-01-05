package parse;

// These are the possible tokens; SYM represents any character not covered by the other token types.
public enum TokenType {
    CONJ
    {
        public String toString()
        {
            return "∧";
        }
        
    },
    
    DISJ
    {
        public String toString()
        {
            return "∨";
        }
        
    },

    COND
    {
        public String toString()
        {
            return "→";
        }
        
    },
    
    BICOND
    {
        public String toString()
        {
            return "↔";
        }
        
    },
    
    NEG
    {
        public String toString()
        {
            return "¬";
        }
        
    },
    
    UNIV
    {
        public String toString()
        {
            return "∀";
        }
        
    },
    
    EXIST
    {
        public String toString()
        {
            return "∃";
        }
        
    },
    
    LPAREN
    {
        public String toString()
        {
            return "(";
        }
        
    },
    
    RPAREN
    {
        public String toString()
        {
            return ")";
        }
        
    },
    
    COMMA
    {
        public String toString()
        {
            return ",";
        }
        
    },
    
    SYM
    {
        public String toString()
        {
            return "SYM";
        }
        
    }
}
