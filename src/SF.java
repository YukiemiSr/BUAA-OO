
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SF {
    private final HashMap<String, ArrayList<String>> functions = new HashMap();

    public SF() {
    }

    public void addFun(String str) {
        String funName = String.valueOf(str.charAt(0));
        ArrayList<String> args = new ArrayList();
        Pattern pattern = Pattern.compile("^\\(.*?\\)");
        Matcher matcher = pattern.matcher(str.substring(1));
        if (matcher.find()) {
            String string = matcher.group(0);
            String[] idVar = string.substring(1, string.length() - 1).split(",");
            args.addAll(Arrays.asList(idVar));
        }

        Pattern pattern1 = Pattern.compile("[=].*$");
        Matcher matcher1 = pattern1.matcher(str);
        if (matcher1.find()) {
            args.add("(" + matcher1.group(0).substring(1) + ")");
        }
        this.functions.put(funName, args);
    }

    public String putin(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            String c = String.valueOf(input.charAt(i));
            if (!this.functions.containsKey(c)) {
                sb.append(c);
            } else {
                int pos = 1;
                int j;
                for (j = 2; pos != 0; ++j) {
                    if (input.charAt(i + j) == '(' || input.charAt(i + j) == ')') {
                        pos = input.charAt(i + j) == '(' ? pos + 1 : pos - 1;
                    }
                }
                String text = this.check(input, i, j);
                String[] args = text.split(",");
                StringBuilder sb1 = new StringBuilder();
                sb1.append((String) ((ArrayList) this.functions.get(c)).
                        get(((ArrayList) this.functions.get(c)).size() - 1));
                String str;
                StringBuilder sb2;
                for (int k = 0; k < args.length; ++k) {
                    if ((((ArrayList) this.functions.get(c)).get(k)).equals("x") ||
                        (((ArrayList) this.functions.get(c)).get(k)).equals("y") ||
                        (((ArrayList) this.functions.get(c)).get(k)).equals("z")) {
                        str = sb1.toString();
                        sb2 = new StringBuilder();
                        args[k] = args[k].replace('x','a');
                        args[k] = args[k].replace('y','b');
                        args[k] = args[k].replace('z','p');
                        sb2.append(str.replaceAll((String)
                                ((ArrayList) this.functions.get(c)).get(k), "(" + args[k] + ")"));
                        sb1 = sb2;
                    }
                }
                sb.append(sb1);
                i = i + j - 1;
            }
        }

        StringBuilder sb2 = new StringBuilder();
        Iterator var15 = this.functions.keySet().iterator();

        while (var15.hasNext()) {
            String s = (String)var15.next();
            sb2.append(s);
        }

        if (sb2.length() >= 1) {
            Pattern selfDefinePattern = Pattern.compile("[" + sb2.toString() + "]");
            Matcher selfDefineMatcher = selfDefinePattern.matcher(sb.toString());
            if (selfDefineMatcher.find()) {
                return this.putin(sb.toString());
            }
        }
        String s = sb.toString();
        s = s.replace('a','x');
        s = s.replace('b','y');
        s = s.replace('p','z');
        return s;
    }

    private String check(String input, int i, int j) {
        String text = input.substring(i + 2, i + j - 1);
        boolean flag = false;
        Iterator var6 = this.functions.keySet().iterator();
        while (var6.hasNext()) {
            String s = (String)var6.next();
            if (text.contains(s)) {
                flag = true;
            }
        }
        if (flag) {
            text = this.putin(text);
        }
        return text;
    }
}
