import java.util.Set;

/*
 * A party of heroes
 */
public class HeroParty {
    private Set<Hero> heroes;

    public Set<Hero> getHeroes() {
        return heroes;
    }

    public HeroParty(Set<Hero> heroes) {
        this.heroes = heroes;
    }

    public void restoreAfterTurn() {
        heroes.stream().forEach(h -> h.restoreAfterTurn());
    }
}
