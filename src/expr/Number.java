package expr;

import java.math.BigInteger;
import java.util.ArrayList;

public class Number implements Factor {
    private final BigInteger num;

    public Number(BigInteger num, String symbol) {
        if (symbol.equals("+")) {
            this.num = num;
        } else {
            this.num = num.negate();
        }
    }

    public BigInteger getNum() {
        return num;
    }

    public String toString() {
        if (new BigInteger("0").compareTo(this.num) > 0) {
            String l1 = this.num.toString();
            StringBuilder s = new StringBuilder(l1);
            s.deleteCharAt(0);
            return "-" + s;
        } else {
            return this.num.toString();
        }
    }

    public Poly toPoly() {
        Poly poly = new Poly();
        Union union = new Union(0, 0, 0, num);
        ArrayList<Union> p1 = new ArrayList<>();
        p1.add(union);
        poly.setPoly(p1);
        return poly;
    }
}
