package com.codenjoy.dojo.snake.client;


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.RandomDice;

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

    private Dice dice;
    private Board board;

    private List<Point> snake;
    private List<Point> route;

    private class auxiliaryMatrix{
        int[][] dexMatrix;
        public auxiliaryMatrix(List<Point> snake,Point head,Point target){
            dexMatrix = new int[board.size()][board.size()];
            initMatrix(snake,head,target);
        }
        public void setValue(Point point, int value) {
            dexMatrix[point.getY()][point.getX()]=value;
        }
        public int getValue(Point point) {
            return dexMatrix[point.getY()][point.getX()];
        }
        private void initMatrix(List<Point> snake,Point head,Point target) {

            //int [][] DexMatrix  = new int[board.size()][board.size()];
            for (int i = 0; i < board.size(); i++) {
                for (int j = 0; j < board.size(); j++) {
                    dexMatrix[i][j] = 0;
                }
            }

            for (Point pStone:board.getStones()) {
                dexMatrix[pStone.getY()][pStone.getX()] = NO_WAY_POINT;
            }
            for (Point pWalls:board.getWalls()) {
                dexMatrix[pWalls.getY()][pWalls.getX()] = NO_WAY_POINT;
            }

            for (Point pSnake:snake) {
                dexMatrix[pSnake.getY()][pSnake.getX()] = NO_WAY_POINT;
            }

            dexMatrix[target.getY()][target.getX()] = APPLE_POINT;
            dexMatrix[head.getY()][head.getX()] = HEAD_POINT;

        }
        private Point getMax() {

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
        private void print(){
            for (int[] string:dexMatrix) {
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
        boolean solved = false;
        Point nextMovePoint = new PointImpl(0,0);
        snake = board.getRightSnake();
        route = new LinkedList<>();

        auxiliaryMatrix auxMatrix = new auxiliaryMatrix(snake,board.getHead(),board.getApples().get(0));
        if(fillWaves(snake.get(0),board.getApples().get(0), auxMatrix)) {  //generate waves
            if (GetRoute(snake,board.getApples().get(0), auxMatrix, false)) { //find paths
                solved = true;
                nextMovePoint = route.get(route.size()-1);
            }
        }
        if (!solved) { //move to tail end
            auxMatrix.initMatrix(snake, snake.get(0), snake.get(snake.size()-1));
            Point toPnt;
            if (fillWaves(snake.get(0),snake.get(snake.size()-1), auxMatrix)) {
                toPnt = snake.get(snake.size()-1); // move to tail end
            }else{
                toPnt = auxMatrix.getMax(); // move to farthest point
            }
            if (GetRoute(snake,toPnt, auxMatrix,true)) {
                auxMatrix.initMatrix(snake, snake.get(0), snake.get(snake.size()-1));
                getFarthestWay(route,snake.get(0),auxMatrix);
                nextMovePoint = route.get(route.size()-1);
            }
        }
        return getDirection(board, nextMovePoint);
    }

    private boolean fillWaves(Point startPoint, Point endPoint,auxiliaryMatrix dexMatrix) {

        int[] appleWaves = new int[4];
        int reached = 0;
        int waveNum=0;
        List<Point> currWave = new LinkedList<>();
        currWave.add(startPoint);
        while (!currWave.isEmpty()){  //&&!arrivedApple
            waveNum++;
            List <Point> newWave = new LinkedList<>();
            for (Point point:currWave) {
                List <Point> movePoints = board.getNextStep(point);
                for (int ind = 0; ind < movePoints.size(); ind++) { //try to continue wave in 4 directions
                    if(movePoints.get(ind).getX() == endPoint.getX()
                            && movePoints.get(ind).getY() == endPoint.getY()) { //target found
                        appleWaves[reached]=waveNum;
                        reached++;
                        break;
                    }
                    if(dexMatrix.getValue(movePoints.get(ind))==0){
                        dexMatrix.setValue(movePoints.get(ind),waveNum);
                        newWave.add(movePoints.get(ind));
                    }

                }

            }
            currWave.clear();
            currWave.addAll(newWave);
        }

        if(reached>0){
            dexMatrix.setValue(endPoint,appleWaves[0]);  // set value to help find route
            return true;
        }else{
            return false;
        }
    }

    private void getFarthestWay(List<Point> route, Point snHead, auxiliaryMatrix dexMatrix) {
        route.add(snHead);
        for (Point routePoint:route) {
            dexMatrix.setValue(routePoint,1);
        }
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
                if (dexMatrix.getValue(pShiftOne)==0&&
                    dexMatrix.getValue(pShiftTwo)==0){
                        dexMatrix.setValue(pShiftOne,1);
                        dexMatrix.setValue(pShiftTwo,1);
                        route.add(route.size() - step,pShiftOne);
                        route.add(route.size() - step,pShiftTwo);
                        wayExt = -1; //start from begin for each added  pair
                }else{
                    //simply go to next try
                }
            }
            step++;
        }
        route.remove(snHead);
        System.out.println("  became: "+route.size());
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

    private boolean GetRoute(List <Point > snake,Point endPnt, auxiliaryMatrix dexMatrix, boolean testOnly){
        //start point - head
        //end point  -  apple or farthest point
        //start search from end point
        List <Point> currRoute = new LinkedList<>();
        currRoute.add(endPnt);

        List <Point> pointList = findNextPoints(endPnt,snake,dexMatrix);

        //goto next rote`s step (recursively )


        return GetRouteNextStep(snake, currRoute, pointList,dexMatrix,testOnly);

    }

    private List <Point> findNextPoints(Point startPoint, List <Point> snake, auxiliaryMatrix dexMatrix){
        List <Point> pointList = new LinkedList<>();
        int nextStep = dexMatrix.getValue(startPoint)-1;

        pointList = board.getNextStep(startPoint);

        int index = 0;
        while ( index < pointList.size()) {
            Point point = pointList.get(index);
            if (point.getX()==snake.get(0).getX()&&
                    point.getY()==snake.get(0).getY()) {
                index++;
            }else if(nextStep>0 && dexMatrix.getValue(point) == nextStep){
                index++;
            }else{
                pointList.remove(point);
            }
        }


        return pointList;
    }

    private boolean GetRouteNextStep(List <Point> snake, List<Point> currRoute, List<Point> pointList,auxiliaryMatrix dexMatrix,boolean test) {
        boolean result = false;
        for (Point point : pointList) {
            List<Point> newRoute = new LinkedList();
            newRoute.addAll(currRoute);

            if (point.getY()==snake.get(0).getY()&& point.getX() == snake.get(0).getX()) {
                if (test||snake.size()<3) {
                    route = newRoute;
                    return true;
                }
                /////////////////////
                //look to the future
                List<Point> virtSnake = moveSnake(newRoute, snake,test);
                Point fromPoint = virtSnake.get(0);  //head
                auxiliaryMatrix virtDexMatrix = new auxiliaryMatrix(virtSnake,fromPoint, virtSnake.get(virtSnake.size()-1));

                if(fillWaves(virtSnake.get(0),virtSnake.get(virtSnake.size() - 1), virtDexMatrix)) { //check snake tail
                    if (GetRoute(virtSnake,virtSnake.get(virtSnake.size() - 1), virtDexMatrix,true)) {
                        route = newRoute; //tail end available after the move
                        return true;
                    }else{
                        Point toPnt = virtDexMatrix.getMax();
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
            List<Point> nextStepList = findNextPoints(point, snake, dexMatrix);
            if (nextStepList.size() > 0) {
                result = GetRouteNextStep(snake,newRoute, nextStepList, dexMatrix,test);
            }
            if (result) return true;
        }
        return result;
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

}
