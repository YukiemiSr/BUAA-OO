import java.util.HashMap;

public class BookBase {
    private final HashMap<String, HashMap<String, Integer>> books;

    public BookBase() {
        books = new HashMap<>();
        books.put("A", new HashMap<>());
        books.put("B", new HashMap<>());
        books.put("C", new HashMap<>());
    }

    public void returnBook(Book book) {
        HashMap<String, Integer> bookMap = books.get(book.getType());
        if (!bookMap.containsKey(book.getName())) {
            bookMap.put(book.getName(), 1);
        } else {
            int num = bookMap.get(book.getName());
            bookMap.put(book.getName(), num + 1);
        }
    }

    public void lendBook(Book book) {
        HashMap<String, Integer> bookMap = books.get(book.getType());
        int num = bookMap.get(book.getName());
        bookMap.put(book.getName(), num - 1);
    }

    public boolean selfServer(Query query) {
        String type = query.getBookType();
        HashMap<String, Integer> bookList = books.get(type);
        if (bookList.containsKey(query.getQueryBook().getName())) {
            return bookList.get(query.getQueryBook().getName()) > 0;
        } else {
            return false;
        }
    }
}
