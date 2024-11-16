/*
 * A weapon
 */
public class Weapon extends Item {
    private int level;
    private double damage;
    private int hands;

    public Weapon(String name, double price, int level, double damage, int hands) {
        super(name, ItemType.WEAPON, price);
        this.damage = damage;
        this.hands = hands;
    }

    public double getDamage() {
        return damage;
    }

    public int getHands() {
        return hands;
    }

    @Override
    protected boolean action(Hero hero) {
        return hero.equip(this);
    }

    @Override
    protected double damage() {
        return damage;
    }

}
