import java.util.List;
import java.util.stream.Collectors;
import java.util.*;

/*
 * The core component where the game is played
*/
public class Legends {
    private Board<CellType> board;
    private List<Lane> lanes;
    private HeroParty heroParty;
    private MonsterParty monsterParty;
    private Market market;
    private IOHelper ioHelper;
    private MarketStrategy marketStrategy;
    private FightStrategy fightStrategy;
    private MonsterFactory monsterFactory;
    private int currentRound;
    private Monsters.SpawnFrequency spawnFrequency;

    public Legends(Board<CellType> board,
                   List<Lane> lanes,
                   HeroParty heroParty,
                   Market market,
                   MarketStrategy marketStrategy,
                   FightStrategy fightStrategy,
                   MonsterFactory monsterFactory,
                   IOHelper ioHelper) {
        this.board = board;
        this.lanes = lanes;
        this.heroParty = heroParty;
        this.market = market;
        this.marketStrategy = marketStrategy;
        this.fightStrategy = fightStrategy;
        this.monsterFactory = monsterFactory;
        this.ioHelper = ioHelper;
        this.monsterParty = new MonsterParty();
        this.currentRound = 0;
        this.spawnFrequency = Monsters.SpawnFrequency.MEDIUM;

        spawnNewMonsters();
    }

    public void play() {
        displayWelcomeMessage();

        // Ensure initial monster wave exists
        if (monsterParty.getAllMonsters().isEmpty()) {
            spawnNewMonsters();
        }


        while (true) {
            currentRound++;
            ioHelper.println("\n=== Round " + currentRound + " ===");

            // Display current board state
            displayBoard();

            // Heroes' turn
            if (!handleHeroesTurn()) {
                handleVictory(true); // Heroes won
                break;
            }

            // Monsters' turn
            if (!handleMonstersTurn()) {
                handleVictory(false); // Monsters won
                break;
            }

            // End of round operations
            handleEndOfRound();
        }
    }

    private boolean handleHeroesTurn() {
        for (Hero hero : heroParty.getHeroes()) {
            displayHeroStatus(hero);

            while (true) {
                String action = ioHelper.nextLine(
                        "Choose action:\n" +
                                "1. Move (W/A/S/D)\n" +
                                "2. Attack\n" +
                                "3. Cast Spell\n" +
                                "4. Teleport\n" +
                                "5. Recall\n" +
                                "6. Use Item\n" +
                                "7. Market (only in Nexus)\n" +
                                "Q. Quit\n" +
                                "Choice: ",
                        "Invalid choice",
                        s -> "1234567Qq".contains(s)
                );

                if (action.equalsIgnoreCase("Q")) {
                    return false;
                }

                if (executeHeroAction(hero, action)) {
                    if (hero.getCurrentPosition().isMonsterNexus()) {
                        return false; // Heroes win
                    }
                    break;
                }
            }
        }
        return true;
    }
    private boolean handleMonstersTurn() {
        // Check if monsterParty is null or needs initialization
        if (monsterParty == null || monsterParty.getAllMonsters().isEmpty()) {
            // Initialize first wave of monsters
            spawnNewMonsters();
            return true;
        }

        // Monster attacks
        for (Monster monster : monsterParty.getAllMonsters()) {
            if (monster == null) continue;  // Skip if monster is null

            // Try to attack first
            boolean attacked = false;
            for (Hero hero : heroParty.getHeroes()) {
                if (monster.canAttack(hero.getCurrentPosition())) {
                    hero.takeDamage(monster.getDamage());
                    attacked = true;
                    break;
                }
            }

            // If didn't attack, move forward
            if (!attacked) {
                if (monster.moveForward(board)) {
                    return false; // Monsters reached hero nexus
                }
            }
        }
        return true;
    }



    private void handleEndOfRound() {
        // Restore heroes
        for (Hero hero : heroParty.getHeroes()) {
            if (hero.getStats().getHp() > 0) {
                hero.getStats().restorePercentHP(0.1);
                hero.getStats().restorePercentMP(0.1);
            }
        }

        // Respawn dead heroes
        for (Hero hero : heroParty.getHeroes()) {
            if (hero.getStats().getHp() <= 0) {
                hero.respawn();
            }
        }

        // Check for monster spawning
        if (Monsters.shouldSpawn(currentRound, spawnFrequency)) {
            spawnNewMonsters();
        }
    }

    private void spawnNewMonsters() {
        int maxHeroLevel = heroParty.getHeroes().stream()
                .mapToInt(Hero::getLevel)
                .max()
                .orElse(1);

        monsterParty.spawnNewWave(lanes, maxHeroLevel);
    }

    private boolean executeHeroAction(Hero hero, String action) {
        switch (action) {
            case "1": return handleHeroMove(hero);
            case "2": return handleHeroAttack(hero);
            case "3": return handleHeroSpell(hero);
            case "4": return handleHeroTeleport(hero);
            case "5": return handleHeroRecall(hero);
            case "6": return handleHeroItem(hero);
            case "7": return handleMarket(hero);
            default: return false;
        }
    }

    private void displayBoard() {
        ioHelper.println("\nCurrent Board State:");
        ioHelper.println("N: Nexus  I: Inaccessible  B: Bush  C: Cave  K: Koulou  P: Plain");
        ioHelper.println("H: Hero   M: Monster       X: Combat\n");

        // Display board implementation
        board.display();
    }

    private void handleVictory(boolean heroesWon) {
        if (heroesWon) {
            ioHelper.println("\nVICTORY! The heroes have reached the monster's nexus!");
        } else {
            ioHelper.println("\nDEFEAT! The monsters have reached your nexus!");
        }
        displayFinalStats();
    }

    // Additional helper methods....

    private void displayHeroStatus(Hero hero) {
        ioHelper.println("\n=== " + hero.getName() + "'s Status ===");
        ioHelper.println("HP: " + hero.getStats().getHp() + "/" + hero.getStats().getMaxHp());
        ioHelper.println("MP: " + hero.getStats().getMp() + "/" + hero.getStats().getMaxMp());
        ioHelper.println("Position: " + hero.getCurrentPosition());
        ioHelper.println("Current Space: " + board.at(hero.getCurrentPosition()).getValue());
    }

    private boolean handleHeroMove(Hero hero) {
        String direction = ioHelper.nextLine(
                "Enter direction (W/A/S/D): ",
                "Invalid direction",
                s -> "WASDwasd".contains(s)
        ).toUpperCase();

        Direction moveDir;
        switch(direction) {
            case "W": moveDir = Direction.UP; break;
            case "S": moveDir = Direction.DOWN; break;
            case "A": moveDir = Direction.LEFT; break;
            case "D": moveDir = Direction.RIGHT; break;
            default: return false;
        }

        return hero.move(moveDir, board);
    }


private boolean handleHeroAttack(Hero hero) {
    // Get attackable monsters
    List<Monster> attackableMonsters = getAttackableMonsters(hero);

    // Check if there are any monsters to attack
    if (attackableMonsters.isEmpty()) {
        ioHelper.println("No monsters in range to attack!");
        return false    ;
    }

    // Display attackable monsters
    displayAttackableMonsters(attackableMonsters);

    // Get player's choice
    int choice = ioHelper.nextLineInt(
            "Choose monster to attack (1-" + attackableMonsters.size() + "): ",
            "Invalid choice",
            i -> i >= 1 && i <= attackableMonsters.size()
    );

    // Attack chosen monster
    Monster target = attackableMonsters.get(choice - 1);
    performHeroAttack(hero, target);
    return true;
}

    private List<Monster> getAttackableMonsters(Hero hero) {
        List<Monster> attackable = new ArrayList<>();
        Lane heroLane = hero.getAssignedLane();

        if (heroLane != null) {
            for (Monster monster : heroLane.getMonsters()) {
                if (hero.canAttack(monster.getCurrentPosition())) {
                    attackable.add(monster);
                }
            }
        }
        return attackable;
    }

    private void displayAttackableMonsters(List<Monster> monsters) {
        ioHelper.println("\nMonsters in range:");
        for (int i = 0; i < monsters.size(); i++) {
            Monster m = monsters.get(i);
            ioHelper.println((i+1) + ". " + m.getName() +
                    " (HP: " + m.getHp() +
                    ", Lane: " + (m.getAssignedLane().getLaneNumber() + 1) + ")");
        }
    }

    private void performHeroAttack(Hero hero, Monster target) {
        if (target.dodged()) {
            ioHelper.println(target.getName() + " dodged the attack!");
            return;
        }

        double damage = hero.damageValue();
        target.takeDamage(damage);
        ioHelper.println(hero.getName() + " dealt " + damage + " damage to " + target.getName());

        if (target.getHp() <= 0) {
            handleMonsterDeath(target);
        }
    }

    private void handleMonsterDeath(Monster target) {
        ioHelper.println(target.getName() + " has been defeated!");
        monsterParty.removeMonster(target);
        distributeRewards(target);
    }

    private void distributeRewards(Monster monster) {
        int goldReward = 500 * monster.getLevel();
        int expReward = 2 * monster.getLevel();

        for (Hero hero : heroParty.getHeroes()) {
            hero.increaseGold((double)goldReward);
            // Add experience points handling if implemented
            ioHelper.println(hero.getName() + " received " + goldReward + " gold!");
        }
    }


    private boolean handleHeroSpell(Hero hero) {
        List<Item> spells = hero.getInventory().stream()
                .filter(i -> i.getType() == ItemType.SPELL)
                .collect(Collectors.toList());

        if (spells.isEmpty()) {
            ioHelper.println("No spells available!");
            return false;
        }

        displaySpells(spells);
        String spellName = ioHelper.nextLine(
                "Choose spell to cast (or 'q' to cancel): ",
                "Invalid spell",
                s -> s.equals("q") || spells.stream().anyMatch(spell -> spell.getName().equals(s))
        );

        if (spellName.equals("q")) return false;

        List<Monster> inRangeMonsters = monsterParty.getAllMonsters().stream()
                .filter(m -> hero.canAttack(m.getCurrentPosition()))
                .collect(Collectors.toList());

        if (inRangeMonsters.isEmpty()) {
            ioHelper.println("No monsters in range!");
            return false;
        }

        displayAttackableMonsters(inRangeMonsters);
        int choice = ioHelper.nextLineInt(
                "Choose target (1-" + inRangeMonsters.size() + "): ",
                "Invalid choice",
                i -> i >= 1 && i <= inRangeMonsters.size()
        );

        Monster target = inRangeMonsters.get(choice - 1);
        Item spell = spells.stream()
                .filter(s -> s.getName().equals(spellName))
                .findFirst().get();

        return hero.castSpell(spell, target);
    }

    private boolean handleHeroTeleport(Hero hero) {
        List<Hero> otherHeroes = heroParty.getHeroes().stream()
                .filter(h -> h != hero && !h.getCurrentPosition().isInSameLane(hero.getCurrentPosition()))
                .collect(Collectors.toList());

        if (otherHeroes.isEmpty()) {
            ioHelper.println("No valid heroes to teleport to!");
            return false;
        }

        displayTeleportTargets(otherHeroes);
        int choice = ioHelper.nextLineInt(
                "Choose hero to teleport to (1-" + otherHeroes.size() + "): ",
                "Invalid choice",
                i -> i >= 1 && i <= otherHeroes.size()
        );

        Hero target = otherHeroes.get(choice - 1);
        return hero.teleport(target.getCurrentPosition(), board);
    }

    private boolean handleHeroRecall(Hero hero) {
        hero.recall(board);
        ioHelper.println(hero.getName() + " recalled to Nexus");
        return true;
    }

    private boolean handleHeroItem(Hero hero) {
        if (hero.getInventory().isEmpty()) {
            ioHelper.println("No items in inventory!");
            return false;
        }

        displayInventory(hero);
        String itemName = ioHelper.nextLine(
                "Choose item to use (or 'q' to cancel): ",
                "Invalid item",
                s -> s.equals("q") || hero.getInventory().stream()
                        .anyMatch(item -> item.getName().equals(s))
        );

        if (itemName.equals("q")) return false;

        Item item = hero.getInventory().stream()
                .filter(i -> i.getName().equals(itemName))
                .findFirst().get();

        return hero.doItemAction(item);
    }

    private boolean handleMarket(Hero hero) {
        if (!hero.getCurrentPosition().isHeroNexus()) {
            ioHelper.println("Must be in Nexus to access market!");
            return false;
        }

        market.handleTransaction(hero, ioHelper);
        return true;
    }

    private void displaySpells(List<Item> spells) {
        ioHelper.println("\nAvailable Spells:");
        for (Item spell : spells) {
            ioHelper.println(spell.getName());
        }
    }

    private void displayTeleportTargets(List<Hero> heroes) {
        ioHelper.println("\nPossible teleport targets:");
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            ioHelper.println((i+1) + ". " + h.getName() + " at " + h.getCurrentPosition());
        }
    }

    private void displayInventory(Hero hero) {
        ioHelper.println("\nInventory:");
        for (Item item : hero.getInventory()) {
            ioHelper.println(item.getName() + " (" + item.getType() + ")");
        }
    }

    private void displayFinalStats() {
        ioHelper.println("\nFinal Statistics:");
        ioHelper.println("Total Rounds: " + currentRound);

        for (Hero hero : heroParty.getHeroes()) {
            ioHelper.println("\n" + hero.getName() + ":");
            ioHelper.println("Level: " + hero.getLevel());
            ioHelper.println("Experience: " + hero.getExp());
            ioHelper.println("Gold: " + hero.getGold());
        }
    }

    private void displayWelcomeMessage() {
        ioHelper.println("Welcome to Legends of Valor!");
        ioHelper.println("-----------------------------");
        ioHelper.println("Reach the monster's nexus to win!");
        ioHelper.println("Don't let monsters reach your nexus!");
        ioHelper.println("\nControls:");
        ioHelper.println("W/A/S/D - Move");
        ioHelper.println("T - Teleport");
        ioHelper.println("R - Recall");
        ioHelper.println("I - Use Item");
        ioHelper.println("M - Market (at Nexus only)");
    }
}
