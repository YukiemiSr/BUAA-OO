package expr;

import java.util.ArrayList;
import java.util.Iterator;

public class Term implements Factor {
    private final ArrayList<Index> factors;

    public Term() {
        this.factors = new ArrayList<>();
    }

    public void addFactor(Index factor) {
        this.factors.add(factor);
    }

    public String toString() {
        Iterator<Index> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString()).append(" ");
        if (iter.hasNext()) {
            sb.append(iter.next().toString()).append(" ");
            sb.append("* ");
            while (iter.hasNext()) {
                sb.append(iter.next().toString()).append(" ");
                sb.append("* ");
            }
        }
        return sb.toString();
    }

    public Poly toPoly() {
        Poly poly = new Poly();
        int sym = 0;
        for (Factor factor:factors) {
            if (sym == 0) {
                poly = factor.toPoly();
                sym = 1;
            }
            else {
                poly = poly.mulPoly(factor.toPoly());
            }
        }
        return poly;
    }
}
