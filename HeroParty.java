import java.util.Set;

/*
 * A party of heroes
*/
import java.util.*;
public class HeroParty {
    private Map<Lane, Hero> heroLaneAssignments;
    private Set<Hero> heroes;

    public HeroParty(Set<Hero> heroes) {
        this.heroes = heroes;
        this.heroLaneAssignments = new HashMap<>();
    }

    public void assignHeroToLane(Hero hero, Lane lane) {
        heroLaneAssignments.put(lane, hero);
        lane.assignHero(hero);
    }

    public Set<Hero> getHeroes() {
        return heroes;
    }

    public Hero getHeroInLane(Lane lane) {
        return heroLaneAssignments.get(lane);
    }

    public Lane getHeroLane(Hero hero) {
        return heroLaneAssignments.entrySet().stream()
                .filter(entry -> entry.getValue().equals(hero))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public void restoreAfterRound() {
        for (Hero hero : heroes) {
            if (hero.getStats().getHp() > 0) {
                // Restore 10% HP and MP
                hero.getStats().restorePercentHP(0.1);
                hero.getStats().restorePercentMP(0.1);
            }
        }
    }

    public void respawnDeadHeroes() {
        for (Hero hero : heroes) {
            if (hero.getStats().getHp() <= 0) {
                hero.respawn();
            }
        }
    }

    public void distributeRewards(Monster defeatedMonster) {
        int goldReward = 500 * defeatedMonster.getLevel();
        int expReward = 2 * defeatedMonster.getLevel();

        for (Hero hero : heroes) {
            hero.increaseGold((double)goldReward);
            // Add experience handling here
            // You might want to implement level up logic
        }
    }

    public int getHighestLevel() {
        return heroes.stream()
                .mapToInt(Hero::getLevel)
                .max()
                .orElse(1);
    }

    public boolean canTeleport(Hero hero, Position targetPosition) {
        // Get the hero's current lane
        Lane currentLane = getHeroLane(hero);

        // Find target hero at or adjacent to target position
        Hero targetHero = heroLaneAssignments.entrySet().stream()
                .filter(entry -> entry.getValue().getCurrentPosition().isAdjacent(targetPosition))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        if (targetHero == null) {
            return false;
        }

        Lane targetLane = getHeroLane(targetHero);

        // Check if teleport is to different lane
        if (currentLane == targetLane) {
            return false;
        }

        // Check if target position is valid
        return !isPositionBlockedByMonster(targetPosition, targetLane) &&
                !isPositionOccupied(targetPosition) &&
                !isPositionAheadOfHero(targetPosition, targetHero);
    }

    private boolean isPositionBlockedByMonster(Position position, Lane lane) {
        return lane.hasMonsterAhead(position);
    }

    private boolean isPositionOccupied(Position position) {
        return heroes.stream()
                .anyMatch(h -> h.getCurrentPosition().equals(position));
    }

    private boolean isPositionAheadOfHero(Position position, Hero targetHero) {
        return position.getX() < targetHero.getCurrentPosition().getX();
    }

    public void displayHeroStatus(IOHelper ioHelper) {
        ioHelper.println("\n=== Hero Party Status ===");
        for (Hero hero : heroes) {
            Lane lane = getHeroLane(hero);
            ioHelper.println("\nHero: " + hero.getName() + " (Lane " + (lane.getLaneNumber() + 1) + ")");
            ioHelper.println("Level: " + hero.getLevel());
            ioHelper.println("HP: " + hero.getStats().getHp() + "/" + hero.getStats().getMaxHp());
            ioHelper.println("MP: " + hero.getStats().getMp() + "/" + hero.getStats().getMaxMp());
            ioHelper.println("Position: " + hero.getCurrentPosition());
        }
    }
}
