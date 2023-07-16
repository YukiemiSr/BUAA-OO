package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Poly {
    private ArrayList<Union> unionCollation = new ArrayList<>();

    public void setPoly(ArrayList<Union> unionCollation) {
        this.unionCollation = unionCollation;
    }

    public void negative() {
        for (Union union : unionCollation) {
            union.negative();
        }
    }

    public Poly addPoly(Poly poly) {
        Poly poly1 = new Poly();
        for (Union union1 : poly.unionCollation) {
            int sym = 0;
            for (Union union : this.unionCollation) {
                if (union.isEqual(union1)) {
                    union.operation("+",union1.getCoe(),0,0,0,union1.getsins(),union1.getcoss());
                    sym = 1;
                }
            }
            if (sym == 0) {
                unionCollation.add(union1);
            }
        }
        poly1.setPoly(this.unionCollation);

        return poly1;
    }

    public Poly subPoly(Poly poly) {
        Poly poly1 = new Poly();
        for (Union union1 : poly.unionCollation) {
            int sym = 0;
            for (Union union : this.unionCollation) {
                if (union.isEqual(union1)) {
                    union.operation("-",union1.getCoe(),0,0,0,union1.getsins(),union1.getcoss());
                    sym = 1;
                }
            }
            if (sym == 0) {
                union1.negative();
                unionCollation.add(union1);
            }
        }
        poly1.setPoly(this.unionCollation);
        return poly1;
    }

    public Poly mulPoly(Poly poly) {
        Poly poly1 = new Poly();
        ArrayList<Union> x1 = new ArrayList<>();
        poly1.setPoly(x1);
        Poly poly2 = new Poly();
        int k = 0;
        for (Union union : this.unionCollation) {
            for (Union union1 : poly.unionCollation) {
                Union union2 = union.operation("*",union1.getCoe(),
                        union1.getExpX(), union1.getExpY(), union1.getExpZ(),
                        union1.getsins(),union1.getcoss());
                ArrayList<Union> x = new ArrayList<>();
                x.add(union2.clone());
                poly2.setPoly(x);
                poly1.addPoly(poly2);
            }
        }
        return poly1;
    }

    public Poly powPoly(int pow) {
        Poly poly1 = new Poly();

        if (pow == 0) {
            Union union = new Union(0,0,0, BigInteger.valueOf(1),null,null);
            ArrayList<Union> p1 = new ArrayList<>();
            p1.add(union);
            poly1.setPoly(p1);
        }
        else {
            Poly temp = new Poly();
            Poly con = new Poly();
            temp.setPoly(this.unionCollation);
            ArrayList<Union> x = new ArrayList<>();
            for (Union union : unionCollation) {
                Union u1 = new Union(union.getExpX(), union.getExpY(), union.getExpZ(),
                        union.getCoe(),union.getsins(),union.getcoss());
                x.add(u1);
            }
            con.setPoly(x);
            int p = pow;
            while (p > 1) {
                temp = temp.mulPoly(con);
                p--;
            }
            poly1 = temp;
        }
        return poly1;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int sym  = 0;
        for (Union union : unionCollation) {
            if (union.isZero()) {
                continue;
            }
            if (sym == 0) {
                s.append(union);
                sym = 1;
            }
            else {
                if (!union.isNegative()) {
                    s.append("+").append(union);
                }
                else {
                    s.append(union);
                }
            }
        }
        if (s.length() == 0) {
            s.append("0");
        }
        return s.toString();
    }

    public boolean equals(Poly poly) {
        for (Union union:this.unionCollation) {
            int sym = 1;
            for (Union union1: poly.unionCollation) {
                if (union.trisEqual(union1)) {
                    sym = 0;
                }
            }
            if (sym == 1) {
                return false;
            }
        }

        for (Union union:poly.unionCollation) {
            int sym = 1;
            for (Union union1: this.unionCollation) {
                if (union.trisEqual(union1)) {
                    sym = 0;
                }
            }
            if (sym == 1) {
                return false;
            }
        }

        return true;
    }

    public Poly clone() {
        Poly poly = new Poly();
        ArrayList<Union> set = new ArrayList<>();
        for (Union union:unionCollation) {
            set.add(union.clone());
        }
        poly.setPoly(set);
        return poly;
    }

    public Poly tri(Poly poly,Integer integer,String type) {
        HashMap<Poly,Integer> one = new HashMap<>();
        HashMap<Poly,Integer> two = new HashMap<>();
        one.put(poly,integer);
        if (type == "sin") {
            Union union = new Union(0,0,0,BigInteger.valueOf(1),one,two);
            ArrayList<Union> s = new ArrayList<>();
            s.add(union);
            Poly poly1 = new Poly();
            poly1.setPoly(s);
            return poly1;
        }
        else {
            Union union = new Union(0,0,0,BigInteger.valueOf(1),two,one);
            ArrayList<Union> s = new ArrayList<>();
            s.add(union);
            Poly poly1 = new Poly();
            poly1.setPoly(s);
            return poly1;
        }
    }

    public Poly derive(char c) {
        Poly poly = new Poly();
        for (Union union:this.unionCollation) {
            poly.addPoly(union.derive(c).clone());
        }
        return poly;
    }
}
