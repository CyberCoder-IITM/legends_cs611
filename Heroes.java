import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
 * all heroes
 */
import java.util.*;
public class Heroes {
    // Warriors
    public static Hero WARRIOR1 = warrior("Gaerdal Ironhand", 100, 2000, 500, 600, 1354, 7);
    public static Hero WARRIOR2 = warrior("Sehanine Monnbow", 600, 2000, 800, 500, 2500, 8);
    public static Hero WARRIOR3 = warrior("Muamman Duathall", 300, 2000, 500, 750, 2546, 6);
    public static Hero WARRIOR4 = warrior("Flandal Steelskin", 200, 2000, 650, 700, 2500, 7);
    public static Hero WARRIOR5 = warrior("Undefeated Yoj", 400, 2000, 400, 700, 2500, 7);
    public static Hero WARRIOR6 = warrior("Eunoia Cyn", 400, 2000, 800, 600, 2500, 6);

    // Sorcerers
    public static Hero SORCEROR1 = sorceror("Rillifane Rallathil", 1300, 2000, 450, 500, 2500, 9);
    public static Hero SORCEROR2 = sorceror("Segojan Earthcaller", 900, 2000, 500, 650, 2500, 5);
    public static Hero SORCEROR3 = sorceror("Reign Havoc", 800, 2000, 800, 800, 2500, 8);
    public static Hero SORCEROR4 = sorceror("Reverie Ashels", 900, 2000, 700, 400, 2500, 7);
    public static Hero SORCEROR5 = sorceror("Kalabar", 800, 2000, 400, 600, 2500, 6);

    // Paladins
    public static Hero PALADIN1 = paladin("Parzival", 300, 2000, 650, 700, 2500, 7);
    public static Hero PALADIN2 = paladin("Sehanine Moonbow", 300, 2000, 700, 700, 2500, 7);
    public static Hero PALADIN3 = paladin("Skoraeus Stonebones", 250, 2000, 600, 350, 2500, 4);

    // Factory methods for creating heroes
    private static Hero warrior(String name, double mp, double str, double agi,
                                double dex, int gold, int exp) {
        return new Hero(name, HeroType.WARRIOR, 1, exp,
                new HeroStats(100, mp, str, agi, dex),
                gold,
                new HashSet<Item>(),
                new HashMap<EquipmentSlot, Item>(),
                null,
                null);
    }

    private static Hero sorceror(String name, double mp, double str, double agi,
                                 double dex, int gold, int exp) {
        return new Hero(name, HeroType.SORCEROR, 1, exp,
                new HeroStats(100, mp, str, agi, dex),
                gold,
                new HashSet<Item>(),
                new HashMap<EquipmentSlot, Item>(),
                null,
                null);
    }

    private static Hero paladin(String name, double mp, double str, double agi,
                                double dex, int gold, int exp) {
        return new Hero(name, HeroType.PALADIN, 1, exp,
                new HeroStats(100, mp, str, agi, dex),
                gold,
                new HashSet<Item>(),
                new HashMap<EquipmentSlot, Item>(),
                null,
                null);
    }
    // Get all heroes
    public static List<Hero> all() {
        return Arrays.asList(
                WARRIOR1, WARRIOR2, WARRIOR3, WARRIOR4, WARRIOR5, WARRIOR6,
                SORCEROR1, SORCEROR2, SORCEROR3, SORCEROR4, SORCEROR5,
                PALADIN1, PALADIN2, PALADIN3
        );
    }

    // Get heroes by type
    public static List<Hero> getWarriors() {
        return Arrays.asList(WARRIOR1, WARRIOR2, WARRIOR3, WARRIOR4, WARRIOR5, WARRIOR6);
    }

    public static List<Hero> getSorcerers() {
        return Arrays.asList(SORCEROR1, SORCEROR2, SORCEROR3, SORCEROR4, SORCEROR5);
    }

    public static List<Hero> getPaladins() {
        return Arrays.asList(PALADIN1, PALADIN2, PALADIN3);
    }

    // Get random hero
    public static Hero randomHero(Random rand) {
        List<Hero> all = all();
        return all.get(rand.nextInt(all.size()));
    }

    // Create a copy of a hero for a specific lane
    public static Hero createHeroForLane(Hero baseHero, Position nexusPosition, Lane lane) {
        // Fix the equipped items copying
        Map<EquipmentSlot, Item> newEquippedItems = new HashMap<>();
        if (baseHero.getEquippedItems() != null) {
            // Convert Collection to Map entries correctly
            for (Item item : baseHero.getEquippedItems()) {
                // Find the slot for this item
                for (Map.Entry<EquipmentSlot, Item> entry : baseHero.equippedItems.entrySet()) {
                    if (entry.getValue().equals(item)) {
                        newEquippedItems.put(entry.getKey(), item);
                    }
                }
            }
        }

        return new Hero(
                baseHero.getName(),
                baseHero.getType(),
                baseHero.getLevel(),
                baseHero.getExp(),
                new HeroStats(
                        baseHero.getStats().getHp(),
                        baseHero.getStats().getMp(),
                        baseHero.getStats().getStr(),
                        baseHero.getStats().getDex(),
                        baseHero.getStats().getAgi()
                ),
                baseHero.getGold(),
                new HashSet<>(baseHero.getInventory()),
                newEquippedItems,  // Use the new map we created
                nexusPosition,
                lane
        );
    }
}