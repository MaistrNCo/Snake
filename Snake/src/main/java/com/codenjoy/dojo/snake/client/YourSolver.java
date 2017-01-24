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
    private int[][] DexMatrix;


    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;

        DexMatrix = createDexMatrix(board);
        int [] routeEndPoint  = fillWaves(board, DexMatrix);
        int [] nextMovePoint =  FindWay(DexMatrix, routeEndPoint);

        int dx = board.getHead().getX() - nextMovePoint[1];
        int dy = board.getHead().getY() - nextMovePoint[0];

        if (dx < 0 ) {
            return Direction.RIGHT.toString();
        }
        if (dx > 0) {
            return Direction.LEFT.toString();
        }
        if (dy < 0 ) {
            return Direction.DOWN.toString();
        }
        if (dy > 0 ) {
            return Direction.UP.toString();
        }

        return Direction.UP.toString();
    }

    private int[] FindWay(int[][] dexMatrix, int[] endPoint) {
        boolean foundHead = false;
        boolean foundNext;
        int xBase,yBase;
        int x=0;
        int y=0;

        //search route from apple
        while(!foundHead){
            int ind = 0;
            foundNext = false;
            xBase = endPoint[1];
            yBase = endPoint[0];
            while (!foundNext){
                switch (ind){
                    case 0:
                        x=xBase-1;
                        y=yBase;
                        break;
                    case 1:
                        y=yBase+1;
                        x=xBase;
                        break;
                    case 2:
                        x=xBase+1;
                        y=yBase;
                        break;
                    case 3:
                        y=yBase-1;
                        x=xBase;
                        break;
                }
                ind++;
                if (x<1||  //check border & "bad"
                    y<1||
                    x>board.size()-2||
                    y>board.size()-2){
                    continue;
                }

                if(dexMatrix[y][x]==-10000){  //head founded
                    foundHead = true;
                    break;
                }
                if(dexMatrix[endPoint[0]][endPoint[1]]==1)continue;
                if(dexMatrix[endPoint[0]][endPoint[1]]- dexMatrix[y][x]==1){ //next point founded
                    foundNext=true;
                    endPoint[1]=x;
                    endPoint[0]=y;
                }
                if(ind >4) System.out.println("Something wrong");
            }

        }
        return endPoint;
    }

    private int[] fillWaves(Board board, int[][] dexMatrix) {
        boolean arrivedApple = false;
        int[] endPoint = new int[2];
        int xBase,yBase;
        int x=0;
        int y=0;
        int waveNum=0;
        ArrayList<Integer> currWave = new ArrayList <>();
        currWave.add(board.getHead().getY());
        currWave.add(board.getHead().getX());
        while (!currWave.isEmpty()&&!arrivedApple){
            waveNum++;
            ArrayList <Integer> newWave = new ArrayList <>();

            for (int indexY =0; indexY < currWave.size(); indexY = indexY +2) {
                //seek up, rigth, down, left & set next wave points (waveNum1)
                yBase = currWave.get(indexY);
                xBase = currWave.get(indexY + 1);
                for (int ind = 0; ind < 4; ind++) { //try to continue wave in 4 directions
                    switch (ind){
                        case 0:
                            y=yBase-1;
                            x=xBase;
                            break;
                        case 1:
                            x=xBase+1;
                            y=yBase;
                            break;
                        case 2:
                            y = yBase+1;
                            x=xBase;
                            break;
                        case 3:
                            x = xBase-1;
                            y=yBase;
                            break;
                    }
                    endPoint[0] = y;
                    endPoint[1] = x;
                    if (x<1||  //check border & "bad"
                        y<1||
                        x>board.size()-2||
                        y>board.size()-2||
                        dexMatrix[y][x] < 0) {
                        continue;
                    }
                    if(dexMatrix[y][x]==10000){ //apple founded
                        dexMatrix[y][x]= waveNum;
                        arrivedApple = true;
                        break;
                    }
                    if(dexMatrix[y][x] ==0){
                        dexMatrix[y][x] = waveNum;
                        newWave.add(y);
                        newWave.add(x);
                    }
                }
                if(arrivedApple) break;
//
            }
            //   wavesArray.add(newWave);
            currWave.clear();
            currWave.addAll(newWave);
        }
        return endPoint;
    }

    private int[][] createDexMatrix(Board board) {

        int [][] DexMatrix  = new int[board.size()][board.size()];
        for (Point pApplle:board.getApples()) {
            DexMatrix[pApplle.getY()][pApplle.getX()] = 10000;
        }

        for (Point pStone:board.getStones()) {
            DexMatrix[pStone.getY()][pStone.getX()] = -1;
        }

        for (Point pSnake:board.getSnake()) {
            DexMatrix[pSnake.getY()][pSnake.getX()] = -1;
        }
        DexMatrix[board.getHead().getY()][board.getHead().getX()] = -10000;
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
