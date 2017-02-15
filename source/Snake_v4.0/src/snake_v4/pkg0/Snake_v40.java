/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake_v4.pkg0;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Vojta
 */
public class Snake_v40 extends Application {

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
    public static final int SIZE = 40;
    public static final int WEIGHT = 20 * SIZE;
    public static final int HEIGHT = 15 * SIZE;

    private Direction direction = Direction.RIGHT;
    private boolean moved = false;
    private boolean running = false;
    private Timeline timeline = new Timeline();
    private ObservableList<Node> snake;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WEIGHT, HEIGHT);

        Group snakeBody = new Group();
        snake = snakeBody.getChildren();

        Rectangle food = new Rectangle(SIZE, SIZE);
        food.setFill(Color.RED);
        food.setTranslateX((int) (Math.random() * (WEIGHT - SIZE)) / SIZE * SIZE);
        food.setTranslateY((int) (Math.random() * (HEIGHT - SIZE)) / SIZE * SIZE);

        KeyFrame frame = new KeyFrame(Duration.seconds(0.2), event -> {
            if (!running) {
                return;
            }
            boolean toRemove = snake.size() > 1;
            Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

            double tailX = tail.getTranslateX();
            double tailY = tail.getTranslateY();

            switch (direction) {
                case UP:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() - SIZE);
                    break;
                case DOWN:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() + SIZE);
                    break;
                case LEFT:
                    tail.setTranslateX(snake.get(0).getTranslateX() - SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
                case RIGHT:
                    tail.setTranslateX(snake.get(0).getTranslateX() + SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
            }
            moved = true;

            if (toRemove) {
                snake.add(0, tail);
            }

            for (Node rect : snake) {
                if (rect != tail && tail.getTranslateX() == rect.getTranslateX()
                        && tail.getTranslateY() == rect.getTranslateY()) {
                    restartGame();
                    break;
                }
            }
            if (tail.getTranslateX() < 0 || tail.getTranslateX() >= WEIGHT
                    || tail.getTranslateY() < 0 || tail.getTranslateY() >= HEIGHT) {
                restartGame();
            }
            if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
                food.setTranslateX((int) (Math.random() * (WEIGHT - SIZE)) / SIZE * SIZE);
                food.setTranslateY((int) (Math.random() * (HEIGHT - SIZE)) / SIZE * SIZE);

                Rectangle rect = new Rectangle(SIZE, SIZE);
                rect.setTranslateX(tailX);
                rect.setTranslateY(tailY);
                snake.add(rect);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        root.getChildren().addAll(food, snakeBody);
        return root;
    }

    private void restartGame() {
        stopGame();
        startGame();
    }

    private void stopGame() {
        running = false;
        timeline.stop();
        snake.clear();
    }

    private void startGame() {
        direction = Direction.RIGHT;
        Rectangle head = new Rectangle(SIZE, SIZE);
        snake.add(head);
        timeline.play();
        running = true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(event -> {
            if (!moved) {
                return;
            }
            switch (event.getCode()) {
                case W:
                    if (direction != Direction.DOWN) {
                        direction = Direction.UP;
                    }
                    break;
                case S:
                    if (direction != Direction.UP) {
                        direction = Direction.DOWN;
                    }
                    break;
                case A:
                    if (direction != Direction.RIGHT) {
                        direction = Direction.LEFT;
                    }
                    break;
                case D:
                    if (direction != Direction.LEFT) {
                        direction = Direction.RIGHT;
                    }
                    break;
            }
            moved = false;
        });
        primaryStage.setTitle("Snake");
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
