package com.sourav.story.Stuffs;

public class StoryData {
    private String uniqueID;
    private String time;
    private String Date;
    private String body;
    private long timestamp;

    public StoryData(String uniqueID, String time, String date, String body, long timestamp) {
        this.uniqueID = uniqueID;
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

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
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
