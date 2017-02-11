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

    private Dice dice;
    private Board board;
    private List<Point> snake;
    private List<Point> route;

    public YourSolver(Dice dice) {
        this.dice = dice;
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

    @Override
    public String get(Board board) {
        this.board = board;
        int[][] DexMatrix;
        boolean solved = false;
        boolean debuging= false;
        //debuging= true;
        Point nextMovePoint = new PointImpl(0,0);
        snake = board.getRightSnake();
        route = new LinkedList<>();

        //generating matrix of available routes

            //generate waves
        DexMatrix = createDexMatrix(snake,board.getHead(),board.getApples().get(0));
        if (debuging) print2DMatrix(DexMatrix);
        if(fillWaves(snake.get(0),board.getApples().get(0), DexMatrix)) {
            if (debuging)print2DMatrix(DexMatrix);
            //find paths
            //   nextMovePoint =  FindWay(DexMatrix, routeEndPoint);

            //List<Point> route = ;
            if (GetRoute(snake,board.getApples().get(0), DexMatrix, false)) {
                solved = true;
                nextMovePoint = route.get(route.size()-1);

            }
        }

        if (!solved) { //move to tail end
            DexMatrix = createDexMatrix(snake, snake.get(0), snake.get(snake.size()-1));
            if (debuging) print2DMatrix(DexMatrix);
            Point toPnt;
            if (fillWaves(snake.get(0),snake.get(snake.size()-1), DexMatrix)) {
                toPnt = snake.get(snake.size()-1);
                if (debuging) print2DMatrix(DexMatrix);
            }else{

                toPnt = getMax(DexMatrix);
            }
            if (GetRoute(snake,toPnt, DexMatrix,true)) {

                //List <Point> route = routeVault.get(0);

                DexMatrix = createDexMatrix(snake, snake.get(0), snake.get(snake.size()-1));
                getFarthestWay(route,snake.get(0),DexMatrix);

                nextMovePoint = route.get(route.size()-1);
            }



        }
        return getDirection(board, nextMovePoint);
    }

    private Point getMax(int[][] dexMatrix) {

        int maxValue = 0;
        int pMaxX,pMaxY;
        pMaxX=pMaxY=1;

        for (int indY = 1; indY< board.size()-1; indY++) {
            for (int indX = 1; indX< board.size()-1; indX++) {
                if (dexMatrix[indY][indX]>maxValue&&
                        dexMatrix[indY][indX]!=APPLE_POINT){
                    maxValue=dexMatrix[indY][indX];
                    pMaxX=indX;
                    pMaxY=indY;
                }
            }
        }
        return new PointImpl(pMaxX,pMaxY);
    }

    private void getFarthestWay(List<Point> route, Point snHead, int[][] dexMatrix) {
        route.add(snHead);
        for (Point routePoint:route) {
            dexMatrix[routePoint.getY()][routePoint.getX()]=1;
        }
        System.out.print("was "+route.size());
        int shiftX = 0;
        int shiftY = 0;
        Point pShiftOne, pShiftTwo;
        int step = 1;

        while (step<route.size()) {
            for (int wayExt = 0; wayExt<4;wayExt++) {
                switch (wayExt){
                    case 0:  //try to shift right
                        shiftX = -1;
                        shiftY = 0;
                        break;
                    case 1:  //try to shift left
                        shiftX = 0;
                        shiftY = 1;
                        break;
                    case 2:  //try to shift down
                        shiftX = 1;
                        shiftY = 0;
                        break;
                    case 3:  //try to shift up
                        shiftX = 0;
                        shiftY = -1;
                        break;
                }

                Point currP = route.get(route.size() - step);
                Point prevP = route.get(route.size() - step - 1);
                pShiftOne = new PointImpl(prevP.getX()+shiftX,prevP.getY()+shiftY);
                pShiftTwo = new PointImpl(currP.getX()+shiftX,currP.getY()+shiftY);
                if (dexMatrix[pShiftOne.getY()][pShiftOne.getX()]==0&&
                    dexMatrix[pShiftTwo.getY()][pShiftTwo.getX()]==0){
                        dexMatrix[pShiftOne.getY()][pShiftOne.getX()]=1;
                        dexMatrix[pShiftTwo.getY()][pShiftTwo.getX()]=1;
                        route.add(route.size() - step,pShiftOne);
                        route.add(route.size() - step,pShiftTwo);
                        wayExt = 0; //start from begin for each added  pair
                }else{
                    //simply go to next try
                }
            }
            step++;
        }
        route.remove(snHead);
        System.out.println("  became: "+route.size());
    }

    private String getDirection(Board board, Point nextMovePoint) {
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

    private List<Point> moveSnake(List<Point> route, List<Point> snake,boolean test) {
        List <Point> result =new LinkedList<>();
        byte correction = 0;
        if (test) correction = 1;
        for (int index = 0; index <=snake.size()-correction ; index++) {
            if(index<route.size()){
                result.add(route.get(index));
            }else{
                result.add(snake.get(index-route.size()-correction));
            }
        }
        return result;
    }

    private List <Point> FindeNextPoints(Point startPoint,List <Point> snake, int[][] dexMatrix){
        List <Point> pointList = new LinkedList<>();
        int nextStep = dexMatrix[startPoint.getY()][startPoint.getX()]-1;

        pointList = board.getNextStep(startPoint);

        int index = 0;
        while ( index < pointList.size()) {
            Point point = pointList.get(index);
            if (point.getX()==snake.get(0).getX()&&
                    point.getY()==snake.get(0).getY()) {
                index++;
            }else if(nextStep>0 && dexMatrix[point.getY()][point.getX()] == nextStep){
                index++;
            }else{
                pointList.remove(point);
            }
        }


        return pointList;
    }

    private boolean GetRoute(List <Point > snake,Point endPnt, int[][] dexMatrix, boolean testOnly){
        //start point - head
        //end point  -  apple or farthest point

        List <Point> currRoute = new LinkedList<>();
        currRoute.add(endPnt);

        List <Point> pointList = FindeNextPoints(endPnt,snake,dexMatrix);

        //goto next rote`s step (iteratively )


        return GetRouteNextStep(snake, currRoute, pointList,dexMatrix,testOnly);

    }

    private boolean GetRouteNextStep(List <Point> snake, List<Point> currRoute, List<Point> pointList,int[][] dexMatrix,boolean test) {
        boolean result = false;
        for (Point point : pointList) {
            List<Point> newRoute = new LinkedList();
            newRoute.addAll(currRoute);

            if (point.getY()==snake.get(0).getY()&& point.getX() == snake.get(0).getX()) {

                if (test||snake.size()<4) {
                    route = newRoute;
                    return true;
                }
                /////////////////////
                List<Point> virtSnake = moveSnake(newRoute, snake,test);
                Point fromPoint = virtSnake.get(0);  //head
                int[][] virtDexMatrix = createDexMatrix(virtSnake, fromPoint, virtSnake.get(virtSnake.size() - 1));

                if(fillWaves(virtSnake.get(0),virtSnake.get(virtSnake.size() - 1), virtDexMatrix)) { //check snake tail
                    if (GetRoute(virtSnake,virtSnake.get(virtSnake.size() - 1), virtDexMatrix,true)) {
                        route = newRoute;
                        return true;
                    }else{
                        Point toPnt = getMax(virtDexMatrix);
                        if (GetRoute(virtSnake,toPnt, virtDexMatrix,true)) {
                            getFarthestWay(route,virtSnake.get(0),virtDexMatrix);
                            if (route.size()>snake.size()) {
                                route = newRoute;
                                return true;
                            }
                        }
                        continue;
                    }
                }else{
                  //  Point toPnt = getMax(virtDexMatrix)
                }

                //////////////////////////////


            }

            newRoute.add(point);

            List<Point> nextStepList = FindeNextPoints(point, snake, dexMatrix);

            if (nextStepList.size() > 0) {
                result = GetRouteNextStep(snake,newRoute, nextStepList, dexMatrix,test);

            }
            if (result) return true;

        }
        return result;
    }

    private boolean fillWaves(Point startPont, Point endPoint, int[][] dexMatrix) {

        int[] appleWaves = new int[4];
        int xBase,yBase;
        int x=0;
        int y=0;
        int reached = 0;
        int waveNum=0;

        List<Point> currWave = new LinkedList<>();
        currWave.add(startPont);


        while (!currWave.isEmpty()){  //&&!arrivedApple
            waveNum++;
            List <Point> newWave = new LinkedList<>();
            for (Point point:currWave) {
                yBase = point.getY();
                xBase = point.getX();
                for (int ind = 0; ind < 4; ind++) { //try to continue wave in 4 directions
                    switch (ind){
                        case 0:
                            y=yBase-1;
                            x=xBase;
                            break;
                        case 2:
                            x=xBase+1;
                            y=yBase;
                            break;
                        case 3:
                            y = yBase+1;
                            x=xBase;
                            break;
                        case 1:
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
                    if(x == endPoint.getX()&& y == endPoint.getY()) { //target founded

                        //   dexMatrix[y][x]= waveNum;
                        appleWaves[reached]=waveNum;
                        reached++;
                        //   arrivedApple = true;
                        break;
                    }
                    if(dexMatrix[y][x] ==0){
                        dexMatrix[y][x] = waveNum;
//                      lastPoint = new PointImpl(x,y);
                        newWave.add(new PointImpl(x,y));

                    }
                }

            }
            currWave.clear();
            currWave.addAll(newWave);
        }

        if(reached>0){
            dexMatrix[endPoint.getY()][endPoint.getX()] = appleWaves[0];
            return true;
        }else{
            return false;
        }
    }

    private int[][] createDexMatrix(List<Point> snake,Point head,Point target) {

        int [][] DexMatrix  = new int[board.size()][board.size()];

        for (Point pStone:board.getStones()) {
            DexMatrix[pStone.getY()][pStone.getX()] = NO_WAY_POINT;
        }
        for (Point pWalls:board.getWalls()) {
            DexMatrix[pWalls.getY()][pWalls.getX()] = NO_WAY_POINT;
        }

        for (Point pSnake:snake) {
            DexMatrix[pSnake.getY()][pSnake.getX()] = NO_WAY_POINT;
        }

        DexMatrix[target.getY()][target.getX()] = APPLE_POINT;
        DexMatrix[head.getY()][head.getX()] = HEAD_POINT;
        return DexMatrix;
    }

    private void print2DMatrix(int[][] matrix){
        for (int[] string:matrix) {
            for (int value:string) {
                if(value==HEAD_POINT) System.out.print("  "+'H');
                else if(value==APPLE_POINT) System.out.print("  "+'A');
                else if(value>9||value==-1)System.out.print(" "+value);
                else System.out.print("  "+value);

            }
            System.out.println("");
        }
        System.out.println(" ");
    }
}
