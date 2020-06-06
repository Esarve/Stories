package com.sourav.story.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sourav.story.R;
import com.sourav.story.Stuffs.Tools;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout clickTimedate, clickCancel;
    private Tools tools = Tools.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        initView();
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
