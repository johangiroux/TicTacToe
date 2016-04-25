package com.bionicapps.tictactoe.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bionicapps.tictactoe.R;
import com.bionicapps.tictactoe.model.Game;

import io.realm.RealmResults;


/**
 * Created by johan on 4/24/16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private LayoutInflater inflater;
    private RealmResults<Game> games;

    public HistoryAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setGames(RealmResults<Game> games) {
        this.games = games;
    }


    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(inflater.inflate(R.layout.cell_history, parent, false));
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Game game = games.get(position);
        holder.players.setText(context.getString(R.string.history_player_vs_player,
                game.getPlayerOne().getPlayerName(), game.getPlayerTwo().getPlayerName()));
        holder.winner.setText(game.getWinner() == null ? context.getString(R.string.draw) :
                            context.getString(R.string.player_won, game.getWinner().getPlayerName()));
    }

    @Override
    public int getItemCount() {
        return games == null ? 0 : games.size();
    }

    protected static class HistoryViewHolder extends RecyclerView.ViewHolder {

        public TextView players;
        public TextView winner;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            players = (TextView) itemView.findViewById(R.id.history_players);
            winner = (TextView) itemView.findViewById(R.id.history_winner);
        }
    }
}
