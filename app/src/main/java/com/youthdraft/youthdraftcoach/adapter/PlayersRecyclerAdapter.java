package com.youthdraft.youthdraftcoach.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.activity.players.PlayersFragment;
import com.youthdraft.youthdraftcoach.activity.players.RescheduleFragment;
import com.youthdraft.youthdraftcoach.datamodel.PlayerInfo;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;
import com.youthdraft.youthdraftcoach.utility.AdminUtils;
import com.youthdraft.youthdraftcoach.utility.DataUtils;
import com.youthdraft.youthdraftcoach.utility.DateUtils;
import com.youthdraft.youthdraftcoach.views.ConnectionsImageView;
import com.youthdraft.youthdraftcoach.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jjupin on 11/15/16.
 */

public class PlayersRecyclerAdapter extends RecyclerView.Adapter<PlayersRecyclerAdapter.ViewHolder> implements OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private static String TAG = "PlayersRecycler" +
            "Adapter";

    private ViewGroup stickyHeaderView;
    public Fragment parentFrag;

    private List<PlayerInfo> mDataset;


    public PlayersRecyclerAdapter(List<PlayerInfo> players, Fragment parentFrag) {
        this.mDataset = players;
        this.parentFrag = parentFrag;
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

    public void remove(int position) {
        mDataset.remove(position);

        //((PlayersRecycleFragment)parentFrag).recyclerView.removeViewAt(position);
        //notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
        notifyDataSetChanged();

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlayersRecyclerAdapter(ArrayList<PlayerInfo> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlayersRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_team_item, parent, false);
        ViewHolder vh = new ViewHolder(v, this);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position, this);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        return mDataset.size();
    }

    @Override
    public void onItemClick(int position) {


        //Intent showStatsIntent = new Intent(this.parentFrag.getActivity(), AssessFragment.class);
        //this.parentFrag.getActivity().startActivity(showStatsIntent);

        Log.e("PlayersRecyclerAdapter", "Survey Says! " + mDataset.get(position).getFirstname());


//        if (parentFrag instanceof PlayersRecycleFragment) {
//            PlayersRecycleFragment frag = (PlayersRecycleFragment) parentFrag;
//            if (frag.isAssessedPlayersOnly() || frag.isDraftedPlayersOnly()) {
//                showDetailDataOfPlayer(mDataset.get(position));
//            }
//        } else {
            showDetailDataOfPlayer(mDataset.get(position));
 //       }
    }

    private void showDetailDataOfPlayer(PlayerInfo player) {
        String rowID;

        //Log.d(LOG_TAG, "playlist val ="+playList.get(position).toString());
        //String pos = playList.get(position).toString();
        //Log.d(LOG_TAG, "mMPlayersHashMap ="+mMPlayersHashMap.get(pos).getRowid());
        //Log.d(LOG_TAG, "playerRank1 ="+playerRank1.toString());
        //rowID = mPlayerScoreHashMap.get(playOrder.get(position)).getPlayerid();

        AlertDialog.Builder alert = new AlertDialog.Builder(parentFrag.getContext());
        LayoutInflater inflater = parentFrag.getLayoutInflater(null);
        View view1 = inflater.inflate(R.layout.player_ranking, null);
        alert.setView(view1);
        TextView playName = (TextView) view1.findViewById(R.id.tvFirstName);
        TextView playLast = (TextView) view1.findViewById(R.id.tvLastName);
        TextView playScore = (TextView) view1.findViewById(R.id.tvCompositeScore);
        TextView playHeight = (TextView) view1.findViewById(R.id.tvHeightScore);
        TextView playWeight = (TextView) view1.findViewById(R.id.tvWeightScore);
        TextView playHit = (TextView) view1.findViewById(R.id.tvHittingScore);
        TextView playBat = (TextView) view1.findViewById(R.id.tvBattingScore);
        TextView playInfield = (TextView) view1.findViewById(R.id.tvInfieldScore);
        TextView playOutfield = (TextView) view1.findViewById(R.id.tvOutfieldScore);
        TextView playThrow = (TextView) view1.findViewById(R.id.tvThrowingScore);
        TextView playArm = (TextView) view1.findViewById(R.id.tvArmScore);
        TextView playSpeed = (TextView) view1.findViewById(R.id.tvSpeedScore);
        TextView playBaseRunning = (TextView) view1.findViewById(R.id.tvBaseScore);
        TextView playDraft = (TextView) view1.findViewById(R.id.tvDraftableScore);
        TextView playDate = (TextView) view1.findViewById(R.id.tvDatetime);

        EditText playNote = (EditText) view1.findViewById(R.id.etNoteValue);


        //Log.d(LOG_TAG, "first pop ="+cursor.getString(cursor.getColumnIndex(RankDB.KEY_FIRSTNAME)));
        playName.setText(player.getFirstname());
        playLast.setText(player.getLastname());

        playScore.setText(String.valueOf(player.getCompositeScore()));
        playHeight.setText(String.valueOf(player.getHeight()));
        playWeight.setText(String.valueOf(player.getWeight()));
        playHit.setText(String.valueOf(player.getHitting()));
        playBat.setText(String.valueOf(player.getBat()));
        playInfield.setText(String.valueOf(player.getInfield()));
        playOutfield.setText(String.valueOf(player.getOutfield()));
        playThrow.setText(String.valueOf(player.getThrowing()));
        playArm.setText(String.valueOf(player.getArm()));
        playSpeed.setText(String.valueOf(player.getSpeed()));
        playBaseRunning.setText(String.valueOf(player.getBase()));
        playDate.setText(player.getTryout_date());


        String draft = "Yes";
        Log.d(TAG, "draft =" + draft);
        if (draft.equals("Yes")) {
            playDraft.setText("Draftable");
        } else {
            playDraft.setText("Not Draftable");
        }
        playNote.setText("Retrieve from scores object...");


        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, OnChartValueSelectedListener {

        public PlayersRecyclerAdapter parentAdapter;

        public ConnectionsImageView avatar;
        public TextView leagueAge;
        public TextView txtHeader;
        public EditText nameEdit;
        public TextView txtDraftable;
        public TextView txtScore;

        public TextView txtAssessedSkill;
        public TextView txtAssessedSkillTitle;

        public TextView txtComposite;
        public TextView txtCompositeTitle;

        public TextView seekBarActivity;
        public TextView seekBarValue;
        public SeekBar seekBarWidget;
        public Button notesButton;

        public SwitchCompat pitcherSwitch;
        public SwitchCompat catcherSwitch;
        public SwitchCompat coachesKidSwitch;

        public Button btnReschedule;
        public Button btnAssessmentCompleted;

        public TextView txtScoreTitle;

        public int position;

        public BarChart mChart;

        public EditText bibEdit;
        public TextView bibView;

        //
        // items to show when the player is scheduled to be assessed
        //

        public TextView monthTitle;
        public TextView dayTitle;
        public TextView timeTitle;

        private RelativeLayout.LayoutParams smallLP;
        private RelativeLayout.LayoutParams maxLP;

        public ViewHolder(View v, PlayersRecyclerAdapter adapter) {
            super(v);
            this.parentAdapter = adapter;

            avatar = (ConnectionsImageView) v.findViewById(R.id.avatarview);
            leagueAge = (TextView) v.findViewById(R.id.league_age_text);

            txtHeader = (TextView) v.findViewById(R.id.text1);
            txtHeader.setVisibility(AdminUtils.isAdmin(null, parentFrag.getContext()) ? View.GONE : View.VISIBLE);
            txtDraftable = (TextView) v.findViewById(R.id.text2);
            nameEdit = (EditText) v.findViewById(R.id.name_edit);
            nameEdit.setVisibility(AdminUtils.isAdmin(null, parentFrag.getContext()) ? View.VISIBLE : View.GONE);

            bibEdit = (EditText) v.findViewById(R.id.bib_edit);
            bibEdit.setVisibility(AdminUtils.isAdmin(null, parentFrag.getContext()) ? View.VISIBLE : View.GONE);
            bibView = (TextView) v.findViewById(R.id.bib_view);
            bibView.setVisibility(AdminUtils.isAdmin(null, parentFrag.getContext()) ? View.GONE : View.VISIBLE);

            txtAssessedSkill = (TextView) v.findViewById(R.id.score);
            txtAssessedSkillTitle = (TextView) v.findViewById(R.id.score_title);
            txtScore = (TextView) v.findViewById(R.id.score2);
            txtScoreTitle = (TextView) v.findViewById(R.id.score_title2);

            btnReschedule = (Button) v.findViewById(R.id.reschedule_button);
            btnReschedule.setVisibility(AdminUtils.isAdmin(null, parentFrag.getContext()) ? View.VISIBLE : View.INVISIBLE);

            btnAssessmentCompleted = (Button) v.findViewById(R.id.assessment_completed_button);

            mChart = (BarChart) v.findViewById(R.id.chart1);

            seekBarActivity = (TextView) v.findViewById(R.id.tvPlayerActivity);
            seekBarValue = (TextView) v.findViewById(R.id.tvPlayerActivityVal);
            seekBarWidget = (SeekBar) v.findViewById(R.id.sPlayerActivity);
            notesButton = (Button) v.findViewById(R.id.notes_button);

            pitcherSwitch = (SwitchCompat) v.findViewById(R.id.toggle_pitcher);
            catcherSwitch = (SwitchCompat) v.findViewById(R.id.toggle_catcher);
            coachesKidSwitch = (SwitchCompat) v.findViewById(R.id.toggle_coaches_kid);

            //
            // Calendar items...
            //

            monthTitle = (TextView) v.findViewById(R.id.month_title);
            dayTitle = (TextView) v.findViewById(R.id.day_title);
            timeTitle = (TextView) v.findViewById(R.id.time_title);

            smallLP = new RelativeLayout.LayoutParams(100, 100);
            smallLP.addRule(RelativeLayout.CENTER_IN_PARENT);

            maxLP = new RelativeLayout.LayoutParams(150, 150); //ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            maxLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            maxLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }

        public void bind(final int position, final OnItemClickListener listener) {
            final PlayerInfo player = mDataset.get(position);

            this.position = position;

            //
            // TEMPY !!!!!
            //

            if (TextUtils.isEmpty(player.getBib()) == false) {
                bibEdit.setText(player.getBib());
                bibView.setText(player.getBib());
            } else {
                bibEdit.setText("");
                bibView.setText("");
                bibEdit.setHint("0");
                bibView.setHint("0");
            }

            bibEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        InputMethodManager imm = (InputMethodManager) parentAdapter.parentFrag.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(bibEdit.getWindowToken(), 0);

                        player.setBib(v.getText().toString());
                        DataUtils.savePlayerInfoData(player, parentAdapter.parentFrag.getContext());
                        return true;
                    }
                    return false;
                }
            });

            nameEdit.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        InputMethodManager imm = (InputMethodManager) parentAdapter.parentFrag.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(bibEdit.getWindowToken(), 0);

                        String[] words = nameEdit.getText().toString().split("\\W+");
                        if (words != null) {
                            player.setFirstname(words.length >= 1 ? words[0] : "");
                            player.setLastname(words.length >= 2 ? words[1] : "");
                            DataUtils.savePlayerInfoData(player, parentAdapter.parentFrag.getContext());
                        }
                        return true;
                    }
                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listener.onItemClick(position);
                }
            });

            //
            // Set the player's league age...
            //

            leagueAge.setText("League Age: " + player.getLeagueage());

            //
            // Configure the seekbar settings...
            //

            String title = PlayerInfo.getTitleForField(PlayerInfo.getAssessComparatorField()).replaceAll("\\r?\\n", "");
            seekBarActivity.setText(title);

            seekBarWidget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    updatePlayerInfoWithSeekBarChange(progress, position);
                    /**
                     seekBarValue.setText(String.valueOf(progress));
                     player.setHitting(progress);
                     mChart.setData(generateBarData(1,10,12, player));
                     mChart.invalidate();
                     **/
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    //updatePlayerInfoWithSeekBarChange(seekBar.getProgress(), position);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //updatePlayerInfoWithSeekBarChange(seekBar.getProgress(), position);
                    PlayerInfo player = mDataset.get(position);
                    DataUtils.savePlayerAssessmentScores(player, parentAdapter.parentFrag.getContext());
                }
            });

            seekBarWidget.setProgress(player.getValueForField(PlayerInfo.getAssessComparatorField()));
            seekBarValue.setText(seekBarWidget.getProgress() + "");


            String s = player.getNoteForField(PlayerInfo.getAssessComparatorField());
            notesButton.setText((s == null || s.length() == 0 ? "ADD" : "EDIT") + " NOTE FOR \n " + title);
            notesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context c = parentAdapter.parentFrag.getContext();
                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                    View mView = layoutInflaterAndroid.inflate(R.layout.input_note_dialog, null);
                    TextView title = (TextView) mView.findViewById(R.id.dialog_title);
                    title.setText("Note for " + PlayerInfo.getTitleForField(PlayerInfo.getAssessComparatorField()).replaceAll("\\r?\\n", ""));

                    AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                    alertDialogBuilderUserInput.setView(mView);

                    final EditText noteEditText = (EditText) mView.findViewById(R.id.note_text);
                    PlayerInfo player = mDataset.get(position);
                    noteEditText.setText(player.getNoteForField(PlayerInfo.getAssessComparatorField()));
                    alertDialogBuilderUserInput
                            .setCancelable(false)
                            .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    // ToDo get user input here
                                    updatePlayerInfoWithNoteChange(noteEditText.getText().toString(), position);
                                    PlayerInfo player = mDataset.get(position);
                                    DataUtils.savePlayerAssessmentScores(player, parentAdapter.parentFrag.getContext());
                                }
                            })

                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialogBox, int id) {
                                            dialogBox.cancel();
                                        }
                                    });

                    AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                    alertDialogAndroid.show();
                }
            });


            mChart.setOnChartValueSelectedListener(this);
            mChart.setDrawBarShadow(false);
            mChart.setDrawValueAboveBar(true);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            //mChart.setMaxVisibleValueCount(10);
            //mChart.setVisibleXRangeMaximum(10.0f);

            // scaling can now only be done on x- and y-axis separately
            mChart.setPinchZoom(false);

            //mChart.setDrawGridBackground(false);

            // Typeface tf = Typeface.createFromAsset(this.parentAdapter.parentFrag.getActivity().getAssets(),"OpenSans-Light.ttf");
            //int highest = mChart.getHighestVisibleXIndex();
            //int lowest = mChart.getLowestVisibleXIndex();

            mChart.setVisibility(View.VISIBLE);
            mChart.setData(generateBarData(1, 10, 12, player));
            Legend l = mChart.getLegend();
            //l.setTypeface(tf);

            YAxis leftAxis = mChart.getAxisLeft();
            //leftAxis.setTypeface(tf);
            //leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            mChart.getAxisRight().setEnabled(false);
            mChart.setDescription("");  // empty it out...
            mChart.getAxisLeft().setDrawLabels(false);
            mChart.getAxisRight().setDrawLabels(false);
            mChart.getXAxis().setDrawLabels(false);
            leftAxis.setAxisMaxValue(10);
            leftAxis.setAxisMinValue(0);

            mChart.getLegend().setEnabled(false);
            mChart.setNoDataText("No data available");

            // xAxis.setEnabled(false);

            if (parentFrag instanceof RescheduleFragment) {
                btnReschedule.setText("Schedule Player");
            }
            btnReschedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (parentFrag instanceof PlayersFragment) {
                        player.setReschedule(true);
                        DataUtils.savePlayerInfoData(player, parentFrag.getContext());
                        remove(position);
                        Toast.makeText(parentFrag.getContext(), player.getFullname() + " moved to reschedule", Toast.LENGTH_SHORT).show();
                    } else if (parentFrag instanceof RescheduleFragment) {
                        RescheduleFragment reschedFrag = (RescheduleFragment)parentFrag;
                        reschedFrag.selectedPosition = position;
                        reschedFrag.showCalendar();
                    }
                }
            });

            btnAssessmentCompleted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    player.setCompletedAssessment(true);
                    DataUtils.savePlayerAssessmentScores(player, parentFrag.getContext());
                    remove(position);
                }
            });
            if (parentFrag instanceof RescheduleFragment) {
                btnAssessmentCompleted.setVisibility(View.GONE);
            }

            txtHeader.setText(player.getFullname());
            nameEdit.setText(player.getFullname());
            txtDraftable.setVisibility(View.VISIBLE);
            if (PlayerManager.getMyTeamPlayers().contains(player)) {
                txtDraftable.setText("Outfielder");
                avatar.getAvatarView().setLayoutParams(maxLP);
                avatar.getAvatarView().setImageDrawable(ContextCompat.getDrawable(parentFrag.getContext(), getPlayerImage(position)));
            } else {
                txtDraftable.setText(player.isCoachsKid() ? "Not Draftable" : "Draftable");
                // turn this back on when we get the actual photos of the players
                //avatar.getAvatarView().setLayoutParams(maxLP);
                //avatar.getAvatarView().setImageDrawable(ContextCompat.getDrawable(parentFrag.getContext(), getPlayerImage(position % 5)));
            }

            boolean visibility = false;

            PlayerInfo.ComparatorField currentF = PlayerInfo.getSortComparatorField();
            if (currentF != PlayerInfo.ComparatorField.ALPHABETICAL_FIRST &&
                    currentF != PlayerInfo.ComparatorField.ALPHABETICAL_LAST &&
                    currentF != PlayerInfo.ComparatorField.ADVANCED &&
                    currentF != PlayerInfo.ComparatorField.RANKING) {
                visibility = true;
            }

            //if (player.getCompositeScore() > 0) {
            txtScoreTitle.setText(PlayerInfo.getTitleForField(PlayerInfo.getSortComparatorField()));
            txtScore.setText(player.getValueForField(PlayerInfo.getSortComparatorField()) + "");
            txtAssessedSkillTitle.setText(PlayerInfo.getTitleForField(null));  // passing a null returns "Composite Score" string automatically
            txtAssessedSkill.setText(player.getCompositeScore() + "");

            //txtComposite.setText(player.getCompositeScore() + "");
            //txtComposite.setVisibility(visibility ? View.VISIBLE : View.GONE);
            //txtCompositeTitle.setVisibility(visibility ? View.VISIBLE : View.GONE);

            /**
             } else {
             txtScore.setText(player.getCompositeScore() + "");
             txtScore.setVisibility(View.VISIBLE);
             txtScoreTitle.setText(PlayerInfo.getTitleForField(PlayerInfo.ComparatorField.ALPHABETICAL_LAST));
             txtScoreTitle.setVisibility(View.VISIBLE);
             }
             **/

            //
            // setup the toggle switches
            // The player's status as a pitcher, catcher or coach's kid
            //

            pitcherSwitch.setChecked(player.isPitcher());
            catcherSwitch.setChecked(player.isCatcher());
            coachesKidSwitch.setChecked(player.isCoachsKid());

            pitcherSwitch.setOnCheckedChangeListener(this);
            catcherSwitch.setOnCheckedChangeListener(this);
            coachesKidSwitch.setOnCheckedChangeListener(this);

            //
            // Now, set these values based on the current Player's Info
            //


            //
            // Now, set the player's scheduled time for assessment
            //

            Date assessmentDate = PlayerManager.getDateForTimeSlot(player.getTryout_date(), player.getTryout_time());
            monthTitle.setText(DateUtils.getMonthForDate(assessmentDate));
            dayTitle.setText(DateUtils.getDayForDate(assessmentDate));
            timeTitle.setText(DateUtils.getTimeForDate(assessmentDate));

        }

        @Override
        public void onClick(View v) {
            if (parentFrag != null) {
                Toast.makeText(parentFrag.getContext(), "We got a clickee!!!", Toast.LENGTH_LONG).show();
            }
        }

        //
        // player type switches (Pitcher / Catcher / Coach's Kid)
        //

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            PlayerInfo currentPlayer = mDataset.get(this.position);

            boolean pitcherOrCatcherChanged = false;

            if (buttonView == this.pitcherSwitch) {
//                if (isChecked) {
//                    this.catcherSwitch.setChecked(false);
//                }

                pitcherOrCatcherChanged = true;
            }

            if (buttonView == this.catcherSwitch) {
//                if (isChecked) {
//                    this.pitcherSwitch.setChecked(false);
//                }

                pitcherOrCatcherChanged = true;
            }

            if (buttonView == this.coachesKidSwitch) {
                txtDraftable.setText(this.coachesKidSwitch.isChecked() ? "Not Draftable" : "Draftable");
                currentPlayer.setCoachsKid(this.coachesKidSwitch.isChecked());
                DataUtils.savePlayerInfoData(currentPlayer, parentAdapter.parentFrag.getContext());
                return;
            }

            if (pitcherOrCatcherChanged) {
                currentPlayer.setPitcher(this.pitcherSwitch.isChecked());
                currentPlayer.setCatcher(this.catcherSwitch.isChecked());
                DataUtils.savePlayerAssessmentScores(currentPlayer, parentAdapter.parentFrag.getContext());
            }

            txtDraftable.setText(currentPlayer.isCoachsKid() ? "Not Draftable" : "Draftable");
        }

        //
        // Propagate the seekbar changes to the player object...
        //

        private void updatePlayerInfoWithSeekBarChange(int progress, int position) {
            final PlayerInfo player = mDataset.get(position);

            switch (PlayerInfo.getAssessComparatorField()) {
                case HITTING_MECHANICS:
                    player.setHitting(progress);
                    break;
                case BAT_SPEED:
                    player.setBat(progress);
                    break;
                case INFIELD_MECHANICS:
                    player.setInfield(progress);
                    break;
                case OUTFIELD_MECHANICS:
                    player.setOutfield(progress);
                    break;
                case THROWING_MECHANICS:
                    player.setThrowing(progress);
                    break;
                case FIELDING:
                    player.setArm(progress);
                    break;
                case SPEED:
                    player.setSpeed(progress);
                    break;
                case BASE_RUNNING:
                    player.setBase(progress);
                    break;
                default:
                    return;
            }

            mChart.setData(generateBarData(1, 10, 12, player));
            mChart.invalidate();

            //
            // Need to update our composite score as well...
            //

            player.updateCompositeScore();
            txtScore.setText(player.getValueForField(PlayerInfo.getAssessComparatorField()) + "");
            txtScoreTitle.setText(player.getTitleForField(PlayerInfo.getAssessComparatorField()));
            txtAssessedSkill.setText(player.getCompositeScore() + "");

        }

        private void updatePlayerInfoWithNoteChange(String note, int position) {
            final PlayerInfo player = mDataset.get(position);

            switch (PlayerInfo.getAssessComparatorField()) {
                case HITTING_MECHANICS: //Hitting mechanics
                    player.setNoteHitting(note);
                    break;
                case BAT_SPEED: //Bat Speed
                    player.setNoteBat(note);
                    break;
                case INFIELD_MECHANICS: //Infield Mechanics
                    player.setNoteInfield(note);
                    break;
                case OUTFIELD_MECHANICS: //Outfield Mechanics
                    player.setNoteOutfield(note);
                    break;
                case THROWING_MECHANICS: //Throwing Mechanics
                    player.setNoteThrowing(note);
                    break;
                case FIELDING: //Fielding
                    player.setNoteArm(note);
                    break;
                case SPEED: //Speed
                    player.setNoteSpeed(note);
                    break;
                case BASE_RUNNING: //Base Running
                    player.setNoteBase(note);
                    break;
                //case : //Custom
                //    return;   // need to do something totally different here...
                default:
                    return;
            }
        }

        protected int getPlayerImage(int posiiton) {
            switch (position) {
                case 0:
                    return R.drawable.player_0;
                case 1:
                    return R.drawable.player_1;
                case 2:
                    return R.drawable.player_2;
                case 3:
                    return R.drawable.player_3;
                case 4:
                    return R.drawable.player_4;
                default:
                    return R.drawable.player_0;
            }
        }

        public final int[] transparentColors = {Color.argb(0, 0, 0, 0), Color.argb(0, 0, 0, 0)};
        public final int transparentColor = Color.argb(0, 0, 0, 0);
        public final int[] VORDIPLOM_COLORS = {
                Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
                Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
        };

        public Map<Integer, PlayerInfo.ComparatorField> dataFieldsMapping;

        protected BarData generateBarData(int dataSets, float range, int count, PlayerInfo player) {

            if (dataFieldsMapping == null || dataFieldsMapping.size() == 0) {
                dataFieldsMapping = new HashMap<Integer, PlayerInfo.ComparatorField>();

                dataFieldsMapping.put(0, PlayerInfo.ComparatorField.HITTING_MECHANICS);
                dataFieldsMapping.put(1, PlayerInfo.ComparatorField.INFIELD_MECHANICS);
                dataFieldsMapping.put(2, PlayerInfo.ComparatorField.OUTFIELD_MECHANICS);
                dataFieldsMapping.put(3, PlayerInfo.ComparatorField.BASE_RUNNING);
                dataFieldsMapping.put(4, PlayerInfo.ComparatorField.THROWING_MECHANICS);
                dataFieldsMapping.put(5, PlayerInfo.ComparatorField.FIELDING);
                dataFieldsMapping.put(6, PlayerInfo.ComparatorField.SPEED);
                dataFieldsMapping.put(7, PlayerInfo.ComparatorField.BAT_SPEED);

            }

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(player.getHitting() == 0 ? 0 : player.getHitting(), 0));
            entries.add(new BarEntry(player.getInfield() == 0 ? 0 : player.getInfield(), 1));
            entries.add(new BarEntry(player.getOutfield() == 0 ? 0 : player.getOutfield(), 2));
            entries.add(new BarEntry(player.getBase() == 0 ? 0 : player.getBase(), 3));
            entries.add(new BarEntry(player.getThrowing() == 0 ? 0 : player.getThrowing(), 4));
            entries.add(new BarEntry(player.getArm() == 0 ? 0 : player.getArm(), 5));
            entries.add(new BarEntry(player.getSpeed() == 0 ? 0 : player.getSpeed(), 6));
            entries.add(new BarEntry(player.getBat() == 0 ? 0 : player.getBat(), 7));


            BarDataSet dataset = new BarDataSet(entries, "Assessment Stats");
            if (PlayerInfo.getSortComparatorField() == PlayerInfo.ComparatorField.ALPHABETICAL_FIRST ||
                    PlayerInfo.getSortComparatorField() == PlayerInfo.ComparatorField.ALPHABETICAL_LAST ||
                    PlayerInfo.getSortComparatorField() == PlayerInfo.ComparatorField.RANKING ||
                    PlayerInfo.getSortComparatorField() == PlayerInfo.ComparatorField.ADVANCED) {
                //dataset.setColors(VORDIPLOM_COLORS);

                int[] colors = new int[8];
                colors[0] = player.getHitting() == 0 ? transparentColor : Color.rgb(192, 255, 140);
                colors[7] = player.getBat() == 0 ? transparentColor : Color.rgb(255, 247, 140);
                colors[1] = player.getInfield() == 0 ? transparentColor : Color.rgb(255, 208, 140);
                colors[2] = player.getOutfield() == 0 ? transparentColor : Color.rgb(140, 234, 255);
                colors[4] = player.getThrowing() == 0 ? transparentColor : Color.rgb(255, 140, 157);
                colors[5] = player.getArm() == 0 ? transparentColor : Color.rgb(192, 255, 140);
                colors[6] = player.getSpeed() == 0 ? transparentColor : Color.rgb(255, 247, 140);
                colors[3] = player.getBase() == 0 ? transparentColor : Color.rgb(255, 208, 140);

                dataset.setColors(colors);
            } else {
                dataset.setColors(getColorsForSelectedFilter(PlayerInfo.getColumnForField(PlayerInfo.getSortComparatorField())));
            }

            /**
             ColorTemplate.LIBERTY_COLORS
             ColorTemplate.COLORFUL_COLORS
             ColorTemplate.JOYFUL_COLORS
             ColorTemplate.PASTEL_COLORS
             ColorTemplate.VORDIPLOM_COLORS

             */

            ArrayList<String> labels = new ArrayList<String>();
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");

            BarData data = new BarData(labels, dataset);

            return data;
        }

        private int[] getColorsForSelectedFilter(int fieldPosition) {
            int[] colors = new int[10];
            for (int i = 0; i < 10; i++) {
                if (fieldPosition != i) {
                    colors[i] = Color.rgb(0, 0, 254);
                } else {
                    colors[i] = Color.rgb(255, 20, 147);
                }
            }

            return colors;
        }

        private String[] mLabels = new String[]{"Company A", "Company B", "Company C", "Company D", "Company E", "Company F"};
//    private String[] mXVals = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };

        private String getLabel(int i) {
            return mLabels[i];

        }


        //
        // Interface methods for the BarChart selection taps
        //

        @Override
        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

            final PlayerInfo player = mDataset.get(position);
            String field = PlayerInfo.getTitleForField(dataFieldsMapping.get(e.getXIndex()));
            String value = player.getValueForField(dataFieldsMapping.get(e.getXIndex())) + "";

            txtScoreTitle.setText(field);
            txtScore.setText(value);

            txtScoreTitle.setBackgroundColor(ContextCompat.getColor(parentAdapter.parentFrag.getContext(), R.color.pale_yellow));
            txtScore.setBackgroundColor(ContextCompat.getColor(parentAdapter.parentFrag.getContext(), R.color.pale_yellow));

        }

        @Override
        public void onNothingSelected() {

        }
    }

}