package parse;

import logic.*;
import logic.conn.*;
import parse.*;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * We here try to parse well-formed formulae by recursive descent.
 *
 * Operator precedence works as follows:
 *
 * At the deepest level, We look to match basic formulae, which are either:
 *  - predicates; or
 *  - arbitrary wffs wrapped in parentheses.
 *
 * Then, match on arbitrary strings of unary connectives: negations and quantifers.
 * 
 * Finally, proceed up the precedence heirarchy from tightest to loosest binding as follows:
 *  - Conjunction;
 *  - Disjunction;
 *  - Conditional;
 *  - Biconditional.
 *
 * As a grammar:
 *
 * WFF      :=  COND ↔ WFF | COND
 * DISJ     :=  CONJ ∨ WFF | CONJ
 * CONJ     :=  UNARY ∧ WFF | UNARY
 * UNARY    :=  PREF* BASE
 * BASE     :=  PRED | ( WFF )
 * PREF     :=  ¬ | ∃ LOWER+ | ∀ LOWER+
 * PRED     :=  UPPER LOWER*
 * UPPER    :=  [A..Z]
 * LOWER    :=  [a..z]
 *
 * As the descent of the hierarchy takes place first upon the left operand,
 * the binary operators are right-associative.
 * The meaning of expressions involving strings of conditionals can differ depending on
 * associativity; so parenthesise the necessaries.
 */

// Classes implementing this handle parsing the operands of binary connectives.
interface ParseLevel
{
    public ParseResult parse(LinkedList<Token> sequence);
}

class BicondParser implements ParseLevel
{
    public ParseResult parse(LinkedList<Token> sequence)
    {
        return Parser.parseConn(sequence, TokenType.BICOND, ConnType.BICOND, Parser.cp);
    }
}

class CondParser implements ParseLevel
{
    public ParseResult parse(LinkedList<Token> sequence)
    {
        return Parser.parseConn(sequence, TokenType.COND, ConnType.COND, Parser.dp);
    }
}

class DisjParser implements ParseLevel
{
    public ParseResult parse(LinkedList<Token> sequence)
    {
        return Parser.parseConn(sequence, TokenType.DISJ, ConnType.DISJ, Parser.cjp);
    }
}

class ConjParser implements ParseLevel
{
    public ParseResult parse(LinkedList<Token> sequence)
    {
        return Parser.parseConn(sequence, TokenType.CONJ, ConnType.CONJ, Parser.pp);
    }
}

class PrefixParser implements ParseLevel
{
    // Pop off unary connectives; negation, existential, universal quantifiers.
    // For quantifiers, we must consume both a quantification and a variable symbol.
    // If the latter is impossible, fail.
    // Having consumed all available prefixes, try to parse a bracketed WFF or a predicate.
    public ParseResult parse(LinkedList<Token> sequence)
    {
        Token next, last, currQuant;
        LinkedList<Token> prefixes = new LinkedList<Token>();
        LinkedList<Token> consumed = new LinkedList<Token>();
        
        // Extract all leading unary connective symbols;
        // We shall construct the connectives themselves after parsing the remainder,
        // so that we have a wff to attach them to.
        // If a quantifier binds multiple variables, translate the string into a sequence of 
        // individually-quantified ones.
        while (true)
        {
            if (sequence.isEmpty()) break;
            
            next = sequence.peekFirst();

            if (next.type == TokenType.NEG)
            {
                consumed.addLast(sequence.removeFirst());
                prefixes.addLast(next);
            }
            else if (next.type == TokenType.UNIV || next.type == TokenType.EXIST)
            {
                consumed.addLast(sequence.removeFirst());
                currQuant = next;
                
                if (sequence.isEmpty() ||
                    sequence.peekFirst().type != TokenType.SYM ||
                    !Character.isLowerCase(sequence.peekFirst().sym))
                {
                    // Incomplete quantification.
                    Parser.transferSequence(consumed, sequence);
                    return null; 
                }

                while (!sequence.isEmpty() &&
                       sequence.peekFirst().type == TokenType.SYM &&
                       Character.isLowerCase(sequence.peekFirst().sym))
                {
                    prefixes.addLast(currQuant);
                    next = sequence.removeFirst();
                    prefixes.addLast(next);
                    consumed.addLast(next);
                }
            }
            else
            {
                break;
            }
        }
        
        // Having consumed all leading unaries, parse the wff they bind.
        ParseResult res = Parser.parseBase(sequence);
        
        if (res == null || res.wff == null)
        {
            Parser.transferSequence(consumed, sequence);
            return null;
        }
        else
        {
            Parser.transferSequence(consumed, res.consumed);
        }

        WFF base = res.wff;

        // Attach the prefixes extracted earlier; 
        // process symbols in reverse: innermost to outermost.
        while (!prefixes.isEmpty()) 
        {
            last = prefixes.removeLast();

            if (last.type == TokenType.NEG)
            {
                Negation newNeg = new Negation(base);
                base = newNeg;
            } 
            else if (last.type == TokenType.SYM) 
            {
                currQuant = prefixes.removeLast();
                WFF quant;

                if (currQuant.type == TokenType.UNIV)
                {
                    quant = new Universal(base, last.sym);
                }
                else if (currQuant.type == TokenType.EXIST)
                {
                    quant = new Existential(base, last.sym);
                }
                else
                {
                    // Should be impossible to get here.
                    Parser.transferSequence(res.consumed, sequence);
                    return null;
                }

                base = quant;
            } 
            else
            {
                // Likewise impossible.
                Parser.transferSequence(res.consumed, sequence);
                return null;
            }
        }

        res.wff = base;
        return res;
    }
}

// This structure contains the wff returned by a parser, along with the sequence
// of tokens that were consumed to produce it.
class ParseResult
{
    WFF wff;
    LinkedList<Token> consumed;

    public ParseResult(WFF wff, LinkedList<Token> consumed)
    {
        this.wff = wff;
        this.consumed = consumed;
    }
}


// We consume tokens from left to right.
// Each function returns either: 
//  - The successfully-parsed wff, with the consumed symbols removed from the input sequence;
//  - null, if the parse fails, with the token sequence unmodified.
// The consumed token list is required so that the input sequence can be reconstituted and
// alternative parses tried, if matching fails at any point.
public class Parser
{
    // These are clumsy, but save some code duplication... I wish I had first-class functions.
    static ParseLevel bcp = new BicondParser();
    static ParseLevel cp = new CondParser();
    static ParseLevel dp = new DisjParser();
    static ParseLevel cjp = new ConjParser();
    static ParseLevel pp = new PrefixParser();

    
    public static WFF parse(LinkedList<Token> sequence)
    {
        ParseResult res = parseWFF(sequence);
        
        // Note that we check if there are unconsumed tokens: this is considered to be a failed
        // parse, as it must result from an invalid formula.
        if (res == null || !sequence.isEmpty())
        {
            return null;
        }
        
        return res.wff;
    }
    
    public static ParseResult parseWFF(LinkedList<Token> sequence)
    {
        return bcp.parse(sequence);
    }
   
    /*
     * Implements the actual logic of the precedence hierarchy.
     * Try first to match with a WFF whose main connective is the first occurrence of the
     * one given, whose left sub-formula results from the given parser, and whose right
     * sub-formula is a generic WFF.
     * If this fails, then try to match with the given parser by itself.
     */
    public static ParseResult parseConn(LinkedList<Token> sequence, TokenType type, ConnType conn,  ParseLevel parser)
    {
        int connIndex = sequence.indexOf(new Token(type));

        if (connIndex == -1)
        {
            return parser.parse(sequence);
        }

        LinkedList<Token> pre = popN(sequence, connIndex);
        
        ParseResult left = parser.parse(pre);

        if (left == null)
        {
            transferSequence(pre, sequence);
            return parser.parse(sequence);
        }
        else if (!pre.isEmpty())
        {
            transferSequence(pre, sequence);
            transferSequence(left.consumed, sequence);
            return parser.parse(sequence);
        }

        left.consumed.addLast(sequence.removeFirst());
        ParseResult right = parseWFF(sequence);

        if (right ==  null)
        {

            transferSequence(left.consumed, sequence);
            return null;
        }
        else 
        {
            transferSequence(left.consumed, right.consumed);
            right.wff = new BinaryConn(left.wff, right.wff, conn);
            return right;
        }
    } 

    // Try matching a predicate, else try a parenthesised wff.
    public static ParseResult parseBase(LinkedList<Token> sequence)
    {
        if (sequence.isEmpty()) return null;

        ParseResult base = parsePred(sequence);

        if (base == null)
        {
            if (sequence.peekFirst().type == TokenType.LPAREN)
            {
                Token lparen = sequence.removeFirst();
                base = parseWFF(sequence);
                
                if (base == null)
                {
                    sequence.addFirst(lparen);
                    return null;
                }

                base.consumed.addFirst(lparen);
                
                if (sequence.isEmpty() || sequence.peekFirst().type != TokenType.RPAREN) {
                    transferSequence(base.consumed, sequence);
                    return null;
                }
                else
                {
                    base.consumed.addLast(sequence.removeFirst());
                    return base;
                }
            }

                return null;
        }

        return base;
    }
    

    // A predicate is a capital letter followed by a possibly-empty argument list.
    public static ParseResult parsePred(LinkedList<Token> sequence)
    {
        if (sequence.isEmpty() ||
            sequence.peekFirst().type != TokenType.SYM || 
            !Character.isUpperCase(sequence.peekFirst().sym))
        {
            return null;
        }

        LinkedList<Token> consumed = new LinkedList<Token>();
        ArrayList<Term> args = new ArrayList<Term>();

        Token pred = sequence.removeFirst();
        consumed.addLast(pred);
        
        Token next;

        while (!sequence.isEmpty())
        {
            if (sequence.peekFirst().type != TokenType.SYM ||
                !Character.isLowerCase(sequence.peekFirst().sym))
            {
                    break;
            }
            
            next = sequence.removeFirst();
            consumed.addLast(next);
            args.add(new Term(next.sym));
        }

        return new ParseResult(new Predicate(pred.sym,
                                             args.size(),
                                             args.toArray(new Term[args.size()])),
                               consumed);

    }

    // Remove all elements from the first argument, prepend them to the second argument.
    public static <E> void transferSequence(LinkedList<E> a, LinkedList<E> b)
    {
        Iterator<E> iter = a.descendingIterator();

        while (iter.hasNext()) {
            b.addFirst(iter.next());
            iter.remove();
        }
    }
    
    // Remove the first n elements from a list, and return those elements as a new list.
    public static <E> LinkedList<E> popN(LinkedList<E> in, int n)
    {
        LinkedList<E> out = new LinkedList<E>();

        for (int i = 0; i < n; ++i)
        {
            if (in.isEmpty()) break;
            out.addLast(in.removeFirst());
        }

        return out;
    }
}

