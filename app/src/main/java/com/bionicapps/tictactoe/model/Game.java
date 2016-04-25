package com.bionicapps.tictactoe.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by johan on 4/24/16.
 */
public class Game extends RealmObject {

    public Game() {
    }

    public Game(Game game) {
        this.id = game.getId() + 1;
        this.gameMode = game.getGameMode();
        this.playerOne = game.getPlayerOne();
        this.playerTwo = game.getPlayerTwo();
        this.rowsCount = game.getRowsCount();
    }

    public Game(long id, GameMode gameMode, String playerOne, String playerTwo, int rowsCount) {
        this.id = id;
        this.gameMode = gameMode;
        this.playerOne = new Player(playerOne);
        this.playerTwo = new Player(playerTwo);
        this.rowsCount = rowsCount;
    }

    public Game(long id, GameMode gameMode, Player playerOne, Player playerTwo, int rowsCount) {
        this.id = id;
        this.gameMode = gameMode;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.rowsCount = rowsCount;
    }

    @PrimaryKey
    private long id;
    private Player playerOne;
    private Player playerTwo;
    private GameMode gameMode;
    private int rowsCount;
    private Player winner; // if null == draw


    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(int rowsCount) {
        this.rowsCount = rowsCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getPlayer(int playerNumber) {
        if (playerNumber == 0) {
            return playerOne;
        } else {
            return playerTwo;
        }
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public static int getNextId(Realm realm) {
        Number number = realm.where(Game.class).max("id");
        // first item in db
        if (number == null) {
            return 1;
        }
        return number.intValue() + 1;
    }
}
