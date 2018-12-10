package com.netease.testease;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LifeService extends Service {

    private final IBinder mBinder = new LifeBinder();
    private final Timer timerMail = new Timer();
    private ActivityManager activityManager=null;
    private Handler mHandler;

    public class LifeBinder extends Binder {
        LifeService getService() {
            return LifeService.this;
        }
    }

    public void setRecord(ScreenRecorder mRecorder){
        mHandler = new LifeHandle(mRecorder);
        timerMail.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                Log.i("MainActivity===", "sendMessage");
                mHandler.sendMessage(message);
            }
        }, 15000, 5000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("MainActivity===", "bindService");
        return mBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Log.i("MainActivity===", "unbindService");
    }

    private boolean isBackgroundRunning() {
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningTaskInfo> processList = activityManager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : processList) {
            if (info.baseActivity.getPackageName().startsWith("com.easetest.recorder")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerMail.cancel();
//        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    class LifeHandle extends Handler{
        private ScreenRecorder mRecorder;
        LifeHandle(ScreenRecorder mRecorder){
            this.mRecorder = mRecorder;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(!isBackgroundRunning() && mRecorder != null){
                    mRecorder.quit1();
                    mRecorder = null;
                    Log.i("MainActivity===", "stopRecord handler");
                }
            }catch (Exception ignored){

            }
        }
    }
}
