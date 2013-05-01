package com.octo.android.robodemo;

import static com.octo.android.robodemo.RoboDemo.BUNDLE_KEY_DEMO_ACTIVITY_ARRAY_LIST_POINTS;
import static com.octo.android.robodemo.RoboDemo.BUNDLE_KEY_DEMO_ACTIVITY_ID;
import static com.octo.android.robodemo.RoboDemo.SHARED_PREFERENCE_NAME;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;

/**
 * Base class of DemoAcitivities. These activities demonstrate the usage of a given activity. They are transparent and
 * will get displayed on top of the activity to demonstrate. They display a list of {@link LabeledPoint} inside a
 * {@link DrawView}.
 * 
 * When subclassing this class, you only have to override {@link #getDrawViewAdapter()} and provide a custom
 * {@link DrawViewAdapter} that will act as model for the {@link DrawView}, giving it all data to draw both texts and
 * associated drawables at given locations.
 * 
 * To start subclasses of this Activity, proceed as follow :
 * 
 * <pre>
 * boolean neverShowDemoAgain = AbstractDemoActivity.isNeverShowAgain( this, demoActivityId );
 * 
 * if ( !neverShowDemoAgain ) {
 *     //create an ArrayList<LabeledPoints> named arrayListPoints.
 * 
 *     Intent intent = new Intent( this, <Your DemoActivity> );
 *     DemoActivity.prepareDemoActivityIntent( intent, demoActivityId, arrayListPoints );
 *     startActivity( intent );
 * }
 * </pre>
 * 
 * And don't forget to declare your {@link DemoActivity} subclass in the AndroidManifest file !
 * 
 * <pre>
 *       <activity
 *             android:name=".activity.MyDemoActivity"
 *             android:screenOrientation="portrait"
 *             android:theme="@style/LayoutDemo" />
 * </pre>
 * 
 * @author sni
 * 
 */
public abstract class DemoActivity extends Activity {

    private ArrayList< LabeledPoint > listPoints = null;
    private String demoActivityId;

    private DrawView drawView;
    private CheckBox checkBox;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        getWindow().requestFeature( Window.FEATURE_NO_TITLE );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_demo );
        // if you use action bar sherlock, add this line
        /*
         * if ( getSupportActionBar() != null ) { getSupportActionBar().hide(); }
         */

        Bundle bundle = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();

        listPoints = bundle.getParcelableArrayList( BUNDLE_KEY_DEMO_ACTIVITY_ARRAY_LIST_POINTS );
        demoActivityId = bundle.getString( BUNDLE_KEY_DEMO_ACTIVITY_ID );
        drawView = (DrawView) findViewById( R.id.drawView_move_content_demo );
        checkBox = (CheckBox) findViewById( R.id.checkbox_demo_never_again );

        drawView.setAnimationListener( new DrawViewAnimationListener() );
        drawView.setDrawViewAdapter( getDrawViewAdapter() );
    }

    protected DrawView getDrawView() {
        return drawView;
    }

    @Override
    public void onConfigurationChanged( Configuration newConfig ) {
        super.onConfigurationChanged( newConfig );
        // as most probably a layout change will change the underlying activity, just finish
        // current demo activity's points are not relevant anymore.
        finish();
    }

    /**
     * Must be overriden by any custom DemoActivity to provide a DrawViewAdapter to the {@link DrawView}.
     * 
     * @return an adapter that will be passed to the {@link DrawView}.
     */
    public abstract DrawViewAdapter getDrawViewAdapter();

    protected List< LabeledPoint > getListPoints() {
        return listPoints;
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        outState.putParcelableArrayList( BUNDLE_KEY_DEMO_ACTIVITY_ARRAY_LIST_POINTS, listPoints );
        outState.putString( BUNDLE_KEY_DEMO_ACTIVITY_ID, demoActivityId );
        super.onSaveInstanceState( outState );
    }

    public void onTap( View view ) {
        if ( drawView.isAnimationTerminated() ) {
            drawView.resetAnimation();
        } else {
            drawView.terminateAnimation();
        }
    }

    public void checkNeverShowAgain( View view ) {
        checkBox.setChecked( !checkBox.isChecked() );
    }

    public void finish( View view ) {
        Editor editor = getSharedPreferences( SHARED_PREFERENCE_NAME, MODE_PRIVATE ).edit();
        if ( checkBox.isChecked() ) {
            editor = editor.putBoolean( demoActivityId, true );
        } else {
            editor = editor.remove( demoActivityId );
        }
        editor.commit();
        finish();
    }

    private void setButtonsVisible( boolean visible ) {
        final View layoutButtons = findViewById( R.id.layout_demo_buttons );
        int animationResId = visible ? android.R.anim.fade_in : android.R.anim.fade_out;
        Animation animation = AnimationUtils.loadAnimation( DemoActivity.this, animationResId );
        animation.setDuration( getResources().getInteger( android.R.integer.config_shortAnimTime ) );
        animation.setAnimationListener( new ButtonsAnimationListener( visible, layoutButtons ) );
        layoutButtons.startAnimation( animation );
    }

    /**
     * Animate the buttons at the bottom of the screen.
     * 
     * @author sni
     * 
     */
    private final class DrawViewAnimationListener implements AnimationListener {

        @Override
        public void onAnimationStart( Animation animation ) {
            setButtonsVisible( false );
        }

        @Override
        public void onAnimationRepeat( Animation animation ) {

        }

        @Override
        public void onAnimationEnd( Animation animation ) {
            setButtonsVisible( true );
        }
    }

    private final class ButtonsAnimationListener implements AnimationListener {
        private final boolean visibleAtEnd;
        private final View layoutButtons;

        private ButtonsAnimationListener( boolean visibleAtEnd, View layoutButtons ) {
            this.visibleAtEnd = visibleAtEnd;
            this.layoutButtons = layoutButtons;
        }

        @Override
        public void onAnimationStart( Animation animation ) {
            layoutButtons.setVisibility( View.VISIBLE );
        }

        @Override
        public void onAnimationRepeat( Animation animation ) {
        }

        @Override
        public void onAnimationEnd( Animation animation ) {
            layoutButtons.setVisibility( visibleAtEnd ? View.VISIBLE : View.GONE );
        }
    }
}
