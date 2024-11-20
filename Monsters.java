import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
 * All monsters
// */
import java.util.*;

public class Monsters {
    // Keep existing monster definitions
    public static Monster DRAGON1 = dragon("Desghidorrah", 3, 30, 400, 35);
    public static Monster DRAGON2 = dragon("Chrysophylax", 2, 20, 500, 20);
    public static Monster DRAGON3 = dragon("BunsenBurner", 4, 40, 500, 45);
    public static Monster DRAGON4 = dragon("Natsunomeryu", 1, 10, 200, 10);
    public static Monster DRAGON5 = dragon("TheScaleless", 7, 70, 600, 75);
    public static Monster DRAGON6 = dragon("Kas-Ethelinh", 5, 60, 500, 60);

    public static Monster EXO1 = exo("Cyrrollalee", 7, 70, 800, 75);
    public static Monster EXO2 = dragon("Brandobaris", 3, 35, 450, 30);
    public static Monster EXO3 = dragon("BigBad-Wolf", 1, 15, 250, 15);
    public static Monster EXO4 = dragon("WickedWitch", 2, 25, 350, 25);

    public static Monster SPIRIT1 = spirit("Andrealphus", 2, 60, 500, 40);
    public static Monster SPIRIT2 = spirit("Blinky", 1, 45, 350, 35);
    public static Monster SPIRIT3 = spirit("Andromalius", 3, 55, 450, 25);
    public static Monster SPIRIT4 = spirit("Chiang-shih", 4, 70, 600, 40);


    // Add new monster factory methods
    public static Monster createLeveledMonster(Monster baseMonster, int targetLevel, Position spawnPosition, Lane lane) {
        // Scale monster stats based on level difference
        double levelRatio = (double) targetLevel / baseMonster.getLevel();

        return new Monster(
                baseMonster.getName(),
                baseMonster.getType(),
                targetLevel,
                baseMonster.getHp() * levelRatio,
                baseMonster.getDamage() * levelRatio,
                baseMonster.getDefense() * levelRatio,
                baseMonster.getDodge(),
                spawnPosition,
                lane
        );
    }

    public static MonsterParty createMonsterWave(List<Lane> lanes, int heroMaxLevel, Random rand) {
        Set<Monster> monsters = new HashSet<>();

        // Create one monster for each lane
        for (Lane lane : lanes) {
            // Get random base monster
            Monster baseMonster = random(rand);

            // Calculate spawn position for this lane
            Position spawnPosition = new Position(0, lane.getMonsterNexus().getY());

            // Create leveled monster
            Monster leveledMonster = createLeveledMonster(
                    baseMonster,
                    heroMaxLevel,
                    spawnPosition,
                    lane
            );

            monsters.add(leveledMonster);
            lane.addMonster(leveledMonster);
        }

        return new MonsterParty(monsters);
    }

    // Add spawn timing enum
    public enum SpawnFrequency {
        EASY(8),    // Spawn every 8 rounds
        MEDIUM(4),  // Spawn every 4 rounds
        HARD(2);    // Spawn every 2 rounds

        private final int rounds;

        SpawnFrequency(int rounds) {
            this.rounds = rounds;
        }

        public int getRounds() {
            return rounds;
        }
    }

    // Add spawn checker
    public static boolean shouldSpawn(int currentRound, SpawnFrequency frequency) {
        return currentRound % frequency.getRounds() == 0;
    }

    // Keep existing helper methods
    private static Monster dragon(String name, int level, int damage, int defense, int dodge) {
        return new Monster(name, MonsterType.DRAGON, level, 100, damage, defense, dodge, null, null);
    }

    private static Monster exo(String name, int level, int damage, int defense, int dodge) {
        return new Monster(name, MonsterType.EXOSKELETON, level, 100, damage, defense, dodge, null, null);
    }

    private static Monster spirit(String name, int level, int damage, int defense, int dodge) {
        return new Monster(name, MonsterType.SPIRIT, level, 100, damage, defense, dodge, null, null);
    }

    public static Monster random(Random rand) {
        List<Monster> all = all();
        return all.get(rand.nextInt(all.size()));
    }

    public static List<Monster> all() {
        return Arrays.asList(
                DRAGON1, DRAGON2, DRAGON3, DRAGON4, DRAGON5, DRAGON6,
                EXO1, EXO2, EXO3, EXO4,
                SPIRIT1, SPIRIT2, SPIRIT3, SPIRIT4
        );
    }
}