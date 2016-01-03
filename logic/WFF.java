package logic;

import java.util.ArrayList;

/*
 * A well-formed formula is as follows:
 *  A predicate, such as Px, Qab, R, etc., is a WFF.
 *  A pair of WFFs with a two-place connective between them, parens around the pair, is a WFF.
 *  A WFF preceded by a one-place connective is a WFF.
 *  A WFF preceded by a quantifier is a WFF.
 *  Nothing else is a WFF.
 *
 *  We will also allow True and False values as zero-place predicates whose
 *  value is predetermined.
*/

public abstract class WFF
{
    public abstract boolean isBasic();
    public abstract String toString();
    public abstract boolean equals(WFF prop);
    public abstract boolean contradicts(WFF prop);
}


