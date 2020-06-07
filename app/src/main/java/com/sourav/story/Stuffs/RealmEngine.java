package com.sourav.story.Stuffs;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

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
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                StoryData story = realm.createObject(StoryData.class, System.currentTimeMillis());
                story.setTime(time);
                story.setDate(date);
                story.setBody(body);
            }
        });
    }

    public void deleteData(long timestamp) {
        realm.executeTransaction(realm -> {
            RealmResults<StoryData> realmResults = realm.where(StoryData.class).equalTo("timestamp", timestamp).findAll();
            realmResults.deleteAllFromRealm();
        });
    }


    public RealmResults<StoryData> getResults(){
        return realm.where(StoryData.class).findAll();
    }

}
