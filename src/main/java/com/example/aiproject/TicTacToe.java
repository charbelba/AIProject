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
                            playerTurn = false;
                        }
                        checkGame();
                        if (!gameEnded && !playerTurn) {
                            int[] move = findBestMove();
                            buttons[move[0]][move[1]].setText("O");
                            playerTurn = true;
                            checkGame();
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

    private void checkGame() {
        String winner = checkWinner();
        if (!winner.equals("")) {
            endGame(winner + " wins!");
        } else if (!isMovesLeft()) {
            endGame("It's a draw!");
        }
    }

    private String checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (!buttons[i][0].getText().isEmpty() && buttons[i][0].getText().equals(buttons[i][1].getText()) && buttons[i][1].getText().equals(buttons[i][2].getText())) {
                return buttons[i][0].getText();
            }
            if (!buttons[0][i].getText().isEmpty() && buttons[0][i].getText().equals(buttons[1][i].getText()) && buttons[1][i].getText().equals(buttons[2][i].getText())) {
                return buttons[0][i].getText();
            }
        }
        // Check diagonls
        if (!buttons[0][0].getText().isEmpty() && buttons[0][0].getText().equals(buttons[1][1].getText()) && buttons[1][1].getText().equals(buttons[2][2].getText())) {
            return buttons[0][0].getText();
        }
        if (!buttons[0][2].getText().isEmpty() && buttons[0][2].getText().equals(buttons[1][1].getText()) && buttons[1][1].getText().equals(buttons[2][0].getText())) {
            return buttons[0][2].getText();
        }
        return "";
    }

    private void endGame(String message) {
        gameEnded = true;
        System.out.println(message);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setDisable(true);
            }
        }
    }

    private int evaluate() {
        String winner = checkWinner();
        if (winner.equals("X")) {
            return -10;
        } else if (winner.equals("O")) {
            return 10;
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

        if (Math.abs(score) == 10) {
            return score - depth;
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
                        int val = minimax(depth + 1, false, alpha, beta);
                        buttons[i][j].setText("");
                        best = Math.max(best, val);
                        alpha = Math.max(alpha, best);
                        if (beta <= alpha) {
                            break;
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
                        int val = minimax(depth + 1, true, alpha, beta);
                        buttons[i][j].setText("");
                        best = Math.min(best, val);
                        beta = Math.min(beta, best);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return best;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
