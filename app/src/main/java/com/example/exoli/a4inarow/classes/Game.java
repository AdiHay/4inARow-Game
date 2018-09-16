package com.example.exoli.a4inarow.classes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Game implements Serializable {
    private User winner;
    private User loser;
    private LocalDateTime ldt;
    private String gameTag;

    public Game() {
    }

    public Game(User winner, User loser) {
        this.winner = winner;
        this.loser = loser;
        this.ldt = LocalDateTime.now();
        setGameTag("game_" + ldt.format(DateTimeFormatter.ofPattern("dd-MM-uu_HH:mm:ss")));
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

    public String getGameTag() { return gameTag; }

    public void setGameTag(String gameTag) { this.gameTag = gameTag; }

    @Override
    public String toString() {
        return "Game{" +
                "winner=" + winner +
                ", loser=" + loser +
                ", ldt=" + ldt +
                '}';
    }
}
