package com.example.aiproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TicTacToe extends Application {

    private Button[][] buttons = new Button[3][3];
    private boolean playerTurn = true;
    private boolean gameEnded = false;

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setPrefSize(100, 100);
                button.setOnAction(event -> {
                    if (!gameEnded && button.getText().isEmpty()) {
                        if (playerTurn) {
                            button.setText("X");
                        } else {
                            button.setText("O");
                        }
                        playerTurn = !playerTurn;
                        checkGame();
                        if (!gameEnded && !playerTurn) {
                            int[] move = findBestMove();
                            buttons[move[0]][move[1]].fire();
                        }
                    }
                });
                buttons[i][j] = button;
                grid.add(button, j, i);
            }
        }

        Scene scene = new Scene(grid, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.show();
    }

    private int evaluate() {
        for (int row = 0; row < 3; row++) {
            if (buttons[row][0].getText().equals(buttons[row][1].getText()) &&
                    buttons[row][1].getText().equals(buttons[row][2].getText())) {
                if (buttons[row][0].getText().equals("X")) {
                    return 10;
                } else if (buttons[row][0].getText().equals("O")) {
                    return -10;
                }
            }
        }

        for (int col = 0; col < 3; col++) {
            if (buttons[0][col].getText().equals(buttons[1][col].getText()) &&
                    buttons[1][col].getText().equals(buttons[2][col].getText())) {
                if (buttons[0][col].getText().equals("X")) {
                    return 10;
                } else if (buttons[0][col].getText().equals("O")) {
                    return -10;
                }
            }
        }

        if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText())) {
            if (buttons[0][0].getText().equals("X")) {
                return 10;
            } else if (buttons[0][0].getText().equals("O")) {
                return -10;
            }
        }

        if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][0].getText())) {
            if (buttons[0][2].getText().equals("X")) {
                return 10;
            } else if (buttons[0][2].getText().equals("O")) {
                return -10;
            }
        }

        return 0;
    }

    private boolean isMovesLeft() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                if (button.getText().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[] findBestMove() {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    buttons[i][j].setText("O");
                    int moveVal = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    buttons[i][j].setText("");
                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(int depth, boolean isMaximizingPlayer, int alpha, int beta) {
        int score = evaluate();

        if (score == 10) {
            return score - depth;
        }
        if (score == -10) {
            return score + depth;
        }
        if (!isMovesLeft()) {
            return 0;
        }

        if (isMaximizingPlayer) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().isEmpty()) {
                        buttons[i][j].setText("O");
                        best = Math.max(best, minimax(depth + 1, false, alpha, beta));
                        buttons[i][j].setText("");
                        alpha = Math.max(alpha, best);
                        if (beta <= alpha) {
                            return best;
                        }
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j].getText().isEmpty()) {
                        buttons[i][j].setText("X");
                        best = Math.min(best, minimax(depth + 1, true, alpha, beta));
                        buttons[i][j].setText("");
                        beta = Math.min(beta, best);
                        if (beta <= alpha) {
                            return best;
                        }
                    }
                }
            }
            return best;
        }
    }

    private void checkGame() {
        for (int row = 0; row < 3; row++) {
            if (!buttons[row][0].getText().isEmpty() &&
                    buttons[row][0].getText().equals(buttons[row][1].getText()) &&
                    buttons[row][1].getText().equals(buttons[row][2].getText())) {
                endGame(buttons[row][0].getText() + " wins!");
                return;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (!buttons[0][col].getText().isEmpty() &&
                    buttons[0][col].getText().equals(buttons[1][col].getText()) &&
                    buttons[1][col].getText().equals(buttons[2][col].getText())) {
                endGame(buttons[0][col].getText() + " wins!");
                return;
            }
        }

        if (!buttons[0][0].getText().isEmpty() &&
                buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText())) {
            endGame(buttons[0][0].getText() + " wins!");
            return;
        }

        if (!buttons[0][2].getText().isEmpty() &&
                buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][0].getText())) {
            endGame(buttons[0][2].getText() + " wins!");
            return;
        }

        if (!isMovesLeft()) {
            endGame("It's a draw!");
            return;
        }
    }

    private void endGame(String message) {
        gameEnded = true;
        System.out.println(message);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
