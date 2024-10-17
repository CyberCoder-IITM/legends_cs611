
/*
 * A cell in a 2D Board
 */
public class Cell<T> {
    private T c;

    public T getValue() {
        return c;
    }

    public Cell(T c) {
        this.c = c;
    }

    public String toString() {
        return c.toString();
    }

    public void set(T c) {
        this.c = c;
    }

    public boolean has(Object o) {
        return c.equals(o);
    }
}
