package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Cos implements Factor {
    private final Poly factor;

    public Cos(Poly factor) {
        this.factor = factor;
    }

    public Poly toPoly() {
        HashMap<Poly,Integer> factors = new HashMap<>();
        factors.put(factor,1);
        ArrayList<Union> p1 = new ArrayList<>();
        HashMap<Poly,Integer> f1 = new HashMap<>();
        Union union = new Union(0,0,0, BigInteger.valueOf(1),f1,factors);
        p1.add(union);
        Poly poly = new Poly();
        poly.setPoly(p1);
        return poly;
    }

}
