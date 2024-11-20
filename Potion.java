/*
 * A potion
 */
public class Potion extends Item {
    private int level;
    private double amount;
    private PotionType potionType;
    private boolean consumed;

    public Potion(String name, double price, int level, double amount, PotionType potionType) {
        super(name, ItemType.POTION, price, level);
        this.level = level;
        this.amount = amount;
        this.potionType = potionType;
        this.consumed = false;
    }



    // Getters
    public int getLevel() { return level; }
    public double getAmount() { return amount; }
    public PotionType getPotionType() { return potionType; }
    public boolean isConsumed() { return consumed; }

    @Override
    protected boolean action(Hero hero) {
        // Check if hero meets level requirement
        if (hero.getLevel() < level) {
            return false;
        }

        // Apply potion effect based on type
        switch (potionType) {
            case HP:
                hero.getStats().increaseHP(amount);
                break;
            case MP:
                hero.getStats().increaseMP(amount);
                break;
            case STR:
                hero.getStats().increaseStr(amount);
                break;
            case DEX:
                hero.getStats().increaseDex(amount);
                break;
            case AGI:
                hero.getStats().increaseAgi(amount);
                break;
            default:
                return false;
        }

        // Mark potion as consumed
        consumed = true;
        return true;
    }

    @Override
    protected double damage() {
        return 0; // Potions don't deal damage
    }



    // Method to check if hero can use this potion
    public boolean canUse(Hero hero) {
        if (consumed) {
            return false;
        }
        return hero.getLevel() >= level;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName())
                .append(" (Level ").append(level).append(")\n")
                .append("Type: ").append(potionType)
                .append(" | Amount: ").append(amount)
                .append(" | Price: ").append(getPrice());
        return sb.toString();
    }

    // Helper method to get effect description
    public String getEffectDescription() {
        return String.format("Increases %s by %.0f",
                potionType.toString(),
                amount);
    }

    // Clone method for creating copies
    public Potion clone() {
        return new Potion(
                getName(),
                getPrice(),
                level,
                amount,
                potionType
        );
    }
}