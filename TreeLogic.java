import logic.*;
import logic.conn.*;

import java.util.ArrayList;

import javax.swing.*;

public class TreeLogic {
    public static void main(String[] args) {
        Term x = new Term('x');
        Term y = new Term('y');
        Term a = new Term('a');

        ArrayList<Term> PArgs = new ArrayList<Term>();
        PArgs.add(x);

        ArrayList<Term> QArgs = new ArrayList<Term>();
        QArgs.add(a);
        QArgs.add(y);

        WFF P = new Predicate('P', 1, PArgs);
        WFF Q = new Predicate('Q', 2, QArgs);
        WFF R = new Predicate('R');
        WFF T = new Verum();
        WFF F = new Falsum();

        WFF conj = new Conjunction(P, Q);
        WFF disj = new Disjunction(R, conj);
        WFF cond = new Conditional(disj, T);
        WFF bicond = new Biconditional(F, cond);
        WFF univ = new Universal(bicond, 'y');
        WFF exist = new Existential(univ, 'x');
        WFF neg = new Negation(exist);

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
