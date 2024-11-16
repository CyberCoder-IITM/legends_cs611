import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
 * All monsters
 */
public class Monsters {
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

    private static Monster dragon(String name, int level, int damage, int defense, int dodge) {
        return new Monster(name, MonsterType.DRAGON, level, 100, damage, defense, dodge);
    }

    private static Monster exo(String name, int level, int damage, int defense, int dodge) {
        return new Monster(name, MonsterType.EXOSKELETON, level, 100, damage, defense, dodge);
    }

    private static Monster spirit(String name, int level, int damage, int defense, int dodge) {
        return new Monster(name, MonsterType.SPIRIT, level, 100, damage, defense, dodge);
    }

    public static List<Monster> all() {
        return Arrays.asList(DRAGON1, DRAGON2, DRAGON3, DRAGON4, DRAGON5, DRAGON6, EXO1, EXO2, EXO3, EXO4, SPIRIT1,
                SPIRIT2, SPIRIT3, SPIRIT4);
    }

    public static Monster random(Random rand) {
        List<Monster> all = all();
        return all.get(rand.nextInt(all.size()));
    }
}
