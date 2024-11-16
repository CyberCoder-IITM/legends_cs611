import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

/*
 * Represents a hero
 */
public class Hero {
    private String name;
    private HeroType type;
    private int level;
    private int exp;
    private HeroStats stats;
    private int gold;

    private Set<Item> inventory;
    private Map<EquipmentSlot, Item> equippedItems;

    public Hero(String name, HeroType type, int level, int exp, HeroStats stats, int gold, Set<Item> inventory,
            Map<EquipmentSlot, Item> equippedItems) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.exp = exp;
        this.stats = stats;
        this.gold = gold;
        this.inventory = inventory;
        this.equippedItems = equippedItems;
    }

    public Hero(String name, HeroType type, int level, int exp, double hp, double mp, double str, double agi,
            double dex, int gold) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.exp = exp;
        this.stats = new HeroStats(hp, mp, str, agi, dex);
        this.gold = gold;
        this.inventory = new HashSet<>();
        this.equippedItems = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public HeroType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public HeroStats getStats() {
        return stats;
    }

    public int getGold() {
        return gold;
    }

    public Set<Item> getInventory() {
        return inventory;
    }

    public Collection<Item> getEquippedItems() {
        return equippedItems.values();
    }

    public boolean equip(Weapon weapon) {
        if (weapon.getHands() == 2) {
            this.equippedItems.put(EquipmentSlot.LEFT_HAND, weapon);
            this.equippedItems.put(EquipmentSlot.RIGHT_HAND, weapon);
        } else {
            Item leftItem = this.equippedItems.get(EquipmentSlot.LEFT_HAND);
            if (leftItem == null) {
                this.equippedItems.put(EquipmentSlot.LEFT_HAND, weapon);
            } else {
                Weapon w = ((Weapon) leftItem);
                if (w.getHands() == 2) {
                    this.equippedItems.remove(EquipmentSlot.LEFT_HAND);
                    this.equippedItems.remove(EquipmentSlot.RIGHT_HAND);
                    this.equippedItems.put(EquipmentSlot.LEFT_HAND, weapon);
                } else {
                    this.equippedItems.put(EquipmentSlot.RIGHT_HAND, weapon);
                }
            }
        }
        return true;
    }

    public boolean equip(Armor armor) {
        this.equippedItems.put(EquipmentSlot.BODY, armor);
        return true;
    }

    public boolean applyPotion(Potion potion) {
        switch (potion.getPotionType()) {
            case HP:
                this.stats.increaseHP(potion.getAmount());
                break;
            case MP:
                this.stats.increaseMP(potion.getAmount());
                break;
            case AGI:
                this.stats.increaseAgi(potion.getAmount());
                break;
            case STR:
                this.stats.increaseStr(potion.getAmount());
                break;
            case DEX:
                this.stats.increaseDex(potion.getAmount());
                break;

            default:
                break;
        }
        this.removeItem(potion);
        return true;
    }

    public boolean doItemAction(Item item) {
        return item.action(this);
    }

    public void addItem(Item item) {
        this.inventory.add(item);
    }

    public void decreaseGold(Double amount) {
        this.gold -= amount;
    }

    public void removeItem(Item item) {
        this.inventory.remove(item);
        Optional<Entry<EquipmentSlot, Item>> e = this.equippedItems.entrySet().stream()
                .filter(ex -> ex.getValue() == item)
                .findAny();
        if (e.isPresent()) {
            this.equippedItems.remove(e.get().getKey());
        }
    }

    public void increaseGold(Double amount) {
        this.gold += amount;
    }

    public void increaseLevel() {
        this.level++;
        this.stats.increaseHP(100);
        this.stats.increaseMP(this.stats.getMp() * 1.1);
    }

    public double damageValue() {
        double left = this.equippedItems.containsKey(EquipmentSlot.LEFT_HAND)
                ? this.equippedItems.get(EquipmentSlot.LEFT_HAND).damage()
                : 0;
        double right = this.equippedItems.containsKey(EquipmentSlot.RIGHT_HAND)
                ? this.equippedItems.get(EquipmentSlot.RIGHT_HAND).damage()
                : 0;
        return (left + right + this.stats.getStr()) * 0.05;
    }

    public void takeDamage(double damage) {
        this.stats.decreaseHP(damage);
    }

    public void restoreAfterTurn() {
        this.stats.increaseHP(this.stats.getHp() * 11 / 10);
    }
}
