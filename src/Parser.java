import expr.Expr;
import expr.Factor;
import expr.Index;
import expr.Number;
import expr.Term;
import expr.Variety;

import java.math.BigInteger;
import java.util.Objects;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        expr.addTerm(parseTerm(), "+");
        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            String x = lexer.peek();
            lexer.next();
            expr.addTerm(parseTerm(), x);
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        term.addFactor(parseIndex());
        while (lexer.peek().equals("*")) {
            lexer.next();
            term.addFactor(parseIndex());
        }
        return term;
    }

    public Index parseIndex() {
        Index index = new Index();
        index.addIndex(parseFactor());
        while (lexer.peek().equals("^")) {
            lexer.next();
            index.addIndex(parseFactor());
        }
        return index;
    }

    public Factor parseFactor() {
        if (lexer.peek().equals("(")) {
            lexer.next();
            Factor expr = parseExpr();
            lexer.next();
            return expr;
        } else if (Objects.equals(lexer.peek(), "x") || Objects.equals(lexer.peek(), "y")
            || Objects.equals(lexer.peek(), "z")) {
            String s = lexer.peek();
            lexer.next();
            return new Variety(s);
        } else if (Objects.equals(lexer.peek(), "+") || Objects.equals(lexer.peek(), "-")) {
            String x = lexer.peek();
            lexer.next();
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num, x);
        } else {
            BigInteger num = new BigInteger(lexer.peek());
            lexer.next();
            return new Number(num, "+");
        }
    }
}
