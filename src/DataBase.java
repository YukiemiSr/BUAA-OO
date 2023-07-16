import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataBase {
    //记录学生的借阅记录
    private HashMap<String, Record> studentRecords;

    public DataBase() {
        this.studentRecords = new HashMap<>();
    }


    public void borrowBook(Query query, Calender calender) {
        Record record;
        String id = query.getId();
        Book book = query.getQueryBook();
        if (!studentRecords.containsKey(id)) {
            record = new Record();
            studentRecords.put(id, record);
        } else {
            record = studentRecords.get(id);
        }
        record.addBook(book, calender);
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

    public void setStudentRecords(HashMap<String, Record> s1) {
        this.studentRecords = s1;
    }

    public DataBase clone() {
        DataBase dataBase = new DataBase();
        HashMap<String, Record> s1 = new HashMap<>();
        for (Map.Entry entry : studentRecords.entrySet()) {
            s1.put((String) entry.getKey(), ((Record) entry.getValue()).clone());
        }
        dataBase.setStudentRecords(s1);
        return dataBase;
    }

    public boolean queryLate(Calender calender, Query query) {
        Integer x = studentRecords.get(query.getId()).getDate(query);
        Integer now = calender.getCnt();
        if (Objects.equals(query.getBookType(), "C")) {
            return now - x > 60;
        } else {
            return now - x > 30;
        }
    }
}
