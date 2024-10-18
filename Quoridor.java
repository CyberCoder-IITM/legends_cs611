import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Quoridor extends Game<QuoridorPiece> {

    int numPlayers;
    int numWalls;
    // initializer for quoridor class
    public static Quoridor init(int n, int m, int numPlayers, int numWalls) {
        Board<QuoridorPiece> board = new Board<>(2 * n + 1, 2 * m + 1, QuoridorPiece::Empty);
        for (int i = 0; i < 2 * n + 1; i++) {
            board.set(new Position(i, 0), QuoridorPiece.Wall());
            board.set(new Position(i, 2 * m), QuoridorPiece.Wall());
        }

        for (int j = 0; j < 2 * m + 1; j++) {
            board.set(new Position(0, j), QuoridorPiece.Wall());
            board.set(new Position(2 * n, j), QuoridorPiece.Wall());
        }

        for (int i = 0; i < 2 * n + 1; i += 2) {
            for (int j = 0; j < 2 * m + 1; j += 2) {
                board.set(new Position(i, j), QuoridorPiece.Wall());
            }
        }

        return new Quoridor(board, new IOHelper(new Scanner(System.in)), numPlayers, numWalls);
    }
    // private quoridor constructor
    private Quoridor(Board<QuoridorPiece> board, IOHelper ioHelper, int numPlayers, int numWalls) {
        super(board, ioHelper);
        this.numPlayers = numPlayers;
        this.numWalls = numWalls;
    }
    // implement quoridor play for game play, rules and logic of the game
    @Override
    public void play() {
        int turn = 0;
        QuoridorConfig allConfigs = QuoridorConfig.init(this.numPlayers, this.numWalls, this.board, iohelper);
        for (QuoridorPlayerConfig c : allConfigs.getConfigs()) {
            this.board.set(c.getPosition(), new QuoridorPiece(QuoridorPieceType.Pawn, c.getLabel()));
        }

        this.printBeforeStart(allConfigs);

        while (true) {
            QuoridorPlayerConfig playerConf = allConfigs.getForTurn(turn);
            this.printQuoridor();

            // do move
            this.doMove(playerConf, allConfigs);

            // check status
            if (playerConf.isFinish(playerConf.getPositionInBoard(board))) {
                this.printQuoridor();
                this.iohelper.println("Game completed, winner is player: " + playerConf.getPlayer().getName());
                return;
            }

            turn++;
        }
    }
    // ask the user if what type of move they want to make, place wall or move pawn
    private void doMove(QuoridorPlayerConfig conf, QuoridorConfig allConfigs) {
        this.iohelper.prompt("Making a move for player:" + conf.getPlayer().getName() + ", pawn:" + conf.getLabel()
                + ", remaining walls:" + conf.getRemainingWalls());

        if (conf.canPlaceWalls()) {
            String pawnMove = this.iohelper.nextLine("Are you moving the pawn? (y/n)", "should be Y or N",
                    s -> s.equalsIgnoreCase("y") || s.equalsIgnoreCase("n"));

            if (pawnMove.equalsIgnoreCase("y")) {
                doPawnMove(conf);
            } else {
                doWallMove(conf, allConfigs);
                conf.decreaseRemainingWalls();
            }
        } else {
            this.iohelper.prompt("player has exhausted all their walls");
            doPawnMove(conf);
        }

    }
    // logic for placing a wall piece on the board, both validation for position and setting the wall piece
    private void doWallMove(QuoridorPlayerConfig conf, QuoridorConfig allConfigs) {
        String s = this.iohelper
                .nextLine("Input where the wall should be placed, format: { {number}, {number}, {u/d/r/l} }");

        String[] split = s.split(",");
        if (split.length != 3) {
            this.iohelper.printErr("not a valid input, format: { {number}, {number}, {u/d/r/l} }");
            doWallMove(conf, allConfigs);
            return;
        }

        int first = 0;
        int second = 0;
        String dir = split[2].trim();

        // first position should be integer
        try {
            first = Integer.parseInt(split[0].trim());
        } catch (NumberFormatException e) {
            this.iohelper.printErr("first position is not an integer");
            this.doWallMove(conf, allConfigs);
            return;
        }

        // second position should be integer
        try {
            second = Integer.parseInt(split[1].trim());
        } catch (NumberFormatException e) {
            this.iohelper.printErr("second position is not an integer");
            this.doWallMove(conf, allConfigs);
            return;
        }

        Position firstPos = displayToPosition(first);
        Position secondPos = displayToPosition(second);
        Direction direction = null;

        // first position should be on the board
        if (!this.board.isValid(firstPos)) {
            this.iohelper.printErr("first position is not in the board");
            this.doWallMove(conf, allConfigs);
            return;
        }

        // second position should be on the board
        if (!this.board.isValid(secondPos)) {
            this.iohelper.printErr("second position is not in the board");
            this.doWallMove(conf, allConfigs);
            return;
        }

        if (dir.equalsIgnoreCase("U")) {
            direction = Direction.UP;
        } else if (dir.equalsIgnoreCase("D")) {
            direction = Direction.DOWN;
        } else if (dir.equalsIgnoreCase("L")) {
            direction = Direction.LEFT;
        } else if (dir.equalsIgnoreCase("R")) {
            direction = Direction.RIGHT;
        }

        // direction should be valid
        if (direction == null) {
            this.iohelper.printErr("not a valid input, direction should be among U,D,L,R");
            this.doWallMove(conf, allConfigs);
            return;
        }

        // positions should be adjacent and directions should be appropriate
        if (firstPos.isSeparated(secondPos, 2, Direction.UP)) {
            if (direction.equals(Direction.RIGHT) || direction.equals(Direction.LEFT)) {
                this.iohelper.printErr("direction can be up or down for these cells");
                this.doWallMove(conf, allConfigs);
                return;
            }
        } else if (firstPos.isSeparated(secondPos, 2, Direction.LEFT)) {
            if (direction.equals(Direction.UP) || direction.equals(Direction.DOWN)) {
                this.iohelper.printErr("direction can be left or right for these cells");
                this.doWallMove(conf, allConfigs);
                return;
            }
        } else {
            this.iohelper.printErr("the cells are not adjacent");
            this.doWallMove(conf, allConfigs);
            return;
        }

        Position firstWall = firstPos.move(direction);
        Position secondWall = secondPos.move(direction);

        // the walls should not overlap horizontally
        if (hasWall(firstWall) || hasWall(secondWall)) {
            this.iohelper.printErr("there is already a wall here");
            this.doWallMove(conf, allConfigs);
            return;
        }

        // the walls should not overlap vertically
        Position middle = firstWall.middle(secondWall);
        if (direction.equals(Direction.RIGHT) || direction.equals(Direction.LEFT)) {
            if (hasWall(middle.move(Direction.LEFT)) && hasWall(middle.move(Direction.RIGHT))) {
                this.iohelper.printErr("walls cannot be placed across another wall");
                this.doWallMove(conf, allConfigs);
                return;
            }
        } else {
            if (hasWall(middle.move(Direction.UP)) && hasWall(middle.move(Direction.DOWN))) {
                this.iohelper.printErr("walls cannot be placed across another wall");
                this.doWallMove(conf, allConfigs);
                return;
            }
        }

        this.board.set(firstWall, QuoridorPiece.Wall());
        this.board.set(secondWall, QuoridorPiece.Wall());

        // check if all players can reach the finish line or not
        for (QuoridorPlayerConfig c : allConfigs.getConfigs()) {
            if (!reachable(c)) {
                this.board.set(firstWall, QuoridorPiece.Empty());
                this.board.set(secondWall, QuoridorPiece.Empty());
                this.iohelper.printErr("walls cannot be placed if they block the finish line");
                this.doWallMove(conf, allConfigs);
                return;
            }
        }

    }
    // calling the reachable method to check if players are not blocked and reachable to their wining side
    private boolean reachable(QuoridorPlayerConfig conf) {
        return reachable(conf, conf.getPositionInBoard(board),
                new Board<>(this.board.getNumRows(), this.board.getNumCols(), () -> false).getBoard());
    }
    // checking if the player could reach their winning side
    private boolean reachable(QuoridorPlayerConfig conf, Position pos, List<List<Cell<Boolean>>> reached) {
        if (hasWall(pos)) {
            return false;
        }
        if (reached.get(pos.getX()).get(pos.getY()).getValue().booleanValue()) {
            return false;
        }
        if (conf.isFinish(pos)) {
            return true;
        }
        reached.get(pos.getX()).get(pos.getY()).set(true);
        for (Direction d : Direction.all()) {
            if (reachable(conf, pos.move(d), reached)) {
                return true;
            }
        }
        return false;
    }
    // check if there is a wall at p position
    private boolean hasWall(Position p) {
        return this.board.at(p).getValue().isWall();
    }
    // for validating pawn move, and placing pawn(player).
    private void doPawnMove(QuoridorPlayerConfig conf) {
        Position position = conf.getPositionInBoard(this.board);
        List<Position> valid = allValidPositions(position, Direction.all(), true);
        String validString = String.join(",",
                valid.stream()
                        .map(this::positionToDisplay).map(i -> i + "")
                        .collect(Collectors.toList()));
        int move = this.iohelper.nextLineInt(
                "Input the position the pawn is moving to, valid moves are: " + validString,
                "should be an integer", i -> valid.stream().anyMatch(x -> positionToDisplay(x) == i),
                "should be in the valid moves");
        Position movePosition = displayToPosition(move);

        this.board.set(position, QuoridorPiece.Empty());
        this.board.set(movePosition, QuoridorPiece.Pawn(conf.getLabel()));
    }
    // return a list of all position the pawn(player) could move to
    private List<Position> allValidPositions(Position pos, List<Direction> directions, boolean recurse) {
        List<Position> valid = new ArrayList<>();
        for (Direction d : Direction.all()) {
            if (hasWall(pos.move(d))) {
                continue;
            }

            Position newPos = pos.move(d).move(d);
            QuoridorPiece piece = this.board.at(newPos).getValue();
            if (piece.isEmpty()) {
                valid.add(newPos);
            } else if (piece.isPawn() && recurse) {
                valid.addAll(allValidPositions(newPos, directions, false));
            }
        }
        return valid;
    }
    // print the quoridor game board with pawns
    protected void printQuoridor() {
        List<List<Cell<QuoridorPiece>>> b = this.board.getBoard();
        int n = b.size();
        int m = b.get(0).size();

        StringBuilder out = new StringBuilder();

        out.append(line(b, 1));
        out.append("\n");

        for (int i = 1; i < n; i += 2) {
            for (int j = 1; j < m; j += 2) {
                char left = b.get(i).get(j - 1).getValue().isWall() ? '+' : '|';
                String val = String.format("%s%3s", left, pieceToString(b.get(i).get(j).getValue(), i, j));
                out.append(val);
            }
            char right = b.get(i).get(m - 1).getValue().isWall() ? '+' : '|';
            out.append(right + "\n");
            out.append(line(b, i + 2));
            out.append("\n");
        }

        this.iohelper.print(out.toString());
    }

    private Position displayToPosition(int p) {
        int m = (this.board.getNumCols() - 1) / 2;
        return new Position(((p - 1) / m) * 2 + 1, ((p - 1) % m) * 2 + 1);
    }

    private int positionToDisplay(Position pos) {
        int m = (this.board.getNumCols() - 1) / 2;
        int i = (pos.getX() - 1) / 2;
        int j = (pos.getY() - 1) / 2;
        return (m * i + j + 1);
    }
    // return string representation of pieces
    private String pieceToString(QuoridorPiece c, int i, int j) {
        if (c.isEmpty()) {
            return positionToDisplay(new Position(i, j)) + "";
        } else if (c.getType().equals(QuoridorPieceType.Pawn)) {
            return c.getLabel();
        } else {
            return c.toString();
        }
    }
    // return string representation of either wall piece or cell outline
    private String line(List<List<Cell<QuoridorPiece>>> b, int row) {
        int m = b.get(0).size();
        StringBuilder out = new StringBuilder();
        for (int j = 1; j < m; j += 2) {
            if (b.get(row - 1).get(j).getValue().isWall()) {
                out.append("++++");
            } else {
                out.append("+---");
            }
        }
        out.append("+");
        return out.toString();
    }
    // print basic game information before game start
    private void printBeforeStart(QuoridorConfig conf) {
        this.iohelper.println("Quoridor Game is starting.....");
        this.iohelper
                .println("Board is of size: " + (this.board.getNumRows() - 1) / 2 + " x "
                        + (this.board.getNumCols() - 1) / 2);
        this.iohelper.println("Players are: {"
                + String.join(",", conf.getPlayers().stream().map(Player::getName).collect(Collectors.toList()))
                + "}");

        this.iohelper.println(".....");
    }

}
