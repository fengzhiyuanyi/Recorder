package com.netease.testease.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.netease.testease.MainActivity;
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
        if (this.mLayout == null || !this.mLayout.isShown()){
            this.mActivity.showFloatView();
        }
        switch (msg.what){
            case 0:
                bundle = msg.getData();
                data = bundle.getString("coor");
                coor = data.substring(1, data.length() - 1).split(", ");
                if (coor.length == 5){
                    int fX = Integer.parseInt(coor[0]);
                    int fY = Integer.parseInt(coor[1]);
                    int tX = Integer.parseInt(coor[2]);
                    int tY = Integer.parseInt(coor[3]);
                    mLayout.setVisibility(View.VISIBLE);
                    mLayout.updateView(fX, fY, tX, tY);
                    Message message = new Message();
                    message.what = 1;
                    sendMessageDelayed(message, 600);
                }else if (coor.length == 3 || coor.length == 2){
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
                            mActivity.stratRecord();
                        }else {
                            Log.i(TAG, "service is down, start recording failed!");
                        }
                    }else if(coor[0].contains("stop_record")){
                        System.out.println("Stop Recording");
                        if (mActivity != null){
                            mActivity.stopRecord();
                        }else {
                            Log.i(TAG, "service is down, start recording failed!");
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
