package com.codenjoy.dojo.snake.client;


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
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

    //PointImpl pointTest = new PointImpl(0,0);
    private ArrayList<ArrayList<Point>> routeVault = new ArrayList();
    private boolean appleFound;
    private Dice dice;
    private Board board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }


    @Override
    public String get(Board board) {
        this.board = board;
        int[][] DexMatrix;

        //generating matrix of available routes

            //generate waves
        DexMatrix = createDexMatrix(board);
        int [] routeEndPoint  = fillWaves(board, DexMatrix);

            //find paths
        int [] nextMovePoint =  FindWay(DexMatrix, routeEndPoint);
            //cases:
                //apple available
                //not apple
        // analise of routes
            //if route to apple
                //check tail availability after route will  completed
                //if no - next route
            //if is not safe route to apple move to farthest point


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

    private ArrayList<PointImpl> FindeNextPoints(PointImpl startPoint,int[][] dexMatrix){
        ArrayList <PointImpl> pointList = new ArrayList<>();
        int currX = startPoint.getX();
        int currY = startPoint.getY();
        int nextStep = dexMatrix[currY][currX]-1;
        if (nextStep == 0  //if next step to head
            ||dexMatrix[currY][currX]== -10000){
            appleFound = true;
            return pointList;  //or stand on head (by first step)
        }

        if (dexMatrix[currY-1][currX]==nextStep) pointList.add(new PointImpl(currX,currY-1));
        if (dexMatrix[currY+1][currX]==nextStep) pointList.add(new PointImpl(currX,currY+1));
        if (dexMatrix[currY][currX-1]==nextStep) pointList.add(new PointImpl(currX-1,currY));
        if (dexMatrix[currY][currX+1]==nextStep) pointList.add(new PointImpl(currX+1,currY));
        return pointList;
    }

    private void GetRoutes(Board board,int[][] dexMatrix,PointImpl startPnt, PointImpl endPnt){
        //start point - head
        //end point  -  apple or farthest point
        appleFound = false;
        ArrayList <Point> currRoute = new ArrayList<>();
        int currX = endPnt.getX();
        int currY = endPnt.getY();
        ArrayList <PointImpl> pointList = new ArrayList<>();
        if (dexMatrix[currY-1][currX]>0 ||dexMatrix[currY-1][currX]==-10000) pointList.add(new PointImpl(currX,currY-1));
        if (dexMatrix[currY+1][currX]>0 ||dexMatrix[currY+1][currX]==-10000) pointList.add(new PointImpl(currX,currY-1));
        if (dexMatrix[currY][currX-1]>0 ||dexMatrix[currY][currX-1]==-10000) pointList.add(new PointImpl(currX,currY-1));
        if (dexMatrix[currY][currX+1]>0 ||dexMatrix[currY][currX+1]==-10000) pointList.add(new PointImpl(currX,currY-1));

        //goto next rote`s step (iteratively )
        GetRouteNextStep(currRoute, pointList,dexMatrix);

    }

    private void GetRouteNextStep(ArrayList<Point> currRoute, ArrayList<PointImpl> pointList,int[][] dexMatrix) {
        for (int ind = pointList.size()-1; ind >=0 ; ind--) {
            //int indR =routeIndex;
            if (ind==0){//first point will continue last route
                currRoute.add(pointList.get(ind));
                ArrayList <PointImpl> nextStepList = FindeNextPoints(pointList.get(ind),dexMatrix);
                if (nextStepList.size()>0){
                    GetRouteNextStep(currRoute,nextStepList,dexMatrix);
                }else{
                    //add complete route to ArrayList
                    routeVault.add(currRoute);
                    continue;
                }

            }else{      //create new branch from previous
                ArrayList Route = new ArrayList();
                Route.addAll(currRoute);
                Route.add(pointList.get(ind));
                ArrayList <PointImpl> nextStepList = FindeNextPoints(pointList.get(ind),dexMatrix);
                if (nextStepList.size()>0){
                    GetRouteNextStep(Route,nextStepList,dexMatrix);
                }else{
                    //add complete route to ArrayList
                    routeVault.add(Route);
                    continue;
                }

            }
        }
       // GetRouteNxtStep(currRoute, pointList);
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
                    if(dexMatrix[y][x]==10000){ //apple founded
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
