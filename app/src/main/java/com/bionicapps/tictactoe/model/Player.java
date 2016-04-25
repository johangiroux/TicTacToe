package com.bionicapps.tictactoe.model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by johan on 4/24/16.
 */
public class Player extends RealmObject {

    public Player() {
    }

    public Player(String playerName) {
        this.playerName = playerName;
    }

    @Required
    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
