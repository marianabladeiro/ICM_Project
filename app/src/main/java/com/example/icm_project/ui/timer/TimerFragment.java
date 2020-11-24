package com.example.icm_project.ui.timer;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
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
import com.example.icm_project.databinding.FragmentCourseBinding;
import com.example.icm_project.databinding.FragmentTimerBinding;
import com.example.icm_project.ui.course.CourseFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Timer;

import static androidx.core.content.ContextCompat.getSystemService;

/*
TODO:
keep timer when change pages
pause button

* */

public class TimerFragment extends Fragment implements View.OnClickListener {

    private TimerViewModel timerViewModel;

    private CountDownTimer countDownTimer;
    private long totalTimeCountInMilliseconds = 0;

    private boolean mTimerRunning;
    private long mEndTime;

    private FragmentTimerBinding binding;


    //user uid
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTimerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);

        binding.buttonTimerviewStart.setOnClickListener(this);
        binding.buttonTimerviewStop.setOnClickListener(this);
        binding.buttonTimerviewStop.setEnabled(false);


        return root;

    }


    public void onClick(View v) {
        String s, n, m;

            if (v.getId() == R.id.button_timerview_start) {
                if (mTimerRunning) {
                    pauseTimer();
                }
                else {
                    if (binding.buttonTimerviewStop.getText().toString() == "Continue") {
                        startTimer();
                    }


                    Bundle bundle = this.getArguments();
                    if (bundle != null) {
                        s = bundle.getString("timercourse");
                        n = bundle.getString("coursename");
                        setTimer();

                        //buttons
                        layoutOnStart();

                        //in case it's coming from CourseFragment
                        if (s.equals("this came from course")) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Add to " + n)
                                    .setMessage("Do you want to add this timer to " + n + " ?")


                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            long time = 0;
                                            startTimer();
                                            binding.buttonTimerviewStop.setEnabled(true);
                                            binding.progressbar1Timerview.setVisibility(View.VISIBLE);

                                            String secs = binding.editTextNumberDecimal3.getText().toString();
                                            String mins = binding.editTextNumberDecimal2.getText().toString();
                                            String hours = binding.textviewTimerviewBack.getText().toString();

                                            if (secs.matches("-?\\d+")) {
                                                time = time + Long.parseLong(secs);
                                            }

                                            if (mins.matches("-?\\d+")) {
                                                time = time + Long.parseLong(mins);
                                            }

                                            if (hours.matches("-?\\d+")) {
                                                time = time + Long.parseLong(hours);
                                            }

                                            Long finalValue = time;


                                            //save data to firebase
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("timespent");
                                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Long quantity = (long) dataSnapshot.child(n).getValue();
                                                    reference.child(n).setValue(quantity + finalValue);

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    System.out.println("The read failed: " + databaseError.getCode());
                                                }
                                            });


                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startTimer();
                                            binding.buttonTimerviewStop.setEnabled(true);
                                            binding.progressbar1Timerview.setVisibility(View.VISIBLE);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }

                    }
                    else {

                        setTimer();
                        layoutOnStart();
                        startTimer();
                        binding.buttonTimerviewStop.setEnabled(true);
                        binding.progressbar1Timerview.setVisibility(View.VISIBLE);
                    }

                }

            }

            //stop button
            else if (v.getId() == R.id.button_timerview_stop) {
                countDownTimer.cancel();
                countDownTimer.onFinish();
                layoutOnStop();

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
        if (binding.textviewTimerviewBack.getText().toString().equals("") && binding.editTextNumberDecimal2.getText().toString().equals("") && binding.editTextNumberDecimal3.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Please Enter Time...",
                    Toast.LENGTH_LONG).show(); }

        if (!binding.textviewTimerviewBack.getText().toString().equals("")) {
            hours = Integer.parseInt(binding.textviewTimerviewBack.getText().toString());
            totalTimeCountInMilliseconds += hours * 3600000;
        }

        if (!binding.editTextNumberDecimal2.getText().toString().equals("")) {
            mins = Integer.parseInt(binding.editTextNumberDecimal2.getText().toString());

            totalTimeCountInMilliseconds += mins * 60000;
        }

        if (!binding.editTextNumberDecimal3.getText().toString().equals("")) {

            secs = Integer.parseInt(binding.editTextNumberDecimal3.getText().toString());

            totalTimeCountInMilliseconds += secs * 1000;
        }

        binding.progressbar1Timerview.setMax((int) totalTimeCountInMilliseconds);
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + totalTimeCountInMilliseconds;
        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                int seconds = (int) (leftTimeInMilliseconds / 1000) % 60;
                int minutes = (int) ((leftTimeInMilliseconds / (1000 * 60)) % 60);
                int hours = (int) ((leftTimeInMilliseconds / (1000 * 60 * 60)) % 24);

                binding.progressbar1Timerview.setProgress((int) (leftTimeInMilliseconds));
                String m = twoDigitString(hours) + ":" + twoDigitString(minutes) + ":"
                        + twoDigitString(seconds);
                binding.textViewTimerviewTime.setText(m);
                binding.progressbar1Timerview.setVisibility(View.VISIBLE);

                //block edit textviews
                blockOnStart();

            }

            @Override
            public void onFinish() {
                layoutOnFinish();
                mTimerRunning = false;

                //notification for when timer stops on the notif bar
                notifyThis();
                //notification in fragment
                if ( getActivity() != null) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Timer is over", Snackbar.LENGTH_LONG).show();
                }

                //enable textviews again
                binding.textviewTimerviewBack.setEnabled(true);
                binding.editTextNumberDecimal2.setEnabled(true);
                binding.editTextNumberDecimal3.setEnabled(true);

                totalTimeCountInMilliseconds = 0;

            }
        }.start();
        mTimerRunning = true;
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        mTimerRunning = false;
        binding.buttonTimerviewStart.setText("Continue");

    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("miliesLeft", totalTimeCountInMilliseconds);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        totalTimeCountInMilliseconds = prefs.getLong("miliesLeft", totalTimeCountInMilliseconds);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            totalTimeCountInMilliseconds = mEndTime - System.currentTimeMillis();
            if (totalTimeCountInMilliseconds < 0) {
                totalTimeCountInMilliseconds = 0;
                mTimerRunning = false;
            } else {
                layoutOnStart();
                startTimer();
                binding.buttonTimerviewStop.setEnabled(true);
                binding.progressbar1Timerview.setVisibility(View.VISIBLE);
            }
        }

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

    public void layoutOnStop() {
        binding.progressbar1Timerview.setVisibility(View.GONE);
        binding.progressbarTimerview.setVisibility(View.VISIBLE);
        binding.buttonTimerviewStart.setVisibility(View.VISIBLE);
        binding.buttonTimerviewStop.setEnabled(false);
        binding.textviewTimerviewBack.setText("");
        binding.editTextNumberDecimal2.setText("");
        binding.editTextNumberDecimal3.setText("");
    }

    public void layoutOnStart() {
        binding.buttonTimerviewStart.setVisibility(View.VISIBLE);
        binding.buttonTimerviewStart.setText("Pause");
        binding.buttonTimerviewStop.setVisibility(View.VISIBLE);
        binding.progressbarTimerview.setVisibility(View.INVISIBLE);
    }

    public void blockOnStart() {
        //block edit textviews
        binding.textviewTimerviewBack.setEnabled(false);
        binding.editTextNumberDecimal2.setEnabled(false);
        binding.editTextNumberDecimal3.setEnabled(false);
    }

    public void layoutOnFinish() {
        binding.textViewTimerviewTime.setText("00:00:00");
        binding.textViewTimerviewTime.setVisibility(View.VISIBLE);
        binding.buttonTimerviewStart.setVisibility(View.VISIBLE);
        binding.buttonTimerviewStop.setVisibility(View.VISIBLE);
        binding.progressbarTimerview.setVisibility(View.VISIBLE);
        binding.progressbar1Timerview.setVisibility(View.GONE);
        binding.buttonTimerviewStart.setText("Start");
        binding.textviewTimerviewBack.setText("");
        binding.editTextNumberDecimal2.setText("");
        binding.editTextNumberDecimal3.setText("");
    }


    //notification for when timer ends
    public void notifyThis() {
        if (getContext() != null) {
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
                        .setContentTitle("TIMER")
                        .setContentText("Timer over")
                        .setAutoCancel(true)
                        .build();
                manager.notify(1, notification);
            }
            else
            {
                //When sdk version is less than26
                Notification notification = new NotificationCompat.Builder(getContext())
                        .setContentTitle("TIMER")
                        .setContentText("Timer over")
                        .setSmallIcon(R.drawable.ic_action_clock_dark)
                        .build();
                manager.notify(1,notification);
            }
        }

    }
}

