public class ReserveRecord extends Record {
    private Integer sum;

    public ReserveRecord(String id) {
        super();
        sum = 0;
    }

    public void addBook(Book book) {
        super.addBook(book);
        sum++;
    }

    public Integer getNum() {
        return this.sum;
    }

}
