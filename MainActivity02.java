package service.imooc.lenovo.service;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.Provider;

public class MainActivity extends Activity {
    Button startService;
    Button stopService;
    Button bindService;
    Button unbindService;
    Button musicPlay;
    Button musicPause;
    Button musicPrevious;
    Button musicNext;
    MyBindService service;
    ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            service= (MyBindService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        startService.findViewById(R.id.start_service_btn);
        stopService.findViewById(R.id.stop_service_btn);
        bindService.findViewById(R.id.bind_service_btn);
        unbindService.findViewById(R.id.unbind_service_btn);
        musicPlay.findViewById(R.id.play_btn);
        musicPause.findViewById(R.id.pause_btn);
        musicPrevious.findViewById(R.id.previous_btn);
        musicNext.findViewById(R.id.next_btn);

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
        Intent intent1=new Intent(MainActivity.this,MyStartService.class);
        Intent intent2=new Intent(MainActivity.this,MyStartService.class);

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start_service_btn:
                    startService(intent1);
                    break;
                case R.id.stop_service_btn:
                    stopService(intent1);
                    break;
                case R.id.bind_service_btn:
                    bindService(intent2,null,BIND_AUTO_CREATE);
                    break;
                case R.id.unbind_service_btn:
                    unbindService(null);
                    break;
                case R.id.play_btn:
                    service.play();
                    break;
                case R.id.pause_btn:
                    service.pause();
                    break;
                case R.id.previous_btn:
                    service.previous();
                    break;
                case R.id.next_btn:
                    service.next();
                    break;
            }
        }
    }
}
