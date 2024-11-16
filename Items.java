import java.util.Arrays;
import java.util.List;

/*
 * All available items
 */
public class Items {
    public static Potion POTION1 = new Potion("Healing Potion", 250, 1, 100, PotionType.HP);
    public static Potion POTION2 = new Potion("Strength Potion", 200, 1, 75, PotionType.STR);
    public static Potion POTION3 = new Potion("Magic Potion", 350, 2, 100, PotionType.MP);
    public static Potion POTION4 = new Potion("Luck Elixir", 500, 4, 65, PotionType.AGI);
    public static Potion POTION5 = new Potion("Mermaid Tears", 850, 5, 100, PotionType.AGI);

    public static Armor ARMOR1 = new Armor("Platinum Shield", 150, 1, 200);
    public static Armor ARMOR2 = new Armor("Breast plate", 350, 3, 600);
    public static Armor ARMOR3 = new Armor("Full Body Armor", 1000, 8, 1100);
    public static Armor ARMOR4 = new Armor("Wizard Shield", 1200, 10, 1500);
    public static Armor ARMOR5 = new Armor("Guardian Angel", 1000, 10, 1000);

    public static Weapon WEAPON1 = new Weapon("Sword", 500, 1, 800, 1);
    public static Weapon WEAPON2 = new Weapon("Bow", 300, 2, 500, 2);
    public static Weapon WEAPON3 = new Weapon("Scythe", 1000, 6, 1100, 2);
    public static Weapon WEAPON4 = new Weapon("Axe", 550, 5, 850, 1);
    public static Weapon WEAPON5 = new Weapon("TSwords", 1400, 8, 1600, 2);
    public static Weapon WEAPON6 = new Weapon("Dagger", 200, 1, 250, 1);

    public static List<Item> all() {
        return Arrays.asList(POTION1, POTION2, POTION3, POTION4, POTION5, ARMOR1, ARMOR2, ARMOR3, ARMOR4, ARMOR5,
                WEAPON1, WEAPON2,
                WEAPON3, WEAPON4, WEAPON5, WEAPON6);
    }

}
