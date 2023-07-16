import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ReserveManager {
    private final HashMap<String, ReserveRecord> reserveRecord; //记录一天的数量
    private ArrayList<Query> querySummary;
    private HashMap<Query, Integer> queryIntegerHashMap;
    private int cnt = 0;

    public ReserveManager() {
        querySummary = new ArrayList<>();
        reserveRecord = new HashMap<>();
        this.queryIntegerHashMap = new HashMap<>();
    }

    public void addReserveQuery(Query query) {
        querySummary.add(query);
        queryIntegerHashMap.put(query, cnt++);
        String id = query.getId();
        if (reserveRecord.containsKey(id)) {
            reserveRecord.get(id).addBook(query.getQueryBook());
        } else {
            ReserveRecord record = new ReserveRecord(id);
            record.addBook(query.getQueryBook());
            reserveRecord.put(id, record);
        }
    }

    public boolean queryReserve(Query query) {
        if (reserveRecord.containsKey(query.getId())) {
            ReserveRecord record = reserveRecord.get(query.getId());
            if (record.getNum() >= 3) {
                return false;
            }
            return record.queryReserveOk(query);
        }
        return true;
    }

    public Pair<Integer, Query> dealBook(Book book) {
        Query query1 = null;
        String id = null;
        int k = -2;
        for (Query query : querySummary) {
            if (query.getQueryBook().equals(book)) {
                id = query.getId();
                query1 = query;
                k = queryIntegerHashMap.get(query);
                break;
            }
        }
        if (id != null) {
            if (Objects.equals(book.getType(), "B")) {
                deleteReserveB(id);
            } else {
                ArrayList<Query> queryArrayList = new ArrayList<>();
                for (Query query : querySummary) {
                    if (!(Objects.equals(query.getId(), id) &&
                        query.getQueryBook().equals(book))) {
                        queryArrayList.add(query);
                    }
                }
                querySummary = queryArrayList;
            }
        }
        if (query1 == null) {
            return null;
        }
        return new Pair<>(k, query1);
    }

    public void deleteReserveB(String id) {
        ArrayList<Query> queryArrayList = new ArrayList<>();
        for (Query query : querySummary) {
            if (!(Objects.equals(query.getId(), id) &&
                Objects.equals(query.getQueryBook().getType(), "B"))) {
                queryArrayList.add(query);
            }
        }
        querySummary = queryArrayList;
    }

    public boolean hasOrdered(Query query) {
        for (Query query1 : querySummary) {
            if (query.getQueryBook().equals(query1.getQueryBook()) &&
                Objects.equals(query.getId(), query1.getId())) {
                return true;
            }
        }
        return false;
    }

    public void dailyClear() {
        reserveRecord.clear();
    }
}
