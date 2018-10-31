package com.netease.testease.http;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.netease.testease.RecordingStatus;

import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class CoordinateServer extends NanoHTTPD {

    private Handler mHandler;

    public CoordinateServer(int port, Handler mHandler) {
        super(port);
        this.mHandler = mHandler;
    }

    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> headers, Map<String, String> params,
                          Map<String, String> files) {
        String data = files.get("postData");
        Message message = new Message();
        message.what = 0;
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        message.setData(bundle);
        mHandler.sendMessage(message);
        if(data.contains("start_record")){
            return recordResponse(true);
        }else if (data.contains("stop_record")){
            return recordResponse(false);
        }else {
            return newFixedLengthResponse("success");
        }

    }

    private Response recordResponse(boolean b) {
        for (int i = 0;i < 8;i++) {
            if (RecordingStatus.getInstance().isRecordOpen() == b) {
                return newFixedLengthResponse("success");
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return newFixedLengthResponse("failure");
    }
}
