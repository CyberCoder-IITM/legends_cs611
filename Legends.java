import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 * The core component where the game is played
 */
public class Legends {

    Position heroPosition;

    Board<CellType> board;
    HeroParty heroParty;
    Market market;
    MarketStrategy marketStrategy;
    FightStrategy fightStrategy;
    MonsterFactory monsterSummoner;
    IOHelper iohelper;

    public Legends(Position heroPosition, Board<CellType> board, HeroParty heroParty, Market market,
            MarketStrategy marketStrategy, FightStrategy fightStrategy, MonsterFactory monsterSummoner,
            IOHelper iohelper) {
        this.heroPosition = heroPosition;
        this.board = board;
        this.heroParty = heroParty;
        this.market = market;
        this.marketStrategy = marketStrategy;
        this.fightStrategy = fightStrategy;
        this.monsterSummoner = monsterSummoner;
        this.iohelper = iohelper;
    }

    public void play() {
        printWorld();
        showHeroInfo();
        while (true) {
            Move move = readMoveAndValidate();
            boolean heroesDied = false;
            switch (move) {
                case UP:
                    heroesDied = moveAndFight(Direction.UP);

                    break;
                case DOWN:
                    heroesDied = moveAndFight(Direction.DOWN);
                    break;
                case LEFT:
                    heroesDied = moveAndFight(Direction.LEFT);
                    break;
                case RIGHT:
                    heroesDied = moveAndFight(Direction.RIGHT);
                    break;
                case INFO:
                    showHeroInfo();
                    break;
                case MARKET:
                    marketStrategy.act(heroParty, market);
                    break;
                case PRINT:
                    printWorld();
                    break;
                case INVENTORY:
                    doInventoryActions();
                    break;
                case HELP:
                    showHelp();
                    break;
                case QUIT:
                    return;
            }

            if (heroesDied) {
                return;
            }
        }
    }

    private void showHelp() {
        this.iohelper.println("------------ Moves and information -----------------");
        this.iohelper.println("w    : move up");
        this.iohelper.println("a    : move left");
        this.iohelper.println("s    : move down");
        this.iohelper.println("d    : move right");
        this.iohelper.println("i    : show hero information");
        this.iohelper.println("m    : open market");
        this.iohelper.println("p    : show world");
        this.iohelper.println("v    : move hero inventory");
        this.iohelper.println("q    : quit");
        this.iohelper.println("h    : help");
    }

    private void doInventoryActions() {
        List<String> heroNames = this.heroParty.getHeroes().stream().map(Hero::getName).collect(Collectors.toList());
        this.iohelper.println("Heroes: " + String.join(", ", heroNames));

        String input = this.iohelper.nextLine("select a hero to show their inventory", "not a valid hero",
                heroNames::contains);
        Hero h = this.heroParty.getHeroes().stream().filter(x -> x.getName().equals(input)).findAny().get();

        if (h.getInventory().size() == 0) {
            this.iohelper.println("this hero does not have any items");
            return;
        }

        this.iohelper.println("inventory for the hero: " + input);
        for (Item i : h.getInventory()) {
            boolean isEquipped = h.getEquippedItems().contains(i);
            this.iohelper.println(
                    "name:" + i.getName() + " type:" + i.getType().toString() + (!isEquipped ? "" : " (Equipped)"));
        }

        String itemName = this.iohelper.nextLine("item to use(q to quit):", "not a valid item name",
                s -> s.equals("q")
                        || h.getInventory().stream().map(Item::getName).collect(Collectors.toList())
                                .contains(s));
        if (itemName.equals("q")) {
            return;
        }

        Item item = h.getInventory().stream().filter(i -> i.getName().equals(itemName)).findAny().get();
        if (h.getEquippedItems().contains(item)) {
            this.iohelper.printErr("item is already equipped");
            doInventoryActions();
            return;
        }

        h.doItemAction(item);
    }

    private void printWorld() {
        this.iohelper.println("This is the World");
        this.iohelper.println("H: heros");
        this.iohelper.println("M: market");
        this.iohelper.println("W: wall");
        this.printBoard();
    }

    // print the board in the terminal
    protected void printBoard() {
        List<List<Cell2D<CellType>>> b = this.board.getBoard();
        int n = b.size();
        int m = b.get(0).size();

        StringBuilder out = new StringBuilder();

        out.append(line(m));
        out.append("\n");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                out.append(String.format("|%2s", printCell(b.get(i).get(j).getValue())));
            }
            out.append("|\n");
            out.append(line(m));
            out.append("\n");
        }

        this.iohelper.print(out.toString());
    }

    protected String printCell(CellType c) {
        switch (c) {
            case NONE:
                return "";
            case HEROES:
                return "H";
            case MARKET:
                return "M";
            case MARKET_WITH_HEROES:
                return "HM";
            case WALL:
                return "W";
            default:
                return "N";
        }
    }

    // printing the lines to separate each cell and outline the board
    protected String line(int n) {
        return String.join("", Collections.nCopies(n, "+--")) + "+";
    }

    private void showHeroInfo() {
        for (Hero h : this.heroParty.getHeroes()) {
            this.iohelper.println("------------- Hero and their information ----------------");
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
    }

    private boolean moveAndFight(Direction dir) {
        Position newHeroPosition = heroPosition.move(dir);
        if (this.board.at(newHeroPosition).getValue() == CellType.MARKET) {
            this.board.set(newHeroPosition, CellType.MARKET_WITH_HEROES);
            this.board.set(heroPosition, CellType.NONE);
            this.heroPosition = newHeroPosition;
            this.heroParty.restoreAfterTurn();
            return false;
        } else if (this.board.at(heroPosition).getValue() == CellType.MARKET_WITH_HEROES) {
            this.board.set(newHeroPosition, CellType.HEROES);
            this.board.set(heroPosition, CellType.MARKET);
        } else {
            this.board.set(heroPosition, CellType.NONE);
            this.board.set(newHeroPosition, CellType.HEROES);
        }
        this.heroPosition = newHeroPosition;
        MonsterParty monsterParty = this.monsterSummoner.summon(this.heroParty);
        boolean heroesDied = this.fightStrategy.fight(this.heroParty, monsterParty);
        if (heroesDied) {
            return true;
        }
        this.heroParty.restoreAfterTurn();
        return false;
    }

    private Move readMoveAndValidate() {
        Move move = readHeroMove();
        if (isDirection(move)) {
            Direction dir = moveToDirection(move);
            Position newPostion = heroPosition.move(dir);
            if (!board.isValid(newPostion)) {
                iohelper.printErr("not a valid direction to move(going out of board)");
                return readMoveAndValidate();
            }

            if (board.at(newPostion).getValue().equals(CellType.WALL)) {
                iohelper.printErr("not a valid direction to move(encountered a wall)");
                return readMoveAndValidate();
            }
        }

        if (move == Move.MARKET && !board.at(heroPosition).getValue().equals(CellType.MARKET_WITH_HEROES)) {
            iohelper.printErr("heroes are not on a market space");
            return readMoveAndValidate();
        }
        return move;
    }

    private boolean isDirection(Move move) {
        return move == Move.UP || move == Move.DOWN || move == Move.RIGHT || move == Move.LEFT;
    }

    private Direction moveToDirection(Move move) {
        switch (move) {
            case UP:
                return Direction.UP;
            case DOWN:
                return Direction.DOWN;
            case LEFT:
                return Direction.LEFT;
            case RIGHT:
                return Direction.RIGHT;
            default:
                return Direction.UP;
        }
    }

    private Move readHeroMove() {
        String input = this.iohelper.nextLine("Move(w/a/s/d/q/i/m/p/v/h): ", "is not a valid input",
                s -> s.toLowerCase().matches("[wasdqimpvh]"));

        switch (input.toLowerCase()) {
            case "w":
                return Move.UP;
            case "a":
                return Move.LEFT;
            case "s":
                return Move.DOWN;
            case "d":
                return Move.RIGHT;
            case "i":
                return Move.INFO;
            case "m":
                return Move.MARKET;
            case "p":
                return Move.PRINT;
            case "v":
                return Move.INVENTORY;
            case "q":
                return Move.QUIT;
            case "h":
                return Move.HELP;
            default:
                break;
        }
        return Move.NONE;
    }

}
