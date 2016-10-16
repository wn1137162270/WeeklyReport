package service.imooc.lenovo.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    Button startService;
    Button stopService;
    Button bindService;
    Button unbindService;
    Button musicPlay;
    Button musicPause;
    Button musicPrevious;
    Button musicNext;
    Intent intent1;
    Intent intent2;
    MyBindService myBindService;
    ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBindService= ((MyBindService.MyBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent1=new Intent(MainActivity.this,MyStartService.class);
        //Intent intent2=new Intent(MainActivity.this,MyBindService.class);

        startService= (Button) findViewById(R.id.start_service_btn);
        stopService= (Button) findViewById(R.id.stop_service_btn);
        bindService= (Button) findViewById(R.id.bind_service_btn);
        unbindService= (Button) findViewById(R.id.unbind_service_btn);
        musicPlay= (Button) findViewById(R.id.play_btn);
        musicPause= (Button) findViewById(R.id.pause_btn);
        musicPrevious= (Button) findViewById(R.id.previous_btn);
        musicNext= (Button) findViewById(R.id.next_btn);

        startService.setOnClickListener(new MyClickListener());
        stopService.setOnClickListener(new MyClickListener());
        bindService.setOnClickListener(new MyClickListener());
        unbindService.setOnClickListener(new MyClickListener());
        musicPlay.setOnClickListener(new MyClickListener());
        musicPause.setOnClickListener(new MyClickListener());
        musicPrevious.setOnClickListener(new MyClickListener());
        musicNext.setOnClickListener(new MyClickListener());
    }

    public class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start_service_btn:
                    intent1=new Intent(MainActivity.this,MyStartService.class);
                    startService(intent1);
                    break;
                case R.id.stop_service_btn:
                    stopService(intent1);
                    break;
                case R.id.bind_service_btn:
                    intent2=new Intent(MainActivity.this,MyBindService.class);
                    startService(intent2);
                    bindService(intent2,conn,BIND_AUTO_CREATE);
                    break;
                case R.id.unbind_service_btn:
                    unbindService(conn);
                    break;
                case R.id.play_btn:
                    myBindService.play();
                    break;
                case R.id.pause_btn:
                    myBindService.pause();
                    break;
                case R.id.previous_btn:
                    myBindService.previous();
                    break;
                case R.id.next_btn:
                    myBindService.next();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(conn);
        stopService(intent2);
        super.onDestroy();
    }
}