import logic.*;
import logic.conn.*;
import parse.*;

//import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.*;


import parse.TokenType;

public class TreeLogic {
    public static void main(String[] args) {
        /*Term x = new Term('x');
        Term y = new Term('y');
        Term a = new Term('a');

        ArrayList<Term> PArgs = new ArrayList<Term>();
        PArgs.add(x);

        ArrayList<Term> QArgs = new ArrayList<Term>();
        QArgs.add(a);
        QArgs.add(y);

        WFF P = new Predicate('P', 1, PArgs.toArray(new Term[PArgs.size()]));
        WFF Q = new Predicate('Q', 2, QArgs.toArray(new Term[QArgs.size()]));
        WFF R = new Predicate('R');
        WFF T = new Verum();
        WFF F = new Falsum();

        WFF conj = new BinaryConn(P, Q, ConnType.CONJ);
        WFF disj = new BinaryConn(R, conj, ConnType.DISJ);
        WFF cond = new BinaryConn(disj, T, ConnType.COND);
        WFF bicond = new BinaryConn(F, cond, ConnType.BICOND);
        WFF univ = new Universal(bicond, 'y');
        WFF exist = new Existential(univ, 'x');
        WFF neg = new Negation(exist);*/
        
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
        
        WFF neg = Parser.parse(sequence);

        final String wffString = neg.toString();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showGUI(wffString);
            }
        });

    }

    private static void showGUI(String text) {
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
