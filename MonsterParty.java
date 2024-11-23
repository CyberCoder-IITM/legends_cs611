import java.util.*;

/*
 * A party of monsters
 */

public class MonsterParty {
    private Set<Monster> monsters;
    private Map<Lane, List<Monster>> monstersByLane;
    private static final double GOLD_REWARD_MULTIPLIER = 500;
    private static final int EXP_REWARD_MULTIPLIER = 2;
    private static final int SPAWN_FREQUENCY = 8;  // Spawn every 8 rounds per PDF


    public MonsterParty(Set<Monster> monsters) {
        this.monsters = new HashSet<>(monsters);
        this.monstersByLane = new HashMap<>();
        initializeMonsterLanes();
    }

    public MonsterParty() {
        this.monsters = new HashSet<>();
        this.monstersByLane = new HashMap<>();
    }

    // Add this method for spawning new wave
    public void spawnNewWave(List<Lane> lanes, int heroMaxLevel) {
        System.out.println("\nNew monsters are spawning!");

        // Create one monster per lane
        for (Lane lane : lanes) {
            // Get base monster
            Monster baseMonster = Monsters.random(new Random());
            Position spawnPos = lane.getMonsterNexus();

            // Scale monster to hero level
            double levelRatio = (double) heroMaxLevel / baseMonster.getLevel();
            Monster scaledMonster = new Monster(
                    baseMonster.getName(),
                    baseMonster.getType(),
                    heroMaxLevel,
                    baseMonster.getHp() * levelRatio,
                    baseMonster.getDamage() * levelRatio,
                    baseMonster.getDefense() * levelRatio,
                    baseMonster.getDodge(),
                    spawnPos,
                    lane
            );

            // Add monster to collections
            addMonster(scaledMonster, lane);
            System.out.println("Spawned " + scaledMonster.getName() +
                    " (Level " + heroMaxLevel + ") in lane " +
                    (lane.getLaneNumber() + 1));
        }
    }


    // Helper method to add monster to both collections
//    private void addMonster(Monster monster, Lane lane) {
//        monsters.add(monster);
//        monstersByLane.computeIfAbsent(lane, k -> new ArrayList<>())
//                .add(monster);
//        lane.addMonster(monster);  // Also add to lane
//    }
    public void addMonster(Monster monster, Lane lane) {
        // Remove any existing monster at the same position
        Position pos = monster.getCurrentPosition();
        monstersByLane.get(lane).removeIf(m ->
                m.getCurrentPosition().equals(pos));

        // Add new monster
        monstersByLane.computeIfAbsent(lane, k -> new ArrayList<>())
                .add(monster);
    }

    private void initializeMonsterLanes() {
        for (Monster monster : monsters) {
            if (monster.getAssignedLane() != null) {
                monstersByLane.computeIfAbsent(monster.getAssignedLane(), k -> new ArrayList<>())
                        .add(monster);
            }
        }
    }

    public Set<Monster> getMonsters() {
        return monsters;
    }

    public List<Monster> getMonstersInLane(Lane lane) {
        return monstersByLane.getOrDefault(lane, new ArrayList<>());
    }

    public void addMonster(Monster monster) {
        monsters.add(monster);
        if (monster.getAssignedLane() != null) {
            monstersByLane.computeIfAbsent(monster.getAssignedLane(), k -> new ArrayList<>())
                    .add(monster);
        }
    }

    public void removeDeadMonsters() {
        for (List<Monster> monsters : monstersByLane.values()) {
            monsters.removeIf(monster -> monster.getHp() <= 0);
        }
    }

    public void spawnNewMonsters(List<Lane> lanes, int heroMaxLevel) {
        // Only spawn if it's the right round
        System.out.println("\nNew monsters are spawning!");

        for (Lane lane : lanes) {
            // Get base monster
            Monster baseMonster = Monsters.random(new Random());
            Position spawnPos = lane.getMonsterNexus();

            // Scale monster to hero level
            Monster scaledMonster = new Monster(
                    baseMonster.getName(),
                    baseMonster.getType(),
                    heroMaxLevel,
                    baseMonster.getHp() * (heroMaxLevel / baseMonster.getLevel()),
                    baseMonster.getDamage() * (heroMaxLevel / baseMonster.getLevel()),
                    baseMonster.getDefense() * (heroMaxLevel / baseMonster.getLevel()),
                    baseMonster.getDodge(),
                    spawnPos,
                    lane
            );

            addMonster(scaledMonster, lane);
            System.out.println("Spawned " + scaledMonster.getName() +
                    " (Level " + heroMaxLevel + ") in lane " +
                    (lane.getLaneNumber() + 1));
        }
    }

//    public void removeMonster(Monster monster) {
//        monsters.remove(monster);
//        if (monster.getAssignedLane() != null) {
//            monstersByLane.get(monster.getAssignedLane()).remove(monster);
//        }
//    }

    public void removeMonster(Monster monster) {
        Lane lane = monster.getAssignedLane();
        if (lane != null && monstersByLane.containsKey(lane)) {
            monstersByLane.get(lane).remove(monster);
        }
    }

    public boolean handleMonstersTurn(Board<CellType> board, HeroParty heroParty) {
        // Process each lane separately
        for (Lane lane : monstersByLane.keySet()) {
            List<Monster> laneMonsters = monstersByLane.get(lane);

            // Process monsters from bottom to top to avoid collision issues
            for (int i = laneMonsters.size() - 1; i >= 0; i--) {
                Monster monster = laneMonsters.get(i);
                boolean attacked = false;

                // Check for heroes in attack range
                for (Hero hero : heroParty.getHeroes()) {
                    if (monster.canAttack(hero.getCurrentPosition())) {
                        double damage = monster.getDamage();
                        hero.takeDamage(damage);
                        System.out.println(monster.getName() + " attacks " +
                                hero.getName() + " for " + damage + " damage!");
                        attacked = true;
                        break;
                    }
                }

                // If couldn't attack, move forward
                if (!attacked) {
                    Position currentPos = monster.getCurrentPosition();
                    Position newPos = new Position(currentPos.getX() + 1, currentPos.getY());

                    if (canMoveToPosition(newPos, board)) {
                        // Remove from old position
                        board.removeMonster(currentPos);

                        // Update position
                        monster.setCurrentPosition(newPos);

                        // Add to new position
                        board.placeMonster(newPos, monster);

                        System.out.println(monster.getName() + " moves forward!");

                        // Check victory condition
                        if (newPos.isHeroNexus()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean canMoveToPosition(Position pos, Board<CellType> board) {
        return board.isValid(pos) &&
                !board.isInaccessible(pos) &&
                !board.hasMonster(pos) &&
                !board.hasHero(pos);
    }

    private boolean canAttackAnyHero(Monster monster, Board<CellType> board) {
        Position monsterPos = monster.getCurrentPosition();
        for (Position pos : getAdjacentPositions(monsterPos)) {
            if (board.hasHero(pos)) {
                return true;
            }
        }
        return false;
    }

    private List<Position> getAdjacentPositions(Position pos) {
        List<Position> adjacent = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            Position newPos = pos.move(dir);
            if (newPos.isValid()) {
                adjacent.add(newPos);
            }
        }
        return adjacent;
    }

    private void moveMonsterForward(Monster monster, Board<CellType> board) {
        Position currentPos = monster.getCurrentPosition();
        Position newPos = new Position(currentPos.getX() + 1, currentPos.getY());

        if (isValidMove(newPos, board)) {
            board.removeMonster(currentPos);
            monster.setPosition(newPos);
            board.placeMonster(newPos, monster);
        }
    }

    private boolean isValidMove(Position pos, Board<CellType> board) {
        return board.isValid(pos) &&
                !board.isInaccessible(pos) &&
                !board.hasMonster(pos);
    }

    public void handleMonsterAttacks(HeroParty heroParty, Board<CellType> board) {
        for (Monster monster : monsters) {
            for (Hero hero : heroParty.getHeroes()) {
                if (monster.canAttack(hero.getCurrentPosition())) {
                    performMonsterAttack(monster, hero);
                    break;  // Monster can only attack one hero per turn
                }
            }
        }
    }

private void performMonsterAttack(Monster monster, Hero hero) {
    // Check if hero is alive (HP greater than 0)
    if (hero.getStats().getHp() > 0) {  // Changed condition
        double damage = monster.getDamage();
        hero.takeDamage(damage);
    }
}

    public void distributeRewards(Monster defeatedMonster, HeroParty heroParty) {
        int goldReward = (int)(GOLD_REWARD_MULTIPLIER * defeatedMonster.getLevel());
        int expReward = EXP_REWARD_MULTIPLIER * defeatedMonster.getLevel();

        for (Hero hero : heroParty.getHeroes()) {
            hero.increaseGold((double)goldReward);
            // Add experience points and handle level up if needed
        }
    }

    public boolean hasMonsterReachedNexus() {
        return monsters.stream()
                .anyMatch(m -> m.getCurrentPosition().isHeroNexus());
    }

    public List<Monster> getAllMonsters() {
        return new ArrayList<>(monsters);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Monster Party:\n");
        for (Lane lane : monstersByLane.keySet()) {
            sb.append("Lane ").append(lane.getLaneNumber()).append(":\n");
            for (Monster monster : monstersByLane.get(lane)) {
                sb.append("  ").append(monster.toString()).append("\n");
            }
        }
        return sb.toString();
    }
}