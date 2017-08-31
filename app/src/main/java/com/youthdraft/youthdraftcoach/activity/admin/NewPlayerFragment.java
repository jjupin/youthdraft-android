package com.youthdraft.youthdraftcoach.activity.admin;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.youthdraft.youthdraftcoach.activity.players.RescheduleFragment;
import com.youthdraft.youthdraftcoach.adapter.PlayersRecyclerAdapter;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;
import com.youthdraft.youthdraftcoach.datamodel.PlayerScores;
import com.youthdraft.youthdraftcoach.interfaces.OnCalendarItemClickListener;
import com.youthdraft.youthdraftcoach.utility.CalendarViewUtils;
import com.youthdraft.youthdraftcoach.utility.DataUtils;
import com.youthdraft.youthdraftcoach.utility.PreferencesUtils;
import com.youthdraft.youthdraftcoach.views.OpenSansTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jjupin on 1/10/17.
 */

public class NewPlayerFragment extends Fragment implements OnCalendarItemClickListener, View.OnClickListener, TextView.OnEditorActionListener {

    public static String TAG = "NewPlayerFragment";

    private LinearLayout timeslotLayout;
    private View calendarDrawer;
    private boolean calendarLoaded = false;

    private Button selectDateButton;

    private TextView scheduleTimeText;
    private String selectedTimeSlot = null;
    private String timeslotDatePart = null;
    private String timeslotTimePart = null;

    private EditText firstNameEdit;
    private EditText middleInitialEdit;
    private EditText lastNameEdit;
    private EditText bibEdit;
    private EditText leagueIdEdit;
    private EditText leagueAgeEdit;

    private Button savePlayerData;


    public NewPlayerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_player, container, false);

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

            timeslotLayout = (LinearLayout) getView().findViewById(R.id.timeslotLayout);
            calendarDrawer = getView().findViewById(R.id.calendar_drawer);
        calendarLoaded = false;
        TextView cancelBtn = (TextView) getView().findViewById(R.id.cancel_selection);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarDrawer != null) {
                    calendarDrawer.setVisibility(View.GONE);
                }
            }
        });

        selectDateButton = (Button) getView().findViewById(R.id.changeScheduleDate);
        selectDateButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    showCalendar();
                                                }
                                            }
        );

        //
        // Now set all the EditText's items...
        //

        scheduleTimeText = (TextView)getView().findViewById(R.id.tvScheduleTime);

        firstNameEdit = (EditText)getView().findViewById(R.id.editFirstName);
        middleInitialEdit = (EditText)getView().findViewById(R.id.editMiddleInitial);
        lastNameEdit = (EditText)getView().findViewById(R.id.editLastName);
        bibEdit = (EditText)getView().findViewById(R.id.editBIB);
        leagueIdEdit = (EditText)getView().findViewById(R.id.editLeagueId);
        leagueAgeEdit = (EditText)getView().findViewById(R.id.editLeagueAge);

        firstNameEdit.setOnEditorActionListener(this);
        middleInitialEdit.setOnEditorActionListener(this);
        lastNameEdit.setOnEditorActionListener(this);
        bibEdit.setOnEditorActionListener(this);
        leagueIdEdit.setOnEditorActionListener(this);
        leagueAgeEdit.setOnEditorActionListener(this);

        this.addTextChangeListenerToEditText(firstNameEdit);
        this.addTextChangeListenerToEditText(middleInitialEdit);
        this.addTextChangeListenerToEditText(lastNameEdit);
        this.addTextChangeListenerToEditText(bibEdit);
        this.addTextChangeListenerToEditText(leagueIdEdit);
        this.addTextChangeListenerToEditText(leagueAgeEdit);

        savePlayerData = (Button)getView().findViewById(R.id.savePlayerButton);
        savePlayerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataCheckIsClean()) {
                    PlayerInfo editedPlayer = new PlayerInfo();
                    editedPlayer.setFirstname(firstNameEdit.getText().toString());
                    editedPlayer.setLastname(lastNameEdit.getText().toString());

                    if (middleInitialEdit.getText().length() > 0) {
                        editedPlayer.setMiddlename(middleInitialEdit.getText().toString());
                    }

                    editedPlayer.setBib(bibEdit.getText().toString());
                    editedPlayer.setLeagueid(leagueIdEdit.getText().toString());
                    editedPlayer.setLeagueage(Integer.parseInt(leagueAgeEdit.getText().toString()));

                    editedPlayer.setTryout_time(timeslotTimePart);
                    editedPlayer.setTryout_date(timeslotDatePart);

                    DataUtils.savePlayerInfoData(editedPlayer, getActivity().getBaseContext());
                    displaySuccessfulSaveMessage("Player info successfully added!");
                }
            }
        });

    }

    private boolean dataCheckIsClean() {
        boolean dataIsClean = true;
        String errorMessage = "The following fields have errors: \n\n";

        if (firstNameEdit.getText().length() == 0) {
            dataIsClean = false;
            errorMessage += "No first name for player. \n";
        }
        if (lastNameEdit.getText().length() == 0) {
            dataIsClean = false;
            errorMessage += "No last name for player. \n";
        }
        if (bibEdit.getText().length() == 0) {
            dataIsClean = false;
            errorMessage += "No BIB number for player. \n";
        }
        if (leagueIdEdit.getText().length() == 0) {
            dataIsClean = false;
            errorMessage += "No League ID for player. \n";
        }
        if (leagueAgeEdit.getText().length() == 0) {
            dataIsClean = false;
            errorMessage += "No League Age for player. \n";
        }
        if (selectedTimeSlot == null) {
            dataIsClean = false;
            errorMessage += "No schedule timeslot selected. \n";
        }

        if (PlayerManager.bibNumberExists(bibEdit.getText().toString())) {
            dataIsClean = false;
            bibEdit.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.red));
            errorMessage += "BIB number already in use by another player. \n";
        }

        if (PlayerManager.leagueIdExistsForPlayer(leagueIdEdit.getText().toString())) {
            dataIsClean = false;
            leagueIdEdit.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.red));
            errorMessage += "League ID already exists for another player.  \n";
        }


        if (!dataIsClean) {
            errorMessage += "\n Please correct errors.";
            displayErrorMessage(errorMessage);
        }

        return dataIsClean;
    }

    private void displayErrorMessage(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle("Error saving New User data!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void displaySuccessfulSaveMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle("New User: " + firstNameEdit.getText().toString() + " " + lastNameEdit.getText().toString() + " has been created.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                firstNameEdit.setText("");
                lastNameEdit.setText("");
                middleInitialEdit.setText("");
                bibEdit.setText("");
                leagueIdEdit.setText("");
                scheduleTimeText.setText("");
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void addTextChangeListenerToEditText(final EditText et) {
        et.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                et.setTextColor(ContextCompat.getColor(getActivity().getBaseContext(), R.color.black));
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
    }


    private String getActionBarTitle() {
        return null;
    }

    @Override
    public void onCalendarItemClick(String entryDateTime) {
        Toast.makeText(getContext(), "Timeslot selected: " + entryDateTime, Toast.LENGTH_SHORT).show();
        scheduleTimeText.setText(entryDateTime);
    }

    @Override
    public void showCalendar() {
        if (calendarDrawer.getVisibility() == View.INVISIBLE || calendarDrawer.getVisibility() == View.GONE) {
            if (calendarLoaded == false) {
                List<String> list = new ArrayList<String>(PlayerManager.getTimeslotToSecondsMap().keySet());
                Collections.sort(list);
                CalendarViewUtils.addTimeslotsToLayout(list, timeslotLayout, NewPlayerFragment.this, getContext());
                calendarLoaded = true;
            }
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
            return;  // nothing to do here...

        } else {
            int id = v.getId() - 1000;  // added an offset so as to not conflict with other generated RES IDs
            List<String> list = new ArrayList<String>(PlayerManager.getTimeslotToSecondsMap().keySet());
            Collections.sort(list);  // might want to store these to save constantly sorting them...

            timeslot = PlayerManager.getTimeslotToSecondsMap().get(list.get(id));
            selectedTimeSlot = timeslot;

            int index = timeslot.indexOf(" \n ");
            timeslotDatePart = timeslot.substring(0, index);
            timeslotTimePart= timeslot.substring(index+3);

            Log.e(TAG, "Date = " + timeslotDatePart + " :: Time = " + timeslotDatePart);
        }

        this.onCalendarItemClick(timeslot);
        calendarDrawer.setVisibility(View.GONE);
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

            //player.setBib(v.getText().toString());
            //DataUtils.savePlayerInfoData(player, parentAdapter.parentFrag.getContext());
            return true;
        }
        return false;
    }

    //
    // methods to reconfigure which players are shown and methods to manipulate the options menu items...
    //

}
