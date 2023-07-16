import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Library {

    private final BookBase bookBase;
    private final DataBase dataBase;
    private final StageBase stageBase;
    private final ArrayList<Query> queries;
    private final Calender calender;
    private final ReserveManager reserveManager;
    private final OutPut output;
    private Integer index;

    public Library() {
        this.bookBase = new BookBase();
        this.dataBase = new DataBase();
        this.stageBase = new StageBase();
        this.queries = new ArrayList<>();
        this.calender = new Calender();
        this.reserveManager = new ReserveManager();
        this.index = 0;
        this.output = new OutPut();
    }

    public void init(String order) {
        String[] s;
        s = order.split("[- ]");
        int num = Integer.parseInt(s[2]);
        for (int i = 0; i < num; i++) {
            Book book = new Book(s[1], s[0]);
            bookBase.returnBook(book);
        }
    }

    public void putQuery(String str) {
        Query query = new Query(str);
        queries.add(query);
    }

    public void work() {
        int k = queries.get(queries.size() - 1).getDayOfYear();
        for (int i = 1; i <= k; i++) {
            if (calender.tidyTime() && !stageBase.isEmpty()) {
                tidyUp();
            }
            ArrayList<Query> todayQuery = todayQuery();
            if (!todayQuery.isEmpty()) {
                for (Query query : todayQuery) {
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
            }
            calender.tomorrow();
        }
    }

    public void borrowBook(Query query) {
        output.selfServer(calender, query);
        if (!Objects.equals(query.getBookType(), "A")) {
            if (bookBase.selfServer(query)) { //目前有书
                Book book = query.getQueryBook();
                bookBase.lendBook(book);
                if (dataBase.query(query)) { //可以借书
                    output.lendOk(calender, query);
                    if (Objects.equals(query.getBookType(), "B")) {
                        reserveManager.deleteReserveB(query.getId());
                    }
                    dataBase.borrowBook(query);//加记录
                } else { //没借到书
                    stageBase.addBook(book);//放暂存
                }
            } else { //藏书仓库没书，走预订流程
                if (reserveManager.queryReserve(query) && !reserveManager.hasOrdered(query) &&
                    dataBase.query(query)) {
                    reserveManager.addReserveQuery(query);
                    output.reserveOk(calender, query);
                }
            }
        }
    }

    public void lostBook(Query query) {
        spendMoney(query);
        dataBase.returnBook(query);
    }

    public void smearBook(Query query) { //改变数据库里的书的属性
        dataBase.getDestroyed(query);
    }

    public void returnBook(Query query) {
        Book book = query.getQueryBook();
        if (dataBase.queryDestroyed(query)) { //有书坏了
            output.getPunished(calender, query);
            output.returnOk(calender, query);
            fixBook(book);
        } else {
            stageBase.addBook(book);
            output.returnOk(calender, query);
        }
        dataBase.returnBook(query);
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

    public void spendMoney(Query query) {
        output.getPunished(calender, query);
    }

    public void fixBook(Book book) {
        book.getFixed();
        output.getRepaired(calender, book);
        stageBase.addBook(book);
    }

    public void tidyUp() {
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

    private HashMap<Integer, Query> dealTidyBooks(HashMap<Book, Integer> typeC) {
        HashMap<Integer, Query> ans = new HashMap<>();
        for (Map.Entry entry : typeC.entrySet()) {
            Book book = (Book) entry.getKey();
            int num = (Integer) entry.getValue();
            for (int i = 0; i < num; i++) {
                Pair<Integer, Query> pair = reserveManager.dealBook(book);
                if (pair != null) {
                    ans.put(pair.getKey(), pair.getValue());
                    Query query = pair.getValue();
                    dataBase.borrowBook(query);
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
            output.getReservedBooks(calender, ans.get(key));
        }
    }

}
