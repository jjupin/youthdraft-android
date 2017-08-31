package com.youthdraft.youthdraftcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;
import com.youthdraft.youthdraftcoach.interfaces.OnItemClickListener;
import com.youthdraft.youthdraftcoach.views.ConnectionsImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjupin on 11/15/16.
 */

public class MyTeamRecyclerAdapter extends RecyclerView.Adapter<MyTeamRecyclerAdapter.ViewHolder> implements OnItemClickListener {

    private static String TAG = "MyTeamRecycleAdapter";

    private ViewGroup stickyHeaderView;

    private List<PlayerInfo> mDataset;

    public MyTeamRecyclerAdapter(List<PlayerInfo> players ) {
        this.mDataset = players;
    }

    public void add(int position, PlayerInfo item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyTeamRecyclerAdapter(ArrayList<PlayerInfo> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyTeamRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_team_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyTeamRecyclerAdapter.ViewHolder vh = new MyTeamRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyTeamRecyclerAdapter.ViewHolder holder, int position) {
        holder.bind(position, this);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onItemClick(int position) {
        Log.e("MyTeamRecyclerAdapter", "Survey Says! " + mDataset.get(position).getFirstname());
        // Toast.makeText(, "Item Clicked", Toast.LENGTH_LONG).show();
    }


public class ViewHolder extends RecyclerView.ViewHolder {

    public ConnectionsImageView avatar;
    public TextView txtHeader;
    public TextView txtFooter;
    public TextView txtScore;

    public ViewHolder(View v) {
        super(v);
        txtHeader = (TextView) v.findViewById(R.id.text1);
        txtFooter = (TextView) v.findViewById(R.id.text2);
        txtScore  = (TextView) v.findViewById(R.id.score);
    }

    public void bind(final int position, final OnItemClickListener listener) {
        final PlayerInfo player = mDataset.get(position);

        txtHeader.setText(player.getFullname());
        txtFooter.setText("Footer: " + mDataset.get(position));
        txtScore.setText("7.9");

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
    }
}

}