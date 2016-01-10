package ru.pandohva.rgbcircles;

import java.util.ArrayList;

public class GameManager {
    public static final int NUMBER_OF_ENEMIES = 10;
    private MainCircle mainCircle;
    private ArrayList<EnemyCircle> circles;
    private CanvasView canvasView;
    private static int width;
    private static int height;

    public GameManager(CanvasView canvasView, int w, int h) {
        this.canvasView = canvasView;
        width = w;
        height = h;
        initMainCircle();
        initEnemyCircles();
    }

    private void initEnemyCircles() {
        SimpleCircle mainCircleArea = mainCircle.getCircleArea();
        circles = new ArrayList<EnemyCircle>();
        for (int i = 0; i < NUMBER_OF_ENEMIES; i++) {
            EnemyCircle circle;
            do {
                circle = EnemyCircle.getRandomCircle();
            } while (circle.isIntersect(mainCircleArea));
            recalculateCircleRadius(circle);
            circles.add(circle);
        }
        calculateAndSetCirclesColor();
    }

    private void calculateAndSetCirclesColor() {
        for (EnemyCircle circle : circles) {
            circle.setEnemyOrFoodDependsOn(mainCircle);
        }
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    private void initMainCircle() {
        mainCircle = new MainCircle(width / 2, height / 2);
    }

    public void onDraw() {
        canvasView.drawCircle(mainCircle);
        for (EnemyCircle circle : circles) {
            canvasView.drawCircle(circle);
        }
    }

    public void onTouchEvent(int x, int y) {
        mainCircle.moveMainCircleWhenTouchAt(x, y);
        checkCollision();
        moveCircles();
    }

    private void checkCollision() {
        SimpleCircle circleForDel = null;
        for (EnemyCircle circle : circles) {
            if (mainCircle.isIntersect(circle)) {
                if (circle.isSmallerThan(mainCircle)) {
                    mainCircle.growRadius(circle);
                    circleForDel = circle;
                    calculateAndSetCirclesColor();
                    break;
                } else {
                    gameEnd("You lose!");
                    return;
                }
            }
        }
        if (circleForDel != null) {
            circles.remove(circleForDel);
        }
        if (circles.isEmpty()) {
            gameEnd("You win!");
        }
    }
    
    private void gameEnd(String text) {
        canvasView.showMessage(text);
        mainCircle.initRadius();
        initEnemyCircles();
        canvasView.redraw();
    }
    
    private void moveCircles() {
        for (EnemyCircle circle : circles) {
            circle.moveOneStep();
        }
    }

    //// TODO: 04.01.2016 Доделать
    public static void recalculateCircleRadius(SimpleCircle circle) {
        circle.radius *= 768 / (width < height ? width : height);
//        System.out.println(circle.radius);
    }
}
