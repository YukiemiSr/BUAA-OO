import java.util.Objects;

public class Book {
    private final String name;
    private final String type;
    private boolean destroy;

    public Book(String name, String type) {
        this.name = name;
        this.type = type;
        this.destroy = false;
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

    public String toString() {
        return type + "-" + name;
    }
}
