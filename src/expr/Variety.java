package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Variety implements Factor {
    private final String var;

    public Variety(String x) {
        this.var = x;
    }

    public String toString() {
        return var;
    }

    public Poly toPoly() {
        Poly poly = new Poly();
        Union union = null;
        HashMap<Poly,Integer> s = new HashMap<>();
        HashMap<Poly,Integer> c = new HashMap<>();
        if (Objects.equals(var, "x")) {
            union = new Union(1,0,0, BigInteger.valueOf(1),s,c);
        }
        else if (Objects.equals(var, "y")) {
            union = new Union(0,1,0, BigInteger.valueOf(1),s,c);
        }
        else if (Objects.equals(var, "z")) {
            union = new Union(0,0,1, BigInteger.valueOf(1),s,c);
        }
        ArrayList<Union> p1 = new ArrayList<>();
        p1.add(union);
        poly.setPoly(p1);
        return poly;
    }

    public Factor clone() {
        return new Variety(var);
    }
}
