package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Union {
    private BigInteger coe;
    private final int expX;
    private final int expY;
    private final int expZ;
    private final HashMap<Poly, Integer> sins;//多个三角函数相乘关系//三角函数和指数
    private final HashMap<Poly, Integer> coss;

    public HashMap<Poly, Integer> getsins() {
        return this.sins;
    }

    public HashMap<Poly, Integer> getcoss() {
        return this.coss;
    }

    static boolean judgeHash(HashMap<Poly,Integer> t1, HashMap<Poly,Integer> t2) {

        if (t1.isEmpty() && t2.isEmpty()) {
            return true;
        }
        else if (!t1.isEmpty() && t2.isEmpty()) {
            return false;
        }
        else if (t1.isEmpty() && !t2.isEmpty()) {
            return false;
        }
        else {
            int sym = 0;
            for (Map.Entry<Poly, Integer> entry : t1.entrySet()) {
                sym = 1;
                for (Map.Entry<Poly, Integer> entry1 : t2.entrySet()) {
                    if (entry.getKey().equals(entry1.getKey()) &&
                            Objects.equals(entry.getValue(), entry1.getValue())) {
                        sym = 0;
                    }
                }
                if (sym == 1) {
                    break;
                }
            }
            return sym != 1;
        }
    }

    public boolean isEqual(Union union) {
        if (union.expX == this.expX && union.expY == this.expY && union.expZ == this.expZ) {
            return judgeHash(this.getsins(),union.getsins()) &&
                    judgeHash(this.getcoss(),union.getcoss())
                    && judgeHash(union.getsins(),this.getsins())
                    && judgeHash(union.getcoss(),this.getcoss());
        }
        else {
            return false;
        }
    }

    public boolean trisEqual(Union union) {
        return union.expX == this.expX && union.expY == this.expY && union.expZ == this.expZ
                && Objects.equals(union.getCoe(), this.coe);
    }

    public int getExpX() {
        return expX;
    }

    public BigInteger getCoe() {
        return coe;
    }

    public int getExpY() {
        return expY;
    }

    public int getExpZ() {
        return expZ;
    }

    public static void mapCopy(Map paramsMap, Map resultMap) {
        if (resultMap == null) {
            return;
        }
        if (paramsMap == null) {
            return;
        }
        for (Object o : paramsMap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Object key = entry.getKey();
            resultMap.put(key, paramsMap.get(key) != null ? paramsMap.get(key) : "");
        }
    }

    public Union(int x, int y, int z, BigInteger coe,
                 HashMap<Poly, Integer> sins,HashMap<Poly, Integer> coss) {
        this.expX = x;
        this.expY = y;
        this.coe = coe;
        this.expZ = z;
        this.coss = new HashMap<>();
        this.sins = new HashMap<>();
        mapCopy(sins, getsins());
        mapCopy(coss, getcoss());
    }

    public boolean isNegative() {
        return this.coe.compareTo(BigInteger.valueOf(0)) < 0;
    }

    public void negative() {
        this.coe = this.coe.negate();
    }

    public Union operation(String sym, BigInteger coe1, int x, int y,
                           int z,HashMap<Poly, Integer> sins,HashMap<Poly, Integer> coss) {
        BigInteger coe2 = new BigInteger(String.valueOf(0));
        int x1 = 0;
        int y1 = 0;
        int z1 = 0;
        HashMap<Poly, Integer> s = new HashMap<>();
        HashMap<Poly, Integer> c = new HashMap<>();
        if (Objects.equals(sym, "+")) {
            this.coe = coe.add(coe1);
        } else if (Objects.equals(sym, "-")) {
            this.coe = coe.subtract(coe1);
        } else if (Objects.equals(sym, "*")) {
            coe2 = coe.multiply(coe1);
            x1 = expX + x;
            y1 = expY + y;
            z1 = expZ + z;
            c = new HashMap<>();
            if (!this.coss.isEmpty()) {
                mapCopy(this.coss, c);
                if (!coss.isEmpty()) {
                    for (Map.Entry<Poly, Integer> entry : coss.entrySet()) {
                        int sym1 = 0;
                        for (Map.Entry<Poly, Integer> entry1 : c.entrySet()) {
                            if (entry1.getKey().equals(entry.getKey())) {
                                c.replace(entry1.getKey(),
                                    entry.getValue() + c.get(entry1.getKey()));
                                sym1 = 1;
                                break;
                            }
                        }
                        if (sym1 == 0) {
                            c.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            } else {
                mapCopy(coss, c);
            }
            s = new HashMap<>();
            if (!this.sins.isEmpty()) {
                mapCopy(this.sins, s);
                if (!sins.isEmpty()) {
                    for (Map.Entry<Poly, Integer> entry : sins.entrySet()) {
                        int sym1 = 0;
                        for (Map.Entry<Poly, Integer> entry1 : s.entrySet()) {
                            if (entry1.getKey().equals(entry.getKey())) {
                                s.replace(entry1.getKey(), entry.getValue()
                                    + s.get(entry1.getKey()));
                                sym1 = 1;
                                break;
                            }
                        }
                        if (sym1 == 0) {
                            s.put(entry.getKey(),entry.getValue());
                        }
                    }
                }
            } else {
                mapCopy(sins, s);
            }
        }
        return new Union(x1, y1, z1, coe2, s, c);
    }

    public boolean isZero() {
        return coe.equals(BigInteger.valueOf(0));
    }

    public String toString() {
        if (coe.equals(BigInteger.valueOf(0))) {
            return "0";
        }
        else {
            StringBuilder s = new StringBuilder();
            if (expX == 0 & expY == 0 & expZ == 0 & sins.isEmpty() & coss.isEmpty()) {
                s.append(coe);
            }
            else {
                if (!this.coe.equals(BigInteger.valueOf(1))) {
                    if (this.coe.equals(BigInteger.valueOf(-1))) {
                        s.append("-");
                    }
                    else {
                        s.append(this.coe);
                    }
                }
                if (this.expX != 0) {
                    if (s.length() > 0) {
                        if (!(s.length() == 1 && s.charAt(0) == '-') && s.length() > 0) {
                            s.append("*");
                        }
                    }
                    s.append("x");
                    if (this.expX > 1) {
                        if (this.expX == 2) {
                            s.append("*x");
                        }
                        else {
                            s.append("**");
                            s.append(this.expX);
                        }
                    }
                }
                if (this.expY != 0) {
                    if (!(s.length() == 1 && s.charAt(0) == '-') && s.length() > 0) {
                        s.append("*");
                    }
                    s.append("y");
                    if (this.expY > 1) {
                        if (this.expY == 2) {
                            s.append("*y");
                        }
                        else {
                            s.append("**");
                            s.append(this.expY);
                        }
                    }
                }
                if (this.expZ != 0) {
                    if (!(s.length() == 1 && s.charAt(0) == '-') && s.length() > 0) {
                        s.append("*");
                    }
                    s.append("z");
                    if (this.expZ > 1) {
                        if (this.expZ == 2) {
                            s.append("*z");
                        }
                        else {
                            s.append("**");
                            s.append(this.expZ);
                        }
                    }
                }
                if (!this.sins.isEmpty()) {
                    for (Map.Entry<Poly, Integer> entry : sins.entrySet()) {
                        if (!(s.length() == 1 && s.charAt(0) == '-') && s.length() > 0) {
                            s.append("*");
                        }
                        s.append("sin((");
                        s.append(entry.getKey().toString());
                        s.append("))");
                        if (entry.getValue() > 1) {
                            s.append("**").append(entry.getValue());
                        }
                    }
                }
                if (!this.coss.isEmpty()) {
                    for (Map.Entry<Poly, Integer> entry : coss.entrySet()) {
                        if (!(s.length() == 1 && s.charAt(0) == '-') && s.length() > 0) {
                            s.append("*");
                        }
                        s.append("cos((");
                        s.append(entry.getKey().toString());
                        s.append("))");
                        if (entry.getValue() > 1) {
                            s.append("**").append(entry.getValue());
                        }
                    }
                }
            }
            return s.toString();
        }
    }

    public Union clone() {
        HashMap<Poly,Integer> s = new HashMap<>();
        HashMap<Poly,Integer> c = new HashMap<>();
        mapCopy(this.sins,s);
        mapCopy(this.coss,c);
        Union union = new Union(this.expX,this.expY,this.expZ,this.coe,s,c);
        return union;
    }

    public Union empty() {
        HashMap<Poly,Integer> s = new HashMap<>();
        HashMap<Poly,Integer> c = new HashMap<>();
        Union union = new Union(0,0,0,BigInteger.valueOf(0),s,c);
        return union;
    }

    public Poly derive(char c) {
        Poly poly = new Poly();
        ArrayList<Union> set = new ArrayList<>();
        HashMap<Poly,Integer> s = new HashMap<>();
        HashMap<Poly,Integer> cc = new HashMap<>();
        mapCopy(sins,s);
        mapCopy(coss,cc);
        if (c == 'x') {
            if (this.expX >= 1) {
                Union union = new Union(expX - 1, expY, expZ,
                        coe.multiply(BigInteger.valueOf(expX)),s,cc);
                set.add(union);
            }
            else {
                Union union = empty();
                set.add(union);
            }
        }
        if (c == 'y') {
            if (this.expY >= 1) {
                Union union = new Union(expX, expY - 1, expZ,
                        coe.multiply(BigInteger.valueOf(expY)),s,cc);
                set.add(union);
            }
            else {
                Union union = empty();
                set.add(union);
            }

        }
        if (c == 'z') {
            if (this.expZ >= 1) {
                Union union = new Union(expX, expY, expZ - 1,
                        coe.multiply(BigInteger.valueOf(expZ)),s,cc);
                set.add(union);
            }
            else {
                Union union = empty();
                set.add(union);
            }
        }
        Poly polyLeft = new Poly();
        polyLeft.setPoly(set);
        //polyLeft is f'(x)*g(x)
        Poly poly1;
        Poly poly11;
        Poly polyRight = new Poly();
        Poly exp;
        Poly tri;
        for (Map.Entry<Poly, Integer> entry : sins.entrySet()) {
            exp = entry.getKey().clone().derive(c);
            tri = new Poly();
            HashMap<Poly,Integer> triLeftS = new HashMap<>();
            HashMap<Poly,Integer> triLeftC = new HashMap<>();
            int x = entry.getValue();
            if (x > 1) {
                triLeftS.put(entry.getKey().clone(),x - 1);
            }
            int coe;
            if (x > 1) {
                coe = x;
            }
            triLeftC.put(entry.getKey().clone(),1);
            Union union = new Union(0,0,0,BigInteger.valueOf(x),triLeftS,triLeftC);
            ArrayList<Union> setLeft = new ArrayList<>();
            setLeft.add(union);
            tri.setPoly(setLeft);
            poly1 = tri.mulPoly(exp);
            for (Map.Entry<Poly, Integer> entry1 : sins.entrySet()) {
                if (!(entry.getKey().equals(entry1.getKey()))) {
                    Poly poly2 = poly.tri(entry1.getKey(), entry1.getValue(),"sin");
                    poly1 = poly1.mulPoly(poly2);
                }
            }
            for (Map.Entry<Poly, Integer> entry1 : coss.entrySet()) {
                Poly poly2 = poly.tri(entry1.getKey(), entry1.getValue(),"cos");
                poly1 = poly1.mulPoly(poly2);
            }
            polyRight.addPoly(poly1);
        }
        for (Map.Entry<Poly, Integer> entry : coss.entrySet()) {
            exp = entry.getKey().clone().derive(c);
            tri = new Poly();
            HashMap<Poly,Integer> triLeftS = new HashMap<>();
            HashMap<Poly,Integer> triLeftC = new HashMap<>();
            int x = entry.getValue();
            if (x > 1) {
                triLeftC.put(entry.getKey().clone(),x - 1);
            }
            triLeftS.put(entry.getKey().clone(),1);
            Union union = new Union(0,0,0,BigInteger.valueOf(-x),triLeftS,triLeftC);
            ArrayList<Union> setLeft = new ArrayList<>();
            setLeft.add(union);
            tri.setPoly(setLeft);
            poly11 = tri.mulPoly(exp);
            for (Map.Entry<Poly, Integer> entry1 : sins.entrySet()) {
                Poly poly2 = poly.tri(entry1.getKey(), entry1.getValue(),"sin");
                poly11 = poly11.mulPoly(poly2);
            }
            for (Map.Entry<Poly, Integer> entry1 : coss.entrySet()) {
                if (!(entry.getKey().equals(entry1.getKey()))) {
                    Poly poly2 = poly.tri(entry1.getKey(), entry1.getValue(),"cos");
                    poly11 = poly11.mulPoly(poly2);
                }
            }
            polyRight.addPoly(poly11);
        }
        HashMap<Poly,Integer> s2 = new HashMap<>();
        HashMap<Poly,Integer> c2 = new HashMap<>();
        Union union = new Union(this.expX,this.expY,this.expZ,this.coe,s2,c2);
        ArrayList<Union> set2 = new ArrayList<>();
        set2.add(union);
        Poly poly5 = new Poly();
        poly5.setPoly(set2);
        polyRight = polyRight.mulPoly(poly5);
        polyLeft.addPoly(polyRight);
        return polyLeft;
    }
}
