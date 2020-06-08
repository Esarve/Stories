package com.sourav.story.Stuffs;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class StoryData extends RealmObject {
    private String uniqueID;
    private String time;
    private String Date;
    private String body;
    @PrimaryKey
    private long timestamp;

    public StoryData() {
    }

    public StoryData(String time, String date, String body, long timestamp) {
        this.time = time;
        Date = date;
        this.body = body;
        this.timestamp = timestamp;
    }

    public StoryData(String time, String date, String body) {
        this.time = time;
        Date = date;
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
