package me.parkerwilliams.periscope.example;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by parker on 9/16/15.
 */
public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Check to make sure the activity is not leaking.
        LeakCanary.install(this);
    }
}
