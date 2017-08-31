package com.youthdraft.youthdraftcoach.utility;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youthdraft.youthdraftcoach.R;
import com.youthdraft.youthdraftcoach.datamodel.PlayerManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jjupin on 1/5/17.
 */

public class CalendarViewUtils {

    public static LinearLayout addTimeslotsToLayout(List<String> timeslots, LinearLayout ll, View.OnClickListener clickHandler, Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);

        int counter = 0;
        for (String timeslot : timeslots) {

            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.calendar_entry_layout, null, false);
            layout.setId(counter+1000);

            int colorId =  counter%2 == 0 ? R.color.pale_orange : R.color.pale_blue;
            layout.setBackgroundColor(ContextCompat.getColor(context, colorId));

            Date date = new Date(Long.parseLong(timeslot));
            String s = PlayerManager.getTimeslotToSecondsMap().get(timeslot);


            TextView rowTextViewMonth = (TextView)layout.findViewById(R.id.month_title);
            TextView rowTextViewDay   = (TextView)layout.findViewById(R.id.day_title);
            TextView rowTextViewTime  = (TextView)layout.findViewById(R.id.time_title);
            TextView rowTextViewCount = (TextView)layout.findViewById(R.id.player_count_title);

            rowTextViewMonth.setText(DateUtils.getMonthForDate(date));
            rowTextViewDay.setText(DateUtils.getDayForDate(date));
            rowTextViewTime.setText(DateUtils.getTimeForDate(date));
            rowTextViewCount.setText("( " + PlayerManager.getPlayersForTimeslot(s).size() + " )");

            layout.setOnClickListener(clickHandler);

            counter++;

            // add the textview to the linearlayout
            ll.addView(layout);

        }

        return ll;
    }

    public static LinearLayout removeAllTimeslots(LinearLayout ll) {  // except for the "All Players" item...

        List<View> viewsToRemove = new ArrayList<View>();

        for (int i=0; i<ll.getChildCount(); i++) {
            View v = ll.getChildAt(i);
            if (v.getId() >= 1000) {
                viewsToRemove.add(v);
            }
        }

        for (View v : viewsToRemove) {
            ll.removeView(v);
        }

        return ll;
    }


}
