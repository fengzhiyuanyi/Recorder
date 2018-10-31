package com.netease.testease;

public class RecordingStatus {
    private static volatile RecordingStatus instance;
    private boolean recordStatus = false;

    private RecordingStatus() {
    }

    public static RecordingStatus getInstance(){
        if(instance == null){
            synchronized (RecordingStatus.class){
                if(instance == null){
                    instance = new RecordingStatus();
                }
            }
        }
        return instance;
    }

    public boolean isRecordOpen() {
        return recordStatus;
    }

    public void setRecordStatus(boolean recordStatus) {
        this.recordStatus = recordStatus;
    }



}
