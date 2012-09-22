package com.octo.android.robodemo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class RoboDemo {

    public static final String SHARED_PREFERENCE_NAME = "default";
    public static final String BUNDLE_KEY_DEMO_ACTIVITY_ARRAY_LIST_POINTS = "BUNDLE_KEY_DEMO_ARRAY_LIST_POINTS";
    public static final String BUNDLE_KEY_DEMO_ACTIVITY_ID = "BUNDLE_KEY_DEMO_ACTIVITY_ID";

    /**
     * Prepares an intent for a DemoActivity.
     * 
     * @param intent
     *            the intent to be used to launch the sublcass of {@link DemoActivity}.
     * @param demoActivityId
     *            the id that will be used to store the information about the 'never show again' checkbox.
     * @param listPoints
     *            an {@link ArrayList} of {@link LabeledPoint} to Display.
     */
    public static void prepareDemoActivityIntent( Intent intent, String demoActivityId, ArrayList< LabeledPoint > listPoints ) {
        intent.putExtra( BUNDLE_KEY_DEMO_ACTIVITY_ID, demoActivityId );
        intent.putParcelableArrayListExtra( BUNDLE_KEY_DEMO_ACTIVITY_ARRAY_LIST_POINTS, listPoints );
    }

    /**
     * Allows to check if a demo activity has been set never to display again.
     * 
     * @param caller
     *            the activity that is calling the {@link DemoActivity}. Its {@link SharedPreferences} will be used
     *            internally by the {@link DemoActivity}.
     * @param demoActivityId
     *            the id that will be used to store the information about the 'never show again' checkbox.
     */
    public static boolean isNeverShowAgain( Activity caller, String demoActivityId ) {
        return caller.getSharedPreferences( SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE ).getBoolean( demoActivityId, false );
    }

    /**
     * Reset a demo activity to show again.
     * 
     * @param caller
     *            the activity that is calling the {@link DemoActivity}. Its {@link SharedPreferences} will be used
     *            internally by the {@link DemoActivity}.
     * @param demoActivityId
     *            the id that will be used to store the information about the 'never show again' checkbox.
     */
    public static boolean showAgain( Activity caller, String demoActivityId ) {
        return caller.getSharedPreferences( SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE ).edit().remove( demoActivityId ).commit();
    }
}
