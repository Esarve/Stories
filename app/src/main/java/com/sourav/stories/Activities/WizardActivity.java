package com.sourav.stories.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sourav.stories.R;
import com.sourav.stories.Stuffs.Constants;
import com.sourav.stories.Stuffs.Tools;

public class WizardActivity extends AppCompatActivity {
    private EditText editText;
    private Button done;
    private String name;
    private Tools tools = new Tools();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wizard);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        initToolbar();

        editText = findViewById(R.id.etName);
        done = findViewById(R.id.btnSetupDone);
        done.setOnClickListener(v -> {
            if (isValidName()) saveName();
        });

    }
    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void initToolbar() {
        tools.setSystemBarColor(this, R.color.colorAccentLight);
        tools.setSystemBarLight(this);
    }

    private void saveName() {
        tools.saveToSharedPref(this, Constants.PREFTYPE_NAME, Constants.USERNAME, name);
        finish();
    }

    private boolean isValidName() {
        boolean result = false;
        name = editText.getText().toString();
        if (name.matches(".*\\d.*")){
            new Tools().errorToast(this,"Please enter a valid name");;
        }else {
            name = editText.getText().toString();
            result = true;
        }
        return result;
    }
}