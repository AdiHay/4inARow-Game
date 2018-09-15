package com.example.exoli.a4inarow;

import android.os.Bundle;

import com.example.exoli.a4inarow.classes.AI;

public class GameRules {

    public class FirstTurn extends Rule {
        public static final int PLAYER1 = AI.USER;
        public static final int PLAYER2 = AI.AI_USER;

        FirstTurn() {
            super(new int[]{PLAYER1, PLAYER2});
        }
    }

    public class Level extends Rule {

        public static final int EASY = 0;
        public static final int NORMAL = 1;
        public static final int HARD = 2;

        Level() {
            super(new int[]{EASY, NORMAL, HARD});
        }
    }

    public class Opponent extends Rule {
        public static final int PLAYER = R.string.opponent_player;
        public static final int AI = R.string.opponent_ai;


        Opponent() {
            super(new int[]{PLAYER, AI});
        }
    }

    public class Coin extends Rule {
        public static final int RED = R.drawable.red_disc;
        public static final int YELLOW = R.drawable.yellow_disc;


        Coin() {
            super(new int[]{RED, YELLOW});
        }
    }

    public static final int FIRST_TURN = 0;
    public static final int LEVEL = 1;
    public static final int OPPONENT = 2;
    public static final int COIN1 = 3;
    public static final int COIN2 = 4;
    private final Rule[] rules;

    public GameRules() {
        rules = new Rule[]{
                new FirstTurn(),
                new Level(),
                new Opponent(),
                new Coin(),
                new Coin()
        };
    }

    public int getRule(int rule) {
        return rules[rule].getSelectId();
    }

    public void setRule(int rule, int value) {
        rules[rule].setSelectId(value);
    }

    public Bundle exportTo(Bundle bundle) {
        int[] bundleRules = new int[rules.length];
        for(int i = 0; i < rules.length; ++i) {
            bundleRules[i] = rules[i].getSelectId();
        }

        bundle.putIntArray("rules", bundleRules);
        return bundle;
    }

    public void importFrom(Bundle bundle) {
        int[] bundleRules = bundle.getIntArray("rules");
        for(int i = 0; i < (bundleRules != null ? bundleRules.length : 0); ++i) {
            rules[i].setSelectId(bundleRules[i]);
        }
    }
}
