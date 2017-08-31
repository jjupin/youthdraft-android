package com.youthdraft.youthdraftcoach.activity.players.adapterModel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.views.ConnectionsImageView;
import com.youthdraft.youthdraftcoach.views.OpenSansTextView;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by jjupin on 11/15/16.
 */

public class MyTeamItem extends AbstractSectionableItem<MyTeamItem.ViewHolder, MyTeamHeaderItem> {

    private final String TAG = "MyTeamItem";

    private Context context;

    public boolean usedBySearchView;

    private String contactRefId;
    private String letter;

    public MyTeamItem(Context context, String refId, String letter, MyTeamHeaderItem header) {

        super(header);
        this.context = context;
        this.contactRefId = refId;
        this.letter = letter;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MyTeamItem) {
            MyTeamItem oo = (MyTeamItem) o;
            return oo.contactRefId.equals(this.contactRefId);
        }
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.my_team_item;
    }

    public String getContactRefId() {
        return this.contactRefId;
    }

    @Override
    public ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int positron, List payloads) {

        ConnectionsImageView avatarView = holder.avatarView;
        OpenSansTextView moreCountView = holder.moreCountView;
        OpenSansTextView textView = holder.textView;

        holder.textView.setText(this.contactRefId);

        /**

        ContactObject contact = ModelManager.getContact(this.contactRefId, context, null);

        int count = -1;
        if (contact != null) {
            count = contact.getUnread_count();
        }

        String displayCount = (count > 99) ? " 99+ " : count + "";
        moreCountView.setText(displayCount);

        // TO-DO: turn this back to ">0" when needing to hide the visibility of the updates flag
        moreCountView.setVisibility((count > 0) ? View.VISIBLE : View.GONE);
        moreCountView.setVisibility(getHeader().getTitle().equals("Your Contacts") ? View.GONE : View.VISIBLE);

        avatarView.setAvatar(contact);

        if (contact != null) {
            textView.setText(contact.getDisplayName());
        }

        SocialNetworkTextView socialNetworkTextView = holder.socialNetworkView;

        String networks = "";
        if (contact != null && contact.getProfile() != null) {
            RealmList<RealmString> networksList = contact.getProfile().getNetworks();
            for (RealmString string : networksList) {
                try {
                    SocialNetworkTextView.Icon icon = SocialNetworkTextView.Icon.valueOf(string.getValue().toUpperCase());
                    networks += icon.getValue() + "    ";
                } catch (Exception ex) { // if we get here, then the data contained an unknown network - so ignore it
                    Log.d(TAG, "Contact: " + contact.getContent() + " has unknown network in feed: " + string);
                }
            }
        }
        socialNetworkTextView.setText(networks);

        // since I'm temporarily using this for searchView results - I need to modify the items to look like the UX for Searches...

        // ProfileObject profile = contact.getProfile();

        //boolean dontShowMore = (profile == null || (profile.getEmails().size() == 0 && profile.getAddresses().size() == 0 && profile.getPhone_numbers().size() == 0));

        socialNetworkTextView.setVisibility(usedBySearchView ? View.GONE : View.VISIBLE);
         **/

        holder.letterView.setText(letter);
    }

    protected static final class ViewHolder extends FlexibleViewHolder {

        public ConnectionsImageView avatarView;
        public OpenSansTextView moreCountView;
        public OpenSansTextView textView;
        public OpenSansTextView letterView;

        public ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            avatarView = (ConnectionsImageView) view.findViewById(R.id.avatarview);
            //moreCountView = (OpenSansTextView) view.findViewById(R.id.moreCount);
            textView = (OpenSansTextView) view.findViewById(R.id.text1);
            letterView = (OpenSansTextView) view.findViewById(R.id.score);
        }
    }
}