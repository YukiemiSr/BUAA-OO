import java.util.Scanner;

public class MainClass {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        Library library = new Library();
        for (int i = 0; i < n; i++) {
            String s = scanner.nextLine();
            library.init(s);
        }
        n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; i++) {
            library.putQuery(scanner.nextLine());
        }
        library.work();
    }
}
