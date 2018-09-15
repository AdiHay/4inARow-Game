package com.example.exoli.a4inarow.classes;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Game implements Serializable {
    private User winner;
    private User loser;
    private LocalDateTime ldt;

    public Game() {
    }

    public Game(User winner, User loser) {
        this.winner = winner;
        this.loser = loser;
        this.ldt = LocalDateTime.now();
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public User getLoser() {
        return loser;
    }

    public void setLoser(User loser) {
        this.loser = loser;
    }

    public LocalDateTime getLdt() {
        return ldt;
    }

    public void setLdt(LocalDateTime ldt) {
        this.ldt = ldt;
    }

    @Override
    public String toString() {
        return "Game{" +
                "winner=" + winner +
                ", loser=" + loser +
                ", ldt=" + ldt +
                '}';
    }
}
