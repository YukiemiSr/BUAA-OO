import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Library {

    private final String name;
    private final BookBase bookBase;
    private final StageBase stageBase;
    private final Calender calender;
    private final HashMap<String, Record> outRecord;
    private final ReserveManager reserveManager;
    private final OutPut output;
    private final ArrayList<Pair<Query, Book>> tomorrowOutput;
    private final ArrayList<Query> newBook;
    private final ArrayList<Book> outReturnList;
    private int haveBook;
    private int borrowOk;
    private int judge;
    private DataBase dataBase;
    private DataBase dataBaseTmp;

    public Library(String name, City city, Calender calender) {
        this.tomorrowOutput = new ArrayList<>();
        this.calender = calender;
        this.name = name;
        this.bookBase = new BookBase();
        this.dataBase = new DataBase();
        this.stageBase = new StageBase();
        this.reserveManager = new ReserveManager();
        this.output = new OutPut();
        this.outRecord = new HashMap<>();
        this.newBook = new ArrayList<>();
        this.outReturnList = new ArrayList<>();
        this.dataBaseTmp = new DataBase();
    }

    public void addInitBook(String order) {
        String[] s;
        s = order.split("[- ]");
        int num = Integer.parseInt(s[2]);
        boolean judge;
        judge = !Objects.equals(s[3], "N");
        for (int i = 0; i < num; i++) {
            Book book = new Book(s[1], s[0], judge, name);
            bookBase.returnBook(book);
        }
    }

    public boolean haveBook(Query query) {
        return bookBase.selfServer(query);
    }

    public void work(Query query) {
        if (query.isBorrow()) {
            borrowBook(query);
        }
        if (query.isLost()) {
            lostBook(query);
        }
        if (query.isSmeared()) {
            smearBook(query);
        }
        if (query.isReturned()) {
            returnBook(query);
        }
    }

    public boolean borrowOk(Query query) {
        return dataBaseTmp.query(query);
    }

    public void outBorrow(Query query, Book book) {
        if (book != null) {
            String name = query.getId();
            Calender calender1 = new Calender();
            calender1.setCnt(calender.getCnt() + 1);
            if (!outRecord.containsKey(name)) {
                Record record = new Record();
                outRecord.put(name, record);
                record.addBook(book, calender1);
            } else {
                outRecord.get(name).addBook(book, calender1);
            }
            dataBaseTmp.borrowBook(query, calender1);
            tomorrowOutput.add(new Pair<>(query, book));
            output.transportOk(calender, book, book.getHome());
            output.stateTransfer(calender, book, "OwnLibrary", "tmp");
        } else { //走预定
            if (reserveManager.queryReserve(query) && !reserveManager.hasOrdered(query) &&
                dataBase.query(query)) {
                reserveManager.addReserveQuery(query,calender);
                output.reserveOk(calender, query);
                output.record(calender, query, name);
                if (!bookBase.queryBookExist(query)) {
                    newBook.add(query);
                }
            }
        }
    }

    public void borrowBook(Query query) {
        if (!Objects.equals(query.getBookType(), "A")) {
            if (bookBase.selfServer(query)) { //目前有书
                Book book = query.getQueryBook();
                bookBase.lendBook(book);
                if (dataBase.query(query)) { //可以借书
                    output.lendOk(calender, query);
                    output.stateTransfer(calender, book, "OwnLibrary", "borrowByOwnReader");
                    if (Objects.equals(query.getBookType(), "B")) {
                        reserveManager.deleteReserveB(query.getId());
                    }
                    dataBase.borrowBook(query, calender);//加记录
                    dataBaseTmp.borrowBook(query, calender);
                } else { //没借到书
                    stageBase.addBook(book);//放暂存
                    output.refuseLend(calender, query, query.getQueryBook());
                    output.stateTransfer(calender, book, "OwnLibrary", "OwnStateBase");
                }
            }
        }
    }

    public void lostBook(Query query) {
        spendMoney(query);
        dataBase.returnBook(query);
        dataBaseTmp.returnBook(query);
    }

    public void smearBook(Query query) {
        dataBase.getDestroyed(query);
    }

    public void returnBook(Query query) {
        Book book = query.getQueryBook();
        if (dataBase.queryLate(calender, query)) {
            output.getPunished(calender, query);
        }
        if (dataBase.queryDestroyed(query)) {
            output.getPunished(calender, query);
            if (outRecord.containsKey(query.getId())) {
                Book book1 = outRecord.get(query.getId()).getBook(query);
                if (book1 != null) {
                    book1.getFixed();
                    output.returnOk(calender, query, book1);
                    output.stateTransfer(calender, book1, "borrowByAnotherReader",
                        "anotherLibrary");
                    output.getRepaired(calender, book1, name);
                    output.stateTransfer(calender, book1, "anotherLibrary", "anotherLibrary");
                    output.transportOk(calender, book1, name);
                    output.stateTransfer(calender, book1, "anotherLibrary", "tmp");
                    outReturnList.add(book1);
                    Record record = outRecord.get(query.getId());
                    record.subBook(book);
                } else {
                    output.returnOk(calender, query, null);
                    output.stateTransfer(calender, book, "borrowByAnotherReader", "anotherLibrary");
                    fixBook(book);
                }
            } else {
                output.returnOk(calender, query, null);
                output.stateTransfer(calender, book, "borrowByOwnReader", "OwnLibrary");
                fixBook(book);
            }
        } else {
            if (outRecord.containsKey(query.getId())) {
                Book book1 = outRecord.get(query.getId()).getBook(query);
                if (book1 != null) {
                    outReturnList.add(book1);
                    output.returnOk(calender, query, book1);
                    output.stateTransfer(calender, book, "borrowByAnotherReader", "anotherLibrary");
                    output.transportOk(calender, book1, name);
                    output.stateTransfer(calender, book1, "anotherLibrary", "tmp");
                    Record record = outRecord.get(query.getId());
                    record.subBook(book);
                } else {
                    output.returnOk(calender, query, book1);
                    stageBase.addBook(book);
                }
            } else {
                output.returnOk(calender, query, null);
                output.stateTransfer(calender, book, "borrowByOwnReader", "OwnLibrary");
                stageBase.addBook(book);
            }
        }
        dataBaseTmp.returnBook(query);
        dataBase.returnBook(query);
    }

    public void spendMoney(Query query) {
        output.getPunished(calender, query);
    }

    public void fixBook(Book book) {
        book.getFixed();
        output.getRepaired(calender, book, name);
        output.stateTransfer(calender, book, "OwnDestroy", "OwnStateBase");
        stageBase.addBook(book);
    }

    public void tidyUp() {
        if (calender.tidyTime() && !stageBase.isEmpty()) {
            reserveManager.dailyClear();
            HashMap<String, HashMap<Book, Integer>> books = stageBase.getBooks();
            HashMap<Book, Integer> typeB = books.get("B");
            HashMap<Book, Integer> typeC = books.get("C");
            HashMap<Integer, Query> s = new HashMap<>();
            s.putAll(dealTidyBooks(typeB));
            s.putAll(dealTidyBooks(typeC));
            outReserve(s);
            stageBase.emptyStageBooks();
        }
    }

    private HashMap<Integer, Query> dealTidyBooks(HashMap<Book, Integer> typeC) {
        HashMap<Integer, Query> ans = new HashMap<>();
        for (Map.Entry entry : typeC.entrySet()) {
            Book book = (Book) entry.getKey();
            int num = (Integer) entry.getValue();
            for (int i = 0; i < num; i++) {
                Pair<Integer, Query> pair = reserveManager.dealBook(book, dataBase);
                if (pair != null) {
                    ans.put(pair.getKey(), pair.getValue());
                    Query query = pair.getValue();
                    dataBase.borrowBook(query, calender);
                    dataBaseTmp.borrowBook(query, calender);
                } else {
                    bookBase.returnBook(book);
                }
            }
        }
        return ans;
    }

    private void outReserve(HashMap<Integer, Query> ans) {
        Integer[] arr = ans.keySet().toArray(new Integer[0]);
        Arrays.sort(arr);
        for (Integer key : arr) {
            output.getReservedBooks(calender, ans.get(key), name);
            output.stateTransfer(calender, ans.get(key).getQueryBook(), "OwnLibrary",
                "borrowByOwnReader");
        }
    }

    public Book queryOutBorrow(Query query) {
        if (bookBase.queryOutBorrow(query)) {
            return new Book(query.getQueryBook().getName(), query.getBookType(), true, name);
        }
        return null;
    }

    public void dealOutBook() {
        dataBase = dataBaseTmp.clone();
        for (Pair pair : tomorrowOutput) {
            output.receiveOk(calender, (Book) pair.getValue(), name);
            output.stateTransfer(calender, (Book) pair.getValue(), "tmp", "anotherLibrary");
        }
        for (Pair pair : tomorrowOutput) {
            output.lendOutOk(calender, (Query) pair.getKey(), (Book) pair.getValue());
            output.stateTransfer(calender, (Book) pair.getValue(), "anotherLibrary",
                "borrowByAnotherReader");
        }
        tomorrowOutput.clear();
    }

    public void dealNewBook() {
        HashMap<String, Integer> s = new HashMap<>();
        for (Query query : newBook) {
            Book book = query.getQueryBook();
            String name = book.toString();
            if (!s.containsKey(name)) {
                s.put(name, 1);
            } else {
                int num = s.get(name);
                s.put(name, num + 1);
            }
        }
        for (Map.Entry entry : s.entrySet()) {
            int num = (Integer) entry.getValue();
            if (num < 3) {
                num = 3;
            }
            output.buyOk(calender, ((String) entry.getKey()), name);
            String[] s1 = ((String) entry.getKey()).split("-");
            for (int i = 0; i < num; i++) {
                Book book = new Book(s1[1], s1[0], true, name);
                stageBase.addBook(book);
            }
        }
        newBook.clear();
    }

    public ArrayList<Book> getOutReturnList() {
        return this.outReturnList;
    }

    public void addOutBook(Book book) {
        output.stateTransfer(calender, book, "tmp", "OwnLibrary");
        this.stageBase.addBook(book);
    }

    public void outReturnListClear() {
        this.outReturnList.clear();
    }

    public void setDataBaseTmp() {
        dataBaseTmp = dataBase.clone();
    }

    public void orderNewBook() {

    }

    public void getOrderedBook() {

    }
}
