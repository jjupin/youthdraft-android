package com.youthdraft.youthdraftcoach.activity.players;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.activity.MainActivity;
import com.youthdraft.youthdraftcoach.adapter.PlayersRecyclerAdapter;
import com.youthdraft.youthdraftcoach.adapter.ViewPagerAdapter;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;
import com.youthdraft.youthdraftcoach.datamodel.PlayerScores;
import com.youthdraft.youthdraftcoach.interfaces.DataUtilsListener;
import com.youthdraft.youthdraftcoach.utility.PreferencesUtils;
import com.youthdraft.youthdraftcoach.views.NonSwipeableViewPager;
import com.youthdraft.youthdraftcoach.views.OpenSansTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by jjupin on 12/13/16.
 */

public class DraftedPlayersFragment extends Fragment  implements
        android.support.v7.widget.SearchView.OnQueryTextListener,
        DataUtilsListener {

    public static String TAG = "DraftedPlayersFragment";

    private FloatingActionButton floatingActionButton;

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OpenSansTextView filterLabel;

    private boolean doReschedulePlayers = false;
    private boolean assessedPlayers = false;
    private boolean nonAssessedPlayersOnly = false;  // need to get rid of all of these damn booleans
    private boolean draftedPlayersOnly = false;

    private boolean isPickingPerson = false;


    public DraftedPlayersFragment(){}

    public void setFragmentToReschedule(boolean useReschedulePlayesOnly) {
        this.doReschedulePlayers = useReschedulePlayesOnly;
    }

    public void setFragmentToAssessed(boolean useAssessedPlayersOnly) {
        this.assessedPlayers = useAssessedPlayersOnly;
    }

    public void setFragmentToNonAssessedOnly(boolean useNonAssessedOnly) {
        this.nonAssessedPlayersOnly = useNonAssessedOnly;
    }

    public void setFragmentToDraftedPlayers(boolean useDraftedOnly) {
        this.draftedPlayersOnly = useDraftedOnly;
    }

    public boolean isAssessedPlayersOnly() {
        return this.assessedPlayers;
    }
    public boolean isNonAssessedPlayersOnly() {
        return this.nonAssessedPlayersOnly;
    }
    public boolean isDraftedPlayersOnly() {
        return this.draftedPlayersOnly;
    }
    public boolean isRescheduledPlayersOnly() {
        return this.doReschedulePlayers;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_recycle_view, container, false);

        // loadUi(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Log.d(TAG, "onCreate NULL");
        } else {
            Log.d(TAG, "onCreate not NULL");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        Firebase.setAndroidContext(getContext());
        loadUi(getView());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(TAG, "onSaveInstanceState");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        //ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        //viewPager.setCurrentItem(lastSelectedTab);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated");
    }



    public void loadUi(View view) {

        final Context context = getContext();

        setFragmentToReschedule(true);

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

        filterLabel = (OpenSansTextView)getView().findViewById(R.id.filtered_by_label);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PlayersRecyclerAdapter(PlayerManager.getMyTeamPlayers(), this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.invalidate();

        //floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        //floatingActionButton.setVisibility(View.GONE);

        //
        // Now, do the other things necessary to layout the view...
        //

        setMenuVisibility(true);

        if (isPickingPerson) {
            isPickingPerson = false;
        }

        if (PlayerManager.getPlayersMap().entrySet().size() > 0) {
            loadPlayersToUse();
            return;
        }

    }

    private String getActionBarTitle() {
        return null;
    }

    //
    // methods to reconfigure which players are shown and methods to manipulate the options menu items...
    //

    private void createPlayersRecycleAdapter(List<PlayerInfo> players, Fragment fragment) {

        Context context = getContext();

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PlayersRecyclerAdapter(players, fragment);

        recyclerView.setAdapter(mAdapter);
        recyclerView.invalidate();

        //setProgressBarVisibility(View.GONE);
    }

    private void loadPlayersToUse() {

        List<PlayerInfo> playersToUse = determinePlayersToUse();

        Collections.sort(playersToUse);
        createPlayersRecycleAdapter(playersToUse, this);
    }

    private List<PlayerInfo> determinePlayersToUse() {
        List<PlayerInfo> playersToUse = null;

        if (this.doReschedulePlayers) {
            playersToUse = PlayerManager.getPlayersToReschedule();
        } else if (this.assessedPlayers) {
            playersToUse = PlayerManager.getAssessedPlayers(null);
        } else if (this.nonAssessedPlayersOnly) {
            playersToUse = PlayerManager.getPlayersExceptForRescheduledAndAssessed();
        } else if (this.draftedPlayersOnly) {
            playersToUse = PlayerManager.getDraftedPlayers();
        } else {
            playersToUse = PlayerManager.getPlayersExceptForRescheduled();
        }

        return playersToUse;
    }

    private MenuItem searchMenuItem;
    private SearchView searchView;


    //
    // SearchView onQuerySubmit listener methods...
    //

    @Override
    public boolean onQueryTextSubmit(String query) {
        List<PlayerInfo> players = determinePlayersToUse();
        Collections.sort(players);
        createPlayersRecycleAdapterForSearchString(query, players);
        //MenuItemCompat.collapseActionView(searchMenuItem);
        searchMenuItem.collapseActionView();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<PlayerInfo> players = determinePlayersToUse();
        Collections.sort(players);
        createPlayersRecycleAdapterForSearchString(newText, players);
        return true;
    }

    private void createPlayersRecycleAdapterForSearchString(String searchStr, List<PlayerInfo> players) {
        List<PlayerInfo> matchedPlayers = new ArrayList<PlayerInfo>();
        for (PlayerInfo p : players) {
            if (p.getFullname().toLowerCase().indexOf(searchStr) > -1) {
                matchedPlayers.add(p);
            }
        }

        PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.ALPHABETICAL_LAST);
        createPlayersRecycleAdapter(matchedPlayers, this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_players_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();

        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(this);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    searchMenuItem.collapseActionView();
                    return false;
                }
            });
            searchView.setSubmitButtonEnabled(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
//            case R.id.player_alphabetical_first:
//                Log.e(TAG, "Sorted Alphabetical (First Name)");
//                PlayerInfo.setComparatorField(PlayerInfo.ComparatorField.ALPHABETICAL_FIRST);
//                filterLabel.setText("Filter: Alphabetical by First Name");
//                break;
//
//            case R.id.player_alphabetical_last:
//                Log.e(TAG, "Sorted Alphabetical (Last Name)");
//                PlayerInfo.setComparatorField(PlayerInfo.ComparatorField.ALPHABETICAL_LAST);
//                filterLabel.setText("Filter: Alphabetical by Last Name");
//                break;
//
//            case R.id.player_height:
//                Log.e(TAG, "Player Height");
//                PlayerInfo.setComparatorField(PlayerInfo.ComparatorField.HEIGHT);
//                filterLabel.setText("Filter: Player Height");
//                break;
//
//            case R.id.player_weight:
//                Log.e(TAG, "Select player weight");
//                PlayerInfo.setComparatorField(PlayerInfo.ComparatorField.WEIGHT);
//                filterLabel.setText("Filter: Player Weight");
//                break;

            case R.id.player_hitting_mechanics:
                Log.e(TAG, "Selected hitting mechanics...");
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.HITTING_MECHANICS);
                filterLabel.setText("Filter: Player Hitting Mechanics");
                break;

            case R.id.player_bat_speed:
                Log.e(TAG, "Selected bat speed...");
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.BAT_SPEED);
                filterLabel.setText("Filter: Player Bat Speed");
                break;

            case R.id.player_infield_mechanics:
                Log.e(TAG, "Selected infield mechanics...");
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.INFIELD_MECHANICS);
                filterLabel.setText("Filter: Player Infield Mechanics");
                break;

            case R.id.player_outfield_mechanics:
                Log.e(TAG, "Selected outfield mechanics...");
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.OUTFIELD_MECHANICS);
                filterLabel.setText("Filter: Player Outfield Mechanics");
                break;

            case R.id.player_throwing_mechanics:
                Log.e(TAG, "Selected throwing mechanics...");
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.THROWING_MECHANICS);
                filterLabel.setText("Filter: Player Throwing Mechanics");
                break;

            case R.id.player_fielding:
                Log.e(TAG, "Selected fielding...");
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.FIELDING);
                filterLabel.setText("Filter: Player Fielding");
                break;

            case R.id.player_speed:
                Log.e(TAG, "Selected speed...");
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.SPEED);
                filterLabel.setText("Filter: Player Speed");
                break;

            case R.id.player_base_running:
                Log.e(TAG, "Selected base running...");
                PlayerInfo.setSortComparatorField(PlayerInfo.ComparatorField.BASE_RUNNING);
                filterLabel.setText("Filter: Player Base Running");
                break;
//            case R.id.player_advanced:
//                Log.e(TAG, "Selected advanced...");
//                PlayerInfo.setComparatorField(PlayerInfo.ComparatorField.ADVANCED);
//                filterLabel.setText("Filter: Alphabetical by Last Name");
//                break;
//
//            case R.id.player_ranking:
//                PlayerInfo.setComparatorField(PlayerInfo.ComparatorField.RANKING);
//                filterLabel.setText("Filter: Player Ranking");
//                break;

            default:
                filterLabel.setText("Sorted Alphabetically (Last Name)");

        }

        List<PlayerInfo> playersToUse = determinePlayersToUse();

        if (PlayerInfo.getSortComparatorField() == PlayerInfo.ComparatorField.ADVANCED) {
            showAdvancedOptionsDialog();
        }

        if (PlayerInfo.getSortComparatorField() == PlayerInfo.ComparatorField.ALPHABETICAL_FIRST ||
                PlayerInfo.getSortComparatorField() == PlayerInfo.ComparatorField.ALPHABETICAL_LAST) {
            Collections.sort(playersToUse);
        } else {
            Collections.sort(playersToUse);  // first, resort the players alphabetically
            Collections.sort(playersToUse, PlayerInfo.PlayerSpeedComparator);
        }

        createPlayersRecycleAdapter(playersToUse, this);

        return false;
    }

    private void showAdvancedOptionsDialog() {

        List<String> ss = new ArrayList<String>();
        ss.add(" ");
        String[] s = PlayerInfo.getFieldTitles();
        for (String st : s) {
            ss.add(st);
        }

        s = (String[])ss.toArray(s);

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, s);

        final Spinner sp1 = new Spinner(getActivity());
        sp1.setPadding(10,10,10,10);
        sp1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        sp1.setAdapter(adp);

        final Spinner sp2 = new Spinner(getActivity());
        sp2.setPadding(15,15,15,15);
        sp2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        sp2.setAdapter(adp);

        final Spinner sp3 = new Spinner(getActivity());
        sp3.setPadding(20,20,20,20);
        sp3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        sp3.setAdapter(adp);

        //Creating LinearLayout.
        LinearLayout linearlayout = new LinearLayout(getActivity());

        //Setting up LinearLayout Orientation
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams linearlayoutlayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        final TextView rowTextView1 = new TextView(getActivity());
        rowTextView1.setPadding(4,4,4,4);
        rowTextView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        // set some properties of TextView
        rowTextView1.setText("\n\nFirst Filter Selection");
        rowTextView1.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        rowTextView1.setTextSize(16);

        final TextView rowTextView2 = new TextView(getActivity());
        rowTextView2.setPadding(4,4,4,4);
        rowTextView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        // set some properties of TextView
        rowTextView2.setText("\n\nSecond Filter Selection");
        rowTextView2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        rowTextView2.setTextSize(16);

        final TextView rowTextView3 = new TextView(getActivity());
        rowTextView3.setPadding(4,4,4,4);
        rowTextView3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        // set some properties of TextView
        rowTextView3.setText("\n\nThird Filter Selection");
        rowTextView3.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        rowTextView3.setTextSize(16);

        linearlayout.addView(rowTextView1);
        linearlayout.addView(sp1);
        linearlayout.addView(rowTextView2);
        linearlayout.addView(sp2);
        linearlayout.addView(rowTextView3);
        linearlayout.addView(sp3);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setView(linearlayout);

        // Setting Dialog Title
        alertDialog.setTitle("Advanced Filtering Options");

        // Setting Dialog Message
        alertDialog.setMessage("Please select the assessment values to filter by:");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_menu_manage);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed YES button. Write Logic Here
                Toast.makeText(getActivity().getApplicationContext(), "Coming soon!",
                        Toast.LENGTH_SHORT).show();

                /**
                 List<PlayerInfo> playersToUse = determinePlayersToUse();
                 PlayerInfo.setComparatorField(PlayerInfo.ComparatorField.RANKING);
                 Collections.sort(playersToUse);  // first, resort the players alphabetically
                 Collections.sort(playersToUse, PlayerInfo.PlayerSpeedComparator);
                 createPlayersRecycleAdapter(playersToUse, PlayersRecycleFragment.this);
                 **/
            }
        });

//        // Setting Negative "NO" Button
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                // User pressed No button. Write Logic Here
//                Toast.makeText(getActivity().getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Setting Netural "Cancel" Button
        alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed Cancel button. Write Logic Here
                Toast.makeText(getActivity().getApplicationContext(), "You clicked on Cancel",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

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


    @Override
    public void cleanUpPlayersAndScoresAfterDownloadCompletes() {
        if (getParentFragment() != null) {
            Toast.makeText(getParentFragment().getContext(), "Finished retrieval all players....", Toast.LENGTH_SHORT).show();
        }

        List<String> list = new ArrayList<String>(PlayerManager.getTimeslotToSecondsMap().keySet());
        Collections.sort(list);
        MainActivity.addTimeslotsToBottomSheet(list);

        loadPlayersToUse();

        /**
         Collections.sort(doReschedulePlayers ? PlayerManager.getPlayersToReschedule() : PlayerManager.getPlayersExceptForRescheduled());
         createPlayersRecycleAdapter(doReschedulePlayers ? PlayerManager.getPlayersToReschedule() : PlayerManager.getPlayersExceptForRescheduled(), PlayersRecycleFragment.this);
         **/
    }

//    private View createCustomTab(int drawableId, String title) {
//        View view = getActivity().getLayoutInflater().inflate(R.layout.tab_with_importing, null);
//
//        ImageView iconView = (ImageView) view.findViewById(R.id.icon);
//        iconView.setImageResource(drawableId);
//        iconView.setVisibility(View.GONE);
//
//        OpenSansTextView tabText = (OpenSansTextView)view.findViewById(R.id.badge);
//        tabText.setText(title);
//        tabText.setTextSize(14);
//
//        return view;
//    }

}
