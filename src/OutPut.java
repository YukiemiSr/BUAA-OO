import java.util.Objects;

public class OutPut {
    public void selfServer(Calender calender, Query query) {
        System.out.println(calender + " "
            + query.getId() + " queried " + query.getQueryBook() +
            " from self-service machine");
    }

    public void lendOk(Calender calender, Query query) {
        if (Objects.equals(query.getBookType(), "C")) {
            System.out.println(calender + " "
                + query.getId() + " borrowed " + query.getQueryBook() +
                " from self-service machine");
        } else {
            System.out.println(calender + " "
                + query.getId() + " borrowed " + query.getQueryBook() +
                " from borrowing and returning librarian");
        }
    }

    public void reserveOk(Calender calender, Query query) {
        System.out.println(calender + " "
            + query.getId() + " ordered " + query.getQueryBook() +
            " from ordering librarian");
    }

    public void getPunished(Calender calender, Query query) {
        System.out.println(calender + " "
            + query.getId() + " got punished by" +
            " borrowing and returning librarian");
    }

    public void returnOk(Calender calender, Query query) {
        if (Objects.equals(query.getBookType(), "C")) {
            System.out.println(calender + " "
                + query.getId() + " returned " + query.getQueryBook() +
                " to self-service machine");
        } else {
            System.out.println(calender + " "
                + query.getId() + " returned " + query.getQueryBook() +
                " to borrowing and returning librarian");
        }
    }

    public void getRepaired(Calender calender, Book book) {
        System.out.println(calender + " "
            + book + " got repaired by " +
            "logistics division");
    }

    public void getReservedBooks(Calender calender, Query query) {
        System.out.println(calender + " "
            + query.getId() + " borrowed " + query.getQueryBook() +
            " from ordering librarian");
    }
}
