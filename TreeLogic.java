import logic.*;
import logic.conn.*;
import parse.*;

import java.util.LinkedList;
import javax.swing.*;

public class TreeLogic
{
    public static void main(String[] args)
    {
        LinkedList<Token> sequence = new LinkedList<Token>();

        sequence.addLast(new Token(TokenType.NEG, '~'));
        sequence.addLast(new Token(TokenType.UNIV, 'A'));
        sequence.addLast(new Token(TokenType.SYM, 'x'));
        sequence.addLast(new Token(TokenType.SYM, 'z'));
        sequence.addLast(new Token(TokenType.EXIST, 'E'));
        sequence.addLast(new Token(TokenType.SYM, 'y'));
        sequence.addLast(new Token(TokenType.LPAREN, '('));
        sequence.addLast(new Token(TokenType.SYM, 'F'));
        sequence.addLast(new Token(TokenType.BICOND, '%'));
        sequence.addLast(new Token(TokenType.LPAREN, '('));
        sequence.addLast(new Token(TokenType.LPAREN, '('));
        sequence.addLast(new Token(TokenType.SYM, 'R'));
        sequence.addLast(new Token(TokenType.DISJ, 'v'));
        sequence.addLast(new Token(TokenType.LPAREN, '('));
        sequence.addLast(new Token(TokenType.SYM, 'P'));
        sequence.addLast(new Token(TokenType.SYM, 'x'));
        sequence.addLast(new Token(TokenType.CONJ, '^'));
        sequence.addLast(new Token(TokenType.SYM, 'Q'));
        sequence.addLast(new Token(TokenType.SYM, 'a'));
        sequence.addLast(new Token(TokenType.SYM, 'y'));
        sequence.addLast(new Token(TokenType.SYM, 'z'));
        sequence.addLast(new Token(TokenType.RPAREN, ')'));
        sequence.addLast(new Token(TokenType.RPAREN, ')'));
        sequence.addLast(new Token(TokenType.COND, '>'));
        sequence.addLast(new Token(TokenType.SYM, 'T'));
        sequence.addLast(new Token(TokenType.RPAREN, ')'));
        sequence.addLast(new Token(TokenType.RPAREN, ')'));
        
        WFF wff = Parser.parse(sequence);

        final String wffString = wff.toString();

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
