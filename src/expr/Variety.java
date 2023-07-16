package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

public class Variety implements Factor {
    private final String ver;

    public Variety(String x) {
        this.ver = x;
    }

    public String toString() {
        return ver;
    }

    public Poly toPoly() {
        Poly poly = new Poly();
        Union union = null;
        if (Objects.equals(ver, "x")) {
            union = new Union(1, 0, 0, BigInteger.valueOf(1));
        } else if (Objects.equals(ver, "y")) {
            union = new Union(0, 1, 0, BigInteger.valueOf(1));
        } else if (Objects.equals(ver, "z")) {
            union = new Union(0, 0, 1, BigInteger.valueOf(1));
        }
        ArrayList<Union> p1 = new ArrayList<>();
        p1.add(union);
        poly.setPoly(p1);
        return poly;
    }
}
