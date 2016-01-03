package logic.conn;

public enum ConnType
{
    BICOND
    {
        public String toString()
        {
            return " <-> ";
        }
        
    }, 
    
    COND
    {
        public String toString()
        {
            return " -> ";
        }
        
    }, 
    
    DISJ
    {
        public String toString()
        {
            return " | ";
        }
        
    },
    
    CONJ
    {
        public String toString()
        {
            return " & ";
        }
        
    }
}
