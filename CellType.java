/*
 * Type of a cell
 */
public enum CellType {
    NEXUS,          // Hero/Monster spawn points
    INACCESSIBLE,   // Lane dividers
    OBSTACLE,       // Removable obstacles
    PLAIN,          // Normal spaces
    BUSH,           // Dexterity bonus
    CAVE,           // Agility bonus
    KOULOU,         // Strength bonus
    HEROES,         // Space with hero
    MONSTER,        // Space with monster
    MARKET_WITH_HEROES;  // Kept for compatibility

    public double getBonus() {
        switch(this) {
            case BUSH:
            case CAVE:
            case KOULOU:
                return 0.1; // 10% bonus
            default:
                return 0.0;
        }
    }

    public String getSymbol() {
        switch(this) {
            case NEXUS: return "N";
            case INACCESSIBLE: return "I";
            case OBSTACLE: return "O";
            case PLAIN: return "P";
            case BUSH: return "B";
            case CAVE: return "C";
            case KOULOU: return "K";
            case HEROES: return "H";
            case MONSTER: return "M";
            default: return " ";
        }
    }
}