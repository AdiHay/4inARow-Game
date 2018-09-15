package com.example.exoli.a4inarow.classes;

public class Move {
    private final int col;
    private final int score;

    public Move(int column, int score) {
        this.col = column;
        this.score = score;
    }

    public int getCol() {
        return col;
    }

    public int getScore() {
        return score;
    }
}
