package com.example.a41p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    Button startButton, pauseButton, stopButton;
    EditText enterTask;
    TextView timerView, previousView;
    Timer timer;
    TimerTask timerTask;
    double time = 0.0;
    boolean running;
    public String TASK_PREFS = "shared";
    public String TEXTKEY = "text";
    public String TIMEKEY = "time";
    public String task="";
    public String prevTask;
    public double prevTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        enterTask = findViewById(R.id.enterTask);
        timer = new Timer();
        timerView = findViewById(R.id.timerView);
        previousView = findViewById(R.id.previousView);
        taskGet();

        String prevTimeAsText = returnTime(prevTime);
        previousView.setText("You spent "+prevTimeAsText+" on "+prevTask+" last time.");


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View screen) {
                if(!running)
                {
                    startTimerActivity();
                }
                taskSave();
                running=true;
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View screen) {
                pauseTimerActivity();
                running=false;
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View screen) {
                stopTimerActivity();
                running=false;
            }
        });
    }

    public void startTimerActivity(){
        timerTask = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        time++;
                        timerView.setText(returnTime(time));
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000 ,1000);
    }
    public void pauseTimerActivity(){
        if(running)
        {
            timerTask.cancel();
        }
    }

    public void stopTimerActivity(){
        if(running)
        {
            timerTask.cancel();
        }
        task = enterTask.getText().toString();
        taskSave();
    }

    public void taskSave(){
        SharedPreferences sharedPreferences = getSharedPreferences(TASK_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXTKEY, task);
        editor.putLong(TIMEKEY, (Long)Double.doubleToRawLongBits(time));
        editor.apply();
    }

    public void taskGet(){
        SharedPreferences sharedPreferences = getSharedPreferences(TASK_PREFS, MODE_PRIVATE);
        prevTask = sharedPreferences.getString(TEXTKEY,"this task").toString();
        prevTime = Double.longBitsToDouble(sharedPreferences.getLong(TIMEKEY, 0));
    }

    public String returnTime(double timeIn){
        String output;

        int hrs = (int)(timeIn/60)/60;
        int min = (int)(timeIn/60);
        int sec = (int)timeIn;
        String col = ":";
        String dot = ".";
        String zer = "0";
        if(sec<10){
            dot = ".0";
        }
        if(min<10)
        {
            col = ":0";
        }
        if(hrs>0)
        {
            zer="";
        }
        output = (zer+hrs+col+min%60+dot+sec%60);
        return output;
    }
}