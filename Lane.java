import java.util.*;
import java.util.stream.Collectors;

public class Lane {
    private final int laneNumber;
    private final List<Position> positions;
    private Hero assignedHero;
    private List<Monster> monsters;
    private final Position heroNexus;
    private final Position monsterNexus;
    private static final int LANE_WIDTH = 2;

    public Lane(int laneNumber, Position heroNexus, Position monsterNexus) {
        this.laneNumber = laneNumber;
        this.heroNexus = heroNexus;
        this.monsterNexus = monsterNexus;
        this.monsters = new ArrayList<>();
        this.positions = calculateLanePositions();
    }

    private List<Position> calculateLanePositions() {
        List<Position> lanePositions = new ArrayList<>();
        int startCol = laneNumber * 3; // Each lane starts at 0, 3, or 6

        // Add all positions in this lane
        for (int row = 0; row < 8; row++) {
            for (int col = startCol; col < startCol + LANE_WIDTH; col++) {
                lanePositions.add(new Position(row, col));
            }
        }

        return lanePositions;
    }

    public boolean hasMonsterAt(Position position) {
        return monsters.stream()
                .anyMatch(m -> m.getCurrentPosition().equals(position));
    }

    // Add method to get adjacent positions within lane
    public List<Position> getAdjacentPositions(Position pos) {
        return positions.stream()
                .filter(p -> p.isAdjacent(pos))
                .collect(Collectors.toList());
    }

    // Add method to validate teleport position
    public boolean isValidTeleportPosition(Position targetPos, Position heroPos) {
        // Can't teleport within same lane
        if (contains(heroPos)) {
            return false;
        }

        // Can't teleport ahead of target hero
        if (targetPos.getX() < assignedHero.getCurrentPosition().getX()) {
            return false;
        }

        // Can't teleport behind a monster
        if (hasMonsterAhead(targetPos)) {
            return false;
        }

        // Can't teleport to occupied space
        if (isBlocked(targetPos)) {
            return false;
        }

        return true;
    }

    // Add method to check if position is in attack range
    public boolean isInAttackRange(Position attacker, Position target) {
        int dx = Math.abs(target.getX() - attacker.getX());
        int dy = Math.abs(target.getY() - attacker.getY());
        return (dx <= 1 && dy <= 1) && (dx + dy <= 1);
    }

    // Add method to get valid move positions
    public List<Position> getValidMovePositions(Position currentPos) {
        return positions.stream()
                .filter(p -> p.isAdjacent(currentPos))
                .filter(p -> !isBlocked(p))
                .filter(p -> !hasMonsterAhead(p))
                .collect(Collectors.toList());
    }

    // Add method to check if lane is blocked by monster
    public boolean isLaneBlocked() {
        return monsters.stream()
                .anyMatch(m -> m.getCurrentPosition().getX() == 7); // Check if monster reached end
    }

    // Add method to get monsters by position
    public List<Monster> getMonstersAtPosition(Position pos) {
        return monsters.stream()
                .filter(m -> m.getCurrentPosition().equals(pos))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.format("Lane %d [Hero: %s, Monsters: %d]",
                laneNumber,
                assignedHero != null ? assignedHero.getName() : "None",
                monsters.size());
    }

    public boolean contains(Position position) {
        return positions.contains(position);
    }

    public void assignHero(Hero hero) {
        this.assignedHero = hero;
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
    }

    public void removeMonster(Monster monster) {
        monsters.remove(monster);
    }

    public List<Monster> getMonsters() {
        return new ArrayList<>(monsters);
    }

    public Hero getHero() {
        return assignedHero;
    }

    public Position getHeroNexus() {
        return heroNexus;
    }

    public Position getMonsterNexus() {
        return monsterNexus;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public boolean hasMonsterAhead(Position position) {
        return monsters.stream()
                .anyMatch(m -> m.getCurrentPosition().getX() < position.getX() &&
                        m.getCurrentPosition().getY() == position.getY());
    }

    public List<Monster> getMonstersInRange(Position position) {
        return monsters.stream()
                .filter(m -> position.isInAttackRange(m.getCurrentPosition()))
                .collect(Collectors.toList());
    }

    public void clearDeadMonsters() {
        monsters.removeIf(m -> m.getHp() <= 0);
    }

    public boolean isBlocked(Position position) {
        // Check if position is occupied by hero or monster
        return monsters.stream().anyMatch(m -> m.getCurrentPosition().equals(position)) ||
                (assignedHero != null && assignedHero.getCurrentPosition().equals(position));
    }
}