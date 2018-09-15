package com.example.exoli.a4inarow.classes;

public class AI {

    public static final int USER = 1;
    public static final int AI_USER = 2;

    private final Logic logic;

    private int maxDepth;

    public AI(Logic logic) {
        this.logic = logic;
    }

    public void difficulty(int depth) {
        this.maxDepth = depth;
    }

    public int getColumn() {
        return move(AI_USER, USER, -10000, 10000, maxDepth).getCol();
    }

    private Move move(int player1, int player2, int bestP1Score, int bestP2Score, int depth) {
        Move best = new Move(-1, player1 == AI.AI_USER ? bestP1Score : bestP2Score);

        for (int i = 0; i < logic.getColsNum(); i++) {
            if (logic.columnHeight(i) > 0) {
                // add a counter to that column, then check for win-condition
                logic.placeMove(i, player1);
                // score this move and all its children
                int score = 0;
                if (logic.checkMatch(i, logic.columnHeight(i))) {
                    // this move is a winning move for the player
                    score = player1 == AI.AI_USER ? 1 : -1;
                } else if (depth != 1) {
                    // this move wasn't a win or a draw, so go to the next move
                    score = move(player2, player1, bestP1Score, bestP2Score,
                            depth - 1).getScore();
                }
                logic.undoMove(i);
                // if this move beats this player's best move so far, record it
                if (player1 == AI.AI_USER && score > best.getScore()) {
                    best = new Move(i, score);
                    bestP1Score = score;
                } else if (player1 == AI.USER && score < best.getScore()) {
                    best = new Move(i, score);
                    bestP2Score = score;
                }
                // don't continue with this branch, we've already found better
                if (bestP1Score >= bestP2Score) {
                    return best;
                }
            }
        }

        return best;
    }
}
