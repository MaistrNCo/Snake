package com.codenjoy.dojo.snake.client;


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.snake.model.Elements;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = "maistrenko@pkzp.com.ua";

    private Dice dice;
    private Board board;
    public Elements [] tail = {Elements.TAIL_END_DOWN,
                               Elements.TAIL_END_UP,
                               Elements.TAIL_END_LEFT,
                               Elements.TAIL_END_RIGHT,
                               Elements.TAIL_HORIZONTAL,
                               Elements.TAIL_VERTICAL,
                               Elements.TAIL_LEFT_DOWN,
                               Elements.TAIL_LEFT_UP,
                               Elements.TAIL_RIGHT_UP,
                               Elements.TAIL_RIGHT_DOWN,
                               Elements.BAD_APPLE
                               };

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;

//        Point point = board.getApples().get(0);
//        point.getX()
//        point.getY()
        int siZe = board.size();

        char[][] field = board.getField();

        // found snake
        int snakeHeadX = -1;
        int snakeHeadY = -1;
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field.length; y++) {
                char ch = field[x][y];
                if (ch == Elements.HEAD_DOWN.ch() ||
                    ch == Elements.HEAD_UP.ch() ||
                    ch == Elements.HEAD_LEFT.ch() ||
                    ch == Elements.HEAD_RIGHT.ch())
                {
                    snakeHeadX = x;
                    snakeHeadY = y;
                    break;

                }
            }
            if (snakeHeadX != -1) {
                break;
            }
        }

        // нашли змейку
        int appleX = -1;
        int appleY = -1;
        for (int x = 0; x < field.length; x++) {
            for (int y = 0; y < field.length; y++) {
                char ch = field[x][y];
                if (ch == Elements.GOOD_APPLE.ch()) {
                    appleX = x;
                    appleY = y;
                    break;

                }
            }
            if (appleX != -1) {
                break;
            }
        }
        int dx = snakeHeadX - appleX;
        int dy = snakeHeadY - appleY;

        if (dx < 0 && !board.isAt(snakeHeadX+1, snakeHeadY,tail)) {
            return Direction.RIGHT.toString();
        }
        if (dx > 0 && !board.isAt(snakeHeadX-1, snakeHeadY,tail)) {
            return Direction.LEFT.toString();
        }
        if (dy < 0 && !board.isAt(snakeHeadX, snakeHeadY+1,tail)) {
            return Direction.DOWN.toString();
        }
        if (dy > 0 && !board.isAt(snakeHeadX, snakeHeadY-1,tail)) {
            return Direction.UP.toString();
        }

        return Direction.UP.toString();
    }

    public static void main(String[] args) {
        start(USER_NAME, WebSocketRunner.Host.REMOTE);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new YourSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
