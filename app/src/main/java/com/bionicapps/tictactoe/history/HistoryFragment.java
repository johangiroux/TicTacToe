package com.bionicapps.tictactoe.history;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bionicapps.tictactoe.R;
import com.bionicapps.tictactoe.model.Game;
import com.bionicapps.tictactoe.view.SimpleDividerItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by johan on 4/24/16.
 */
public class HistoryFragment extends Fragment {

    private HistoryAdapter historyAdapter;

    @Bind(R.id.history_recyclerview)
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        ButterKnife.bind(this, view);

        historyAdapter = new HistoryAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        recyclerView.setAdapter(historyAdapter);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Game> result = realm.where(Game.class).findAllSortedAsync("id", Sort.DESCENDING);
        result.addChangeListener(callback);
        historyAdapter.setGames(result);


        return view;
    }

    private RealmChangeListener callback = new RealmChangeListener() {
        @Override
        public void onChange() {
            historyAdapter.notifyDataSetChanged();
        }
    };
}
