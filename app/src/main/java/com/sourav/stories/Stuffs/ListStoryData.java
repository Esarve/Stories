package com.sourav.stories.Stuffs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListStoryData {
    @SerializedName("results")
    @Expose
    private List<StoryData> results;

    public ListStoryData(List<StoryData> results) {
        this.results = results;
    }

    public List<StoryData> getResults() {
        return results;
    }

    public void setResults(List<StoryData> results) {
        this.results = results;
    }
}
