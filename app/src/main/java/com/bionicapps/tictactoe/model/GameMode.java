package com.bionicapps.tictactoe.model;

import io.realm.RealmObject;

/**
 * Created by johan on 4/24/16.
 */
public class GameMode extends RealmObject {

    public GameMode() {
    }

    public GameMode(GameModeEnum gameModeEnum) {
        saveEnum(gameModeEnum);
    }

    public enum GameModeEnum {
        PLAYER_VS_PLAYER,
        PLAYER_VS_ANDROID
    }


    private String enumGameMode;

    public void saveEnum(GameModeEnum val) {
        this.enumGameMode = val.toString();
    }

    public GameModeEnum getEnum() {
        return GameModeEnum.valueOf(enumGameMode);
    }

}
