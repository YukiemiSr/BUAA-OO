import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Record {
    private final HashMap<String, ArrayList<Book>> hasLend;
    private HashMap<String, Integer> btimelist;
    private HashMap<String, Integer> ctimelist;

    public Record() { //一个是借书记录，一个是预订记录
        hasLend = new HashMap<>();
        hasLend.put("A", new ArrayList<>());
        hasLend.put("B", new ArrayList<>());
        hasLend.put("C", new ArrayList<>());
        this.btimelist = new HashMap<>();
        this.ctimelist = new HashMap<>();
    }

    public void addBook(Book book, Calender calender) {
        ArrayList<Book> lendMap = hasLend.get(book.getType());
        lendMap.add(book);
        if (Objects.equals(book.getType(), "B")) {
            btimelist.put(book.getName(), calender.getCnt());
        } else {
            ctimelist.put(book.getName(), calender.getCnt());
        }
    }

    public void subBook(Book book) {
        Book book2 = null;
        ArrayList<Book> lendMap = hasLend.get(book.getType());
        for (Book book1 : lendMap) {
            if (book.equals(book1)) {
                book2 = book1;
                break;
            }
        }
        if (book2 != null) {
            lendMap.remove(book2);
        }
        if (Objects.equals(book.getType(), "B")) {
            btimelist.remove(book.getName());
        } else {
            ctimelist.remove(book.getName());
        }
    }

    public boolean queryRecordOk(Query query) {
        if (Objects.equals(query.getBookType(), "C")) {
            ArrayList<Book> bookList = hasLend.get("C");
            if (bookList.isEmpty()) {
                return true;
            }
            for (Book book : bookList) {
                if (query.getQueryBook().equals(book)) {
                    return false;
                }
            }
            return true;
        } else {
            ArrayList<Book> bookList = hasLend.get("B");
            return bookList.isEmpty();
        }
    }

    public boolean queryReserveOk(Query query) {
        ArrayList<Book> bookList;
        if (Objects.equals(query.getBookType(), "C")) {
            bookList = hasLend.get("C");
        } else {
            bookList = hasLend.get("B");
        }
        if (bookList.isEmpty()) {
            return true;
        }
        for (Book book : bookList) {
            if (query.getQueryBook().equals(book)) {
                return false;
            }
        }
        return true;
    }

    public Book getBook(Query query) {
        for (Book book : hasLend.get(query.getBookType())) {
            if (book.equals(query.getQueryBook())) {
                return book;
            }
        }
        return null;
    }

    public Record clone() {
        Record record = new Record();
        ArrayList<Book> books = getBookList("B");
        ArrayList<Book> book1 = new ArrayList<>();
        for (Book book : books) {
            book1.add(book.clone());
        }
        record.setBookList("B", book1);
        books = getBookList("C");
        book1 = new ArrayList<>();
        for (Book book : books) {
            book1.add(book.clone());
        }
        record.setBookList("C", book1);
        record.setTimeList(this.btimelist, "B");
        record.setTimeList(this.ctimelist, "C");
        return record;
    }

    public ArrayList<Book> getBookList(String s) {
        return hasLend.get(s);
    }

    public void setBookList(String s, ArrayList<Book> books) {
        this.hasLend.put(s, books);
    }

    public void setTimeList(HashMap<String, Integer> map, String type) {
        HashMap<String, Integer> s1 = new HashMap<>();
        if (Objects.equals(type, "B")) {
            for (Map.Entry entry : map.entrySet()) {
                String s = (String) entry.getKey();
                Integer x = (Integer) entry.getValue();
                s1.put(s, x);
            }
            this.btimelist = s1;
        } else {
            for (Map.Entry entry : map.entrySet()) {
                String s = (String) entry.getKey();
                Integer x = (Integer) entry.getValue();
                s1.put(s, x);
            }
            this.ctimelist = s1;
        }
    }

    public Integer getDate(Query query) {
        String name = query.getQueryBook().getName();
        String type = query.getBookType();
        if (Objects.equals(type, "B")) {
            return btimelist.get(name);
        } else {
            return ctimelist.get(name);
        }
    }
}
