import java.util.HashMap;
import java.util.Map;

public class StageBase {
    private final HashMap<String, HashMap<Book, Integer>> books;

    public StageBase() {
        books = new HashMap<>();
        books.put("A", new HashMap<>());
        books.put("B", new HashMap<>());
        books.put("C", new HashMap<>());
    }

    public void addBook(Book book) {
        HashMap<Book, Integer> bookMap = books.get(book.getType());
        int sym = 0;
        int num = 0;
        Book book1 = null;
        for (Map.Entry entry : bookMap.entrySet()) {
            if ((entry.getKey()) == book) {
                sym = 1;
                book1 = (Book) entry.getKey();
                num = (Integer) entry.getValue();
            }
        }
        if (sym == 0) {
            bookMap.put(book, 1);
        } else {
            bookMap.put(book1, num);
        }
    }

    public HashMap<String, HashMap<Book, Integer>> getBooks() {
        return books;
    }

    public void emptyStageBooks() {
        HashMap<Book, Integer> bookMap = books.get("A");
        bookMap.clear();
        bookMap = books.get("B");
        bookMap.clear();
        bookMap = books.get("C");
        bookMap.clear();

    }

    public boolean isEmpty() {
        return books.get("B").isEmpty() && books.get("C").isEmpty();
    }
}
