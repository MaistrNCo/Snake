package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.services.RandomDice;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by oleksandr.baglai on 01.12.2015.
 */
public class YourSolverTest {


    @Test
    public void testClose() {
        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                          "☼     ╔══════╕☼" +
                          "☼     ╚╗    ☻ ☼" +
                          "☼  ☺   ║      ☼" +
                          "☼    ╔═╝      ☼" +
                          "☼  ╔═╝        ☼" +
                          "☼  ║          ☼" +
                          "☼ ╔╝          ☼" +
                          "☼ ║           ☼" +
                          "☼ ║           ☼" +
                          "☼ ║           ☼" +
                          "☼ ║           ☼" +
                          "☼◄╝           ☼" +
                          "☼             ☼" +
                          "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "UP");
    }@Test
    public void testClose2() {
        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                          "☼             ☼" +
                          "☼             ☼" +
                          "☼ ╔═══════╗   ☼" +
                          "☼ ║ ╔╗╔╗  ║   ☼" +
                          "☼ ║ ║║║║  ║   ☼" +
                          "☼ ║ ║║║║  ║   ☼" +
                          "☼☺║ ║║║║  ║   ☼" +
                          "☼◄╝ ║║║║╔╗║   ☼" +
                          "☼   ║║║╚╝║║   ☼" +
                          "☼   ║║║ ☻║╚══╗☼" +
                          "☼╓  ║║║  ║╔══╝☼" +
                          "☼║  ║║║  ║╚══╗☼" +
                          "☼╚══╝╚╝  ╚═══╝☼" +
                          "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "DOWN");
    }

    @Test
    public void testSimply() {
        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                          "☼ ╔════════╗  ☼" +
                          "☼ ║  ☻     ║  ☼" +
                          "☼ ║        ║  ☼" +
                          "☼ ╚═╗      ║  ☼" +
                          "☼   ║ ╔╗   ║  ☼" +
                          "☼   ║ ║╙   ║  ☼" +
                          "☼   ║ ║ ☺  ║  ☼" +
                          "☼   ║ ║    ║  ☼" +
                          "☼   ║ ║    ║  ☼" +
                          "☼   ║ ║    ║  ☼" +
                          "☼   ║ ║    ║  ☼" +
                          "☼   ║ ╚════╝  ☼" +
                          "☼   ╚═►       ☼" +
                          "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "RIGHT");
    }
    @Test
    public void testSimply2() {
        assertB(  "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                            "☼             ☼" +
                            "☼             ☼" +
                            "☼             ☼" +
                            "☼             ☼" +
                            "☼             ☼" +
                            "☼            ☺☼" +
                            "☼     ╔════╗  ☼" +
                            "☼     ║    ╚► ☼" +
                            "☼     ║ ☻     ☼" +
                            "☼     ║       ☼" +
                            "☼     ║       ☼" +
                            "☼╔════╝       ☼" +
                            "☼╚═════╕      ☼" +
                            "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "UP");
    }
    @Test
    public void testSimply4() {
        assertB(  "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼             ☼" +
                "☼             ☼" +
                "☼             ☼" +
                "☼             ☼" +
                "☼             ☼" +
                "☼          ☻  ☼" +
                "☼             ☼" +
                "☼       ☺     ☼" +
                "☼╔═══════════╕☼" +
                "☼║            ☼" +
                "☼║            ☼" +
                "☼║            ☼" +
                "☼▼            ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "RIGHT");
    }
    @Test
    public void testSimply5() {
        assertB(  "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                            "☼        ╔═╗  ☼" +
                            "☼╔══╗ ╔══╝ ║  ☼" +
                            "☼║  ║ ║╔═╗ ║╔╗☼" +
                            "☼║  ╚╗║║ ║ ║║║☼" +
                            "☼║  ☺║║╚╗║ ╚╝║☼" +
                            "☼║   ╚╝ ║║   ║☼" +
                            "☼║╔═════╝║   ║☼" +
                            "☼╚╝      ╚═╗╔╝☼" +
                            "☼ ╔════════╝║ ☼" +
                            "☼ ║╔══════╗╔╝ ☼" +
                            "☼ ║╚══╗   ║╚╗ ☼" +
                            "☼ ╚══╕▼   ╚═╝ ☼" +
                            "☼ ☻           ☼" +
                            "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "RIGHT");
    }
    @Test
    public void testSimply6() {
        assertB(  "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                            "☼╔═════╗    ╔╗☼" +
                            "☼╚═╗╔══╝  ▲╓║║☼" +
                            "☼╔═╝╚═══╗╔╝║║║☼" +
                            "☼║╔╗☻╔══╝╚╗╚╝║☼" +
                            "☼║║╚╗╚════╝ ╔╝☼" +
                            "☼╚╝☺║╔═╗╔══╗╚╗☼" +
                            "☼ ╔═╝║╔╝║╔╗║╔╝☼" +
                            "☼ ╚═╗║║╔╝║║║╚╗☼" +
                            "☼╔══╝║║╚═╝║╚╗║☼" +
                            "☼║╔══╝║╔══╝╔╝║☼" +
                            "☼║║╔╗╔╝╚══╗╚═╝☼" +
                            "☼║║║╚╝╔╗╔╗╚══╗☼" +
                            "☼╚╝╚══╝╚╝╚═══╝☼" +
                            "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "LEFT");
    }
    @Test
    public void testSimply7() {
        assertB(  "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                            "☼       ╔════╗☼" +
                            "☼     ╓ ║╔═══╝☼" +
                            "☼╔═►  ╚╗║╚═══╗☼" +
                            "☼╚╗ ☻╔═╝╚══╗ ║☼" +
                            "☼╔╝╔╗╚═╗╔══╝ ║☼" +
                            "☼╚═╝║╔╗║║ ╔══╝☼" +
                            "☼╔══╝║║║║ ╚═╗☺☼" +
                            "☼╚══╗║║║║╔═╗╚╗☼" +
                            "☼╔══╝║║╚╝╚╗╚╗║☼" +
                            "☼║╔══╝║╔══╝╔╝║☼" +
                            "☼║║╔╗╔╝╚══╗╚═╝☼" +
                            "☼║║║╚╝╔╗╔╗╚══╗☼" +
                            "☼╚╝╚══╝╚╝╚═══╝☼" +
                            "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "UP");
    }
    @Test
    public void testSimply8() {
        assertB(  "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                            "☼  ☺          ☼" +
                            "☼╔══╗         ☼" +
                            "☼║  ║         ☼" +
                            "☼║╔►║   ╔╗    ☼" +
                            "☼║║ ║   ║║    ☼" +
                            "☼║║ ╚═╗ ║║    ☼" +
                            "☼║║   ╚╗║╚═╗  ☼" +
                            "☼║║    ║╚═╗║  ☼" +
                            "☼║║    ║╔╗║║  ☼" +
                            "☼╚╝    ╚╝║║║  ☼" +
                            "☼ ☻      ╚╝║  ☼" +
                            "☼          ╙  ☼" +
                            "☼             ☼" +
                            "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", "DOWN");
    }
    @Test
    public void testSameDirection() {
        assertB("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ ▼  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼☼☼☼☼☼", "DOWN");

        assertB("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼► ☺ ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼", "RIGHT");

        assertB("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼ ▲  ☼" +
                "☼☼☼☼☼☼", "UP");

        assertB("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☺  ◄☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼", "LEFT");

    }

    @Test
    public void testEatMe() {
        assertB("☼☼☼☼☼☼☼☼☼☼" +
                          "☼        ☼" +
                          "☼╔════►  ☼" +
                          "☼╙       ☼" +
                          "☼        ☼" +
                          "☼        ☼" +
                          "☼   ☺    ☼" +
                          "☼     ☻  ☼" +
                          "☼        ☼" +
                          "☼☼☼☼☼☼☼☼☼☼", "DOWN");
    }
    @Test
    public void testEatMe2() {
        assertB("☼☼☼☼☼☼☼☼☼" +
                          "☼       ☼" +
                          "☼   ☺   ☼" +
                          "☼       ☼" +
                          "☼       ☼" +
                          "☼   ╓   ☼" +
                          "☼   ╚══╗☼" +
                          "☼ ☻ ◄══╝☼" +
                          "☼☼☼☼☼☼☼☼☼", "LEFT");
    }

    @Test
    public void EatMe3(){
        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                          "☼          ☻  ☼" +
                          "☼          ╔═╗☼" +
                          "☼          ║☺║☼" +
                          "☼          ║ ║☼" +
                          "☼         ╔╝ ║☼" +
                          "☼         ║  ║☼" +
                          "☼        ╔╝  ║☼" +
                          "☼╔═══════╝   ║☼" +
                          "☼╚════════╗  ║☼" +
                          "☼         ║  ║☼" +
                          "☼   ╔═════╝◄═╝☼" +
                          "☼   ║╔╗    ╓  ☼" +
                          "☼   ╚╝╚════╝  ☼" +
                          "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼","DOWN");
    }
    @Test
    public void EatMe4(){
        assertB("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                          "☼   ◄════╗    ☼" +
                          "☼        ║    ☼" +
                          "☼        ║    ☼" +
                          "☼        ║    ☼" +
                          "☼   ☻    ║    ☼" +
                          "☼        ║    ☼" +
                          "☼        ║ ☺  ☼" +
                          "☼        ║    ☼" +
                          "☼  ╓     ╚═══╗☼" +
                          "☼  ╚════════╗║☼" +
                          "☼           ╚╝☼" +
                          "☼             ☼" +
                          "☼             ☼" +
                          "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼","LEFT");
    }

    @Test
    public void testNotSameDirection() {
        assertB("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ ►  ☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼☼☼☼☼☼", "DOWN");

        assertB("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼◄ ☺ ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼", "RIGHT");

        assertB("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ ☺  ☼" +
                "☼    ☼" +
                "☼ ►  ☼" +
                "☼☼☼☼☼☼", "UP");

        assertB("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼☺  ►☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼", "LEFT");

    }

    private void assertB(String boardString, String expected) {
        // given
        YourSolver solver = new YourSolver(new RandomDice());

        // when
        String direction = solver.get((Board)new Board().forString(
                boardString));
        // then

        assertEquals(expected, direction);
    }

}
