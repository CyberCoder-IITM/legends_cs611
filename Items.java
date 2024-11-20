import java.util.Arrays;
import java.util.List;

/*
 * All available items
 */
//public class Items {
//    public static Potion POTION1 = new Potion("Healing Potion", 250, 1, 100, PotionType.HP);
//    public static Potion POTION2 = new Potion("Strength Potion", 200, 1, 75, PotionType.STR);
//    public static Potion POTION3 = new Potion("Magic Potion", 350, 2, 100, PotionType.MP);
//    public static Potion POTION4 = new Potion("Luck Elixir", 500, 4, 65, PotionType.AGI);
//    public static Potion POTION5 = new Potion("Mermaid Tears", 850, 5, 100, PotionType.AGI);
//
//    public static Armor ARMOR1 = new Armor("Platinum Shield", 150, 1, 200);
//    public static Armor ARMOR2 = new Armor("Breast plate", 350, 3, 600);
//    public static Armor ARMOR3 = new Armor("Full Body Armor", 1000, 8, 1100);
//    public static Armor ARMOR4 = new Armor("Wizard Shield", 1200, 10, 1500);
//    public static Armor ARMOR5 = new Armor("Guardian Angel", 1000, 10, 1000);
//
//    public static Weapon WEAPON1 = new Weapon("Sword", 500, 1, 800, 1);
//    public static Weapon WEAPON2 = new Weapon("Bow", 300, 2, 500, 2);
//    public static Weapon WEAPON3 = new Weapon("Scythe", 1000, 6, 1100, 2);
//    public static Weapon WEAPON4 = new Weapon("Axe", 550, 5, 850, 1);
//    public static Weapon WEAPON5 = new Weapon("TSwords", 1400, 8, 1600, 2);
//    public static Weapon WEAPON6 = new Weapon("Dagger", 200, 1, 250, 1);
//
//    public static List<Item> all() {
//        return Arrays.asList(POTION1, POTION2, POTION3, POTION4, POTION5, ARMOR1, ARMOR2, ARMOR3, ARMOR4, ARMOR5,
//                WEAPON1, WEAPON2,
//                WEAPON3, WEAPON4, WEAPON5, WEAPON6);
//    }
//
//}

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * All available items
 */
public class Items {
    // Potions
    public static Potion POTION1 = new Potion("Healing Potion", 250, 1, 100, PotionType.HP);
    public static Potion POTION2 = new Potion("Strength Potion", 200, 1, 75, PotionType.STR);
    public static Potion POTION3 = new Potion("Magic Potion", 350, 2, 100, PotionType.MP);
    public static Potion POTION4 = new Potion("Luck Elixir", 500, 4, 65, PotionType.AGI);
    public static Potion POTION5 = new Potion("Mermaid Tears", 850, 5, 100, PotionType.AGI);

    // Armor
    public static Armor ARMOR1 = new Armor("Platinum Shield", 150, 1, 200);
    public static Armor ARMOR2 = new Armor("Breast plate", 350, 3, 600);
    public static Armor ARMOR3 = new Armor("Full Body Armor", 1000, 8, 1100);
    public static Armor ARMOR4 = new Armor("Wizard Shield", 1200, 10, 1500);
    public static Armor ARMOR5 = new Armor("Guardian Angel", 1000, 10, 1000);

    // Weapons
    public static Weapon WEAPON1 = new Weapon("Sword", 500, 1, 800, 1);
    public static Weapon WEAPON2 = new Weapon("Bow", 300, 2, 500, 2);
    public static Weapon WEAPON3 = new Weapon("Scythe", 1000, 6, 1100, 2);
    public static Weapon WEAPON4 = new Weapon("Axe", 550, 5, 850, 1);
    public static Weapon WEAPON5 = new Weapon("TSwords", 1400, 8, 1600, 2);
    public static Weapon WEAPON6 = new Weapon("Dagger", 200, 1, 250, 1);

    // Get all items
    public static List<Item> all() {
        return Arrays.asList(
                POTION1, POTION2, POTION3, POTION4, POTION5,
                ARMOR1, ARMOR2, ARMOR3, ARMOR4, ARMOR5,
                WEAPON1, WEAPON2, WEAPON3, WEAPON4, WEAPON5, WEAPON6
        );
    }

    // Get items by type
    public static List<Item> getPotions() {
        return Arrays.asList(POTION1, POTION2, POTION3, POTION4, POTION5);
    }

    public static List<Item> getArmor() {
        return Arrays.asList(ARMOR1, ARMOR2, ARMOR3, ARMOR4, ARMOR5);
    }

    public static List<Item> getWeapons() {
        return Arrays.asList(WEAPON1, WEAPON2, WEAPON3, WEAPON4, WEAPON5, WEAPON6);
    }

    // Get items by level
    public static List<Item> getItemsByLevel(int level) {
        return all().stream()
                .filter(item -> {
                    if (item instanceof Weapon) {
                        return ((Weapon)item).getLevel() <= level;
                    } else if (item instanceof Armor) {
                        return ((Armor)item).getLevel() <= level;
                    } else if (item instanceof Potion) {
                        return ((Potion)item).getLevel() <= level;
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    // Get items by price range
    public static List<Item> getItemsByPriceRange(double minPrice, double maxPrice) {
        return all().stream()
                .filter(item -> item.getPrice() >= minPrice && item.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    // Helper method to get item by name
    public static Item getItemByName(String name) {
        return all().stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // Helper method to check if item exists
    public static boolean itemExists(String name) {
        return all().stream()
                .anyMatch(item -> item.getName().equalsIgnoreCase(name));
    }
}