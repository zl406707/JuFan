package com.example.admin.jufan;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
private ImageView img;
    private TextView time_tv;
    private Timer timer;
private int i=3;

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {

            int j = (Integer) msg.obj;

            time_tv.setText("还剩" + j + "秒");

            if(j==0){
                Intent intent=new Intent(MainActivity.this,HomepageActivity.class);
                startActivity(intent);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img=(ImageView) findViewById(R.id.imgdaohang);
        time_tv =(TextView) findViewById(R.id.time_tv);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.obj = i;
                handler.sendMessage(message);
                i--;
            }
        }, 0, 1000);





    }
}
