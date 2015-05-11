package com.github.davidmihola.brandingexampleproject;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by dmta on 11.05.15.
 */
public class BrandingExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // see here: https://www.parse.com/apps/quickstart?onboard=#parse_push/android/existing
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));

        // don't be fooled by the Parse documentation: they would have you believe that you now to do this:
        // ParseInstallation.getCurrentInstallation().saveInBackground();

        // but in my experience this will not lead to your app receiving pushes - what is needed instead is this:
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
