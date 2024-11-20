/*
 * A weapon
// */

public class Weapon extends Item {
    private int level;
    private double damage;
    private int hands;  // Number of hands required (1 or 2)

    public Weapon(String name, double price, int level, double damage, int hands) {
        super(name, ItemType.WEAPON, price,level);
        this.level = level;
        this.damage = damage;
        this.hands = hands;
    }

    // Getters
    public int getLevel() {
        return level;
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

    // Check if hero can equip this weapon
    public boolean canEquip(Hero hero) {
        // Check level requirement
        if (hero.getLevel() < level) {
            return false;
        }

        // Check if hero has enough free hands
        if (hands == 2) {
            // For two-handed weapons, both hands must be free
            return !hero.hasWeaponEquipped(EquipmentSlot.LEFT_HAND) &&
                    !hero.hasWeaponEquipped(EquipmentSlot.RIGHT_HAND);
        } else {
            // For one-handed weapons, at least one hand must be free
            return !hero.hasWeaponEquipped(EquipmentSlot.LEFT_HAND) ||
                    !hero.hasWeaponEquipped(EquipmentSlot.RIGHT_HAND);
        }
    }

    @Override
    public String toString() {
        return String.format("%s (Level %d) | Damage: %.0f | Hands: %d | Price: %.0f",
                getName(),
                level,
                damage,
                hands,
                getPrice()
        );
    }

    // Clone method for creating copies
    public Weapon clone() {
        return new Weapon(
                getName(),
                getPrice(),
                level,
                damage,
                hands
        );
    }
}
