public class TreeLogic {
    public static void main(String[] args) {
        WFF p = new BasicProp('p');
        WFF q = new BasicProp('q');
        WFF r = new BasicProp('r');
        WFF T = new Verum();
        WFF F = new Falsum();

        WFF conj = new Conjunction(p, q);
        WFF disj = new Disjunction(r, conj);
        WFF cond = new Conditional(disj, T);
        WFF bicond = new Biconditional(F, cond);

        System.out.println(new Negation(bicond));
    }
}
