import java.util.Scanner;

public class MainClass {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        Calender calender = new Calender();
        City city = new City(calender);
        for (int i = 0; i < n; i++) {
            String s = scanner.nextLine();
            String[] s1 = s.split(" ");
            Library library = new Library(s1[0], city, calender);
            city.addLibrary(library, s1[0]);
            int num = Integer.parseInt(s1[1]);
            for (int j = 0; j < num; j++) {
                s = scanner.nextLine();
                library.addInitBook(s);
            }
        }
        n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; i++) {
            city.putQuery(scanner.nextLine());
        }
        city.work();
    }
}
