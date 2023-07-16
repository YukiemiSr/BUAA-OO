import java.util.Objects;

public class Query {

    private final String aim;
    private final Book queryBook;
    private String date;
    private String id;

    public Query(String s) {
        String[] s1 = s.split(" ");
        date = s1[0].substring(1, s1[0].length() - 1);
        aim = s1[2];
        id = s1[1];
        String[] s2 = s1[3].split("-");
        queryBook = new Book(s2[1], s2[0]);
    }

    public String getId() {
        return id;
    }

    public String getBookType() {
        return this.queryBook.getType();
    }

    public Book getQueryBook() {
        return this.queryBook;
    }

    public boolean isBorrow() {
        return Objects.equals(this.aim, "borrowed");
    }

    public boolean isSmeared() {
        return Objects.equals(this.aim, "smeared");
    }

    public boolean isLost() {
        return Objects.equals(this.aim, "lost");
    }

    public boolean isReturned() {
        return Objects.equals(this.aim, "returned");
    }

    public int getDayOfYear() {
        String[] s = date.split("-");
        int month = Integer.parseInt(s[1]);
        int day = Integer.parseInt(s[2]);
        int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int dayOfYear = 0;
        for (int i = 1; i < month; i++) {
            dayOfYear += daysOfMonth[i - 1];
        }
        dayOfYear += day;
        return dayOfYear;
    }

}
