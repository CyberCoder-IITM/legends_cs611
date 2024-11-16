import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 * Concrete implementation of fight strategy
 */
public class AllFightStrategy implements FightStrategy {

    private IOHelper iohelper;

    public AllFightStrategy(IOHelper ioHelper) {
        this.iohelper = ioHelper;
    }

    @Override
    public boolean fight(HeroParty heroParty, MonsterParty monsterParty) {
        this.iohelper.println("fight started with the monsters");
        showMonsters(monsterParty);

        while (true) {
            for (Hero h : heroParty.getHeroes()) {
                if (h.getStats().getHp() == 0) {
                    continue;
                }

                Optional<Monster> monster = getMonsterToAttack(monsterParty, h);
                if (!monster.isPresent()) {
                    this.iohelper.println("All monsters are dead");
                    return false;
                }

                heroAttack(h, monster.get());
            }

            boolean allHeroesDied = monstersAttack(monsterParty, heroParty);
            if (allHeroesDied) {
                return true;
            }
        }

    }

    private boolean monstersAttack(MonsterParty monsterParty, HeroParty heroParty) {
        for (Monster m : monsterParty.getMonsters()) {
            if (m.getHp() == 0) {
                continue;
            }

            Optional<Hero> hero = getHeroToAttack(heroParty);
            if (!hero.isPresent()) {
                this.iohelper.println("all heroes are dead");
                return true;
            }

            double damage = m.getDamage();
            heroTakesDamage(hero.get(), m, damage);
        }
        return false;
    }

    private void heroTakesDamage(Hero hero, Monster monster, double damage) {
        this.iohelper.println(
                "hero " + hero.getName() + " has taken " + damage + " damage from the monster " + monster.getName());
        hero.takeDamage(damage);

        if (hero.getStats().getHp() == 0) {
            this.iohelper.println("hero " + hero.getName() + " has died");
        }
    }

    private Optional<Hero> getHeroToAttack(HeroParty heroParty) {
        return heroParty.getHeroes().stream().filter(h -> h.getStats().getHp() != 0).findAny();
    }

    private Optional<Monster> getMonsterToAttack(MonsterParty monsterParty, Hero h) {
        List<Monster> liveMonsters = monsterParty.getMonsters().stream().filter(m -> m.getHp() != 0)
                .collect(Collectors.toList());
        if (liveMonsters.isEmpty()) {
            return Optional.empty();
        }

        this.iohelper.println("the following monsters are alive: " +
                String.join(",", liveMonsters.stream().map(m -> m.getName()).collect(Collectors.toList())));

        this.iohelper.println("it is " + h.getName() + "'s turn");
        String stats = this.iohelper.nextLine("Do you want to see their stats?(y/n)", "not y/n",
                s -> s.equalsIgnoreCase("y") || s.equalsIgnoreCase("n"));
        if (stats.equalsIgnoreCase("y")) {
            showStats(h);
        }

        String monsterName = this.iohelper.nextLine("which monster do you want to attact?",
                "not a valid input or monster is dead",
                s -> liveMonsters.stream().anyMatch(m -> m.getName().equals(s)));
        return liveMonsters.stream().filter(m -> m.getName().equals(monsterName)).findAny();
    }

    private void heroAttack(Hero hero, Monster monster) {
        // check if a spell can be used and use it
        boolean usedSpell = spellCheck(hero, monster);
        if (usedSpell) {
            return;
        }

        // attack with weapon
        double damage = hero.damageValue();
        if (monster.dodged()) {
            this.iohelper.println("monster " + monster.getName() + " has dodged the attack");
            return;
        }

        monsterTakeDamage(hero, monster, damage);
    }

    private void monsterTakeDamage(Hero hero, Monster monster, double damage) {
        monster.takeDamage(damage);
        this.iohelper.println(
                "monster " + monster.getName() + " has taken " + damage + " damage from the hero " + hero.getName());

        if (monster.getHp() == 0) {
            this.iohelper.println("monster " + monster.getName() + " has died");
        }
    }

    private boolean spellCheck(Hero hero, Monster monster) {
        List<Item> spells = hero.getInventory().stream().filter(i -> i.getType() == ItemType.SPELL)
                .collect(Collectors.toList());
        if (spells.isEmpty()) {
            return false;
        }
        String useSpell = this.iohelper.nextLine(
                "hero has the folling spells: {"
                        + String.join(",", spells.stream().map(s -> s.getName()).collect(Collectors.toList()))
                        + "}, Do you want to use any?(y/n)",
                "not y or n", s -> s.equalsIgnoreCase("y") || s.equalsIgnoreCase("n"));
        if (useSpell.equalsIgnoreCase("y")) {
            String spellName = this.iohelper.nextLine("which spell do you want to use?", "not a valid spell name",
                    s -> spells.stream().anyMatch(x -> x.getName().equals(s)));
            Item spell = spells.stream().filter(s -> s.getName().equals(spellName)).findAny().get();
            // TODO: fill it
        }
        return true;
    }

    private void showStats(Hero h) {
        this.iohelper.println("------------- Heroe and their information ----------------");
        this.iohelper.println("Name         : " + h.getName());
        this.iohelper.println("Type         : " + h.getType().toString());
        this.iohelper.println("level        : " + h.getLevel());
        this.iohelper.println("experience   : " + h.getExp());
        this.iohelper.println("HP           : " + h.getStats().getHp());
        this.iohelper.println("MP           : " + h.getStats().getMp());
        this.iohelper.println("Strength     : " + h.getStats().getStr());
        this.iohelper.println("Dexterity    : " + h.getStats().getDex());
        this.iohelper.println("Agility      : " + h.getStats().getAgi());
        this.iohelper.println("Gold         : " + h.getGold());
    }

    private void showMonsters(MonsterParty monsterParty) {
        this.iohelper.println("Showing all monsters");
        for (Monster m : monsterParty.getMonsters()) {
            this.iohelper.println("------------------------------");
            this.iohelper.println("name     : " + m.getName());
            this.iohelper.println("type     : " + m.getType().toString());
            this.iohelper.println("level    : " + m.getLevel());
            this.iohelper.println("HP       : " + m.getHp());
            this.iohelper.println("damage   : " + m.getDamage());
            this.iohelper.println("defense  : " + m.getDefense());
            this.iohelper.println("dodge    : " + m.getDodge());
        }
    }

}
