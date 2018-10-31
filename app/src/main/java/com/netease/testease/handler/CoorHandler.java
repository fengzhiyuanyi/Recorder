package com.netease.testease.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.netease.testease.MainActivity;
import com.netease.testease.RecordingStatus;
import com.netease.testease.view.FloatView;

public class CoorHandler extends Handler {
    private final String TAG = "Handler";
    private FloatView mLayout;
    private MainActivity mActivity;

    public CoorHandler(FloatView mLayout, MainActivity mActivity){
        this.mLayout = mLayout;
        this.mActivity = mActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle bundle;
        String data;
        String[] coor;
        boolean isShown = true;
        if (this.mLayout == null || !this.mLayout.isShown()){
            isShown = this.mActivity.showFloatView();
        }
        switch (msg.what){
            case 0:
                bundle = msg.getData();
                data = bundle.getString("data");
                assert data != null;
                coor = data.substring(1, data.length() - 1).split(", ");
                if (coor.length == 5 && isShown){
                    int fX = Integer.parseInt(coor[0]);
                    int fY = Integer.parseInt(coor[1]);
                    int tX = Integer.parseInt(coor[2]);
                    int tY = Integer.parseInt(coor[3]);
                    mLayout.setVisibility(View.VISIBLE);
                    mLayout.updateView(fX, fY, tX, tY);
                    Message message = new Message();
                    message.what = 1;
                    sendMessageDelayed(message, 600);
                }else if ((coor.length == 3 || coor.length == 2) && isShown){
                    int x = (int)Float.parseFloat(coor[0]);
                    int y = (int)Float.parseFloat(coor[1]);
                    mLayout.setVisibility(View.VISIBLE);
                    mLayout.updateView(x, y, x, y);
                    Message message = new Message();
                    message.what = 1;
                    sendMessageDelayed(message, 600);
                }else if (coor.length == 1){
                    if(coor[0].contains("start_record")){
                        System.out.println("Begin Recording");
                        if (mActivity != null){
                            mActivity.startRecord();
                            RecordingStatus.getInstance().setRecordStatus(true);
                        }else {
                            Log.i(TAG, "activity is down, start recording failed!");
                        }
                    }else if(coor[0].contains("stop_record")){
                        System.out.println("Stop Recording");
                        if (mActivity != null){
                            mActivity.stopRecord();
                            RecordingStatus.getInstance().setRecordStatus(false);
                        }else {
                            Log.i(TAG, "activity is down, stop recording failed!");
                        }
                    }
                }
                break;
            case 1:
                mLayout.clearView();
                break;
        }
    }

    public void updateFloatView(FloatView mLayout){
        this.mLayout = mLayout;
    }
}
