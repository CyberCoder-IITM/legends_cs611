/*
 * Create monsters which will fight the heroes
 */
public interface MonsterFactory {

    MonsterParty summon(HeroParty heroParty);
    Monster createMonster(int heroLevel, Position spawnPosition, Lane lane);


}