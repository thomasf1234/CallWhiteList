package com.abstractx1.callwhitelist;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void log(String message) {
        Log.v(getLogKey(), message);
    }

    public static String getLogKey() {
        return "DEBUG";
    }
}
