import java.util.Objects;

public class OutPut {
    public void selfServer(Calender calender, Query query) {
        System.out.println(calender + " " + query.getSchool() + "-"
            + query.getId() + " queried " + query.getQueryBook() +
            " from self-service machine");
        System.out.println(
            calender + " self-service machine provided information of " + query.getQueryBook());
    }

    public void lendOk(Calender calender, Query query) {
        if (Objects.equals(query.getBookType(), "C")) {
            System.out.println(
                calender + " " + "self-service machine lent " + query.getSchool() + "-"
                    + query.getQueryBook() + " to " + query.getSchool() + "-"
                    + query.getId());
            System.out.println(calender + " " + query.getSchool() + "-"
                + query.getId() + " borrowed " + query.getSchool() + "-" + query.getQueryBook() +
                " from self-service machine");

        } else {
            System.out.println(
                calender + " " + "borrowing and returning librarian lent " + query.getSchool() + "-"
                    + query.getQueryBook() + " to " + query.getSchool() + "-"
                    + query.getId());
            System.out.println(calender + " " + query.getSchool() + "-"
                + query.getId() + " borrowed " + query.getSchool() + "-" + query.getQueryBook() +
                " from borrowing and returning librarian");
        }
    }

    public void lendOutOk(Calender calender, Query query, Book book) {
        System.out.println(calender + " " + "purchasing department lent " + book.getHome() + "-"
            + book + " to " + query.getSchool() + "-" + query.getId());
        System.out.println(calender + " " + query.getSchool() + "-"
            + query.getId() + " borrowed " + book.getHome() + "-" + query.getQueryBook() +
            " from " +
            "purchasing department");
    }

    public void reserveOk(Calender calender, Query query) {
        System.out.println(calender + " " + query.getSchool() + "-"
            + query.getId() + " ordered " + query.getSchool() + "-" + query.getQueryBook() +
            " from ordering librarian");
    }

    public void getPunished(Calender calender, Query query) {
        System.out.println(calender + " " + query.getSchool() + "-"
            + query.getId() + " got punished by" +
            " borrowing and returning librarian");
        System.out.println(calender + " borrowing and returning librarian received "
            + query.getSchool() + "-" + query.getId() + "'s fine");
    }

    public void returnOk(Calender calender, Query query, Book book) {
        if (book != null) {
            if (Objects.equals(query.getBookType(), "C")) {
                System.out.println(calender + " " + query.getSchool() + "-"
                    + query.getId() + " returned " + book.getHome() + "-" + book +
                    " to self-service machine");
                System.out.println(
                    calender + " self-service machine collected " + book.getHome() + "-"
                        + query.getQueryBook() + " from " + query.getSchool() + "-"
                        + query.getId());
            } else {
                System.out.println(calender + " " + query.getSchool() + "-"
                    + query.getId() + " returned " + book.getHome() + "-" + query.getQueryBook() +
                    " to borrowing and returning librarian");
                System.out.println(
                    calender + " borrowing and returning librarian collected " + book.getHome() +
                        "-"
                        + query.getQueryBook() + " from " + query.getSchool() + "-"
                        + query.getId());
            }
        } else {
            if (Objects.equals(query.getBookType(), "C")) {
                System.out.println(calender + " " + query.getSchool() + "-"
                    + query.getId() + " returned " + query.getSchool() + "-" +
                    query.getQueryBook() +
                    " to self-service machine");
                System.out.println(
                    calender + " self-service machine collected " + query.getSchool() + "-"
                        + query.getQueryBook() + " from " + query.getSchool() + "-"
                        + query.getId());
            } else {
                System.out.println(calender + " " + query.getSchool() + "-"
                    + query.getId() + " returned " + query.getSchool() + "-" +
                    query.getQueryBook() +
                    " to borrowing and returning librarian");
                System.out.println(
                    calender + " borrowing and returning librarian collected " + query.getSchool() +
                        "-"
                        + query.getQueryBook() + " from " + query.getSchool() + "-"
                        + query.getId());
            }
        }
    }

    public void getRepaired(Calender calender, Book book, String name) {
        System.out.println(calender + " " + book.getHome() + "-"
            + book + " got repaired by " +
            "logistics division" + " in " + name);
    }

    public void getReservedBooks(Calender calender, Query query, String name) {
        System.out.println(
            calender + " " + "ordering librarian lent " + name + "-" + query.getQueryBook()
                + " to " + name + "-" + query.getId());
        System.out.println(calender + " "
            + name + "-" + query.getId() + " borrowed " + name + "-" + query.getQueryBook() +
            " from ordering librarian");
    }

    public void buyOk(Calender calender, String s, String name) {
        System.out.println(
            calender + " " + name + "-" + s + " got purchased by purchasing department in " + name);
    }

    public void receiveOk(Calender calender, Book book, String name) {
        System.out.println(calender + " " + book.getHome() + "-"
            + book + " got received by purchasing department in " + name);
    }

    public void transportOk(Calender calender, Book book, String name) {
        System.out.println(calender + " " + book.getHome() + "-"
            + book + " got transported by purchasing department in " + name);
    }

    public void arrangeOk(Calender calender) {
        System.out.println(calender + " " + "arranging librarian arranged all the books");
    }

    public void record(Calender calender, Query query, String name) {
        System.out.println(calender + " ordering librarian recorded "
            + name + "-" + query.getId() + "'s order of " + name + "-" + query.getQueryBook());
    }

    public void refuseLend(Calender calender, Query query, Book book) {
        if (Objects.equals(book.getType(), "C")) {
            System.out.println(
                calender + " self-service machine refused lending " + book.getHome() + "-" + book +
                    " to " + query.getSchool() + "-" + query.getId());
        } else {
            System.out.println(
                calender + " borrowing and returning librarian refused lending " + book.getHome() +
                    "-" + book +
                    " to " + query.getSchool() + "-" + query.getId());
        }
    }

    public void stateTransfer(Calender calender, Book book, String old, String ne) {
        System.out.println("(State) " + calender + " " + book +
            " transfers from " + old + " to " + ne);
    }
}
