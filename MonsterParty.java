import java.util.Set;

/*
 * A party of monsters
 */
public class MonsterParty {
    Set<Monster> monsters;

    public Set<Monster> getMonsters() {
        return monsters;
    }

    public MonsterParty(Set<Monster> monsters) {
        this.monsters = monsters;
    }
}
