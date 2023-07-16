import java.util.HashMap;
import java.util.Objects;

public class BookBase {
    private final HashMap<String, HashMap<String, Integer>> books;
    private final HashMap<String, String> bookState;

    public BookBase() {
        books = new HashMap<>();
        books.put("A", new HashMap<>());
        books.put("B", new HashMap<>());
        books.put("C", new HashMap<>());
        this.bookState = new HashMap<>();
    }

    public void returnBook(Book book) {
        HashMap<String, Integer> bookMap = books.get(book.getType());
        if (!bookMap.containsKey(book.getName())) {
            if (book.getExchange()) {
                bookState.put(book.getName(), "true");
            } else {
                bookState.put(book.getName(), "false");
            }
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

    public boolean queryBookExist(Query query) {
        String type = query.getBookType();
        HashMap<String, Integer> bookList = books.get(type);
        return bookList.containsKey(query.getQueryBook().getName());
    }

    public boolean queryOutBorrow(Query query) {
        String type = query.getBookType();
        HashMap<String, Integer> bookList = books.get(type);
        String name = query.getQueryBook().getName();
        if (bookList.containsKey(name)) {
            if (bookList.get(name) > 0 && Objects.equals(bookState.get(name), "true")) {
                lendBook(query.getQueryBook());
                return true;
            }
        }
        return false;
    }
}
