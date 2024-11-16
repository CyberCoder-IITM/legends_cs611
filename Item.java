/*
 * An item
 */
public abstract class Item {
    String name;
    ItemType type;
    double price;

    public Item(String name, ItemType type, double price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    protected abstract boolean action(Hero hero);

    protected abstract double damage();

}