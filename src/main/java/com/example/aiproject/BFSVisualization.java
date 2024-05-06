package com.example.aiproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Queue;

public class BFSVisualization extends Application {
    private static final int SIZE = 10;
    private static final int SQUARE_SIZE = 50;
    private static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private Rectangle[][] squares;
    private boolean[][] visited;
    private int[][] parent;
    private int startX = -1;
    private int startY = -1;
    private int endX = -1;
    private int endY = -1;

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(1);
        grid.setVgap(1);

        squares = new Rectangle[SIZE][SIZE];
        visited = new boolean[SIZE][SIZE];
        parent = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                square.setFill(Color.GREY);
                grid.add(square, j, i);
                int x = i;
                int y = j;
                square.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (startX == -1 && startY == -1) {
                            startX = x;
                            startY = y;
                            square.setFill(Color.GREEN);
                        } else if (endX == -1 && endY == -1) {
                            endX = x;
                            endY = y;
                            square.setFill(Color.RED);
                            new Thread(() ->bfs()).start();
                        }
                    }
                });
                squares[i][j] = square;
            }
        }

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.setTitle("BFS Visualization");
        primaryStage.show();
    }

    private void bfs() {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (x == endX && y == endY) {
                squares[x][y].setFill(Color.GREEN);
                backtrackPath();
                return;
            }

            for (int[] dir : DIRS) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (isValid(newX, newY) && !visited[newX][newY]) {
                    queue.add(new int[]{newX, newY});
                    visited[newX][newY] = true;
                    parent[newX][newY] = x * SIZE + y;
                    squares[newX][newY].setFill(Color.LIGHTBLUE);
                }
            }
        }
    }

    private void backtrackPath() {
        int currentX = endX;
        int currentY = endY;
        while (currentX != startX || currentY != startY) {
            int parentX = parent[currentX][currentY] / SIZE;
            int parentY = parent[currentX][currentY] % SIZE;
            squares[parentX][parentY].setFill(Color.YELLOW);
            currentX = parentX;
            currentY = parentY;
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
