/*
 * All types of potions
*/
public enum PotionType {
    HP("Health"),
    MP("Mana"),
    STR("Strength"),
    DEX("Dexterity"),
    AGI("Agility");

    private final String description;

    PotionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}