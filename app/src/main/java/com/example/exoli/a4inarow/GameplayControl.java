package com.example.exoli.a4inarow;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

import com.example.exoli.a4inarow.activities.GameActivity;
import com.example.exoli.a4inarow.classes.AI;
import com.example.exoli.a4inarow.classes.Game;
import com.example.exoli.a4inarow.classes.Logic;
import com.example.exoli.a4inarow.classes.Logic.Status;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class GameplayControl implements View.OnClickListener {

    public static final int COLS = 7;
    public static final int ROWS = 6;
    private final int[][] grid = new int[ROWS][COLS];
    private final int[] freeCells = new int[COLS];
    private final Logic logic = new Logic(grid, freeCells);
    private AI ai;
    private Status status = Status.CONTINUE;
    private boolean finished = true;
    private int playerTurn;
    private final Context context;
    private final BoardView boardView;
    private final GameRules gameRules;
    private boolean aiTurn;


    class AITask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            aiTurn = true;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                Thread.currentThread();
                sleep(1000);
            } catch (InterruptedException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
            return ai.getColumn();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            selectColumn(integer);
        }
    }

    public GameplayControl(Context context, BoardView boardView, GameRules mGameRules) {
        this.context = context;
        this.gameRules = mGameRules;
        this.boardView = boardView;
        initialize();
        if (boardView != null) {
            boardView.initialize(this, mGameRules);
        }
    }

    private void initialize() {

        playerTurn = gameRules.getRule(GameRules.FIRST_TURN);

        // unfinished the game
        finished = false;
        status = Status.CONTINUE;
        // null the mGrid and mFree counter for every column
        for (int j = 0; j < COLS; ++j) {
            for (int i = 0; i < ROWS; ++i) {
                grid[i][j] = 0;
            }
            freeCells[j] = ROWS;
        }

        // create AI if needed
        if (gameRules.getRule(GameRules.OPPONENT) == GameRules.Opponent.AI) {
            ai = new AI(logic);

            switch (gameRules.getRule(GameRules.LEVEL)) {
                case GameRules.Level.EASY:
                    ai.difficulty(4);
                    break;
                case GameRules.Level.NORMAL:
                    ai.difficulty(7);
                    break;
                case GameRules.Level.HARD:
                    ai.difficulty(10);
                    break;
                default:
                    ai = null;
                    break;
            }

        } else {
            ai = null;
        }

        // if it is a computer turn, go ahead with it
        if (playerTurn == GameRules.FirstTurn.PLAYER2 && ai != null)
            aiTurn();
    }

    private void aiTurn() {
        if (finished)
            return;
        new AITask().execute();
    }

    private void selectColumn(int column) {
        if (freeCells[column] == 0)
            return;

        logic.placeMove(column, playerTurn);

        boardView.dropCoin(freeCells[column], column, playerTurn);
        playerTurn = playerTurn == AI.USER ? AI.AI_USER : AI.USER;
        checkForWin();
        aiTurn = false;
        if (BuildConfig.DEBUG) {
            logic.displayBoard();
        }
        if (playerTurn == AI.AI_USER && ai != null)
            aiTurn();
    }

    private void checkForWin() {
        status = logic.checkStatus();

        if (status != status.CONTINUE) {
            finished = true;
            ArrayList<ImageView> winDiscs = logic.getWinDiscs(boardView.getCells());
            boardView.showWinStatus(status, winDiscs);

        } else {
            boardView.togglePlayer(playerTurn);
        }
    }

    public void exitGame() {
        ((GameActivity) context).finish();
    }

    public void restartGame() {
        initialize();
        boardView.resetBoard();
    }

    @Override
    public void onClick(View v) {
        if (finished || aiTurn)
            return;
        int col = boardView.colAtX(v.getX());
        selectColumn(col);
    }

}
