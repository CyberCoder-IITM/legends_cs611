import java.util.Set;

/*
 * Market containing all items
 */
public class Market {
    private Set<Item> items;

    public Market(Set<Item> items) {
        this.items = items;
    }

    public Set<Item> getItems() {
        return items;
    }
}
