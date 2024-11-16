/*
 * Strategy pattern for fight simulation
 */
public interface FightStrategy {

    boolean fight(HeroParty heroParty, MonsterParty monsterParty);

}
