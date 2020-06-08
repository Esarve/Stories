package com.sourav.story.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sourav.story.R;
import com.sourav.story.Stuffs.RealmEngine;
import com.sourav.story.Stuffs.StoryData;
import com.sourav.story.Stuffs.Tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton clickTimedate, clickCancel;
    private FloatingActionButton fab;
    private TextView displayTime, displayDate;
    private EditText editText;
    private Tools tools = Tools.getInstance();
    private String time, date, body;
    private int pos;
    private long timestamp;
    private String TAG = "Write";
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        fetchData();
        initView();
        initDataIfEdit();
    }

    private void initDataIfEdit() {
        createLog(time);
        createLog(date);
        createLog(body);
        createLog(String.valueOf(pos));
    }

    private void fetchData() {
        if (getIntent().getExtras()!=null){
            isEditMode = true;
            time = getIntent().getExtras().getString(Tools.TIME);
            date = getIntent().getExtras().getString(Tools.DATE);
            body = getIntent().getExtras().getString(Tools.BODY);
            timestamp = getIntent().getExtras().getLong(Tools.TIMESTAMP);
            pos = getIntent().getExtras().getInt(Tools.POSITION);
        }
    }

    private void initView() {
        clickTimedate = findViewById(R.id.btnTimeDate);
        clickCancel = findViewById(R.id.btnCancel);
        fab = findViewById(R.id.add_button);
        editText = findViewById(R.id.etWrite);
        displayTime = findViewById(R.id.tvTime);
        displayDate = findViewById(R.id.tvDate);

        clickTimedate.setOnClickListener(this);
        clickCancel.setOnClickListener(this);
        fab.setOnClickListener(v -> {
            writeData();
            closeKeyboard();
        });

        tools.setSystemBarColor(this, R.color.grey_5);
        tools.setSystemBarLight(this);
        tools.setNavigationBarColor(getWindow().getDecorView(),this, R.color.grey_3,true);

        if (isEditMode) {
            String fullDate = date + ", " + getCurrentTimeDate(Tools.WEEKDAY_FORMAT);
            displayDate.setText(fullDate);
            displayTime.setText(time);
            editText.setText(body);
        } else {
            displayDate.setText(getCurrentTimeDate(Tools.DATEWITHDAY_FORMAT));
            displayTime.setText(getCurrentTimeDate(Tools.TIME_FORMAT));
        }
    }

    private void writeData() {
        RealmEngine realmEngine = RealmEngine.getInstance();
        String bodyFinal = editText.getText().toString();
        if (!bodyFinal.isEmpty()) {
            if (!isEditMode) {
                String time = getCurrentTimeDate(Tools.TIME_FORMAT);
                String date = getCurrentTimeDate(Tools.DATE_FORMAT);
                realmEngine.insertData(time, date, bodyFinal);
            } else {
                realmEngine.deleteData(timestamp);
                realmEngine.addSpecificStory(
                        new StoryData(
                                time, date, bodyFinal, timestamp
                        )
                );
            }
            Log.d(TAG, "writeData: Modified Millis: " + Tools.generateMillis(date, time));
        } else {
            String message = "Text field is empty";
            tools.errorToast(this, message);
        }

    }

    private String getCurrentTimeDate(String timeOrDate) {
        DateFormat dateFormat = new SimpleDateFormat(timeOrDate, Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void createLog(String text){
        Log.d(TAG, "createLog: found "+ text);
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTimeDate:
                Toast.makeText(this, "TimeDate Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnCancel:
                finish();
                break;
            default:
                break;
        }
    }
}
