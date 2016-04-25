package com.bionicapps.tictactoe.game;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bionicapps.tictactoe.R;
import com.bionicapps.tictactoe.mode.GameModeActivity;
import com.bionicapps.tictactoe.model.Game;
import com.bionicapps.tictactoe.model.GameMode;
import com.bionicapps.tictactoe.model.Player;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by johan on 4/23/16.
 */
public class GameFragment extends Fragment implements GameAdapter.GameAdapterListener {

    @Bind(R.id.game_gridview)
    RecyclerView recyclerView;

    @Bind(R.id.game_nextplayer_turn)
    TextView nextPlayerTextView;

    private GridLayoutManager gridLayoutManager;
    private GameAdapter gameAdapter;
    private Game game;
    private Player currentPlayer;
    private int currentPlayerNumber;
    private Realm realm = Realm.getDefaultInstance();
    private Random random;
    private boolean recreating = false;

    int rowsCount;
    int playCount;

    State[][] states;

    enum State {
        X,
        O
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!recreating) {
            outState.putSerializable("states", states);
            outState.putInt("playCount", playCount);
            outState.putInt("currentPlayerNumber", currentPlayerNumber);
        }
        super.onSaveInstanceState(outState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        ButterKnife.bind(this, view);

        long gameId = getActivity().getIntent().getLongExtra(GameActivity.ARG_OBJECT_GAMEID, -1);
        if (gameId == -1) {
            getActivity().finish();
            return view;
        }
        game = realm.where(Game.class).equalTo("id", gameId).findFirst();

        rowsCount = game.getRowsCount();
        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(getActivity(), rowsCount);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (savedInstanceState != null && savedInstanceState.getSerializable("states") != null) {
            states = (State[][]) savedInstanceState.getSerializable("states");
            playCount = savedInstanceState.getInt("playCount");
            currentPlayerNumber = savedInstanceState.getInt("currentPlayerNumber");
            gameAdapter = new GameAdapter(states, rowsCount, this);
            displayNextPlayer();
        } else {
            states = new State[rowsCount][rowsCount];
            gameAdapter = new GameAdapter(states, rowsCount, this);
        }


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(gameAdapter);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int max = Math.max(recyclerView.getWidth(), recyclerView.getHeight());
                gameAdapter.setRowSize(max / rowsCount);
                recyclerView.removeOnLayoutChangeListener(this);
            }
        });

        displayNextPlayer();
    }


    private void displayNextPlayer() {
        currentPlayerNumber = playCount % 2;
        currentPlayer = game.getPlayer(currentPlayerNumber);
        nextPlayerTextView.setText(getString(R.string.player_turn, currentPlayer.getPlayerName()));
        // if Android is playing, generate random
        if (currentPlayerNumber == 1 && game.getGameMode().getEnum() == GameMode.GameModeEnum.PLAYER_VS_ANDROID) {
            boolean canPlay = false;
            int x = 0;
            int y = 0;
            while (!canPlay) {
                int r = random();
                x = r / rowsCount;
                y = r % rowsCount;
                if (states[x][y] == null) {
                    canPlay = true;
                }
            }
            play(State.O, x, y);
        }
    }

    private int random() {
        if (random == null) {
            random = new Random();
        }
        return random.nextInt(rowsCount * rowsCount);
    }


    private void currentPlayerWon() {
        realm.beginTransaction();
        game.setWinner(currentPlayer);
        realm.copyToRealm(game);
        realm.commitTransaction();
        displayResult(getString(R.string.player_won, currentPlayer.getPlayerName()));
    }

    private void displayResult(String result) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(result)
                .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        realm.beginTransaction();
                        Game newGame = new Game(game);
                        Intent intent = getActivity().getIntent();
                        intent.putExtra(GameActivity.ARG_OBJECT_GAMEID, newGame.getId());
                        realm.copyToRealm(newGame);
                        realm.commitTransaction();

                        recreating = true;
                        dialog.dismiss();
                        getActivity().recreate();
                    }
                })
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void draw() {
        realm.beginTransaction();
        game.setWinner(null);
        realm.copyToRealm(game);
        realm.commitTransaction();
        displayResult(getString(R.string.draw));
    }

    @Override
    public void onClick(int position) {
        play(currentPlayerNumber == 0 ? State.X : State.O, position / rowsCount, position % rowsCount);
    }

    private void play(State state, int x, int y) {

        if (states[x][y] == null) {
            states[x][y] = state;
        } else {
            return;
        }
        playCount++;
        gameAdapter.notifyDataSetChanged();

        //check end conditions

        // columns check
        for (int i = 0; i < rowsCount; i++) {
            if (states[x][i] != state)
                break;
            if (i == rowsCount - 1) {
                currentPlayerWon();
                return;
            }
        }

        // rows check
        for (int i = 0; i < rowsCount; i++) {
            if (states[i][y] != state)
                break;
            if (i == rowsCount - 1) {
                currentPlayerWon();
                return;
            }
        }

        // diagonal top left to bottom right check
        if (x == y) {
            for (int i = 0; i < rowsCount; i++) {
                if (states[i][i] != state)
                    break;
                if (i == rowsCount - 1) {
                    currentPlayerWon();
                    return;
                }
            }
        }

        // diagonal bottom left to top right check
        for (int i = 0; i < rowsCount; i++) {
            if (states[i][(rowsCount - 1) - i] != state)
                break;
            if (i == rowsCount - 1) {
                currentPlayerWon();
                return;
            }
        }

        //check draw
        if (playCount == (rowsCount * rowsCount)) {
            //report draw
            draw();
            return;
        }
        displayNextPlayer();
    }


    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
}
