package com.octo.android.robodemo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

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
    
    /**
     * Should the Demo be shown.
     * @param caller - the activity that is calling the demo. It will be used internally to get a {@link SharedPreferences}.
     * @param demoActivityId - the id that will be used to store the information about showing the demo.
     * @return true if the demo should be shown.
     */
    public static boolean shouldShowDemo(Activity caller, String demoActivityId) {
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(caller.getApplicationContext());
    	return settings.getBoolean( demoActivityId, false );
    }
    
    /**
     * Set whether the demo should be shown.
     * @param caller - the activity that is calling the demo. It will be used internally to get a {@link SharedPreferences}.
     * @param demoActivityId  - the id that will be used to store the information about showing the demo.
     * @param value - the value for whether the demo should be shown.
     */
    public static void setShowDemo(Activity caller, String demoActivityId, boolean value) {
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(caller.getApplicationContext());
    	settings.edit().putBoolean(demoActivityId, value).commit();
    }
    
    /**
     * 
     * @param caller - the activity that is calling the {@link DemoActivity}. Its {@link SharedPreferences} will be used
     *            internally by the {@link DemoActivity}.
     * @param demoActivityId - the id that will be used to store the information about the 'never show again' checkbox.
     * @param value
     */
    public static void setNeverShowAgain(Activity caller, String demoActivityId, boolean value) {
    	Editor editor = caller.getSharedPreferences( SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE ).edit();
    	editor = editor.putBoolean( demoActivityId, value );
    	editor.commit();
    }
}
