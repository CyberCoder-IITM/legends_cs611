import java.util.Random;

/*
 * Represents a monster
 */
public class Monster {
    private String name;
    private MonsterType type;
    private int level;
    private double hp;
    private double damage;
    private double defense;
    private double dodge;

    public String getName() {
        return name;
    }

    public MonsterType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public double getHp() {
        return hp;
    }

    public double getDamage() {
        return damage;
    }

    public double getDefense() {
        return defense;
    }

    public double getDodge() {
        return dodge;
    }

    public Monster(String name, MonsterType type, int level, double hp, double damage, double defense, double dodge) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.hp = hp;
        this.damage = damage;
        this.defense = defense;
        this.dodge = dodge;
    }

    public boolean dodged() {
        Random rand = new Random();
        return rand.nextDouble() < this.dodge * 0.01;
    }

    public void takeDamage(double dam) {
        this.hp -= dam;
        if (this.hp <= 0) {
            this.hp = 0;
        }
    }
}
