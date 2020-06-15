package com.sourav.story.Stuffs;

import java.util.Objects;

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
            StoryData story = realm.createObject(StoryData.class, System.currentTimeMillis());
            story.setTime(time);
            story.setDate(date);
            story.setBody(body);
        });
    }

    public void deleteData(long timestamp) {
        realm.executeTransactionAsync(realm -> {
            RealmResults<StoryData> realmResults = realm.where(StoryData.class).equalTo("timestamp", timestamp).findAll();
            realmResults.deleteAllFromRealm();
        });
    }

    public StoryData getSpecificData(long timestamp) {
        return realm.copyFromRealm(Objects.requireNonNull(
                realm.where(StoryData.class).equalTo("timestamp", timestamp).findFirst()));
    }

    public void addSpecificStory(StoryData deletedStory) {
        realm.executeTransactionAsync(realm -> {
            realm.insert(deletedStory);
        });
    }

    public RealmResults<StoryData> getResults(){
        return realm.where(StoryData.class).sort("timestamp", Sort.DESCENDING).findAll();
    }

}
