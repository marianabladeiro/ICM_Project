package com.example.icm_project.ui.timer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.icm_project.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Timer;

import static androidx.core.content.ContextCompat.getSystemService;

/*
TODO:
option to have to stay on timer
add ok? to alert
Pause button
Fix buttons going up when teclado opens
personalized timmers
* */

public class TimerFragment extends Fragment implements View.OnClickListener {

    private TimerViewModel timerViewModel;
    int i = -1;
    ProgressBar mProgressBar, mProgressBar1;

    private TextView time;

    private Button buttonStartTime, buttonStopTime, button_picker;
    private EditText edtTimerValueHours, edtTimerValueMins, edtTimerValueSecs;
    private TextView textViewShowTime;
    private CountDownTimer countDownTimer;
    private int totalTimeCountInMilliseconds;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_timer, container, false);


        buttonStartTime = (Button) root.findViewById(R.id.button_timerview_start);
        buttonStopTime = (Button) root.findViewById(R.id.button_timerview_stop);
        textViewShowTime = (TextView) root.findViewById(R.id.textView_timerview_time);
        edtTimerValueHours = (EditText) root.findViewById(R.id.textview_timerview_back);
        edtTimerValueMins = (EditText) root.findViewById(R.id.editTextNumberDecimal2);
        edtTimerValueSecs = (EditText) root.findViewById(R.id.editTextNumberDecimal3);

        buttonStartTime.setOnClickListener(this);
        buttonStopTime.setOnClickListener(this);
        buttonStopTime.setEnabled(false);



        mProgressBar = (ProgressBar) root.findViewById(R.id.progressbar_timerview);
        mProgressBar1 = (ProgressBar) root.findViewById(R.id.progressbar1_timerview);


        return root;

    }


    public void onClick(View v) {

            if (v.getId() == R.id.button_timerview_start) {
                setTimer();

                buttonStartTime.setVisibility(View.VISIBLE);
                buttonStartTime.setText("Pause");
                buttonStopTime.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                startTimer();
                buttonStopTime.setEnabled(true);
                mProgressBar1.setVisibility(View.VISIBLE);


            } else if (v.getId() == R.id.button_timerview_stop) {
                countDownTimer.cancel();
                countDownTimer.onFinish();
                mProgressBar1.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                buttonStartTime.setVisibility(View.VISIBLE);
                buttonStopTime.setEnabled(false);
                //buttonStopTime.setVisibility(View.INVISIBLE);

            }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setTimer(){
        int hours = 0;
        int mins = 0;
        int secs = 0;
        if (edtTimerValueHours.getText().toString().equals("") && edtTimerValueMins.getText().toString().equals("") && edtTimerValueSecs.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Please Enter Time...",
                    Toast.LENGTH_LONG).show(); }

        if (!edtTimerValueHours.getText().toString().equals("")) {
            hours = Integer.parseInt(edtTimerValueHours.getText().toString());
            totalTimeCountInMilliseconds = hours * 3600000;
        }

        if (!edtTimerValueMins.getText().toString().equals("")) {
            mins = Integer.parseInt(edtTimerValueMins.getText().toString());
            totalTimeCountInMilliseconds = mins * 60000;
        }

        if (!edtTimerValueSecs.getText().toString().equals("")) {
            secs = Integer.parseInt(edtTimerValueSecs.getText().toString());
            totalTimeCountInMilliseconds = secs * 1000;
        }

        mProgressBar1.setMax(totalTimeCountInMilliseconds);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                int seconds = (int) (leftTimeInMilliseconds / 1000) % 60;
                int minutes = (int) ((leftTimeInMilliseconds / (1000 * 60)) % 60);
                int hours = (int) ((leftTimeInMilliseconds / (1000 * 60 * 60)) % 24);
                mProgressBar1.setProgress((int) (leftTimeInMilliseconds));
                String m = twoDigitString(hours) + ":" + twoDigitString(minutes) + ":"
                        + twoDigitString(seconds);
                textViewShowTime.setText(m);


            }

            @Override
            public void onFinish() {
                textViewShowTime.setText("00:00:00");
                textViewShowTime.setVisibility(View.VISIBLE);
                buttonStartTime.setVisibility(View.VISIBLE);
                buttonStopTime.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar1.setVisibility(View.GONE);
                buttonStartTime.setText("Start");

                //notification for when timer stops on the notif bar
                notifyThis();
                //notification in fragment
                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Timer ended", Snackbar.LENGTH_LONG).show();


            }
        }.start();
    }

    private String twoDigitString(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    //notification for when timer ends
    public void notifyThis() {
        NotificationManager manager = (NotificationManager)getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= 26)
        {
            //When sdk version is larger than26
            String id = "channel_1";
            String description = "143";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(id, description, importance);

            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(getContext(), id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.ic_action_clock_dark)
                    .setContentTitle("PomodoroUA")
                    .setContentText("Timer over")
                    .setAutoCancel(true)
                    .build();
            manager.notify(1, notification);
        }
        else
        {
            //When sdk version is less than26
            Notification notification = new NotificationCompat.Builder(getContext())
                    .setContentTitle("This is content title")
                    .setContentText("This is content text")
                    .setSmallIcon(R.drawable.ic_action_clock_dark)
                    .build();
            manager.notify(1,notification);
        }
    }




}


