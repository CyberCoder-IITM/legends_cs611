/*
 * An Armor
 */
public class Armor extends Item {

    int level;
    double damageReduction;

    public Armor(String name, double price, int level, double damageReduction) {
        super(name, ItemType.ARMOR, price);
        this.level = level;
        this.damageReduction = damageReduction;
    }

    @Override
    protected boolean action(Hero hero) {
        return hero.equip(this);
    }

    @Override
    protected double damage() {
        return 0;
    }

}
