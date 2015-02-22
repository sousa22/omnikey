package com.sec.omnium.omnikey.akts;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.sec.omnium.omnikey.R;
import com.sec.omnium.omnikey.UI.SetScreens;


public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SetScreens.SetLoginScreen task = new SetScreens.SetLoginScreen(this, getWindow().getDecorView().getRootView());
        task.execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}


