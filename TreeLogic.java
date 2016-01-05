import logic.*;
import logic.conn.*;
import parse.*;

import java.util.*;
import java.io.*;
import javax.swing.*;

public class TreeLogic
{
    public static void main(String[] args)
    {
        LinkedList<Token> sequence = new LinkedList<Token>();

        sequence.addLast(new Token(TokenType.NEG));
        sequence.addLast(new Token(TokenType.UNIV));
        sequence.addLast(new Token('x'));
        sequence.addLast(new Token('z'));
        sequence.addLast(new Token(TokenType.EXIST));
        sequence.addLast(new Token('y'));
        sequence.addLast(new Token(TokenType.LPAREN));
        sequence.addLast(new Token('F'));
        sequence.addLast(new Token(TokenType.BICOND));
        sequence.addLast(new Token(TokenType.LPAREN));
        sequence.addLast(new Token(TokenType.LPAREN));
        sequence.addLast(new Token('R'));
        sequence.addLast(new Token(TokenType.DISJ));
        sequence.addLast(new Token(TokenType.LPAREN));
        sequence.addLast(new Token('P'));
        sequence.addLast(new Token('x'));
        sequence.addLast(new Token(TokenType.CONJ));
        sequence.addLast(new Token('Q'));
        sequence.addLast(new Token('a'));
        sequence.addLast(new Token('y'));
        sequence.addLast(new Token('z'));
        sequence.addLast(new Token(TokenType.RPAREN));
        sequence.addLast(new Token(TokenType.RPAREN));
        sequence.addLast(new Token(TokenType.COND));
        sequence.addLast(new Token('T'));
        sequence.addLast(new Token(TokenType.RPAREN));
        sequence.addLast(new Token(TokenType.RPAREN));
        

        String line;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

            if ((line = reader.readLine()) != null)
            {
                line = line.trim();
            }
            else
            {
                line = "A \\and B";
            }
        }
        catch (IOException ex)
        {
            System.out.println("Unable to open input file.");
            line = "A \\and B";
        }


        System.out.println(line);
        sequence = Lexer.tokenise(line);
        System.out.println(sequence);
        
        WFF wff = Parser.parse(sequence);
        final String wffString;

        if (wff != null)
        {
            wffString = wff.toString();
        }
        else
        {
            wffString = "Failed parse. First unconsumed token: " + sequence.peekFirst().toString();
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showGUI(wffString);
            }
        });

    }

    private static void showGUI(String text)
    {
        // Create a window.
        JFrame frame = new JFrame("Logical Tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Make a label.
        JLabel label = new JLabel(text);
        frame.getContentPane().add(label);

        // Display the window.
        frame.pack();
        frame.setVisible(true);

    }
}
