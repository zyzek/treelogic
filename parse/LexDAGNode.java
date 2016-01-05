package parse;

import java.util.HashMap;

public class LexDAGNode
{
    public HashMap<Character, LexDAGNode> charmap;
    public boolean isLeaf;
    public TokenType token;

    public LexDAGNode(TokenType token)
    {
        this.token = token;
        this.isLeaf = true;
    }

    public LexDAGNode()
    {
        this.isLeaf = false;
        this.charmap = new HashMap<Character, LexDAGNode>();
    }
}
