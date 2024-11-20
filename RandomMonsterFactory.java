import java.util.HashSet;
import java.util.Random;

/*
 * Generates random number of monsters
 */

import java.util.*;
public class RandomMonsterFactory implements MonsterFactory {
    private Random rand;
    private int roundCounter;
    private final int spawnFrequency;

    // Spawn frequency based on difficulty
    private static final int EASY_SPAWN_RATE = 8;
    private static final int MEDIUM_SPAWN_RATE = 4;
    private static final int HARD_SPAWN_RATE = 2;

    public RandomMonsterFactory(GameDifficulty difficulty) {
        this.rand = new Random();
        this.roundCounter = 0;

        // Set spawn frequency based on difficulty
        switch(difficulty) {
            case EASY:
                this.spawnFrequency = EASY_SPAWN_RATE;
                break;
            case MEDIUM:
                this.spawnFrequency = MEDIUM_SPAWN_RATE;
                break;
            case HARD:
                this.spawnFrequency = HARD_SPAWN_RATE;
                break;
            default:
                this.spawnFrequency = MEDIUM_SPAWN_RATE;
        }
    }

    @Override
    public MonsterParty summon(HeroParty heroParty) {
        // Check if it's time to spawn monsters
        roundCounter++;
        if (roundCounter % spawnFrequency != 0) {
            return null;
        }

        // Get highest hero level for scaling monsters
        int maxHeroLevel = heroParty.getHeroes().stream()
                .mapToInt(Hero::getLevel)
                .max()
                .orElse(1);

        // Create one monster per lane
        Set<Monster> monsters = new HashSet<>();
        for (int lane = 0; lane < 3; lane++) {
            // Get base monster
            Monster baseMonster = Monsters.random(rand);

            // Scale monster to hero level
            double levelRatio = (double) maxHeroLevel / baseMonster.getLevel();

            // Create scaled monster
            Monster scaledMonster = new Monster(
                    baseMonster.getName(),
                    baseMonster.getType(),
                    maxHeroLevel,
                    baseMonster.getHp() * levelRatio,
                    baseMonster.getDamage() * levelRatio,
                    baseMonster.getDefense() * levelRatio,
                    baseMonster.getDodge(),
                    new Position(0, lane * 3), // Spawn in correct lane
                    null  // Lane will be set by MonsterParty
            );

            monsters.add(scaledMonster);
        }

        return new MonsterParty(monsters);
    }

    public boolean shouldSpawnNewWave() {
        return roundCounter % spawnFrequency == 0;
    }

    public void resetRoundCounter() {
        roundCounter = 0;
    }

    // Helper method to calculate monster rewards
    public static int calculateGoldReward(Monster monster) {
        return 500 * monster.getLevel();
    }

    public static int calculateExpReward(Monster monster) {
        return 2 * monster.getLevel();
    }
}

