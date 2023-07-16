public class ReserveRecord extends Record {
    private Integer sum;

    public ReserveRecord(String id) {
        super();
        sum = 0;
    }

    public void addBook(Book book,Calender calender) {
        super.addBook(book, calender);
        sum++;
    }

    public Integer getNum() {
        return this.sum;
    }

}
