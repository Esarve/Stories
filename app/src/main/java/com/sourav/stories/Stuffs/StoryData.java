package com.sourav.stories.Stuffs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class StoryData extends RealmObject {
    @Expose
    @SerializedName("uniqueID")
    @PrimaryKey
    private String uniqueID;

    @Expose
    @SerializedName("time")
    private String time;

    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("body")
    private String body;

    @Expose
    @SerializedName("timestamp")
    private long timestamp;

    public StoryData() {
    }

    public StoryData(String time, String date, String body, long timestamp, String uid) {
        this.time = time;
        this.date = date;
        this.body = body;
        this.timestamp = timestamp;
        uniqueID = uid;
    }

    public StoryData(String time, String date, String body) {
        this.time = time;
        this.date = date;
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
