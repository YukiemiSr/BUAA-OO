import expr.Expr;

public class Der {
    public String deal(String line) {
        StringBuilder s = new StringBuilder();
        char para;
        for (int i = 0;i < line.length();) {
            if (line.charAt(i) == 'd') {
                i++;
                para = line.charAt(i);
                i++;
                int sum = 0;
                sum++;
                i++;
                StringBuilder s1 = new StringBuilder();
                while (sum > 0 && i < line.length()) {
                    if (line.charAt(i) == '(') {
                        sum++;
                    }
                    else if (line.charAt(i) == ')') {
                        sum--;
                    }
                    if (sum > 0) {
                        s1.append(line.charAt(i));
                    }
                    i++;
                }

                s.append(derivative(s1.toString(),para));
            }
            else {
                s.append(line.charAt(i));
                i++;
            }
        }
        return s.toString();
    }

    public String derivative(String string, char c)
    {
        Lexer lexer = new Lexer(string);
        Parser parser = new Parser(lexer);
        Expr expr = parser.parseExpr();
        return "(" + expr.toPoly().derive(c).toString() + ")";
    }

}
