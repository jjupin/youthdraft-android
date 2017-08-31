package com.youthdraft.youthdraftcoach.activity.players;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.activity.MainActivity;
import com.youthdraft.youthdraftcoach.activity.assess.AssessPlayerActivity;
import com.youthdraft.youthdraftcoach.adapter.PlayersRecyclerAdapter;
import com.youthdraft.youthdraftcoach.adapter.RVAdapter;
import com.youthdraft.youthdraftcoach.adapter.ViewPagerAdapter;
import com.youthdraft.youthdraftcoach.datamodel.PlayerDBHelper;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;
import com.youthdraft.youthdraftcoach.datamodel.PlayerScores;
import com.youthdraft.youthdraftcoach.datamodel.RankDBHelper;
import com.youthdraft.youthdraftcoach.interfaces.DataUtilsListener;
import com.youthdraft.youthdraftcoach.interfaces.OnCalendarItemClickListener;
import com.youthdraft.youthdraftcoach.utility.CalendarViewUtils;
import com.youthdraft.youthdraftcoach.utility.DataUtils;
import com.youthdraft.youthdraftcoach.utility.MenuUtils;
import com.youthdraft.youthdraftcoach.utility.PreferencesUtils;
import com.youthdraft.youthdraftcoach.views.NonSwipeableViewPager;
import com.youthdraft.youthdraftcoach.views.OpenSansTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.RunnableFuture;

/**
 * Created by jjupin on 11/15/16.
 */

public class PlayersFragment extends Fragment implements
        android.support.v7.widget.SearchView.OnQueryTextListener,
        OnCalendarItemClickListener, View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        PopupMenu.OnMenuItemClickListener,
        DataUtilsListener {

    public static String TAG = "PlayersFragment";

    private FloatingActionButton floatingActionButton;

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OpenSansTextView filterLabel;
    private OpenSansTextView draftableLabel;

    private boolean doReschedulePlayers = false;
    private boolean assessedPlayers = false;
    private boolean nonAssessedPlayersOnly = false;  // need to get rid of all of these damn booleans
    private boolean draftedPlayersOnly = false;

    private boolean isPickingPerson = false;

    private OpenSansTextView assessmentSelectionView;
    private OpenSansTextView sortPlayersView;

    private LinearLayout timeslotLayout;
    private View calendarDrawer;
    private View allPlayersSlot;

    public PlayersFragment() {
    }

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

        if (PlayerManager.getAllPlayers().size() <= 0) {
            DataUtils.loadDataFromFirebase(getActivity());
        }

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

        DataUtils.addDataUtilsListener(this, getActivity());

        this.setHasOptionsMenu(true);
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
        setMenuVisibility(false);
    }

    private boolean calendarLoaded = false;
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        Firebase.setAndroidContext(getContext());
        setMenuVisibility(true);

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

        setFragmentToNonAssessedOnly(true);

        swipeContainer = (SwipeRefreshLayout) getView().findViewById(R.id.swipeContainer);
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

        filterLabel = (OpenSansTextView) getView().findViewById(R.id.filtered_by_label);

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
        // The new popupmenu for the assessment selections
        //

        assessmentSelectionView = (OpenSansTextView) getView().findViewById(R.id.popupmenu_selection);
        assessmentSelectionView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        assessmentSelectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayersFragment.isSortedMenu = false;
                // create a new popup menu so it doesn't appear at the top of the screen when bindViewHolder gets called when scrolling
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.setOnMenuItemClickListener(PlayersFragment.this);
                popupMenu.inflate(R.menu.assessment_menu_short);
                popupMenu.show();
                    /*
                        this is to show the popup menu over the anchor view
                        http://stackoverflow.com/a/29702608/3622171
                     */
//                    if (popupMenu.getDragToOpenListener() instanceof android.support.v7.widget.ListPopupWindow.ForwardingListener) {
//                        android.support.v7.widget.ListPopupWindow.ForwardingListener listener = (android.support.v7.widget.ListPopupWindow.ForwardingListener) popupMenu.getDragToOpenListener();
//                        listener.getPopup().setVerticalOffset(-view.getHeight());
//                        listener.getPopup().show();
//                    }
            }
        });

        //
        // The new popupmenu for the sorting options
        //

        sortPlayersView = (OpenSansTextView) getView().findViewById(R.id.filtered_by_label);
        sortPlayersView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.dropdown_arrow, 0);
        sortPlayersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayersFragment.isSortedMenu = true;
                // create a new popup menu so it doesn't appear at the top of the screen when bindViewHolder gets called when scrolling
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.setOnMenuItemClickListener(PlayersFragment.this);
                popupMenu.inflate(R.menu.sort_players_menu);
                popupMenu.show();
                    /*
                        this is to show the popup menu over the anchor view
                        http://stackoverflow.com/a/29702608/3622171
                     */
//                    if (popupMenu.getDragToOpenListener() instanceof android.support.v7.widget.ListPopupWindow.ForwardingListener) {
//                        android.support.v7.widget.ListPopupWindow.ForwardingListener listener = (android.support.v7.widget.ListPopupWindow.ForwardingListener) popupMenu.getDragToOpenListener();
//                        listener.getPopup().setVerticalOffset(-view.getHeight());
//                        listener.getPopup().show();
//                    }
            }
        });

        //
        // Need to set the initial assessment slider setting...
        //

        handleAssessmentSelection(R.id.player_hitting_mechanics);  // need to adjust this if the initial selection is ever changed!!!

        //
        // Start setting up the calendar selection upper drawer...
        //

            timeslotLayout = (LinearLayout) getView().findViewById(R.id.timeslotLayout);
            calendarDrawer = getView().findViewById(R.id.calendar_drawer);
            allPlayersSlot = getView().findViewById(R.id.all_players);
            if (allPlayersSlot != null) {
                allPlayersSlot.setOnClickListener(this);  // needs to respond to taps...
            }

        TextView cancelBtn = (TextView)getView().findViewById(R.id.cancel_selection);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarDrawer != null) {
                    calendarDrawer.setVisibility(View.GONE);
                }
            }
        });

        calendarLoaded = false;

        //filterPlayersSpinner = (AppCompatSpinner)getView().findViewById(R.id.seekbar_select);
        //filterPlayersSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //filterPlayersSpinner.setOnItemSelectedListener(this);

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


        /**
        Firebase.setAndroidContext(getContext());
        accountuid = PreferencesUtils.getUid(getContext());
        leagueid = PreferencesUtils.getLeague(getContext());
        sport = PreferencesUtils.getPropertyCoachSport(getContext());
        season = PreferencesUtils.getPropertyCoachSeason(getContext());
        coachYear = PreferencesUtils.getPropertyCoachYear(getContext());

        // just a comment to force a recompile
        new GetScores().execute();
         **/

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

        //Parcelable state = recyclerView.getLayoutManager().onSaveInstanceState();

       // listView.setAdapter(adapter);

// Restore previous state (including selected item index and scroll position)
        //listView.onRestoreInstanceState(state);

        recyclerView.setAdapter(mAdapter);
        recyclerView.getLayoutManager().onRestoreInstanceState(state);
        //recyclerView.invalidate();

        //setProgressBarVisibility(View.GONE);
    }

    private void loadPlayersToUse() {

        List<PlayerInfo> playersToUse = determinePlayersToUse();

        Collections.sort(playersToUse);
        createPlayersRecycleAdapter(playersToUse, this);
    }

    private List<PlayerInfo> playersToUse = null;

    private List<PlayerInfo> determinePlayersToUse() {
        if (PlayerInfo.getCurrentTimeslotSort() != null) {
            playersToUse = PlayerManager.getPlayersForTimeslot(PlayerInfo.getCurrentTimeslotSort());
        } else {

            // if (playersToUse == null || playersToUse.size() == 0) {
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
        }
       // }
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

    @Override
    public void onCalendarItemClick(String entryDateTime) {

        Log.e(TAG, "got a time change: " + entryDateTime);

        List<PlayerInfo> timeslotPlayers = PlayerManager.getPlayersForTimeslot(entryDateTime);
        if (timeslotPlayers == null) {
            Toast.makeText(getContext(), "No players found for that assessment time slot.", Toast.LENGTH_SHORT).show();
        } else {
            if (PlayerManager.currentTimeslot.toLowerCase().indexOf("all") > -1) {
                timeslotPlayers = determinePlayersToUse();
                Collections.sort(timeslotPlayers);
            }
            createPlayersRecycleAdapter(timeslotPlayers, this);
            if (PlayerManager.currentTimeslot.toLowerCase().indexOf("all") > -1) {
                filterLabel.setText("Sorted Alphabetically (Last Name)");
            } else {
                String timeslotStr = PlayerManager.currentTimeslot;
                timeslotStr = timeslotStr.replace("\n", "");
                filterLabel.setText("Sorted Alphabetically - for timeslot: " + timeslotStr);
            }
        }

    }

    @Override
    public void showCalendar() {
        if (calendarDrawer.getVisibility() == View.INVISIBLE || calendarDrawer.getVisibility() == View.GONE) {
        //    if (calendarLoaded == false) {
                List<String> list = new ArrayList<String>(PlayerManager.getTimeslotToSecondsMap().keySet());
                Collections.sort(list);
                CalendarViewUtils.removeAllTimeslots(this.timeslotLayout);
                CalendarViewUtils.addTimeslotsToLayout(list, this.timeslotLayout, this, getContext());
                calendarLoaded = true;
        //    }
            calendarDrawer.setVisibility(View.VISIBLE);  // need animation here to show it smoothly sliding down -  TO COME
        } else {
            calendarDrawer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {  // OnClickListener interface method implementation

        String timeslot = "All Players";
        if (v.getId() == R.id.all_players) {
            timeslot = "All Players";
        } else {
            int id = v.getId() - 1000;  // added an offset so as to not conflict with other generated RES IDs
            List<String> list = new ArrayList<String>(PlayerManager.getTimeslotToSecondsMap().keySet());
            Collections.sort(list);  // might want to store these to save constantly sorting them...
            timeslot = PlayerManager.getTimeslotToSecondsMap().get(list.get(id));
            PlayerInfo.setCurrentTimeslotSort(timeslot);
        }

        this.onCalendarItemClick(timeslot);
        calendarDrawer.setVisibility(View.GONE);
    }

    //
    // popup menu adapter interface methods
    //

    public static boolean isSortedMenu = false;
    private Parcelable state;
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        int selection = 0; //R.id.player_alphabetical_last;

        if (isSortedMenu) {
            handleFilterSelection(menuItem.getItemId());

        } else {
            switch (menuItem.getItemId()) {
                case R.id.player_hitting_mechanics:
                case R.id.player_bat_speed:
                case R.id.player_infield_mechanics:
                case R.id.player_outfield_mechanics:
                case R.id.player_throwing_mechanics:
                case R.id.player_fielding:
                case R.id.player_speed:
                case R.id.player_base_running:
                    selection = menuItem.getItemId();
                    break;
                case R.id.player_custom: //Custom
                    return false;   // need to do something totally different here...
                default:
                    selection = R.id.player_hitting_mechanics;
            }

            state = recyclerView.getLayoutManager().onSaveInstanceState();
            handleAssessmentSelection(selection);
        }

        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        int selection = 0; //R.id.player_alphabetical_last;

        switch (pos) {
            case 0: //hitting mechanics
                selection = R.id.player_hitting_mechanics;
                break;
            case 1: //Bat Speed
                selection = R.id.player_bat_speed;
                break;
            case 2: //Infield Mechanics
                selection = R.id.player_infield_mechanics;
                break;
            case 3: //Outfield Mechanics
                selection = R.id.player_outfield_mechanics;
                break;
            case 4: //Throwing Mechanics
                selection = R.id.player_throwing_mechanics;
                break;
            case 5: //Fielding
                selection = R.id.player_fielding;
                break;
            case 6: //Speed
                selection = R.id.player_speed;
                break;
            case 7: //Base Running
                selection = R.id.player_base_running;
                break;
            case 8: //Custom
                return;   // need to do something totally different here...
            default:
                selection = R.id.player_hitting_mechanics;
        }

        handleFilterSelection(selection);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
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
        //this.handleFilterSelection(menuItem.getItemId());

        switch (menuItem.getItemId()) {
            case R.id.calendar:
                showCalendar();
                break;

            default:
                Log.e(TAG, "Unkonwn menuItem passed to Options Menu selection: " + menuItem.toString());

        }

        /**
        if (MainActivity.getBottomSheet().getVisibility() == View.VISIBLE) {
            MainActivity.getBottomSheet().setVisibility(View.INVISIBLE);
            MainActivity.getBottomSheetBehavior().setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            MainActivity.getBottomSheet().setVisibility(View.VISIBLE);
            MainActivity.getBottomSheetBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        }
         **/

        return false;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

    }


    private void handleFilterSelection(int itemId) {
        PlayerInfo.setCurrentTimeslotSort(null);
        PlayerInfo.updateSortComparatorFieldBasedOnMenuSelection(itemId);
        String sorting = MenuUtils.returnStringForPlayerSorting(itemId);

        Log.e(TAG, "Sorted " + sorting);
        filterLabel.setText(sorting);

        updateListForFilter();

    }

    private void handleAssessmentSelection(int itemId) {
        PlayerInfo.updateAssessComparatorFieldBasedOnMenuSelection(itemId);
        String assessing = MenuUtils.returnStringForPlayerAssessmentSelection(itemId);

        Log.e(TAG, "Assessing " + assessing);
        assessmentSelectionView.setText(assessing);
        if (assessing.toLowerCase().indexOf("alpha") > -1) {
            assessmentSelectionView.setText("Hitting Mechanics");
        }

        updateListForAssessmentSelection();
    }

    private void updateListForFilter() {

        playersToUse = determinePlayersToUse();

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
    }

    private void updateListForAssessmentSelection() {
        //List<PlayerInfo> playersToUse = determinePlayersToUse();
        //Collections.sort(playersToUse);
        //Collections.sort(playersToUse, PlayerInfo.PlayerSpeedComparator);
        playersToUse = determinePlayersToUse();
        createPlayersRecycleAdapter(playersToUse, this);
    }

    private void showAdvancedOptionsDialog() {

        List<String> ss = new ArrayList<String>();
        ss.add(" ");
        String[] s = PlayerInfo.getFieldTitles();
        for (String st : s) {
            ss.add(st);
        }

        s = (String[]) ss.toArray(s);

        final ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, s);

        final Spinner sp1 = new Spinner(getActivity());
        sp1.setPadding(10, 10, 10, 10);
        sp1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        sp1.setAdapter(adp);

        final Spinner sp2 = new Spinner(getActivity());
        sp2.setPadding(15, 15, 15, 15);
        sp2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        sp2.setAdapter(adp);

        final Spinner sp3 = new Spinner(getActivity());
        sp3.setPadding(20, 20, 20, 20);
        sp3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        sp3.setAdapter(adp);

        //Creating LinearLayout.
        LinearLayout linearlayout = new LinearLayout(getActivity());

        //Setting up LinearLayout Orientation
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams linearlayoutlayoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        final TextView rowTextView1 = new TextView(getActivity());
        rowTextView1.setPadding(4, 4, 4, 4);
        rowTextView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        // set some properties of TextView
        rowTextView1.setText("\n\nFirst Filter Selection");
        rowTextView1.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        rowTextView1.setTextSize(16);

        final TextView rowTextView2 = new TextView(getActivity());
        rowTextView2.setPadding(4, 4, 4, 4);
        rowTextView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        // set some properties of TextView
        rowTextView2.setText("\n\nSecond Filter Selection");
        rowTextView2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        rowTextView2.setTextSize(16);

        final TextView rowTextView3 = new TextView(getActivity());
        rowTextView3.setPadding(4, 4, 4, 4);
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
        //Toast.makeText(getContext(), "Finished retrieval all players....", Toast.LENGTH_SHORT).show();
        //MainActivity.addTimeslotsToBottomSheet(list);

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

    //
    // Particular methods dealing with selecting the calendar for filtering the players showing in the players list...
    //

}

/**
 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 * View view = inflater.inflate(R.layout.players_tab_view, container, false);
 * <p>
 * floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
 * floatingActionButton.setOnClickListener(this);
 * <p>
 * // loadUi(view);
 * <p>
 * return view;
 * }
 * @Override public void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState);
 * if (savedInstanceState == null) {
 * Log.d(LOG_TAG, "onCreate NULL");
 * } else {
 * Log.d(LOG_TAG, "onCreate not NULL");
 * }
 * }
 * @Override public void onDestroy() {
 * super.onDestroy();
 * }
 * @Override public void onDestroyView() {
 * super.onDestroyView();
 * Log.d(LOG_TAG, "onDestroyView");
 * }
 * @Override public void onPause() {
 * super.onPause();
 * Log.d(LOG_TAG, "onPause");
 * }
 * @Override public void onResume() {
 * super.onResume();
 * Log.d(LOG_TAG, "onResume");
 * Firebase.setAndroidContext(getContext());
 * loadUi(getView());
 * <p>
 * }
 * @Override public void onSaveInstanceState(Bundle outState) {
 * super.onSaveInstanceState(outState);
 * <p>
 * Log.d(LOG_TAG, "onSaveInstanceState");
 * <p>
 * }
 * @Override public void onStart() {
 * super.onStart();
 * Log.d(LOG_TAG, "onStart");
 * <p>
 * ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
 * viewPager.setCurrentItem(lastSelectedTab);
 * }
 * @Override public void onStop() {
 * super.onStop();
 * Log.d(LOG_TAG, "onStop");
 * }
 * @Override public void onDetach() {
 * super.onDetach();
 * Log.d(LOG_TAG, "onDetach");
 * }
 * @Override public void onActivityCreated(Bundle savedInstanceState) {
 * super.onActivityCreated(savedInstanceState);
 * Log.d(LOG_TAG, "onActivityCreated");
 * }
 * @Override public void onAttach(Context context) {
 * super.onAttach(context);
 * Log.d(LOG_TAG, "onAttach");
 * }
 * @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
 * super.onViewCreated(view, savedInstanceState);
 * Log.d(LOG_TAG, "onViewCreated");
 * }
 * @Override public void onClick(View v) {
 * switch (v.getId()) {
 * case R.id.fab:
 * <p>
 * try {
 * //createPdfPrint();
 * MainActivity.getBottomSheet().setVisibility(View.VISIBLE);
 * MainActivity.getBottomSheetBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
 * <p>
 * } finally {
 * //viewPdf();
 * }
 * <p>
 * <p>
 * break;
 * //            case R.id.fabRecalc:
 * //                recalcRanks.recalcPlayers(context.getApplicationContext());
 * //                displayRank();
 * //                break;
 * }
 * }
 * <p>
 * <p>
 * <p>
 * public void loadUi(View view) {
 * <p>
 * viewPager = (NonSwipeableViewPager) view.findViewById(R.id.viewpager);
 * viewPager.setIsSwipeable(false);
 * viewPager.setOffscreenPageLimit(3);
 * <p>
 * if (playersRecycleFrag == null) {
 * playersRecycleFrag = new PlayersRecycleFragment();
 * playersRecycleFrag.setFragmentToNonAssessedOnly(true);
 * //  myTeamRecycleFrag = new MyTeamRecycleFragment();
 * }
 * <p>
 * ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
 * //adapter.addFragment(myTeamRecycleFrag, "My Team");
 * adapter.addFragment(playersRecycleFrag, "Players");
 * viewPager.setAdapter(adapter);
 * <p>
 * tabLayout = (TabLayout) view.findViewById(R.id.tabs);
 * if (tabLayout.getTabCount() <= 0) {
 * //tabLayout.addTab(tabLayout.newTab().setCustomView(createCustomTab(R.drawable.football, "My Team")));
 * tabLayout.addTab(tabLayout.newTab().setCustomView(createCustomTab(R.drawable.football, getActivity().getResources().getString(R.string.players))));
 * }
 * <p>
 * viewPager.setCurrentItem(1, true);  // move to the players tab...
 * TabLayout.Tab tab = tabLayout.getTabAt(0);
 * if (tabLayout.getTabCount() > 0) {
 * tab = tabLayout.getTabAt(tabLayout.getTabCount() - 1);  // always select the last one...
 * }
 * tab.select();
 * <p>
 * if (getActivity() instanceof AssessPlayerActivity) {
 * floatingActionButton.setVisibility(View.GONE);
 * } else {
 * floatingActionButton.setVisibility(View.VISIBLE);
 * }
 * <p>
 * <p>
 * final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
 * <p>
 * tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
 * @Override public void onTabSelected(TabLayout.Tab tab) {
 * //TextView badgeCount = (TextView) tab.getCustomView().findViewById(R.id.badge);
 * //badgeCount.setSelected(true);
 * viewPager.setCurrentItem(tab.getPosition(), true);
 * lastSelectedTab = tabLayout.getSelectedTabPosition();
 * <p>
 * floatingActionButton.setVisibility(tabLayout.getSelectedTabPosition() != 0 ? View.GONE : View.VISIBLE);
 * <p>
 * // hide keyboard
 * // imm.hideSoftInputFromWindow(tab.getCustomView().getWindowToken(), 0);
 * }
 * @Override public void onTabUnselected(TabLayout.Tab tab) {
 * //TextView badgeCount = (TextView) tab.getCustomView().findViewById(R.id.badge);
 * //badgeCount.setSelected(false);
 * }
 * @Override public void onTabReselected(TabLayout.Tab tab) {
 * <p>
 * }
 * });
 * <p>
 * }
 * <p>
 * private View createCustomTab(int drawableId, String title) {
 * View view = getActivity().getLayoutInflater().inflate(R.layout.tab_with_importing, null);
 * <p>
 * ImageView iconView = (ImageView) view.findViewById(R.id.icon);
 * iconView.setImageResource(drawableId);
 * iconView.setVisibility(View.GONE);
 * <p>
 * OpenSansTextView tabText = (OpenSansTextView)view.findViewById(R.id.badge);
 * tabText.setText(title);
 * tabText.setTextSize(14);
 * <p>
 * return view;
 * }
 * }
 **/

