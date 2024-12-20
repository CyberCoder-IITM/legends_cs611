import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * The core component where the game is played
*/
public class Legends implements RPGGame {
    private Board<CellType> board;
    private List<Lane> lanes;
    private HeroParty heroParty;
    private MonsterParty monsterParty;
    private Market market;
    private IOHelper ioHelper;
    private MonsterFactory monsterFactory;
    private int currentRound;
    private Monsters.SpawnFrequency spawnFrequency;

    public Legends(Board<CellType> board,
            List<Lane> lanes,
            HeroParty heroParty,
            Market market,
            MonsterFactory monsterFactory,
            IOHelper ioHelper) {
        this.board = board;
        this.lanes = lanes;
        this.heroParty = heroParty;
        this.market = market;
        this.monsterFactory = monsterFactory;
        this.ioHelper = ioHelper;
        this.monsterParty = new MonsterParty();
        this.currentRound = 1;
        this.spawnFrequency = Monsters.SpawnFrequency.MEDIUM;

    }

    public void play() {
        displayWelcomeMessage();
        // if (monsterParty.getAllMonsters().isEmpty()) {
        // spawnNewMonsters();
        // }
        spawnNewMonsters();

        while (true) {
            // currentRound++;
            ioHelper.println("\n=== Round " + currentRound + " ===");
            displayBoard();
            ioHelper.println("\n=== Heroes' Turn ===");
            for (Hero hero : heroParty.getHeroes()) {
                displayHeroStatus(hero);
                // Check victory before hero's action

                if (hero.getCurrentPosition().isMonsterNexus()) {
                    handleVictory(true);
                    return;
                }
                // Hero's action

                while (true) {
                    String action = getHeroAction(hero);
                    if (executeHeroAction(hero, action)) {
                        break;
                    }
                }

                // Check victory after hero's action
                if (hero.getCurrentPosition().isMonsterNexus()) {
                    handleVictory(true);
                    return;
                }
            }

            // Monster either attacks if hero in range or moves south
            ioHelper.println("\n=== Monsters' Turn ===");
            if (!handleMonstersTurn()) {
                // Check monster victory condition

                for (Monster monster : monsterParty.getAllMonsters()) {
                    if (monster.getCurrentPosition().isHeroNexus()) {
                        handleVictory(false);
                        return;
                    }
                }
                continue;
            }

            // End of round operations
            handleEndOfRound();

            // Spawn new monsters every 8 rounds (as per PDF)
            if (currentRound % 8 == 0) {
                spawnNewMonsters();
            }
            currentRound++;
        }
    }

    private String getHeroAction(Hero hero) {
        return ioHelper.nextLine(
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
                s -> "1234567Qq".contains(s));
    }

    private boolean handleMonstersTurn() {
        return monsterParty.handleMonstersTurn(board, heroParty);
    }

    private void handleEndOfRound() {
        // Restore heroes
        for (Hero hero : heroParty.getHeroes()) {
            if (hero.getStats().getHp() > 0) {
                hero.getStats().restorePercentHP(0.1);
                hero.getStats().restorePercentMP(0.1);
            }
        }

        // Spawn new monsters every 8 rounds
        // if (currentRound % 8 == 0) {
        // ioHelper.println("\nNew monsters are spawning!");
        // spawnNewMonsters();
        // }
        // Spawn new monsters every 8 rounds
        if (currentRound > 0 && currentRound % 8 == 0) {
            spawnNewMonsters();
        }

        // Respawn dead heroes
        for (Hero hero : heroParty.getHeroes()) {
            if (hero.getStats().getHp() <= 0) {
                hero.respawn();
                ioHelper.println(hero.getName() + " has respawned at their Nexus!");
            }
        }

        monsterParty.removeDeadMonsters();

    }

    private void spawnNewMonsters() {
        // Get highest hero level for scaling
        int maxHeroLevel = heroParty.getHighestLevel();

        // Spawn one monster per lane
        for (Lane lane : lanes) {
            Position spawnPos = new Position(0, lane.getMonsterNexus().getY());
            Monster monster = monsterFactory.createMonster(maxHeroLevel, spawnPos, lane);
            monsterParty.addMonster(monster);
            board.placeMonster(spawnPos, monster);
            ioHelper.println("Spawned " + monster.getName() + " (Level " + maxHeroLevel + ") in lane " +
                    (lane.getLaneNumber() + 1));
        }
    }

    private boolean executeHeroAction(Hero hero, String action) {
        switch (action) {
            // case "1": return handleHeroMove(hero);
            case "1":
                String direction = ioHelper.nextLine(
                        "[+] Enter direction (W/A/S/D): ",
                        "Invalid direction",
                        s -> "WASDwasd".contains(s)).toUpperCase();

                Direction moveDir;
                switch (direction) {
                    case "W":
                        moveDir = Direction.UP;
                        break;
                    case "S":
                        moveDir = Direction.DOWN;
                        break;
                    case "A":
                        moveDir = Direction.LEFT;
                        break;
                    case "D":
                        moveDir = Direction.RIGHT;
                        break;
                    default:
                        return false;
                }

                boolean moved = hero.move(moveDir, board);
                if (moved) {
                    ioHelper.println(hero.getName() + " moved " + direction);
                    displayBoard(); // Add only this line to show updated board
                    return true;
                }
                ioHelper.println("Invalid move!");
                return false;
            case "2":
                return handleHeroAttack(hero);
            case "3":
                return handleHeroSpell(hero);
            case "4":
                return handleHeroTeleport(hero);
            case "5":
                return handleHeroRecall(hero);
            case "6":
                return handleHeroItem(hero);
            case "7":
                return handleMarket(hero);
            default:
                return false;
        }
    }

    private void displayBoard() {
        // Display board implementation
        board.display();
    }

    private void checkVictoryConditions() {
        // Check if any hero reached monster nexus
        for (Hero hero : heroParty.getHeroes()) {
            if (hero.getCurrentPosition().isMonsterNexus()) {
                handleVictory(true);
                return;
            }
        }

        // Check if any monster reached hero nexus
        for (Monster monster : monsterParty.getAllMonsters()) {
            if (monster.getCurrentPosition().isHeroNexus()) {
                handleVictory(false);
                return;
            }
        }
    }

    private void handleVictory(boolean heroesWon) {
        if (heroesWon) {
            ioHelper.println("\nVICTORY! The heroes have reached the monster's nexus!");
        } else {
            ioHelper.println("\nDEFEAT! The monsters have reached your nexus!");
        }
        displayFinalStats();
        // System.exit(0);
    }

    // Additional helper methods....

    private void displayHeroStatus(Hero hero) {
        ioHelper.println("\n=== " + hero.getName() + "'s Status ===");
        ioHelper.println("HP: " + hero.getStats().getHp() + "/" + hero.getStats().getMaxHp());
        ioHelper.println("MP: " + hero.getStats().getMp() + "/" + hero.getStats().getMaxMp());
        ioHelper.println("Position: " + hero.getCurrentPosition());
        ioHelper.println("Current Space: " + board.at(hero.getCurrentPosition()).getValue());
    }

    private boolean handleHeroTurn(Hero hero) {
        displayHeroStatus(hero);

        while (true) {
            String action = getHeroAction(hero);
            if (executeHeroAction(hero, action)) {
                // After successful action, show updated board
                ioHelper.println("\nAfter " + hero.getName() + "'s action:");
                displayBoard();
                return true;
            }
        }
    }

    private boolean handleHeroMove(Hero hero) {
        String direction = ioHelper.nextLine(
                "Enter direction (W/A/S/D): ",
                "Invalid direction",
                s -> "WASDwasd".contains(s)).toUpperCase();

        Direction moveDir;
        switch (direction) {
            case "W":
                moveDir = Direction.UP;
                break;
            case "S":
                moveDir = Direction.DOWN;
                break;
            case "A":
                moveDir = Direction.LEFT;
                break;
            case "D":
                moveDir = Direction.RIGHT;
                break;
            default:
                return false;
        }

        return hero.move(moveDir, board);
    }

    private boolean handleHeroAttack(Hero hero) {
        // Get attackable monsters
        List<Monster> attackableMonsters = new ArrayList<>();
        Position heroPos = hero.getCurrentPosition();

        // Check all adjacent positions including current position for monsters
        for (Monster monster : monsterParty.getAllMonsters()) {
            Position monsterPos = monster.getCurrentPosition();
            int dx = Math.abs(heroPos.getX() - monsterPos.getX());
            int dy = Math.abs(heroPos.getY() - monsterPos.getY());

            // Monster is in range if it's in current space or adjacent (no diagonals)
            if ((dx == 0 && dy == 0) || (dx + dy == 1)) {
                attackableMonsters.add(monster);
            }
        }

        if (attackableMonsters.isEmpty()) {
            ioHelper.println("No monsters in range to attack!");
            return false;
        }

        // Display attackable monsters
        ioHelper.println("\nMonsters in range:");
        for (int i = 0; i < attackableMonsters.size(); i++) {
            Monster m = attackableMonsters.get(i);
            ioHelper.println((i + 1) + ". " + m.getName() +
                    " (HP: " + m.getHp() + ", Position: " + m.getCurrentPosition() + ")");
        }

        // Get player's choice
        int choice = ioHelper.nextLineInt(
                "Choose monster to attack (1-" + attackableMonsters.size() + "): ",
                "Invalid choice",
                i -> i >= 1 && i <= attackableMonsters.size());

        // Attack chosen monster
        Monster target = attackableMonsters.get(choice - 1);
        double damage = hero.damageValue();

        if (target.dodged()) {
            ioHelper.println(target.getName() + " dodged the attack!");
            displayBoard(); // Show board after dodge
            return true;
        }

        target.takeDamage(damage);
        ioHelper.println(hero.getName() + " dealt " + damage + " damage to " +
                target.getName() + "!");

        if (target.getHp() <= 0) {
            ioHelper.println(target.getName() + " has been defeated!");
            board.removeMonster(target.getCurrentPosition());
            monsterParty.removeMonster(target);
            // distributeRewards(target);

            int goldReward = 500 * target.getLevel();
            for (Hero h : heroParty.getHeroes()) {
                h.increaseGold((double) goldReward);
                ioHelper.println(h.getName() + " received " + goldReward + " gold!");
            }
            // Display updated board after monster defeat
            ioHelper.println("\nAfter monster defeat:");
            displayBoard();
        } else {
            // Display board after attack
            ioHelper.println("\nAfter attack:");
            displayBoard();
        }

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
            ioHelper.println((i + 1) + ". " + m.getName() +
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
            hero.increaseGold((double) goldReward);
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
                s -> s.equals("q") || spells.stream().anyMatch(spell -> spell.getName().equals(s)));

        if (spellName.equals("q"))
            return false;

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
                i -> i >= 1 && i <= inRangeMonsters.size());

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
                i -> i >= 1 && i <= otherHeroes.size());

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
                        .anyMatch(item -> item.getName().equals(s)));

        if (itemName.equals("q"))
            return false;

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
            ioHelper.println((i + 1) + ". " + h.getName() + " at " + h.getCurrentPosition());
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
