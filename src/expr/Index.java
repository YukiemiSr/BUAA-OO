package expr;

import java.util.ArrayList;
import java.util.Iterator;

public class Index implements Factor {
    private final ArrayList<Factor> factors;

    public Index() {
        this.factors = new ArrayList<>();
    }

    public void addIndex(Factor factor) {
        this.factors.add(factor);
    }

    public String toString() {
        Iterator<Factor> iter = factors.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iter.next().toString()).append(" ");
        while (iter.hasNext()) {
            sb.append("^ ");
            sb.append(iter.next().toString()).append(" ");
        }
        return sb.toString();
    }

    public Poly toPoly() {
        Factor factor1 = this.factors.get(0);
        if (factors.size() == 1) {
            return factor1.toPoly();
        } else {
            Factor factor2 = this.factors.get(1);
            return factor1.toPoly().powPoly(((Number) factor2).getNum().intValue());
        }
    }
}
