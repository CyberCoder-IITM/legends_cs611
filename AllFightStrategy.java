
/*
 * Concrete implementation of fight strategy
// */

public class
AllFightStrategy implements FightStrategy {
    private IOHelper ioHelper;
    private static final double TERRAIN_BONUS = 0.1; // 10% bonus

    public AllFightStrategy(IOHelper ioHelper) {
        this.ioHelper = ioHelper;
    }

    @Override
    public boolean fight(Hero hero, Monster monster, Board<CellType> board) {
        if (!canFight(hero, monster)) {
            ioHelper.println("Target is not in range!");
            return false;
        }

        return handleCombat(hero, monster, monster.getCurrentPosition(), board);
    }

    @Override
    public boolean handleCombat(Hero hero, Monster monster, Position targetPosition, Board<CellType> board) {
        // Display combat information
        ioHelper.println("Combat started between " + hero.getName() + " and " + monster.getName());
        showCombatStats(hero, monster);

        // Calculate damage with terrain bonuses
        double heroDamage = calculateDamage(hero, board);

        // Check if monster dodges
        if (monster.dodged()) {
            ioHelper.println("Monster " + monster.getName() + " dodged the attack!");
            return false;
        }

        // Apply damage to monster
        monster.takeDamage(heroDamage);
        ioHelper.println(monster.getName() + " took " + heroDamage + " damage!");

        // Check if monster died
        if (monster.getHp() <= 0) {
            handleMonsterDeath(monster, hero);
            return true;
        }

        // Monster counterattack if still alive
        if (monster.canAttack(hero.getCurrentPosition())) {
            double monsterDamage = monster.getDamage();
            hero.takeDamage(monsterDamage);
            ioHelper.println(hero.getName() + " took " + monsterDamage + " damage from counterattack!");

            if (hero.getStats().getHp() <= 0) {
                handleHeroDeath(hero);
            }
        }

        return false;
    }

    private boolean canFight(Hero hero, Monster monster) {
        Position heroPos = hero.getCurrentPosition();
        Position monsterPos = monster.getCurrentPosition();

        int dx = Math.abs(heroPos.getX() - monsterPos.getX());
        int dy = Math.abs(heroPos.getY() - monsterPos.getY());

        return (dx <= 1 && dy <= 1) && (dx + dy <= 1);
    }

    private double calculateDamage(Hero hero, Board<CellType> board) {
        double baseDamage = hero.damageValue();
        CellType cellType = board.at(hero.getCurrentPosition()).getValue();

        switch(cellType) {
            case BUSH:
                baseDamage *= (1 + TERRAIN_BONUS);
                ioHelper.println("Bush terrain bonus applied: +" + (TERRAIN_BONUS * 100) + "% damage");
                break;
            case KOULOU:
                baseDamage *= (1 + TERRAIN_BONUS);
                ioHelper.println("Koulou terrain bonus applied: +" + (TERRAIN_BONUS * 100) + "% damage");
                break;
            default:
                break;
        }

        return baseDamage;
    }

    private void handleMonsterDeath(Monster monster, Hero hero) {
        ioHelper.println(monster.getName() + " has been defeated!");

        // Calculate rewards
        int goldReward = 500 * monster.getLevel();
        int expReward = 2 * monster.getLevel();

        // Apply rewards
        hero.increaseGold((double)goldReward);
        // Add experience points and handle level up

        ioHelper.println("Rewards earned:");
        ioHelper.println("Gold: " + goldReward);
        ioHelper.println("Experience: " + expReward);
    }

    private void handleHeroDeath(Hero hero) {
        ioHelper.println(hero.getName() + " has fallen in battle!");
        ioHelper.println("Hero will respawn at their Nexus next round.");
    }

    private void showCombatStats(Hero hero, Monster monster) {
        ioHelper.println("\n=== Combat Stats ===");
        ioHelper.println("Hero: " + hero.getName());
        ioHelper.println("HP: " + hero.getStats().getHp());
        ioHelper.println("Damage: " + hero.damageValue());

        ioHelper.println("\nMonster: " + monster.getName());
        ioHelper.println("HP: " + monster.getHp());
        ioHelper.println("Damage: " + monster.getDamage());
        ioHelper.println("Dodge Chance: " + monster.getDodge() + "%");
    }
}