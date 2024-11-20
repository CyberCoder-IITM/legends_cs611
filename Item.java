/*
 * An item
 */
import java.util.*;
public abstract class Item {
    private String name;
    private ItemType type;
    private double price;
    private int levelRequirement;

    public Item(String name, ItemType type, double price, int levelRequirement) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.levelRequirement = levelRequirement;
    }

    // Basic getters
    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    // Abstract methods that must be implemented by subclasses
    protected abstract boolean action(Hero hero);
    protected abstract double damage();

    // New methods for Valor mechanics
    public boolean canUse(Hero hero) {
        // Check if hero meets level requirement
        if (hero.getLevel() < levelRequirement) {
            return false;
        }

        // Check if hero is in combat
        if (hero.isInCombat()) {
            return type == ItemType.POTION || type == ItemType.SPELL;
        }

        // Check if hero is in nexus for market items
        if (type == ItemType.WEAPON || type == ItemType.ARMOR) {
            return !hero.getCurrentPosition().isHeroNexus();
        }

        return true;
    }

    public String getItemInfo() {
        StringBuilder info = new StringBuilder();
        info.append(String.format("%-20s | ", name));
        info.append(String.format("Type: %-8s | ", type));
        info.append(String.format("Price: %-6.0f | ", price));
        info.append(String.format("Level Req: %d", levelRequirement));

        // Add specific item details based on type
        switch(type) {
            case WEAPON:
                info.append(String.format(" | Damage: %.0f", damage()));
                break;
            case ARMOR:
                info.append(String.format(" | Defense: %.0f", damage()));
                break;
            case POTION:
                info.append(" | Single use");
                break;
            case SPELL:
                info.append(String.format(" | Mana cost: %.0f", damage()));
                break;
        }

        return info.toString();
    }

    public double getSellPrice() {
        return price * 0.5; // Items sell for half their purchase price
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return name.equals(item.name) && type == item.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}