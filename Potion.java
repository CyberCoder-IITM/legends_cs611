/*
 * A potion
 */
public class Potion extends Item {

    public Potion(String name, double price, int level, double amount, PotionType potionType) {
        super(name, ItemType.POTION, price);
        this.level = level;
        this.amount = amount;
        this.potionType = potionType;
    }

    private int level;
    private double amount;
    private PotionType potionType;

    public int getLevel() {
        return level;
    }

    public double getAmount() {
        return amount;
    }

    public PotionType getPotionType() {
        return potionType;
    }

    @Override
    protected boolean action(Hero hero) {
        return hero.applyPotion(this);
    }

    @Override
    protected double damage() {
        return 0;
    }

}
