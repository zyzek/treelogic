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
 * WFF      :=  COND | COND <-> WFF
 * COND     :=  DISJ | DISJ -> WFF
 * DISJ     :=  CONJ | CONJ v WFF
 * CONJ     :=  UNARY | UNARY ^ WFF
 * UNARY    :=  PREF* BASE
 * BASE     :=  PRED | (WFF)
 * PREF     :=  negation | existential | universal
 * PRED     :=  UPPER LOWER*
 * UPPER    :=  [A...Z]
 * LOWER    :=  [a...z]
 *
 * That the hierarchy descent takes place first upon the left operand means that
 * the binary operators are right-associative.
 */

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

            if (next.token == TokenType.NEG)
            {
                consumed.addLast(sequence.removeFirst());
                prefixes.addLast(next);
            }
            else if (next.token == TokenType.UNIV || next.token == TokenType.EXIST)
            {
                consumed.addLast(sequence.removeFirst());
                currQuant = next;
                
                if (sequence.isEmpty() ||
                    sequence.peekFirst().token != TokenType.SYM ||
                    !Character.isLowerCase(sequence.peekFirst().sym))
                {
                    // Incomplete quantification.
                    Parser.transferSequence(consumed, sequence);
                    return null; 
                }

                while (!sequence.isEmpty() &&
                       sequence.peekFirst().token == TokenType.SYM &&
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

        ParseResult res = Parser.parseBase(sequence);
        WFF base = res.wff;

        if (base == null)
        {
            Parser.transferSequence(consumed, sequence);
            return null;
        }
        else
        {
            Parser.transferSequence(consumed, res.consumed);
        }
        
        // Attach the prefixes extracted earlier; 
        // process symbols in reverse: innermost to outermost.
        while (!prefixes.isEmpty()) 
        {
            last = prefixes.removeLast();

            if (last.token == TokenType.NEG)
            {
                Negation newNeg = new Negation(base);
                base = newNeg;
            } 
            else if (last.token == TokenType.SYM) 
            {
                currQuant = prefixes.removeLast();
                WFF quant;

                if (currQuant.token == TokenType.UNIV)
                {
                    quant = new Universal(base, last.sym);
                }
                else if (currQuant.token == TokenType.EXIST)
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

public class Parser
{
    static ParseLevel bcp = new BicondParser();
    static ParseLevel cp = new CondParser();
    static ParseLevel dp = new DisjParser();
    static ParseLevel cjp = new ConjParser();
    static ParseLevel pp = new PrefixParser();

    
    public static WFF parse(LinkedList<Token> sequence)
    {
        ParseResult res = parseWFF(sequence);

        if (res == null) {
            return null;
        }

        return res.wff;
    }

    public static ParseResult parseWFF(LinkedList<Token> sequence)
    {
        return bcp.parse(sequence);
    }
   
    /*
     * Match either with the given parser by itself,
     * or with that parser, followed by the given connective, then a general WFF.
     */
    public static ParseResult parseConn(LinkedList<Token> sequence, TokenType token, ConnType conn,  ParseLevel parser)
    {
        ParseResult left = parser.parse(sequence);
        
        if (left.wff == null)
        {
            return null;
        }
        else if (sequence.isEmpty())
        {
            return left;
        }
        else
        {
            Token next = sequence.peekFirst();

            if (next.token == token)
            {
                left.consumed.addLast(sequence.removeFirst());
                ParseResult right = parseWFF(sequence);
                
                if (right.wff == null)
                {
                    transferSequence(left.consumed, sequence);
                    return null;
                }
                
                right.wff = new BinaryConn(left.wff, right.wff, conn);
                return right;
            }
            
            return left;
        }
    } 

    // Try matching a predicate, else try a parenthesised wff.
    public static ParseResult parseBase(LinkedList<Token> sequence)
    {
        ParseResult base = parsePred(sequence);

        if (base == null)
        {
            if (sequence.peekFirst().token == TokenType.LPAREN)
            {
                Token lparen = sequence.removeFirst();
                base = parseWFF(sequence);
                
                if (base == null)
                {
                    sequence.addFirst(lparen);
                    return null;
                }

                base.consumed.addFirst(lparen);

                if (sequence.peekFirst().token == TokenType.RPAREN)
                {
                    base.consumed.addLast(sequence.removeFirst());
                    return base;
                }

                transferSequence(base.consumed, sequence);
                return null;
            }

                return null;
        }

        return base;

    }
    

    // A predicate is a capital letter followed by a possibly-empty argument list.
    public static ParseResult parsePred(LinkedList<Token> sequence)
    {
        if (sequence.isEmpty() ||
            sequence.peekFirst().token != TokenType.SYM || 
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
            if (sequence.peekFirst().token != TokenType.SYM ||
                !Character.isLowerCase(sequence.peekFirst().sym))
            {
                    break;
            }
            
            next = sequence.removeFirst();
            consumed.addLast(next);
            args.add(new Term(next.sym));
        }

        return new ParseResult(new Predicate(pred.sym, args.size(), (Term[])args.toArray()),
                               consumed);

    }

    // Remove all elements from the first argument, prepend them to the second argument.
    public static <E> void transferSequence(LinkedList<E> a, LinkedList<E> b)
    {
        Iterator<E> iter = a.descendingIterator();

        while (iter.hasNext()) {
            b.addFirst(iter.next());
            a.remove();
        }
    }
}

