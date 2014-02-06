package com.octo.android.robodemo.sample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.octo.android.robodemo.LabeledPoint;
import com.octo.android.robodemo.RoboDemo;

/**
 * Sample activity to be explained by RoboDemo.
 * 
 * The activity will display a RoboDemo to illustrate the way user can interact with the activity. It will display this
 * demo when it is recreated (for instance on every rotation, but not on back key. If you want to preserve state accross
 * rotation, use {@link #onSaveInstanceState(Bundle)}.
 * 
 * This activity holds a menu that can be used to reset RoboDemo, it avoids to uninstall sample app :)
 * 
 * @author stephanenicolas
 * 
 */
public class MainActivity extends Activity {

    /** The id used to identifiy the robodemo "instance" related to this activity. */
    private final static String DEMO_ACTIVITY_ID = "demo-main-activity";
    /** A boolean holding the internal state of the activity under RoboDemo, whether or not to display RoboDemo. */
    private boolean showDemo = true;

    // internal stuff

    private static final int ITEM_COUNT = 100;
    private ListView listMain;
    private ArrayAdapter< String > arrayAdapter;
    private Button clearButton;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        listMain = (ListView) findViewById( R.id.listview_main );
        clearButton = (Button) findViewById( R.id.button_clear );
        refreshList();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate( R.menu.activity_main, menu );
        return true;
    }

    @Override
    public boolean onMenuItemSelected( int featureId, MenuItem item ) {
        if ( item.getItemId() == R.id.menu_refresh ) {
            refreshList();
            return true;
        } else if ( item.getItemId() == R.id.menu_show_again ) {
            showDemoAgain();
            return true;
        }
        return super.onMenuItemSelected( featureId, item );
    }

    public void onClear( View v ) {
        arrayAdapter.clear();
    }

    private List< String > initListItem() {
        List< String > result = new ArrayList< String >();
        for ( int index = 0; index < ITEM_COUNT; index++ ) {
            result.add( "Item " + index );
        }
        return result;
    }

    private void refreshList() {
        List< String > listItems = initListItem();
        arrayAdapter = new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, listItems );
        listMain.setAdapter( arrayAdapter );
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                displayDemoIfNeeded();
            }
        }, 500 );
    }

    // --------------------------------------------------
    // ----------RoboDemo related methods
    // --------------------------------------------------

    /**
     * Displays demo if never show again has never been checked by the user.
     */
    private void displayDemoIfNeeded() {

//        boolean neverShowDemoAgain = RoboDemo.isNeverShowAgain( this, DEMO_ACTIVITY_ID );
        boolean neverShowDemoAgain = RoboDemo.isNeverShowAgain( this,  MainActivityDemoFragment.DEMO_FRAGMENT_ID);

        if ( !neverShowDemoAgain && showDemo ) {
            showDemo = false;
            ArrayList< LabeledPoint > arrayListPoints = new ArrayList< LabeledPoint >();

            // create a list of LabeledPoints
            LabeledPoint p = new LabeledPoint( clearButton, getString( R.string.text_move_demo_step_1 ) );
            arrayListPoints.add( p );

            p = new LabeledPoint( this, 0.95f, 0.05f, getString( R.string.text_move_demo_step_2 ) );
            arrayListPoints.add( p );

            // start DemoActivity.
//            Intent intent = new Intent( this, MainActivityDemoActivity.class );
//            RoboDemo.prepareDemoActivityIntent( intent, DEMO_ACTIVITY_ID, arrayListPoints );
//            startActivity( intent );
            
            // start DemoFragment
            MainActivityDemoFragment f = MainActivityDemoFragment.newInstance(arrayListPoints);
            f.show(getFragmentManager(), MainActivityDemoFragment.TAG);
        }
    }

    /**
     * Reset the checkbox so that RoboDemo will be shown again even if user checked it previously.
     */
    private void showDemoAgain() {
        RoboDemo.showAgain( this, DEMO_ACTIVITY_ID );
        this.showDemo = true;
        displayDemoIfNeeded();
    }
}
