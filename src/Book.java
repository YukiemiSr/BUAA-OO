import java.util.Objects;

public class Book {
    private final String name;
    private final String type;
    private final boolean exchange;
    private final String home;
    private boolean destroy;

    public Book(String name, String type, boolean exchange, String home) {
        this.name = name;
        this.type = type;
        this.destroy = false;
        this.exchange = exchange;
        this.home = home;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(this.name, ((Book) obj).getName()) &&
            Objects.equals(this.type, ((Book) obj).getType());
    }

    public void getDestroyed() {
        this.destroy = true;
    }

    public boolean queryDestroy() {
        return this.destroy;
    }

    public void getFixed() {
        this.destroy = false;
    }

    public boolean getExchange() {
        return this.exchange;
    }

    public String getHome() {
        return this.home;
    }

    public String toString() {
        return type + "-" + name;
    }

    public void setDestroy(boolean state) {
        this.destroy = state;
    }

    public Book clone() {
        Book book = new Book(name, type, exchange, home);
        book.setDestroy(destroy);
        return book;
    }
}
