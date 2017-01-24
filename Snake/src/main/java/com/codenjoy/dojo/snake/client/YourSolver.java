package com.codenjoy.dojo.snake.client;


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.snake.model.Elements;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

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

        int waveNum=1;
        boolean arrivedApple = false;

        int[][] DexMatrix = createDexMatrix(board);

        ArrayList <Integer> currWave = new ArrayList <Integer>();
        currWave.add(board.getHead().getX());
        currWave.add(board.getHead().getY());
        while (!currWave.isEmpty()||!arrivedApple){
            ArrayList <Integer> newWave = new ArrayList <Integer>();

            for (int indexX =0; indexX < currWave.size(); indexX = indexX +2) {
                //seekn up, rigth, down, left & set next wave points (waveNum+1)
                for (int ind = 0; ind < 4; ind++) { //try to continue wave in 4 directions
                    int x = currWave.get(indexX);
                    int y = currWave.get(indexX + 1);
                    switch (ind){
                        case 0:
                            x--;
                            break;
                        case 1:
                            y++;
                            break;
                        case 2:
                            x ++;
                            break;
                        case 3:
                            y --;
                            break;
                    }
                    if (x<1||  //check border & "bad"
                        y<1||
                        x>board.size()-2||
                        y>board.size()-2||
                        DexMatrix[x][y] < 0) {
                        continue;
                    }
                    if(DexMatrix[x][y]==10000){ //apple founded
                        arrivedApple = true;
                        break;
                    }
                    if(DexMatrix[x][y] ==0){
                        DexMatrix[x][y] = waveNum;
                        newWave.add(x);
                        newWave.add(y);
                    }
                }
//
            }
         //   wavesArray.add(newWave);
            waveNum++;
            currWave.clear();
            currWave.addAll(newWave);
        }


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

    private int[][] createDexMatrix(Board board) {

        int [][] DexMatrix  = new int[board.size()][board.size()];
        DexMatrix[board.getHead().getX()][board.getHead().getY()] = -10000;
        for (Point pApplle:board.getApples()) {
            DexMatrix[pApplle.getX()][pApplle.getY()] = 10000;
        }

        for (Point pStone:board.getStones()) {
            DexMatrix[pStone.getX()][pStone.getY()] = -1;
        }

        for (Point pSnake:board.getSnake()) {
            DexMatrix[pSnake.getX()][pSnake.getY()] = -1;
        }
        return DexMatrix;
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
