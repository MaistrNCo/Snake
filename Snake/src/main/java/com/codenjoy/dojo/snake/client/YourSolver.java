package com.codenjoy.dojo.snake.client;


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.RandomDice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = "maistrenko@pkzp.com.ua";

    private static final int APPLE_POINT = 10000;
    private static final int HEAD_POINT = -10000;
    private static final int NO_WAY_POINT = -1;

    //PointImpl pointTest = new PointImpl(0,0);
    private ArrayList<List<Point>> routeVault = new ArrayList();
    private boolean appleFound;
    private Dice dice;
    private Board board;
    private int minRouteLength = 100000;
    private int minRouteIndex;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }


    @Override
    public String get(Board board) {
        this.board = board;
        int[][] DexMatrix;
        boolean solved = false;
        Point nextMovePoint = new PointImpl(0,0);
        List<Point> snake = board.getRightSnake();


        //generating matrix of available routes

            //generate waves
        DexMatrix = createDexMatrix(snake,board.getHead(),board.getApples());
        int [] routeEndPoint  = fillWaves(board, DexMatrix);

            //find paths
     //   nextMovePoint =  FindWay(DexMatrix, routeEndPoint);

        if (GetRoutes(board.getApples().get(0),DexMatrix)){
            //move snake by route & check tail
            for (List <Point> route:routeVault) {
                List <Point> virtSnake = moveSnake(route,snake);
                Point fromPoint = virtSnake.get(0);  //head
                List <Point> toPoint = new LinkedList<>();
                toPoint.add(virtSnake.get(snake.size()-1)); //tail end

                int[][] virtDexMatrix = createDexMatrix(virtSnake,fromPoint,toPoint);
                if (GetRoutes(virtSnake.get(snake.size()-1),virtDexMatrix)){
                    solved = true;
                    nextMovePoint = route.get(route.size()-1);
                }
            }

        }
        if(!solved){
            nextMovePoint =  FindWay(DexMatrix, routeEndPoint); //TODO implement search to tail end
        }

            //cases:
                //apple available
                //not apple
        // analise of routes
            //if route to apple
                //check tail availability after route will  completed
                //if no - next route
            //if is not safe route to apple move to farthest point


        int dx = board.getHead().getX() - nextMovePoint.getX();
        int dy = board.getHead().getY() - nextMovePoint.getY();

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

    private List<Point> moveSnake(List<Point> route, List<Point> snake) {
        List <Point> result =new LinkedList<>();
        for (int index = 0; index <=snake.size() ; index++) {
            if(index<route.size()){
                result.add(route.get(index));
            }else{
                result.add(snake.get(index-route.size()));
            }
        }
        return result;
    }

    private List <Point> FindeNextPoints(Point startPoint,int[][] dexMatrix){
        List <Point> pointList = new LinkedList<>();
        int currX = startPoint.getX();
        int currY = startPoint.getY();
        int nextStep = dexMatrix[currY][currX]-1;
        if (dexMatrix[currY - 1][currX] == nextStep) pointList.add(new PointImpl(currX, currY - 1));
        if (dexMatrix[currY + 1][currX] == nextStep) pointList.add(new PointImpl(currX, currY + 1));
        if (dexMatrix[currY][currX - 1] == nextStep) pointList.add(new PointImpl(currX - 1, currY));
        if (dexMatrix[currY][currX + 1] == nextStep) pointList.add(new PointImpl(currX + 1, currY));

        return pointList;
    }

    private boolean GetRoutes(Point endPnt,int[][] dexMatrix){
        //start point - head
        //end point  -  apple or farthest point
        appleFound = false;
        List <Point> currRoute = new LinkedList<>();
        currRoute.add(endPnt);
        int currX = endPnt.getX();
        int currY = endPnt.getY();
        if(dexMatrix[currY][currX]==1){
            routeVault.add(currRoute);
            appleFound = true;
            return appleFound;
        }

        List <Point> pointList = FindeNextPoints(endPnt,dexMatrix);

        //goto next rote`s step (iteratively )
        GetRouteNextStep(currRoute, pointList,dexMatrix);
        return appleFound;
    }

    private void GetRouteNextStep(List<Point> currRoute, List<Point> pointList,int[][] dexMatrix) {
        for (Point point : pointList) {
            List<Point> newRoute = new LinkedList();
            newRoute.addAll(currRoute);
            newRoute.add(point);

            if (dexMatrix[point.getY()][point.getX()] == 1) {
                appleFound = true;
                if (newRoute.size() < minRouteLength) minRouteLength = newRoute.size();
                routeVault.add(newRoute);
                continue;
            }

            List<Point> nextStepList = FindeNextPoints(point, dexMatrix);
            if (nextStepList.size() > 0) {
                GetRouteNextStep(newRoute, nextStepList, dexMatrix);
            }


        }
    }


    private Point FindWay(int[][] dexMatrix, int[] endPoint) {
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
                        if(xBase>board.size()/2){
                            x=xBase+1;
                        }else{
                            x=xBase-1;
                        }
                        y=yBase;
                        break;
                    case 1:
                        if(yBase>board.size()/2){
                            y=yBase+1;
                        }else{
                            y=yBase-1;
                        }
                        x=xBase;
                        break;
                    case 2:
                        if(xBase>board.size()/2){
                            x=xBase-1;
                        }else{
                            x=xBase+1;
                        }
                        y=yBase;
                        break;
                    case 3:
                        if(yBase>board.size()/2){
                            y=yBase-1;
                        }else{
                            y=yBase+1;
                        }
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

                if(dexMatrix[y][x]== HEAD_POINT){  //head founded
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
        return new PointImpl(endPoint[1],endPoint[0]);
    }

    private int[] fillWaves(Board board, int[][] dexMatrix) {
    //    boolean arrivedApple = false;
        int[] endPoint = new int[2];
        int[] applePoint = new int[2];
        int[] appleWaves = new int[4];
        int xBase,yBase;
        int x=0;
        int y=0;
        int reached = 0;
        int waveNum=0;
        ArrayList<Integer> currWave = new ArrayList <>();
        currWave.add(board.getHead().getY());
        currWave.add(board.getHead().getX());
        while (!currWave.isEmpty()){  //&&!arrivedApple
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
                    if (x<1||  //check border & "bad"
                        y<1||
                        x>board.size()-2||
                        y>board.size()-2||
                        dexMatrix[y][x] < 0) {
                        continue;
                    }
                    if(dexMatrix[y][x]== APPLE_POINT){ //apple founded
                        applePoint[0] = y;
                        applePoint[1] = x;
                     //   dexMatrix[y][x]= waveNum;
                        appleWaves[reached]=waveNum;
                        reached++;
                     //   arrivedApple = true;
                        break;
                    }
                    if(dexMatrix[y][x] ==0){
                        dexMatrix[y][x] = waveNum;
                        endPoint[0] = y;
                        endPoint[1] = x;
                        newWave.add(y);
                        newWave.add(x);
                    }
                }
                //if(arrivedApple) break;
//
            }
            //   wavesArray.add(newWave);
            currWave.clear();
            currWave.addAll(newWave);
        }

        if(reached>0){
            dexMatrix[applePoint[0]][applePoint[1]] = appleWaves[0];
            return applePoint;
        }else{
            return endPoint;
        }
    }

    private int[][] createDexMatrix(List<Point> snake,Point head,List<Point> target) {

        int [][] DexMatrix  = new int[board.size()][board.size()];
        for (Point pApplle:target) {
            DexMatrix[pApplle.getY()][pApplle.getX()] = APPLE_POINT;
        }

        for (Point pStone:board.getStones()) {
            DexMatrix[pStone.getY()][pStone.getX()] = NO_WAY_POINT;
        }
        for (Point pWalls:board.getWalls()) {
            DexMatrix[pWalls.getY()][pWalls.getX()] = NO_WAY_POINT;
        }

        for (Point pSnake:snake) {
            DexMatrix[pSnake.getY()][pSnake.getX()] = NO_WAY_POINT;
        }
        DexMatrix[head.getY()][head.getX()] = HEAD_POINT;
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
