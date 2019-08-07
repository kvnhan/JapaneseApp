package com.example.newjapaneseapp;

public class ActivityMemory {
    private static final ActivityMemory ourInstance = new ActivityMemory();
    private static MyActivity currentActivity;

    public static ActivityMemory getInstance() {
        return ourInstance;
    }

    private ActivityMemory() {
    }

    public static MyActivity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(MyActivity currentActivity) {
        ActivityMemory.currentActivity = currentActivity;
    }
}
