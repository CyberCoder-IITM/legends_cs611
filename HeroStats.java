/*
 * Stats of a hero
 */
public class HeroStats {
    private double hp;
    private double mp;
    private double str;
    private double dex;
    private double agi;

    public HeroStats(double hp, double mp, double str, double dex, double agi) {
        this.hp = hp;
        this.mp = mp;
        this.str = str;
        this.dex = dex;
        this.agi = agi;
    }

    public double getHp() {
        return hp;
    }

    public double getMp() {
        return mp;
    }

    public double getStr() {
        return str;
    }

    public double getDex() {
        return dex;
    }

    public double getAgi() {
        return agi;
    }

    public void increaseHP(double amount) {
        this.hp += amount;
    }

    public void increaseAgi(double amount) {
        this.agi += amount;
    }

    public void increaseStr(double amount) {
        this.str += amount;
    }

    public void increaseDex(double amount) {
        this.dex += amount;
    }

    public void increaseMP(double amount) {
        this.mp += amount;
    }

    public void decreaseHP(double damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            this.hp = 0;
        }
    }
}
