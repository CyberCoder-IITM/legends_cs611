/*
 * Strategy pattern for fight simulation
 */
public interface FightStrategy {
    boolean fight(Hero hero, Monster monster, Board<CellType> board);
    boolean handleCombat(Hero hero, Monster monster, Position targetPosition, Board<CellType> board);
}

