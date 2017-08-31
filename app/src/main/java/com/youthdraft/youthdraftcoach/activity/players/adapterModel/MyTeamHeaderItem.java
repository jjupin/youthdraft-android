package com.youthdraft.youthdraftcoach.activity.players.adapterModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.views.OpenSansTextView;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by jjupin on 11/15/16.
 */

public class MyTeamHeaderItem extends AbstractHeaderItem<MyTeamHeaderItem.ViewHolder> {

    private String title;
    private boolean doShowMore = false;
    private boolean expanded = false;

    public MyTeamHeaderItem(String title) {
        this(title, false, false);
    }
    public MyTeamHeaderItem(String title, boolean doShowMore, boolean isExpanded) {
        super();
        this.title = title;
        this.doShowMore = doShowMore;
        this.expanded = isExpanded;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MyTeamHeaderItem) {
            MyTeamHeaderItem  oo = (MyTeamHeaderItem ) o;
            return oo.title.equals(this.title);
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.my_team_header;
    }

    @Override
    public ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        holder.titleView.setText(title);
        holder.rightTitleView.setVisibility(this.doShowMore ? View.VISIBLE : View.GONE);
        holder.rightTitleView.setText(expanded ? "VIEW LESS" : "VIEW ALL");
        holder.titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    public static class ViewHolder extends FlexibleViewHolder {
        public OpenSansTextView titleView;
        public OpenSansTextView rightTitleView;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter, true);
            this.titleView = (OpenSansTextView) view.findViewById(R.id.title);
            this.rightTitleView = (OpenSansTextView) view.findViewById(R.id.rightTitle);
        }
    }
}
