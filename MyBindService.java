package service.imooc.lenovo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Lenovo on 2016/10/9.
 */

public class MyBindService extends Service{
    @Override
    public void onCreate() {
        System.out.println("onCreate");
        super.onCreate();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind");
        return new MyBinder();
    }

    public class MyBinder extends Binder{
        public MyBindService getService(){
            return MyBindService.this;
        }
    }

    public void play(){
        System.out.println("play");
    }

    public void pause(){
        System.out.println("pause");
    }

    public void previous(){
        System.out.println("previous");
    }

    public void next(){
        System.out.println("next");
    }
}
