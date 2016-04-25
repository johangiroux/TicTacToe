package com.bionicapps.tictactoe.mode;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bionicapps.tictactoe.R;
import com.bionicapps.tictactoe.game.GameActivity;
import com.bionicapps.tictactoe.model.Game;
import com.bionicapps.tictactoe.model.GameMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by johan on 4/23/16.
 */
public class GameModeFragment extends Fragment {

    private static final int MIN_ROWS = 3;
    private static final int MAX_ROWS = 8;

    @Bind(R.id.spinner_game_mode)
    Spinner spinnerGameMode;

    @Bind(R.id.spinner_grid_size)
    Spinner spinnerGridSize;

    @Bind(R.id.player_one_name)
    EditText playerOneEditText;

    @Bind(R.id.player_two_name)
    EditText playerTwoEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mode, container, false);

        ButterKnife.bind(this, view);

        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.game_mode_values, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGameMode.setAdapter(modeAdapter);
        spinnerGameMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                playerTwoEditText.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayList<String> rows = new ArrayList<>(MAX_ROWS - MIN_ROWS);
        for (int i = MIN_ROWS; i < MAX_ROWS + 1; i++) {
            rows.add(i + "x" + i);
        }

        ArrayAdapter<String> rowsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, rows);
        rowsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGridSize.setAdapter(rowsAdapter);

        return view;
    }

    @OnClick(R.id.start_game)
    protected void startGame(View view) {
        Realm realm = Realm.getDefaultInstance();

        Game game = new Game(Game.getNextId(realm), new GameMode(spinnerGameMode.getSelectedItemPosition() == 0 ?
                GameMode.GameModeEnum.PLAYER_VS_PLAYER :
                GameMode.GameModeEnum.PLAYER_VS_ANDROID), getPlayerNameOne(),
                getPlayerNameTwo(spinnerGameMode.getSelectedItemPosition()), spinnerGridSize.getSelectedItemPosition() + MIN_ROWS);
        realm.beginTransaction();
        realm.copyToRealm(game);
        realm.commitTransaction();

        Intent intent = new Intent(getActivity(), GameActivity.class);
        intent.putExtra(GameActivity.ARG_OBJECT_GAMEID, game.getId());
        startActivity(intent);

        getActivity().finish();
    }

    private String getPlayerNameOne() {
        if (playerOneEditText.getText().toString().isEmpty()) return getString(R.string.player_one);
        return playerOneEditText.getText().toString();
    }

    private String getPlayerNameTwo(int mode) {
        if (mode == 1) return getString(R.string.android);
        if (playerTwoEditText.getText().toString().isEmpty()) return getString(R.string.player_two);
        return playerTwoEditText.getText().toString();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
}
