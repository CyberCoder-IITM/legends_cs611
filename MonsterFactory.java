/*
 * Create monsters which will fight the heroes
 */
public interface MonsterFactory {

    MonsterParty summon(HeroParty heroParty);

}
//import java.util.*;
//public class MonsterFactory {
//    private Random rand;
//    private static final int SPAWN_FREQUENCY_EASY = 8;
//    private static final int SPAWN_FREQUENCY_MEDIUM = 4;
//    private static final int SPAWN_FREQUENCY_HARD = 2;
//    private final int spawnFrequency;
//    private int roundCounter;
//
//    public MonsterFactory(GameDifficulty difficulty) {
//        this.rand = new Random();
//        this.roundCounter = 0;
//
//        // Set spawn frequency based on difficulty
//        switch(difficulty) {
//            case EASY:
//                this.spawnFrequency = SPAWN_FREQUENCY_EASY;
//                break;
//            case MEDIUM:
//                this.spawnFrequency = SPAWN_FREQUENCY_MEDIUM;
//                break;
//            case HARD:
//                this.spawnFrequency = SPAWN_FREQUENCY_HARD;
//                break;
//            default:
//                this.spawnFrequency = SPAWN_FREQUENCY_MEDIUM;
//        }
//    }
//
//    @Override
//    public MonsterParty summon(HeroParty heroParty) {
//        // Get highest hero level for scaling monsters
//        int maxHeroLevel = heroParty.getHeroes().stream()
//                .mapToInt(Hero::getLevel)
//                .max()
//                .orElse(1);
//
//        // Create one monster per lane
//        Set<Monster> monsters = new HashSet<>();
//        for (int lane = 0; lane < 3; lane++) {
//            // Get base monster
//            Monster baseMonster = Monsters.random(rand);
//
//            // Scale monster to hero level
//            double levelRatio = (double) maxHeroLevel / baseMonster.getLevel();
//            Monster scaledMonster = new Monster(
//                    baseMonster.getName(),
//                    baseMonster.getType(),
//                    maxHeroLevel,
//                    baseMonster.getHp() * levelRatio,
//                    baseMonster.getDamage() * levelRatio,
//                    baseMonster.getDefense() * levelRatio,
//                    baseMonster.getDodge(),
//                    new Position(0, lane * 3), // Spawn in correct lane
//                    null  // Lane will be set by MonsterParty
//            );
//
//            monsters.add(scaledMonster);
//        }
//
//        return new MonsterParty(monsters);
//    }
//
//    public boolean shouldSpawnNewWave() {
//        roundCounter++;
//        return roundCounter % spawnFrequency == 0;
//    }
//
//    public void resetRoundCounter() {
//        roundCounter = 0;
//    }
//
//    // Helper method to calculate monster rewards
//    public static int calculateGoldReward(Monster monster) {
//        return 500 * monster.getLevel();
//    }
//
//    public static int calculateExpReward(Monster monster) {
//        return 2 * monster.getLevel();
//    }
//}
//
