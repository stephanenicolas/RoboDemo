package com.octo.android.robodemo.sample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.octo.android.robodemo.DemoFragment;
import com.octo.android.robodemo.LabeledPoint;
import com.octo.android.robodemo.RoboDemo;

/**
 * Sample activity to be explained by RoboDemo.
 * 
 * The activity will display a RoboDemo to illustrate the way a user can interact with the activity. It will display this
 * demo when it is recreated (for instance on every rotation, but not on back key. If you want to preserve state accross
 * rotation, use {@link #onSaveInstanceState(Bundle)}.
 * 
 * This activity holds a menu that can be used to reset RoboDemo, it avoids the need to uninstall the sample app :)
 * 
 * @author stephanenicolas
 * @author ericharlow
 */
public class MainActivity extends Activity {

    /** The id used to identifiy the robodemo "instance" related to this activity. */
    private final static String DEMO_ACTIVITY_ID = "demo-main-activity";

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
        listMain.setOnItemClickListener(createItemListener());
        clearButton = (Button) findViewById( R.id.button_clear );
        refreshList();
        showDemo();
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
    
    private OnItemClickListener createItemListener() {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getBaseContext(), SecondActivity.class);
				intent.putExtra("title", arrayAdapter.getItem(position));
				startActivity(intent);
			}
		};
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
    }
    
//    private void displayDemoOnLayout(int resourceId) {
//    	final View v = findViewById(resourceId);
//    	v.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//			
//			@Override
//			public void onGlobalLayout() {
//				showDemo();
//				v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//			}
//		});
//    }

    // --------------------------------------------------
    // ----------RoboDemo related methods
    // --------------------------------------------------

    /**
     * Displays demo if never show again has never been checked by the user,
     * and should show demo is set.
     */
    private void displayDemoIfNeeded() {

        boolean neverShowDemoAgain = RoboDemo.isNeverShowAgain( this,  DemoFragment.DEMO_FRAGMENT_ID);
        boolean showDemo = RoboDemo.shouldShowDemo(this, DemoFragment.DEMO_FRAGMENT_SHOW);

        if ( !neverShowDemoAgain && showDemo ) {
            ArrayList< LabeledPoint > arrayListPoints = new ArrayList< LabeledPoint >();

            // create a list of LabeledPoints
            LabeledPoint p = new LabeledPoint(this, 0.5f, 0.05f, R.string.text_intro);
            arrayListPoints.add( p );
            
            p = new LabeledPoint(clearButton, getString(R.string.text_move_demo_step_1 ));
            arrayListPoints.add( p );

            p = new LabeledPoint( this, 0.90f, 0.05f, getString( R.string.text_move_demo_step_2 ) );
            arrayListPoints.add( p );
            
            p = new LabeledPoint(this, 0.05f, 0.50f, R.string.text_move_demo_step_3);
            arrayListPoints.add( p );
            
            p = new LabeledPoint(this, 0.95f, 0.05f, R.string.text_move_demo_step_5);
            arrayListPoints.add( p );
            
            p = new LabeledPoint(this, 0.95f, 0.95f, R.string.text_move_demo_step_6);
            arrayListPoints.add( p );

            // start DemoActivity.
//            Intent intent = new Intent( this, MainActivityDemoActivity.class );
//            RoboDemo.prepareDemoActivityIntent( intent, DEMO_ACTIVITY_ID, arrayListPoints );
//            startActivity( intent );
            
            // start DemoFragment
            DemoFragment f = DemoFragment.newInstance(R.layout.fragment_customdemo, arrayListPoints);
            f.show(getFragmentManager(), DemoFragment.TAG);
            
        }
    }

    /**
     * Reset the checkbox so that RoboDemo will be shown again even if user checked it previously.
     */
    private void showDemoAgain() {
        RoboDemo.showAgain( this, DEMO_ACTIVITY_ID );
        showDemo();
    }
    
    private void showDemo() {
    	RoboDemo.setShowDemo(this, DemoFragment.DEMO_FRAGMENT_SHOW, true);
        displayDemoIfNeeded();
    }
}
