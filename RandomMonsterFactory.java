import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/*
 * Generates random number of monsters
 */
public class RandomMonsterFactory implements MonsterFactory {

    private Random rand;

    public RandomMonsterFactory(Random rand) {
        this.rand = rand;
    }

    @Override
    public MonsterParty summon(HeroParty heroParty) {
        List<Monster> all = Arrays.asList(Monsters.random(rand), Monsters.random(rand));
        return new MonsterParty(new HashSet<>(all));
    }

}
