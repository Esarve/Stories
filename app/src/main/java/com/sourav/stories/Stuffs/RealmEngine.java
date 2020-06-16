package com.sourav.stories.Stuffs;

import java.util.Objects;
import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmEngine {

    public static RealmEngine instance;
    private Realm realm;

    public static RealmEngine getInstance() {
        if (instance == null){
            instance = new RealmEngine();
        }
        return instance;
    }

    public void initRealm(){
        RealmConfiguration newConfig = new RealmConfiguration.Builder()
                .name("storiesRealm")
                .schemaVersion(1)
                .build();

        realm = Realm.getInstance(newConfig);
    }

    public void insertData(String time, String date, String body){
        realm.executeTransactionAsync(realm -> {
            StoryData story = realm.createObject(StoryData.class, generateUUID());
            story.setTimestamp(System.currentTimeMillis());
            story.setTime(time);
            story.setDate(date);
            story.setBody(body);
        });
    }

    public void deleteData(String uid) {
        realm.executeTransactionAsync(realm -> {
            RealmResults<StoryData> realmResults = realm.where(StoryData.class).equalTo("uniqueID", uid).findAll();
            realmResults.deleteAllFromRealm();
        });
    }

    public StoryData getSpecificData(String uid) {
        return realm.copyFromRealm(Objects.requireNonNull(
                realm.where(StoryData.class).equalTo("uniqueID", uid).findFirst()));
    }

    public void addSpecificStory(StoryData deletedStory) {
        realm.executeTransactionAsync(realm -> {
            realm.insert(deletedStory);
        });
    }

    public RealmResults<StoryData> getSearchResults(){
        return realm.where(StoryData.class).sort("timestamp", Sort.DESCENDING).findAll();
    }

    public RealmResults<StoryData> getSearchResults(String query){
        return realm.where(StoryData.class)
                .contains("body", query, Case.INSENSITIVE)
                .or()
                .contains("Date", query,Case.INSENSITIVE)
                .sort("timestamp", Sort.DESCENDING).findAll();
    }

    private String generateUUID(){
        return UUID.randomUUID().toString();
    }

}
