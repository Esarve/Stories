package com.sourav.stories.Activities;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sourav.stories.Interfaces.OnSuccessListener;
import com.sourav.stories.R;
import com.sourav.stories.Stuffs.Constants;
import com.sourav.stories.Stuffs.ListStoryData;
import com.sourav.stories.Stuffs.RealmEngine;
import com.sourav.stories.Stuffs.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class BackupRestoreActivity extends AppCompatActivity implements OnSuccessListener {
    private static final String TAG = "BackupRestoreActivity";
    private Tools tools = new Tools();
    private TextView textView;
    private RealmEngine realmEngine = RealmEngine.getInstance();
    private ListStoryData listStoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        initData();
        initview();
    }

    private void initData() {
        listStoryData = new ListStoryData(realmEngine.toList());
    }

    private void initview() {
        textView = findViewById(R.id.lastbackup);
        tools.setSystemBarColor(this, R.color.grey_5);
        tools.setSystemBarLight(this);
        tools.setNavigationBarColor(getWindow().getDecorView(),this, R.color.grey_3,true);

        Button btnBackup = findViewById(R.id.btnBackup);
        Button btnRestore = findViewById(R.id.btnRestore);

        btnBackup.setOnClickListener(v -> {
            if (realmEngine.toList().size() > 0){
                createBackup();
            }else tools.errorToast(getApplicationContext(), "You dont have any data to backup");

        });

        btnRestore.setOnClickListener(v -> {
            restore();
        });

        realmEngine.setListener(BackupRestoreActivity.this);
    }

    public void createBackup() {
        //Write into external Storage
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(listStoryData, ListStoryData.class);
        Log.d(TAG, "createBackup: "+ json);
        File dir = new File(Environment.getExternalStorageDirectory(), Constants.BACKUPDIR);
        dir.mkdir();
        File file = new File(dir, Constants.BACKUPNAME);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
            tools.successToast(this, "Backup Successful");
        } catch (Exception e) {
            e.printStackTrace();
            /*FirebaseCrashlytics.getInstance().log("WHILE BACKING UP");
            FirebaseCrashlytics.getInstance().recordException(e);*/
            tools.errorToast(this,"Backup Failed");
        }
        textView.setText("Created");
    }

    private void restore(){
        File dir = new File(Environment.getExternalStorageDirectory(), Constants.BACKUPDIR);
        File file = new File(dir, Constants.BACKUPNAME);

        if (dir.exists()){
            FileOutputStream outputStream = null;
            StringBuilder text = new StringBuilder();
            String read;
            try {
                BufferedReader  bufferedReader = new BufferedReader(new FileReader(file));
                while ((read = bufferedReader.readLine())  != null) {
                    text.append(read);
                    text.append('\n');
                }
                bufferedReader.close();
                Log.d(TAG, "restore: "+ text.toString());
            }catch (IOException e){
                e.printStackTrace();
                /*FirebaseCrashlytics.getInstance().log("IN RESTORE TRY CATCH");
                FirebaseCrashlytics.getInstance().recordException(e);*/
                tools.errorToast(this,"File not found!");
            }
            realmEngine.restoreRealm(text.toString());

        }else tools.errorToast(this, "Directory Does not exist");
    }

    @Override
    public void onRestoreSuccess() {
        tools.successToast(this, "Restored Successfully");
    }
}