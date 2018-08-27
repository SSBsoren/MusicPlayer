package com.soren.sagen.sounddemo;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private Button play,pause,forward,backward;
    private ImageView img;
    private MediaPlayer mPlayer;


    private double startTime=0;
    private double finalTime=0;

    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView startTv,titleTv,endTv;

    public static int oneTimeOnly = 0;


    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play=findViewById(R.id.playBtn);
        pause=findViewById(R.id.pauseBtn);
        forward=findViewById(R.id.forwardBtn);
        backward=findViewById(R.id.backwardBtn);

        startTv=findViewById(R.id.startTime_tv);
        titleTv=findViewById(R.id.title_tv);
        endTv=findViewById(R.id.finalTime_tv);


        mPlayer = MediaPlayer.create(this, R.raw.twentyfour);
        seekbar =findViewById(R.id.seek_bar);
        seekbar.setClickable(false);
        pause.setEnabled(false);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Playing music", Toast.LENGTH_SHORT).show();
                mPlayer.start();


                finalTime = mPlayer.getDuration();
                startTime = mPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                startTv.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );

                endTv.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );

              seekbar.setProgress((int)startTime);
              myHandler.postDelayed(UpdateSongTime,100);
              play.setEnabled(false);
              pause.setEnabled(true);

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Pausing music",Toast.LENGTH_SHORT).show();
                mPlayer.pause();
                pause.setEnabled(false);
                play.setEnabled(true);
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=(int)startTime;

                if ((temp+forwardTime)<=finalTime){
                    mPlayer.seekTo((int)startTime);
                    Toast.makeText(getApplicationContext(),"You have jumped forword 5 seconds",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Cannot jump forword 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp=(int)startTime;

                if ((temp-backwardTime)>0){
                    startTime=startTime-backwardTime;
                    mPlayer.seekTo((int)startTime);
                    Toast.makeText(getApplicationContext(),"You have Jumped backward 5 seconds ",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getApplicationContext(),"Cannot  Jumped backward 5 seconds ",Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private Runnable UpdateSongTime=new Runnable() {
        @Override
        public void run() {
            startTime=mPlayer.getCurrentPosition();
            startTv.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this,100);
        }
    };
}