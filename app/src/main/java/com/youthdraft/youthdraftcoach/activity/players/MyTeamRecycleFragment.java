package com.youthdraft.youthdraftcoach.activity.players;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.activity.MainActivity;
import com.youthdraft.youthdraftcoach.activity.players.adapterModel.MyTeamHeaderItem;
import com.youthdraft.youthdraftcoach.activity.players.adapterModel.MyTeamItem;
import com.youthdraft.youthdraftcoach.adapter.MyTeamRecyclerAdapter;
import com.youthdraft.youthdraftcoach.adapter.PlayersRecyclerAdapter;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by jjupin on 11/15/16.
 */

public class MyTeamRecycleFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    public static final String TAG = "MyTeamRecycleFragment";
    private static final int CALL_LOG = 0;

    private SwipeRefreshLayout swipeContainer;
    public RecyclerView recyclerView;
    public MyTeamRecyclerAdapter adapter;

    private View emptyView;
    private Button grantAccessButton;

    private ProgressBar progressBar;

    private boolean isPickingPerson = false;

    private SimpleDateFormat timeFormatter;
    private SimpleDateFormat dateFormatter;

    private int lastSelectedRow = 0;
    private MainActivity mainActivity;

    private boolean updateCountsExpanded;
    private MyTeamHeaderItem myTeamHeader;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyTeamRecycleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeFormatter = new SimpleDateFormat("h:mm a");
        dateFormatter = new SimpleDateFormat("MMM d");

        mainActivity = (MainActivity) getActivity();

        updateCountsExpanded = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_recycle_view, container, false);

        return view;

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Context context = getContext();

        swipeContainer = (SwipeRefreshLayout)getView().findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        recyclerView = (RecyclerView) getView().findViewById(R.id.list);
        //recyclerView.addItemDecoration(new ConnectionDividerDecoration(context, 5));
        //recyclerView.setLayoutManager(new SmoothScrollLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(null);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "on resume");
        if (isPickingPerson) {
            isPickingPerson = false;
            //setProgressBarVisibility(View.GONE);
        }

        MainActivity.getBottomSheet().setVisibility(View.GONE);

        if (PlayerManager.getMyTeamPlayers() == null || PlayerManager.getMyTeamPlayers().size() == 0) {
            Random rn = new Random();

            List<PlayerInfo> players = new ArrayList<PlayerInfo>(); //Arrays.asList("John Doe", "Jane Doe", "Bill Bonner", "Charlie Brown", "Snoop Dogg", "Jose Cuervo", "Black Jack McGrath");
            PlayerInfo info1 = new PlayerInfo();
            info1.setFirstname("Mike");
            info1.setLastname("Trout");
            info1 = generatePlayerData(info1, rn);
            PlayerManager.addPlayerToMyTeam(info1);
            players.add(info1);

            info1 = new PlayerInfo();
            info1.setFirstname("Anthony");
            info1.setLastname("Rizzo");
            info1 = generatePlayerData(info1, rn);
            PlayerManager.addPlayerToMyTeam(info1);
            players.add(info1);

            info1 = new PlayerInfo();
            info1.setFirstname("Daniel");
            info1.setLastname("Murphy");
            info1 = generatePlayerData(info1, rn);
            PlayerManager.addPlayerToMyTeam(info1);
            players.add(info1);

            info1 = new PlayerInfo();
            info1.setFirstname("David");
            info1.setLastname("Wright");
            info1 = generatePlayerData(info1, rn);
            PlayerManager.addPlayerToMyTeam(info1);
            players.add(info1);

            info1 = new PlayerInfo();
            info1.setFirstname("Buster");
            info1.setLastname("Posey");
            info1 = generatePlayerData(info1, rn);
            PlayerManager.addPlayerToMyTeam(info1);
            players.add(info1);
        }

        createMyTeamRecycleAdapter(PlayerManager.getMyTeamPlayers());

//        if (lastSelectedRow > 0) {
//            int scrollPos = (lastSelectedRow > 3 ? (lastSelectedRow - 3) : 0);
//            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(scrollPos, 0);
//            // recyclerView.smoothScrollToPosition(lastSelectedRow);
//        }
    }

    private PlayerInfo generatePlayerData(PlayerInfo player, Random rn) {

        player.setHeight(rn.nextInt(10) + 1);
        player.setWeight(rn.nextInt(10) + 1);
        player.setHitting(rn.nextInt(10) + 1);
        player.setBat(rn.nextInt(10) + 1);
        player.setInfield(rn.nextInt(10) + 1);
        player.setOutfield(rn.nextInt(10) + 1);
        player.setThrowing(rn.nextInt(10) + 1);
        player.setArm(rn.nextInt(10) + 1);
        player.setSpeed(rn.nextInt(10) + 1);
        player.setBase(rn.nextInt(10) + 1);

        player.setDrafted(true);

        player.updateCompositeScore();

        return player;
    }

    // popup menu click listener

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        /**
        switch (menuItem.getItemId()) {
            case R.id.recent_item:
                createRecentCallAdapter(CallLogUtils.Sort.Recent);
                break;
            case R.id.outgoing_item:
                createRecentCallAdapter(CallLogUtils.Sort.Outgoing);
                break;
            case R.id.incoming_item:
                createRecentCallAdapter(CallLogUtils.Sort.Incoming);
                break;
            case R.id.missed_item:
                createRecentCallAdapter(CallLogUtils.Sort.Missed);
                break;
            case R.id.telemarketers_item:
                createRecentCallAdapter(CallLogUtils.Sort.Telemarketer);
                break;
        }
         **/

        return false;
    }

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private void createMyTeamRecycleAdapter(List<PlayerInfo> players) {

        Context context = getContext();

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PlayersRecyclerAdapter(PlayerManager.getMyTeamPlayers(), this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.invalidate();

        //setProgressBarVisibility(View.GONE);

        /**
        List<AbstractFlexibleItem> playersWithHeaders = new ArrayList<>();

        List<String> sortedByUpdatesCount = new ArrayList<String>();

        //
        // add sorting code here when ready for it...
        //


        if (players != null && players.size() > 0) {
            MyTeamHeaderItem yourContacts = new MyTeamHeaderItem("My Team");
            this.myTeamHeader = yourContacts;

            HashSet<String> firstLetterSet = new LinkedHashSet<>();

            for (int i = 0; i < players.size(); i++) {
                MyTeamItem abi = new MyTeamItem(context, players.get(i), " ",
                            yourContacts);
                    playersWithHeaders.add(abi);
            }
        }

        if (adapter == null) {
            ViewGroup stickyHeaderView = (ViewGroup) getView().findViewById(R.id.sticky_header_container);
            adapter = new MyTeamRecycleAdapter(playersWithHeaders, this, stickyHeaderView);
            adapter.disableStickyHeaders();

            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged(); // to prevent list from scrolling down
            adapter.setDisplayHeadersAtStartUp(true);
            adapter.enableStickyHeaders();
        } else {
            adapter.updateDataSet(playersWithHeaders);
        }

         **/
    }

    //
    // swipe refresh listener
    //


        void refreshItems() {
        // Load items
        // ...

        // Load complete
        onItemsLoadComplete();
        }

        void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
         swipeContainer.setRefreshing(false);
        }

}
