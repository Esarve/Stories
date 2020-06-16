package com.sourav.stories.Stuffs;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class StoryData extends RealmObject {
    @PrimaryKey
    private String uniqueID;
    private String time;
    private String Date;
    private String body;
    private long timestamp;

    public StoryData() {
    }

    public StoryData(String time, String date, String body, long timestamp, String uid) {
        this.time = time;
        Date = date;
        this.body = body;
        this.timestamp = timestamp;
        uniqueID = uid;
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

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }
}
