import expr.Expr;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        Lexer lexer = new Lexer(pre(line));
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        System.out.println(expr.toPoly().toString());
    }

    static String pre(String l) {
        int pos = 0;
        int x1 = 0;
        StringBuilder s = new StringBuilder();
        if (l.charAt(0) == '+' || l.charAt(0) == '-') {
            s.append(0);
        }
        while (pos < l.length()) {
            if (l.charAt(pos) != ' ' && l.charAt(pos) != '\t') {
                s.append(l.charAt(pos));
            }
            pos++;
        }
        pos = 0;
        char c1;
        //去括号
        c1 = s.charAt(pos);
        StringBuilder s1 = new StringBuilder();
        s1.append(c1);
        pos++;
        while (pos < s.length()) {
            if ("+-".indexOf(s1.charAt(x1)) != -1 && s1.length() != 0) {
                if (s1.charAt(x1) == s.charAt(pos)) {
                    s1.deleteCharAt(x1);
                    s1.append("+");
                } else if ("+-".indexOf(s.charAt(pos)) != -1 && (s1.charAt(x1) != s.charAt(pos))) {
                    s1.deleteCharAt(x1);
                    s1.append("-");
                } else {
                    s1.append(s.charAt(pos));
                    x1++;
                }
            } else {
                s1.append(s.charAt(pos));
                x1++;
            }
            pos++;
        }
        pos = 0;

        while (pos < s1.length() - 2) {
            if (s1.charAt(pos) == '*' && s1.charAt(pos + 1) == '*' && s1.charAt(pos + 2) == '+') {
                s1.deleteCharAt(pos + 2);
            }
            pos++;
        }
        pos = 0;
        StringBuilder s2 = new StringBuilder();
        while (pos < s1.length()) {
            if (pos < s1.length() - 1 && (s1.charAt(pos) == '*' && s1.charAt(pos + 1) == '*')) {
                pos += 2;
                s2.append('^');
            } else {
                s2.append(s1.charAt(pos));
                pos++;
            }
        }

        return end(deOp(s2)).toString();
    }

    static StringBuilder end(StringBuilder s3) {
        StringBuilder s4 = new StringBuilder();
        int i;
        for (i = 0; i < s3.length(); i++) {
            if (s3.charAt(i) == '-') {
                s4.append("-1*");
            } else if (s3.charAt(i) == '+') {
                s4.append("+1*");
            } else {
                s4.append(s3.charAt(i));
            }
        }
        return s4;
    }

    static StringBuilder deOp(StringBuilder s2) {
        StringBuilder s3 = new StringBuilder();
        int pos = 0;
        while (pos < s2.length()) {
            if (pos < s2.length() - 1) {
                if (s2.charAt(pos) == '(' && (s2.charAt(pos + 1) == '+'
                    || s2.charAt(pos + 1) == '-')) {
                    s3.append(s2.charAt(pos));
                    s3.append('0');
                } else {
                    s3.append(s2.charAt(pos));
                }
            } else {
                s3.append(s2.charAt(pos));
            }
            pos++;

        }
        return s3;
    }
}


