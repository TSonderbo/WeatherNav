package com.example.weathernav.util.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weathernav.R;
import com.example.weathernav.objects.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder>
{
    private List<Route> mRoutes;
    final private OnListItemClickListener mListener;

    public RouteAdapter(OnListItemClickListener listener)
    {
        this.mListener = listener;
        this.mRoutes = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.route_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.kilometers.setText(holder.itemView.getContext()
                .getString(R.string.route_adapter_units, mRoutes.get(position).getKilometers()));
        holder.from.setText(holder.itemView.getContext()
                .getString(R.string.route_adapter_from, mRoutes.get(position).getFrom()));
        holder.to.setText(holder.itemView.getContext()
                .getString(R.string.route_adapter_to, mRoutes.get(position).getTo()));
    }

    @Override
    public int getItemCount()
    {
        return mRoutes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView kilometers;
        TextView from;
        TextView to;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            kilometers = itemView.findViewById(R.id.container_length);
            from = itemView.findViewById(R.id.container_from);
            to = itemView.findViewById(R.id.container_to);
            itemView.setOnClickListener(view -> mListener.onListItemClick(getAdapterPosition()));
        }
    }

    public void setRoutes(List<Route> mRoutes)
    {
        this.mRoutes = mRoutes;
        notifyDataSetChanged();
    }
}

