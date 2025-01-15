package com.webserva.wings.android.rubik;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class TimerFragment extends Fragment {

    private TextView timerTextView;
    private TextView shortestRecordTextView;
    private Button resumeButton;
    private Button setRecordButton;
    private Button resetButton;

    private Handler handler = new Handler();
    private long startTime;
    private long elapsedTime = 0;
    private long shortestTime = Long.MAX_VALUE;
    private boolean isRunning = false;
    private SharedPreferences sharedPreferences;

    private Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimerText(elapsedTime);
            handler.postDelayed(this, 10);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        timerTextView = view.findViewById(R.id.timerTextView);
        shortestRecordTextView = view.findViewById(R.id.shortestRecordTextView);
        resumeButton = view.findViewById(R.id.resumeButton);

        setRecordButton = view.findViewById(R.id.setRecordButton);
        resetButton = view.findViewById(R.id.resetButton);

        sharedPreferences = getActivity().getSharedPreferences("timer_prefs", Context.MODE_PRIVATE);
        shortestTime = sharedPreferences.getLong("shortestTime", Long.MAX_VALUE);

        updateTimerText(0);
        updateShortestRecordText(shortestTime);

        resumeButton.setOnClickListener(v -> {
            if (isRunning) {
                handler.removeCallbacks(updateTime);
                isRunning = false;
                if (elapsedTime != 0) {
                    setRecordButton.setVisibility(View.VISIBLE);
                }
                resumeButton.setText(getString(R.string.timer_start)); // ボタンのテキストを"Start"に変更
            } else {
                startTime = System.currentTimeMillis() - elapsedTime;
                handler.postDelayed(updateTime, 0);
                isRunning = true;
                setRecordButton.setVisibility(View.INVISIBLE);
                resumeButton.setText(getString(R.string.timer_end)); // ボタンのテキストを"Stop"に変更
            }
        });


        resetButton.setOnClickListener(v -> {
            handler.removeCallbacks(updateTime);
            elapsedTime = 0;
            updateTimerText(elapsedTime);
            isRunning = false;
            setRecordButton.setVisibility(View.INVISIBLE);
        });

        setRecordButton.setOnClickListener(v -> {
            shortestTime = elapsedTime;  // 条件を削除
            updateShortestRecordText(shortestTime);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("shortestTime", shortestTime);
            editor.apply();

            setRecordButton.setVisibility(View.INVISIBLE);
        });
        return view;
    }

    private void updateTimerText(long timeInMillis) {
        int minutes = (int) (timeInMillis / 60000);
        int seconds = (int) (timeInMillis % 60000 / 1000);
        int millis = (int) (timeInMillis % 1000 / 10);
        timerTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, millis));
    }

    private void updateShortestRecordText(long timeInMillis) {
        if (timeInMillis == Long.MAX_VALUE) {
            shortestRecordTextView.setText("00:00:00");
        } else {
            int minutes = (int) (timeInMillis / 60000);
            int seconds = (int) (timeInMillis % 60000 / 1000);
            int millis = (int) (timeInMillis % 1000 / 10);
            shortestRecordTextView.setText(String.format("%02d:%02d:%02d", minutes, seconds, millis));
        }
    }
}
