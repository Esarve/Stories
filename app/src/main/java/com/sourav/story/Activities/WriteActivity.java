package com.sourav.story.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sourav.story.R;
import com.sourav.story.Stuffs.RealmEngine;
import com.sourav.story.Stuffs.Tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TIME = "HH:mm a";
    private static final String DATE = "MMMM dd";
    private static final String DATEWITHDAY = "MMMM dd, EEEE";
    private LinearLayout clickTimedate, clickCancel;
    private FloatingActionButton fab;
    private TextView displayTime, displayDate;
    private EditText editText;
    private Tools tools = Tools.getInstance();
    private String time, date, body;
    private int pos;
    private String TAG = "Write";

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
            time = getIntent().getExtras().getString(Tools.TIME, "-");
            date = getIntent().getExtras().getString(Tools.DATE, "-");
            body = getIntent().getExtras().getString(Tools.BODY,"-");
            pos = getIntent().getExtras().getInt(Tools.POSITION,0);
        }
    }

    private void initView() {
        clickTimedate = findViewById(R.id.editTimeDate);
        clickCancel = findViewById(R.id.editCancel);
        fab = findViewById(R.id.add_button);
        editText = findViewById(R.id.etWrite);
        displayTime = findViewById(R.id.tvTime);
        displayDate = findViewById(R.id.tvDate);

        clickTimedate.setOnClickListener(this);
        clickCancel.setOnClickListener(this);
        fab.setOnClickListener(v -> {
            writeData();
            Toast.makeText(this,"Data Added", Toast.LENGTH_SHORT).show();
            closeKeyboard();
        });

        tools.setSystemBarColor(this, R.color.grey_5);
        tools.setSystemBarLight(this);
        tools.setNavigationBarColor(getWindow().getDecorView(),this, R.color.grey_3,true);

        displayDate.setText(getCurrentTimeDate(DATEWITHDAY));
        displayTime.setText(getCurrentTimeDate(TIME));
/*
        Log.d(TAG, "initView: Current Time "+ getCurrentTimeDate(TIME));
        Log.d(TAG, "initView: Current date "+ getCurrentTimeDate(DATE));*/

    }

    private void writeData() {
        RealmEngine realmEngine = RealmEngine.getInstance();
        String body = editText.getText().toString();
        String time = getCurrentTimeDate(TIME);
        String date = getCurrentTimeDate(DATE);
        realmEngine.insertData(time,date,body);

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
            case R.id.editTimeDate:
                Toast.makeText(this, "TimeDate Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.editCancel:
                Toast.makeText(this,"Cancel CLicked",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
