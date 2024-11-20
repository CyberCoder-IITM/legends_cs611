/*
 * Represents a monster
// */

import java.util.Random;

public class Monster {
    private String name;
    private MonsterType type;
    private int level;
    private double hp;
    private double damage;
    private double defense;
    private double dodge;
    private Position currentPosition;
    private Lane assignedLane;
    private static final double TERRAIN_BONUS = 0.1; // 10% bonus

    public Monster(String name, MonsterType type, int level, double hp, double damage,
                   double defense, double dodge, Position spawnPosition, Lane lane) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.hp = hp;
        this.damage = damage;
        this.defense = defense;
        this.dodge = dodge;
        this.currentPosition = spawnPosition;
        this.assignedLane = lane;
    }

    // Existing getters
    public String getName() { return name; }
    public MonsterType getType() { return type; }
    public int getLevel() { return level; }
    public double getHp() { return hp; }
    public double getDamage() { return damage; }
    public double getDefense() { return defense; }
    public double getDodge() { return dodge; }

    // New getters
    public Position getCurrentPosition() { return currentPosition; }
    public Lane getAssignedLane() { return assignedLane; }

    public boolean moveForward(Board<CellType> board) {
        Position newPosition = new Position(currentPosition.getX() + 1, currentPosition.getY());

        // Check if reached hero nexus
        if (board.isHeroNexus(newPosition)) {
            currentPosition = newPosition;
            return true; // Monster wins
        }

        // Check if move is valid
        if (canMove(newPosition, board)) {
            currentPosition = newPosition;
        }

        return false;
    }

    private boolean canMove(Position newPos, Board<CellType> board) {
        return board.isValid(newPos) &&
                !board.isInaccessible(newPos) &&
                !board.hasMonster(newPos);
    }

    public boolean canAttack(Position targetPosition) {
        int dx = Math.abs(targetPosition.getX() - currentPosition.getX());
        int dy = Math.abs(targetPosition.getY() - currentPosition.getY());
        return (dx <= 1 && dy <= 1) && (dx + dy <= 1);
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

    public void setPosition(Position position) {
        this.currentPosition = position;
    }

    // Optional: Implement terrain bonuses for harder difficulty
    public void applyTerrainEffect(CellType cellType) {
        switch(cellType) {
            case BUSH:
            case CAVE:
                this.dodge *= (1 + TERRAIN_BONUS);
                break;
            case KOULOU:
                this.damage *= (1 + TERRAIN_BONUS);
                break;
            default:
                break;
        }
    }

    public void removeTerrainEffect(CellType cellType) {
        switch(cellType) {
            case BUSH:
            case CAVE:
                this.dodge /= (1 + TERRAIN_BONUS);
                break;
            case KOULOU:
                this.damage /= (1 + TERRAIN_BONUS);
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (Lvl %d) HP:%.0f DMG:%.0f DEF:%.0f DDG:%.0f",
                name, level, hp, damage, defense, dodge);
    }
}