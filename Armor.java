/*
 * An Armor
 */

public class Armor extends Item {
    private int level;
    private double defense;

    public Armor(String name, double price, int level, double defense) {
        super(name, ItemType.ARMOR, price,level);
        this.level = level;
        this.defense = defense;
    }

    public int getLevel() {
        return level;
    }

    public double getDefense() {
        return defense;
    }

    @Override
    protected boolean action(Hero hero) {
        return hero.equip(this);
    }

    @Override
    protected double damage() {
        return defense;
    }

    public boolean canEquip(Hero hero) {
        return hero.getLevel() >= level;
    }

    @Override
    public String toString() {
        return String.format("%s (Level %d) | Defense: %.0f | Price: %.0f",
                getName(),
                level,
                defense,
                getPrice()
        );
    }

    public Armor clone() {
        return new Armor(
                getName(),
                getPrice(),
                level,
                defense
        );
    }
}