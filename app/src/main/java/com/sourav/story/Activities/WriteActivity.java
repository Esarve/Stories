package com.sourav.story.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sourav.story.R;
import com.sourav.story.Stuffs.Tools;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout clickTimedate, clickCancel;
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
        initData();
    }

    private void initData() {
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

        clickTimedate.setOnClickListener(this);
        clickCancel.setOnClickListener(this);

        tools.setSystemBarColor(this, R.color.grey_5);
        tools.setSystemBarLight(this);
        tools.setNavigationBarColor(getWindow().getDecorView(),this, R.color.grey_3,true);

    }

    private void createLog(String text){
        Log.d(TAG, "createLog: found "+ text);
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
