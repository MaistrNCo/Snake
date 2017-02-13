package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.client.*;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snake.model.Elements;

import java.util.*;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 12:07 AM
 */
public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public List<Point> getApples() {
        return get(Elements.GOOD_APPLE);
    }

    public Direction getSnakeDirection() {
        Point head = getHead();
        if (isAt(head.getX(), head.getY(), Elements.HEAD_LEFT)) {
            return Direction.LEFT;
        } else if (isAt(head.getX(), head.getY(), Elements.HEAD_RIGHT)) {
            return Direction.RIGHT;
        } else if (isAt(head.getX(), head.getY(), Elements.HEAD_UP)) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    public Point getHead() {
        List<Point> result = get(
                Elements.HEAD_UP,
                Elements.HEAD_DOWN,
                Elements.HEAD_LEFT,
                Elements.HEAD_RIGHT);
        return result.get(0);
    }

    public List<Point> getBarriers() {
        List<Point> result = getSnake();
        result.addAll(getStones());
        result.addAll(getWalls());
        return result;
    }

    public List<Point> getSnake() {
        List<Point> result = get(
                Elements.TAIL_END_DOWN,
                Elements.TAIL_END_LEFT,
                Elements.TAIL_END_UP,
                Elements.TAIL_END_RIGHT,
                Elements.TAIL_HORIZONTAL,
                Elements.TAIL_VERTICAL,
                Elements.TAIL_LEFT_DOWN,
                Elements.TAIL_LEFT_UP,
                Elements.TAIL_RIGHT_DOWN,
                Elements.TAIL_RIGHT_UP);
        result.add(0, getHead());
        return result;
    }
    public List<Point> getRightSnake(){
        List<Point> snakeMixed = getSnake();
        List<Point> snake = new LinkedList<>();
        Point previousPoint = getHead();
        snakeMixed.remove(previousPoint);
        snake.add(previousPoint);
        int index = 0;
        while(snakeMixed.size()>0){
            Point point = snakeMixed.get(index);
            if(itsNextPoint(previousPoint, point)){
                snake.add(point);
                snakeMixed.remove(point);
                previousPoint = point;
                index = 0;
                continue;
            }
            index++;
        }
        return snake;
    }

    private boolean itsNextPoint(Point prvPoint, Point point) {

        Elements element = this.getAt(point.getX(), point.getY());


            if (point.getX() == prvPoint.getX()){
                if(point.getY() - 1 == prvPoint.getY()) { //upper fro prev
                    if(element.equals(Elements.TAIL_END_DOWN)||
                            element.equals(Elements.TAIL_VERTICAL)||
                            element.equals(Elements.TAIL_LEFT_UP)||
                            element.equals(Elements.TAIL_RIGHT_UP)){
                            return true;
                    }

                }else if(point.getY() + 1 == prvPoint.getY()){ //down from prev
                    if(element.equals(Elements.TAIL_END_UP)||
                       element.equals(Elements.TAIL_VERTICAL)||
                       element.equals(Elements.TAIL_RIGHT_DOWN)||
                       element.equals(Elements.TAIL_LEFT_DOWN)){
                        return true;
                    }
                }
            }
            if (point.getY() == prvPoint.getY()) {
                if (point.getX() - 1 == prvPoint.getX()){  //right from prev
                    if(element.equals(Elements.TAIL_END_RIGHT)||
                            element.equals(Elements.TAIL_HORIZONTAL)||
                            element.equals(Elements.TAIL_LEFT_DOWN)||
                            element.equals(Elements.TAIL_LEFT_UP)){
                        return true;
                    }
                }else if (point.getX() + 1 == prvPoint.getX()){  //left from prev
                    if(element.equals(Elements.TAIL_END_LEFT)||
                            element.equals(Elements.TAIL_HORIZONTAL)||
                            element.equals(Elements.TAIL_RIGHT_DOWN)||
                            element.equals(Elements.TAIL_RIGHT_UP)){
                        return true;
                    }

                }
            }

        return false;
    }

    @Override
    public String toString() {
        return String.format("Board:\n%s\n" +
            "Apple at: %s\n" +
            "Stones at: %s\n" +
            "Head at: %s\n" +
            "Snake at: %s\n" +
            "Current direction: %s",
                boardAsString(),
                getApples(),
                getStones(),
                getHead(),
                getSnake(),
                getSnakeDirection());
    }

    public List<Point> getStones() {
        return get(Elements.BAD_APPLE);
    }

    public List<Point> getWalls() {
        return get(Elements.BREAK);
    }

    public List<Point> getNextStep(Point startPoint) {
        List <Point> resList = new LinkedList<>();  //generating points to 4 direction & checking borders
        if (startPoint.getY() +1 < this.size()-1) resList.add(new PointImpl(startPoint.getX(),startPoint.getY()+1));
        if (startPoint.getX()-1 > 0) resList.add(new PointImpl(startPoint.getX()-1,startPoint.getY()));
        if (startPoint.getX() +1 < this.size()-1)resList.add(new PointImpl(startPoint.getX()+1,startPoint.getY()));
        if (startPoint.getY()-1 > 0)resList.add(new PointImpl(startPoint.getX(),startPoint.getY()-1));

        return resList;
    }
}