import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class City {
    private final HashMap<String, Library> libraries;
    private final Calender calender;
    private ArrayList<Query> queries;
    private ArrayList<Query> tmpBorrowQuery;
    private int index;
    private OutPut output;

    public City(Calender calender) {
        this.queries = new ArrayList<>();
        this.libraries = new HashMap<>();
        this.calender = calender;
        this.tmpBorrowQuery = new ArrayList<>();
        this.index = 0;
        this.output = new OutPut();
    }

    public void addLibrary(Library library, String name) {
        libraries.put(name, library);
    }

    public void work() {
        int k = queries.get(queries.size() - 1).getDayOfYear();
        for (int i = 1; i <= k; i++) {
            for (Map.Entry entry : libraries.entrySet()) {
                Library library = (Library) entry.getValue();
                library.dealOutBook();
            }
            for (Map.Entry entry : libraries.entrySet()) {
                Library library = (Library) entry.getValue();
                ArrayList<Book> bookArrayList = library.getOutReturnList();
                if (!bookArrayList.isEmpty()) {
                    for (Book book : bookArrayList) {
                        String home = book.getHome();
                        output.receiveOk(calender, book, home);
                        libraries.get(home).addOutBook(book);
                    }
                }
                library.outReturnListClear();
            }
            if (calender.tidyTime()) {
                libraryTidy();
            }
            ArrayList<Query> today = todayQuery();
            for (Query query : today) {
                Library library = libraries.get(query.getSchool());
                if (query.isBorrow()) {
                    output.selfServer(calender, query);
                    if (library.borrowOk(query) && !library.haveBook(query)) {
                        tmpBorrowQuery.add(query);
                    } else {
                        library.work(query);
                    }
                } else {
                    library.work(query);
                }
            }
            for (Map.Entry entry : libraries.entrySet()) {
                Library library = (Library) entry.getValue();
                library.setDataBaseTmp();
            }
            for (Query query : tmpBorrowQuery) {
                Library library = libraries.get(query.getSchool());
                if (library.borrowOk(query)) {
                    Book book = queryOutBorrow(query);
                    library.outBorrow(query, book);
                }
            }
            tmpBorrowQuery.clear();
            calender.tomorrow();
        }
    }

    public void putQuery(String str) {
        Query query = new Query(str);
        queries.add(query);
    }

    public ArrayList<Query> todayQuery() {
        ArrayList<Query> queryArrayList = new ArrayList<>();
        int i;
        for (i = index; i < queries.size(); ) {
            int n = queries.get(i).getDayOfYear();
            if (n == calender.getCnt()) {
                queryArrayList.add(queries.get(i));
                i++;
            } else if (n > calender.getCnt()) {
                break;
            } else {
                break;
            }
        }
        this.index = i;
        return queryArrayList;
    }

    public void libraryTidy() {
        for (Map.Entry entry : libraries.entrySet()) {
            Library library = (Library) entry.getValue();
            library.dealNewBook();
        }
        output.arrangeOk(calender);
        for (Map.Entry entry : libraries.entrySet()) {
            Library library = (Library) entry.getValue();
            library.tidyUp();
        }
    }

    public Book queryOutBorrow(Query query) {
        String school = query.getSchool();
        for (Map.Entry entry : libraries.entrySet()) {
            Library library = (Library) entry.getValue();
            String name = (String) entry.getKey();
            if (Objects.equals(name, school)) {
                continue;
            }
            Book book = library.queryOutBorrow(query);
            if (book != null) {
                return book;
            }
        }
        return null;
    }

}
