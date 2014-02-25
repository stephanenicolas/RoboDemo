package com.octo.android.robodemo.sample;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;

import com.octo.android.robodemo.DemoFragment;
import com.octo.android.robodemo.LabeledPoint;
import com.octo.android.robodemo.RoboDemo;

/**
 * SecondActivity is used to demonstrate using RoboDemo as a walkthrough library.
 * @author ericharlow
 *
 */
public class SecondActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		String item = getIntent().getStringExtra("title");
		TextView title = (TextView) findViewById(R.id.Second_Title);
		title.setText(item);
		
		displayDemoIfNeeded();
	}

	public void onClickBack(View v) {
		finish();
	}
	
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
            LabeledPoint p = new LabeledPoint(this, R.id.Second_Back, R.string.text_move_demo_step_4);
            arrayListPoints.add( p );
            
            // start DemoFragment
            DemoFragment f = DemoFragment.newInstance(R.layout.fragment_customdemo, arrayListPoints);
            f.show(getFragmentManager(), DemoFragment.TAG);
            
        }
    }

}
