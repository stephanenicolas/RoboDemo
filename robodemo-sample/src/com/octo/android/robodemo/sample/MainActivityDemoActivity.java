package com.octo.android.robodemo.sample;

import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.TypedValue;

import com.octo.android.robodemo.DefaultDrawViewAdapter;
import com.octo.android.robodemo.DemoActivity;
import com.octo.android.robodemo.DrawViewAdapter;
import com.octo.android.robodemo.R;

/**
 * DemoActivity for the {@link MainActivity}.
 * 
 * @author sni
 * 
 */
public class MainActivityDemoActivity extends DemoActivity {
    private static final int DEFAULT_FONT_SIZE = 22;

    @Override
    public DrawViewAdapter getDrawViewAdapter() {
        Drawable drawable = getResources().getDrawable( R.drawable.ic_lockscreen_handle );
        TextPaint textPaint = new TextPaint();
        textPaint.setColor( getResources().getColor( android.R.color.white ) );
        textPaint.setShadowLayer( 2.0f, 0, 2.0f, getResources().getColor( android.R.color.black ) );
        textPaint.setAntiAlias( true );
        // http://stackoverflow.com/questions/3061930/how-to-set-unit-for-paint-settextsize
        textPaint.setTextSize( TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FONT_SIZE, getResources().getDisplayMetrics() ) );
        return new DefaultDrawViewAdapter( this, drawable, textPaint, getListPoints() );
    }

}
