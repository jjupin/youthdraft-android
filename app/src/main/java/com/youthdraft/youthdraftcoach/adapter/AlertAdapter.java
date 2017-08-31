package com.youthdraft.youthdraftcoach.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.datamodel.PlayerRank;

import java.util.HashMap;
import java.util.List;

/**
 * Created by marty331 on 1/25/16.
 */
public class AlertAdapter extends BaseAdapter {

    private Context mContext;
    Cursor cursor;
    PlayerRank playerRank;
    private HashMap<String, PlayerRank> map;
    List<String> rankValue;

    public static String LOG_TAG = "AlertAdapter";

    public AlertAdapter(Context context, HashMap<String, PlayerRank> mMPlayersHashMap, List<String> rankOrder) {

        super();
        this.mContext = context;
        this.map = mMPlayersHashMap;
        this.rankValue = rankOrder;

    }



    @Override
    public int hashCode() {
        return playerRank != null ? playerRank.hashCode() : 0;
    }

    @Override
    public int getCount() {
        int num = map.size();
        //Log.d(LOG_TAG, "map size="+map.size());
        return num;
    }

    @Override
    public Object getItem(int position) {
        return rankValue.get(position); //map.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each item of listView
        //Log.d(LOG_TAG, "getView");
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.rank_list, null);

        // move the cursor to required position
        String[] dataKey = map.keySet().toArray(new String[map.size()]);
        /*Log.d(LOG_TAG, "map size="+map.size());
        Log.d(LOG_TAG, "map key ="+map.keySet().toString());
        Log.d(LOG_TAG, "positino ="+position);*/

        //Log.d(LOG_TAG, "rowid ="+map.get(rankValue.get(position)).rowid+" name="+map.get(rankValue.get(position)).getFirst_name());
        String firstname = map.get(rankValue.get(position)).getFirst_name();
        String lastname = map.get(rankValue.get(position)).getLast_name();
        String rating = map.get(rankValue.get(position)).getRanking().toString();
        String draft = map.get(rankValue.get(position)).getDraftable();
        TextView fn = (TextView) convertView.findViewById(R.id.tvFirst);
        TextView ln = (TextView) convertView.findViewById(R.id.tvLast);
        TextView rk = (TextView) convertView.findViewById(R.id.tvComNum);
        TextView rn = (TextView) convertView.findViewById(R.id.tvRankNum);
        TextView df = (TextView) convertView.findViewById(R.id.tvDraftVal);
        fn.setText(firstname);
        ln.setText(lastname);
        rk.setText(rating);
        rn.setText(String.valueOf(position+1));
        df.setText(draft);


        return convertView;
    }
}