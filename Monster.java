
/*
 * Represents a monster
 */
import java.util.Random;

public class Monster extends Living {
    private MonsterType type;
    private double hp;
    private double damage;
    private double defense;
    private double dodge;
    private Position currentPosition;
    private Lane assignedLane;
    private static final double TERRAIN_BONUS = 0.1; // 10% bonus

    public Monster(String name, MonsterType type, int level, double hp, double damage,
            double defense, double dodge, Position spawnPosition, Lane lane) {
        super(name, level);
        this.type = type;
        this.hp = hp;
        this.damage = damage;
        this.defense = defense;
        this.dodge = dodge;
        this.currentPosition = spawnPosition;
        this.assignedLane = lane;
    }

    // Existing getters
    public MonsterType getType() {
        return type;
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

    // New getters
    public Position getCurrentPosition() {
        return currentPosition;
    }

    public Lane getAssignedLane() {
        return assignedLane;
    }

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
        // return board.isValid(newPos) &&
        // !board.isInaccessible(newPos) &&
        // !board.hasMonster(newPos);
        // Check basic validity
        if (!board.isValid(newPos)) {
            return false;
        }

        // Check for inaccessible spaces
        if (board.isInaccessible(newPos)) {
            return false;
        }

        // Check for other monsters
        if (board.hasMonster(newPos)) {
            return false;
        }

        // Check for heroes blocking path
        if (board.hasHero(newPos)) {
            return false;
        }

        return true;
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

    public void setCurrentPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.currentPosition = position;
    }

    public void setPosition(Position position) {
        this.currentPosition = position;
    }

    // Optional: Implement terrain bonuses for harder difficulty
    public void applyTerrainEffect(CellType cellType) {
        switch (cellType) {
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
        switch (cellType) {
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
                this.getName(), this.getLevel(), hp, damage, defense, dodge);
    }
}