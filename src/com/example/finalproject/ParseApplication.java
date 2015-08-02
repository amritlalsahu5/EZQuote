package com.example.finalproject;


/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */
import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialize Crash Reporting.
    ParseCrashReporting.enable(this);

    // Enable Local Datastore.
    //Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(this, "RiaPXuUC1AXIqGoCzfUox3vD7vcuHXDVbVaym1MP", "FNJwWIWX9xxqZowd3rNADlXnkLvcruEOW5M2GzBS");
    //Parse.initialize(this);


    //ParseUser.enableAutomaticUser();
    //ParseACL defaultACL = ParseUser.getCurrentUser().getACL();//new ParseACL();
    // Optionally enable public read access.
    // defaultACL.setPublicReadAccess(true);
    //ParseACL.setDefaultACL(defaultACL, true);
  }
}
