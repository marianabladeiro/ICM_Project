package com.example.ICM_Project.ui.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ICM_Project.databinding.FragmentCalendarBinding;

import java.util.Calendar;
import java.util.GregorianCalendar;

/* TODO
Ask permission to sync from calendar
* */
public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener {

    private NotificationsViewModel notificationsViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        FragmentCalendarBinding binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), addActivity.class);
                startActivity(myIntent);
            }
        });

        return root;
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year,
                                    int monthOfYear, int dayOfMonth) {
        Calendar then=new GregorianCalendar(year, monthOfYear, dayOfMonth);


        Toast.makeText(getContext(), then.getTime().toString(), Toast.LENGTH_LONG)
                .show();
    }

    /* TODO SYNCH CALENDAR
    //sync calendar (code taken from https://developers.google.com/calendar/v3/sync)
    private static void run() throws IOException {
        // Construct the {@link Calendar.Events.List} request, but don't execute it yet.
        Calendar.Events.List request = client.events().list("primary");

        // Load the sync token stored from the last execution, if any.
        String syncToken = syncSettingsDataStore.get(SYNC_TOKEN_KEY);
        if (syncToken == null) {
            System.out.println("Performing full sync.");

            // Set the filters you want to use during the full sync. Sync tokens aren't compatible with
            // most filters, but you may want to limit your full sync to only a certain date range.
            // In this example we are only syncing events up to a year old.
            Date oneYearAgo = Utils.getRelativeDate(java.util.Calendar.YEAR, -1);
            request.setTimeMin(new DateTime(oneYearAgo, TimeZone.getTimeZone("UTC")));
        } else {
            System.out.println("Performing incremental sync.");
            request.setSyncToken(syncToken);
        }

        // Retrieve the events, one page at a time.
        String pageToken = null;
        CalendarContract.Events events = null;
        do {
            request.setPageToken(pageToken);

            try {
                events = request.execute();
            } catch (GoogleJsonResponseException e) {
                if (e.getStatusCode() == 410) {
                    // A 410 status code, "Gone", indicates that the sync token is invalid.
                    System.out.println("Invalid sync token, clearing event store and re-syncing.");
                    syncSettingsDataStore.delete(SYNC_TOKEN_KEY);
                    eventDataStore.clear();
                    run();
                } else {
                    throw e;
                }
            }

            List<Event> items = events.getItems();
            if (items.size() == 0) {
                System.out.println("No new events to sync.");
            } else {
                for (Event event : items) {
                    syncEvent(event);
                }
            }

            pageToken = events.getNextPageToken();
        } while (pageToken != null);

        // Store the sync token from the last request to be used during the next execution.
        syncSettingsDataStore.set(SYNC_TOKEN_KEY, events.getNextSyncToken());

        System.out.println("Sync complete.");
    }

     */
}