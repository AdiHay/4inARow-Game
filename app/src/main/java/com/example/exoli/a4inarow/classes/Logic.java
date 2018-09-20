package com.example.exoli.a4inarow.classes;

import android.widget.ImageView;

import com.example.exoli.a4inarow.GameplayControl;

import java.util.ArrayList;

public class Logic {

    private boolean isDrew;
    private int cell;
    private final int[][] board;
    private final int[] freeCells;
    private final int colsNum;
    private final int rowsNum;
    private int winX = 0;
    private int winY = 0;
    private int p;
    private int q;
    private static final int WIN_CONDITION = 4;
    public enum Status {CONTINUE, DRAW, WIN_P1, WIN_P2}

    public Logic (int[][] board, int[] freeCells) {
        this.board = board;
        this.rowsNum = board.length;
        this.colsNum = board[0].length;
        this.freeCells = freeCells;
    }

    private boolean horizontalCheck() {
        for (int i = 0; i < rowsNum; i++) {
            for (int j = 0; j < colsNum - 3; j++) {
                cell = board[i][j];
                if (cell == 0) isDrew = false;
                if (cell != 0 && board[i][j + 1] == cell && board[i][j + 2] == cell && board[i][j + 3] == cell) {
                    p = i;
                    q = j;
                    winX = 1;
                    winY = 0;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verticalCheck() {
        for (int j = 0; j < colsNum; j++) {
            for (int i = 0; i < rowsNum - 3; i++) {
                cell = board[i][j];
                if (cell == 0) isDrew = false;
                if (cell != 0 && board[i + 1][j] == cell && board[i + 2][j] == cell && board[i + 3][j] == cell) {
                    p = i;
                    q = j;
                    winX = 0;
                    winY = 1;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean ascendingDiagonalCheck() {
        // ascendingDiagonalCheck
        for (int i = 3; i < rowsNum; i++) {
            for (int j = 0; j < colsNum - 3; j++) {
                cell = board[i][j];
                if (cell == 0) isDrew = false;
                if (cell != 0 && board[i - 1][j + 1] == cell && board[i - 2][j + 2] == cell && board[i - 3][j + 3] == cell) {
                    p = i;
                    q = j;
                    winX = 1;
                    winY = -1;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean descendingDiagonalCheck() {
        // descendingDiagonalCheck
        for (int i = 3; i < rowsNum; i++) {
            for (int j = 3; j < colsNum; j++) {
                cell = board[i][j];
                if (cell == 0) isDrew = false;
                if (cell != 0 && board[i - 1][j - 1] == cell && board[i - 2][j - 2] == cell && board[i - 3][j - 3] == cell) {
                    p = i;
                    q = j;
                    winX = -1;
                    winY = -1;
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<ImageView> getWinDiscs(ImageView[][] cells) {
        ArrayList<ImageView> combination = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            combination.add(cells[p + winY * i][q + winX * i]);
        }
        return combination;
    }

    public void placeMove(int column, int player) {
        if (freeCells[column] > 0) {
            board[freeCells[column] - 1][column] = player;
            freeCells[column]--;
        }
    }

    public void undoMove(int column) {
        if (freeCells[column] < rowsNum) {
            freeCells[column]++;
            board[freeCells[column] - 1][column] = 0;
        }
    }

    public int columnHeight(int index) {
        return freeCells[index];
    }

    private boolean matchingCounters(int columnA, int rowA, int columnB, int rowB) {
        if (columnA < 0 || columnA >= colsNum
                || rowA < 0 || rowA >= rowsNum
                || columnB < 0 || columnB >= colsNum
                || rowB < 0 || rowB >= rowsNum) {
            return false;
        }
        return !(board[rowA][columnA] == 0 || board[rowB][columnB] == 0) && board[rowA][columnA] == board[rowB][columnB];
    }

    public boolean checkMatch(int column, int row) {
        int horizontal_matches = 0;
        int vertical_matches = 0;
        int forward_diagonal_matches = 0;
        int backward_diagonal_matches = 0;

        // horizontal matches
        for (int i = 1; i < WIN_CONDITION; i++) {
            if (matchingCounters(column, row, column + i, row)) {
                horizontal_matches++;
            } else break;
        }

        for (int i = 1; i < WIN_CONDITION; i++) {
            if (matchingCounters(column, row, column - i, row)) {
                horizontal_matches++;
            } else break;
        }

        // vertical matches
        for (int i = 1; i < WIN_CONDITION; i++) {
            if (matchingCounters(column, row, column, row + i)) {
                vertical_matches++;
            } else break;
        }

        for (int i = 1; i < WIN_CONDITION; i++) {
            if (matchingCounters(column, row, column, row - i)) {
                vertical_matches++;
            } else break;
        }

        // backward diagonal matches ( \ )
        for (int i = 1; i < WIN_CONDITION; i++) {
            if (matchingCounters(column, row, column + i, row - i)) {
                backward_diagonal_matches++;
            } else break;
        }

        for (int i = 1; i < WIN_CONDITION; i++) {
            if (matchingCounters(column, row, column - i, row + i)) {
                backward_diagonal_matches++;
            } else break;
        }

        // forward diagonal matches ( / )
        for (int i = 1; i < WIN_CONDITION; i++) {
            if (matchingCounters(column, row, column + i, row + i)) {
                forward_diagonal_matches++;
            } else break;
        }

        for (int i = 1; i < WIN_CONDITION; i++) {
            if (matchingCounters(column, row, column - i, row - i)) {
                forward_diagonal_matches++;
            } else break;
        }

        return horizontal_matches >= WIN_CONDITION - 1
                || vertical_matches >= WIN_CONDITION - 1
                || forward_diagonal_matches >= WIN_CONDITION - 1
                || backward_diagonal_matches >= WIN_CONDITION - 1;
    }

    private boolean isADraw() {
        for (int j = 0; j < colsNum ; j++) {
            cell = board[0][j];
            if (cell == 0) {
                isDrew = false;
                return false;
            }
            if (cell != 0 ) {
                isDrew = true;
            }
        }

        return true;
    }

    public Status checkStatus() {
        isDrew = true;
        cell = 0;

        if(horizontalCheck() || verticalCheck() || ascendingDiagonalCheck() || descendingDiagonalCheck()) {
            if (cell == AI.USER)
                return Status.WIN_P1;
            else
                return Status.WIN_P2;
        }

        else if(isADraw())
            return Status.DRAW;
        else
            return Status.CONTINUE;

    }

    public int getColsNum() {
        return colsNum;
    }

    public int getRowsNum() {
        return rowsNum;
    }

    public void displayBoard() {
        System.out.println();
        for (int i = 0; i <= 5; ++i) {
            for (int j = 0; j <= 6; ++j) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
