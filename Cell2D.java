
/*
 * A cell in a 2D Board
 */
public class Cell2D<T> {
    private T c;

    // getter function to get the private member c
    public T getValue() {
        return c;
    }

    // constructor
    public Cell2D(T c) {
        this.c = c;
    }

    // to string method
    public String toString() {
        return c.toString();
    }

    // setter function
    public void set(T c) {
        this.c = c;
    }

    // check if the current object's value is equal to the input
    public boolean has(Object o) {
        return c.equals(o);
    }
}
