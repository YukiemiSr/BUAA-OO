package expr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Expr implements Factor {
    private final ArrayList<Term> terms;
    private final ArrayList<String> symbols;

    public Expr() {
        this.terms = new ArrayList<>();
        this.symbols = new ArrayList<>();
    }

    public void addTerm(Term term,String symbol) {
        this.terms.add(term);
        this.symbols.add(symbol);
    }

    public Poly toPoly() {
        Iterator<String> iter2 = symbols.iterator();
        Poly poly = new Poly();
        Poly temp;
        int sym = 0;
        for (Term term : terms) {
            String s = iter2.next();
            if (sym == 0) {
                poly = term.toPoly();
                if (Objects.equals(s, "-")) {
                    poly.negative();
                }
                sym = 1;
            }
            else {
                if (Objects.equals(s, "+")) {
                    temp = poly.addPoly(term.toPoly());
                }
                else {
                    temp = poly.subPoly(term.toPoly());
                }
                poly = temp;
            }
        }
        return poly;
    }

    public String toString() {
        Iterator<Term> iter = terms.iterator();
        Iterator<String> iter2 = symbols.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString()).append(" ");
        iter2.next();
        if (iter.hasNext()) {
            sb.append(iter.next().toString()).append(" ");
            sb.append(iter2.next()).append(" ");
            while (iter.hasNext()) {
                sb.append(iter.next().toString()).append(" ");
                sb.append(iter2.next()).append(" ");
            }
        }
        return sb.toString();
    }
}
