/*
 * Create monsters which will fight the heroes
 */
public interface MonsterFactory {

    MonsterParty summon(HeroParty heroParty);

}
