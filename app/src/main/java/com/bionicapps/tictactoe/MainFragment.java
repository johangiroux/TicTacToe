package com.bionicapps.tictactoe;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bionicapps.tictactoe.history.HistoryActivity;
import com.bionicapps.tictactoe.mode.GameModeActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by johan on 4/23/16.
 */
public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.play_button)
    protected void onPlayClick() {
        startActivity(new Intent(getActivity(), GameModeActivity.class));
    }

    @OnClick(R.id.history_button)
    protected void onHistoryClick() {
        startActivity(new Intent(getActivity(), HistoryActivity.class));
    }
}
