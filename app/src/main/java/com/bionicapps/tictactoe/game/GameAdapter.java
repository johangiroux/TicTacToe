package com.bionicapps.tictactoe.game;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bionicapps.tictactoe.view.GridCell;

/**
 * Created by johan on 4/24/16.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.TicTacToeViewHolder> implements View.OnClickListener {

    private GameFragment.State[][] states;
    private GameAdapterListener gameAdapterListener;
    private int rowsCount;
    private int rowSize;

    public GameAdapter(GameFragment.State[][] states, int rowsCount, GameAdapterListener gameAdapterListener) {
        this.states = states;
        this.rowsCount = rowsCount;
        this.gameAdapterListener = gameAdapterListener;
    }

    public void setRowSize(int rowSize) {
        this.rowSize = rowSize;
        notifyDataSetChanged();
    }

    @Override
    public TicTacToeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridCell gridCell = new GridCell(parent.getContext());
        gridCell.setLayoutParams(new RecyclerView.LayoutParams(rowSize, rowSize));
        return new TicTacToeViewHolder(gridCell);
    }

    @Override
    public void onBindViewHolder(TicTacToeViewHolder holder, int position) {
        holder.stateTextView.setPosition(position, rowsCount);
        holder.stateTextView.setText(getTextForState(states[position/rowsCount][position%rowsCount]));
    }

    @Override
    public int getItemCount() {
        if (rowSize == 0) return 0;
        return rowsCount*rowsCount;
    }

    private static String getTextForState(GameFragment.State state) {
        if (state == null) return " ";
        if (state == GameFragment.State.O) {
            return "O";
        } else {
            return "X";
        }
    }

    @Override
    public void onClick(View v) {
        GridCell gridCell = (GridCell) v;
        int position = gridCell.getPosition();
        if (gameAdapterListener != null) {
            gameAdapterListener.onClick(position);
        }
    }

    public class TicTacToeViewHolder extends RecyclerView.ViewHolder {

        public GridCell stateTextView;

        public TicTacToeViewHolder(View itemView) {
            super(itemView);
            stateTextView = (GridCell) itemView;
            stateTextView.setOnClickListener(GameAdapter.this);
        }
    }

    public interface GameAdapterListener {
        void onClick(int position);
    }

}
