package parse;

import java.util.*;
import java.io.*;

public class Lexer
{
    static final String atomFile = "parse/atoms.txt";

    public static LinkedList<Token> tokenise(String inputString) {
        LinkedList<Token> sequence = new LinkedList<Token>();
        String str = inputString.replaceAll("\\s+", "");
        
        if (str.length() == 0)
        {
            return sequence;
        }
        
        LexDAGNode DAG = buildLexDAG();
        LexDAGNode currNode = DAG;
        
        char c;
        int index = 0;
        
        // Represents the number of examined-but-unconsumed symbols.
        // Consider the first (index - examined) symbols to have been consumed.
        int examined = 0;
        
        // If we reach a leaf, consume examined symbols and return to root.
        // Check if the next symbol points to a child from the current node in the tree.
        //   - if not, rewind to the last unconsumed symbol and consume it, returning to root.
        //   - otherwise, follow the link and continue, incrementing examined.
        // Do this until we're out of symbols
        while (true)
        {
            if (currNode.isLeaf)
            {
                sequence.addLast(new Token(currNode.token));
                currNode = DAG;
                examined = 0;
            }

            if (index >= str.length())
            {
                if (examined == 0)
                {
                    break;
                }
                else
                {
                    index -= examined - 1;
                    examined = 0;
                    c = str.charAt(index - 1);
                    currNode = DAG;
                    sequence.addLast(new Token(c));
                }
            }
            else
            {
                c = str.charAt(index);
                ++index;
                ++examined;

                if (currNode.charmap.get(c) == null) 
                {
                    index -= examined - 1;
                    c = str.charAt(index - 1);
                    examined = 0;
                    currNode = DAG;
                    sequence.addLast(new Token(c));
                }
                else
                {
                   currNode = currNode.charmap.get(c);
                }
            }
        }

        return sequence;
    }

    
    // Build a Trie (but distinct paths can terminate in the same leaf) whose leaves are Tokens.
    // The path from the root to a leaf indicates a string corresponding to that token.
    // Note that this means that no string in the atoms file can be a prefix of any other,
    // so that all parses are guranateed to be unique.
    public static LexDAGNode buildLexDAG()
    {
        LexDAGNode root = new LexDAGNode();
        String line;
        String[] parts;
        LexDAGNode currTokenNode;
        
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(atomFile));

            while ((line = reader.readLine()) != null)
            {
                parts = line.trim().split("\\s+");

                if (parts.length <= 1) {
                    continue;
                }
                
                try
                {
                    currTokenNode = new LexDAGNode(TokenType.valueOf(parts[0]));
                }
                catch (Exception ex)
                {
                    System.out.println("Invalid token in atoms file.");
                    return null;
                }

                for (int i = 1; i < parts.length; ++i)
                {
                    addStringToDAG(root, parts[i], currTokenNode);
                }
            }

            return root;
        }
        catch (IOException ex)
        {
            System.out.println("Unable to open atom file.");
            return null;
        }
    }
    
    // Given a string, root node, and leaf node, construct the corresponding path.
    // If the path already exists, overwrite it.
    public static void addStringToDAG(LexDAGNode root, String str, LexDAGNode token)
    {
        int index = 0;
        char c;

        while (index < str.length())
        {
            c = str.charAt(index);;

            if (index == str.length() - 1)
            {
                root.charmap.put(c, token);
            }
            else
            {
                if (root.charmap.containsKey(c))
                {
                    root = root.charmap.get(c);
                }
                else
                {
                    LexDAGNode node = new LexDAGNode();
                    root.charmap.put(c, node);
                    root = node;
                }
            }

            ++index;
        }
    }
}
