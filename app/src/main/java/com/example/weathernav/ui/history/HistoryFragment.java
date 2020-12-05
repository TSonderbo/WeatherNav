package com.example.weathernav.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathernav.R;
import com.example.weathernav.util.recyclerview.OnListItemClickListener;
import com.example.weathernav.util.recyclerview.RouteAdapter;

public class HistoryFragment extends Fragment implements OnListItemClickListener
{
    RecyclerView mRouteList;
    RouteAdapter mRouteAdapter;
    HistoryViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        mRouteList = root.findViewById(R.id.history_recycler_view);
        mRouteList.hasFixedSize();
        mRouteList.setLayoutManager(new LinearLayoutManager(root.getContext()));
        mRouteAdapter = new RouteAdapter(this);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        mViewModel.getRouteHistory().observe(getViewLifecycleOwner(), routes -> mRouteAdapter.setRoutes(routes));
        mRouteList.setAdapter(mRouteAdapter);
        return root;
    }

    @Override
    public void onListItemClick(int clickedItemIndex)
    {

    }
}
