package com.octo.android.robodemo.sample;

import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.TypedValue;
import com.octo.android.robodemo.*;

/**
 * DemoFragment for the {@link MainActivity}.
 * @author ericharlow
 */
public class MainActivityDemoFragment extends DemoFragment {
	
	public final static String DEMO_FRAGMENT_ID = "demo-main-fragment";
	private static final int DEFAULT_FONT_SIZE = 22;	

	/**
	 * Simplifies creating the fragment and setting the arguments for it.
	 * @param arrayListPoints - an {@link ArrayList} of {@link LabeledPoint} to Display.
	 * @return The fragment with arguments.
	 */
	public static MainActivityDemoFragment newInstance(ArrayList< LabeledPoint > arrayListPoints) {
		MainActivityDemoFragment f = new MainActivityDemoFragment();
		Bundle args = RoboDemo.prepareDemoFragmentArguments(f.getArguments(), DEMO_FRAGMENT_ID, arrayListPoints);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public DrawViewAdapter getDrawViewAdapter() {
		Drawable drawable = getResources().getDrawable( R.drawable.ic_lockscreen_handle_pressed );
        TextPaint textPaint = new TextPaint();
        textPaint.setColor( getResources().getColor( android.R.color.white ) );
        textPaint.setShadowLayer( 2.0f, 0, 2.0f, getResources().getColor( android.R.color.black ) );
        textPaint.setAntiAlias( true );
        // http://stackoverflow.com/questions/3061930/how-to-set-unit-for-paint-settextsize
        textPaint.setTextSize( TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FONT_SIZE, getResources().getDisplayMetrics() ) );
        return new DefaultDrawViewAdapter( getActivity(), drawable, textPaint, getListPoints() );
	}
	
}
