package expr;

import java.math.BigInteger;
import java.util.Objects;

public class Union {
    private BigInteger coe;
    private final int expX;
    private final int expY;
    private final int expZ;

    public boolean isEqual(Union union) {
        return union.expX == this.expX && union.expY == this.expY && union.expZ == this.expZ;
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

    public Union(int x, int y, int z, BigInteger coe) {
        this.expX = x;
        this.expY = y;
        this.coe = coe;
        this.expZ = z;
    }

    public boolean isNegative() {
        return this.coe.compareTo(BigInteger.valueOf(0)) < 0;
    }

    public void negative() {
        this.coe = this.coe.negate();
    }

    public Union operation(String sym, BigInteger coe1, int x, int y, int z) {
        BigInteger coe2 = new BigInteger(String.valueOf(0));
        int x1 = 0;
        int y1 = 0;
        int z1 = 0;
        if (Objects.equals(sym, "+")) {
            this.coe = coe.add(coe1);
        } else if (Objects.equals(sym, "-")) {
            this.coe = coe.subtract(coe1);
        } else if (Objects.equals(sym, "*")) {
            coe2 = coe.multiply(coe1);
            x1 = expX + x;
            y1 = expY + y;
            z1 = expZ + z;
        }
        return new Union(x1, y1, z1, coe2);
    }

    public boolean isZero() {
        return coe.equals(BigInteger.valueOf(0));
    }

    public String toString() {
        if (coe.equals(BigInteger.valueOf(0))) {
            return "0";
        } else {
            StringBuilder s = new StringBuilder();
            if (expX == 0 & expY == 0 & expZ == 0) {
                s.append(coe);
            } else {
                if (!this.coe.equals(BigInteger.valueOf(1))) {
                    if (this.coe.equals(BigInteger.valueOf(-1))) {
                        s.append("-");
                    } else {
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
                        } else {
                            s.append("**").append(this.expX);
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
                        } else {
                            s.append("**").append(this.expY);
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
                        } else {
                            s.append("**").append(this.expZ);
                        }
                    }
                }
            }
            return s.toString();
        }
    }
}
