import java.util.HashMap;

public class DataBase {
    //记录学生的借阅记录
    private final HashMap<String, Record> studentRecords;

    public DataBase() {
        this.studentRecords = new HashMap<>();
    }

    public void borrowBook(Query query) {
        Record record;
        String id = query.getId();
        Book book = query.getQueryBook();
        if (!studentRecords.containsKey(id)) {
            record = new Record();
            studentRecords.put(id, record);
        } else {
            record = studentRecords.get(id);
        }
        record.addBook(book);
    }

    public void returnBook(Query query) {
        Record record = studentRecords.get(query.getId());
        record.subBook(query.getQueryBook());
    }

    public boolean query(Query query) {
        if (!studentRecords.containsKey(query.getId())) {
            return true;
        }
        Record record = studentRecords.get(query.getId());
        return record.queryRecordOk(query);
    }

    public void getDestroyed(Query query) {
        studentRecords.get(query.getId()).getBook(query).getDestroyed();
    }

    public boolean queryDestroyed(Query query) {
        Record record = studentRecords.get(query.getId());
        Book book = record.getBook(query);
        return book.queryDestroy();
    }

}
