/*
 * Stats of a hero
*/
public class HeroStats {
    private double hp;
    private double maxHp;
    private double mp;
    private double maxMp;
    private double str;
    private double baseStr;
    private double dex;
    private double baseDex;
    private double agi;
    private double baseAgi;

    private static final double TERRAIN_BONUS = 0.10; // 10% bonus
    private static final double RESTORE_RATE = 0.10;  // 10% restoration

    public HeroStats(double hp, double mp, double str, double dex, double agi) {
        this.maxHp = hp;
        this.hp = hp;
        this.maxMp = mp;
        this.mp = mp;
        this.baseStr = str;
        this.str = str;
        this.baseDex = dex;
        this.dex = dex;
        this.baseAgi = agi;
        this.agi = agi;
    }

    // Getters
    public double getHp() { return hp; }
    public double getMaxHp() { return maxHp; }
    public double getMp() { return mp; }
    public double getMaxMp() { return maxMp; }
    public double getStr() { return str; }
    public double getDex() { return dex; }
    public double getAgi() { return agi; }

    // Terrain bonus methods
    public void applyTerrainBonus(CellType cellType) {
        switch(cellType) {
            case BUSH:
                dex = baseDex * (1 + TERRAIN_BONUS);
                break;
            case CAVE:
                agi = baseAgi * (1 + TERRAIN_BONUS);
                break;
            case KOULOU:
                str = baseStr * (1 + TERRAIN_BONUS);
                break;
            default:
                break;
        }
    }

    public void increaseStr(double amount) {
        this.str += amount;
    }

    public void increaseDex(double amount) {
        this.dex += amount;
    }

    public void increaseAgi(double amount) {
        this.agi += amount;
    }

    public void removeTerrainBonus(CellType cellType) {
        switch(cellType) {
            case BUSH:
                dex = baseDex;
                break;
            case CAVE:
                agi = baseAgi;
                break;
            case KOULOU:
                str = baseStr;
                break;
            default:
                break;
        }
    }

    // HP/MP modification methods
    public void decreaseHP(double damage) {
        this.hp = Math.max(0, this.hp - damage);
    }

    public void increaseHP(double amount) {
        this.hp = Math.min(maxHp, this.hp + amount);
    }

    public void decreaseMP(double amount) {
        this.mp = Math.max(0, this.mp - amount);
    }

    public void increaseMP(double amount) {
        this.mp = Math.min(maxMp, this.mp + amount);
    }

    // Round end restoration
    public void restoreAfterRound() {
        if (hp > 0) {  // Only restore if hero is alive
            restorePercentHP(RESTORE_RATE);
            restorePercentMP(RESTORE_RATE);
        }
    }

    public void restorePercentHP(double percent) {
        double amount = maxHp * percent;
        increaseHP(amount);
    }

    public void restorePercentMP(double percent) {
        double amount = maxMp * percent;
        increaseMP(amount);
    }

    // Respawn restoration
    public void fullRestore() {
        this.hp = maxHp;
        this.mp = maxMp;
    }

    // Level up stats increase
    public void levelUp() {
        maxHp *= 1.1;  // 10% increase
        maxMp *= 1.1;
        baseStr *= 1.05;  // 5% increase
        baseDex *= 1.05;
        baseAgi *= 1.05;

        // Reset current stats
        str = baseStr;
        dex = baseDex;
        agi = baseAgi;

        // Full restore on level up
        fullRestore();
    }

    @Override
    public String toString() {
        return String.format(
                "HP: %.0f/%.0f | MP: %.0f/%.0f | STR: %.0f | DEX: %.0f | AGI: %.0f",
                hp, maxHp, mp, maxMp, str, dex, agi
        );
    }
}