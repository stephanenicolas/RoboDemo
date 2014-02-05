package com.octo.android.robodemo;

import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

// ============================================================================================
// INNER CLASSES
// ============================================================================================
/**
 * Sample DrawViewAdapter.
 * 
 * @author sni
 * 
 */
public class DefaultDrawViewAdapter implements DrawViewAdapter {

    private static final float TEXT_MARGIN = 7;
    private static final float DEFAULT_FONT_SIZE = 22;
    private Drawable drawable;
    private TextPaint textPaint;
    private int maxTextWidth = 80;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private List< LabeledPoint > listPoints;
    private int margin;
    private Context context;

    public DefaultDrawViewAdapter( Context context, List< LabeledPoint > listPoints ) {
        this.context = context;
        this.drawable = context.getResources().getDrawable( R.drawable.ic_lockscreen_handle_pressed );
        this.textPaint = initializeDefaultTextPaint();
        this.listPoints = listPoints;

        initialize();

    }

    public DefaultDrawViewAdapter( Context context, Drawable drawable, TextPaint textPaint, List< LabeledPoint > listPoints ) {
        this.context = context;
        this.drawable = drawable;
        this.textPaint = textPaint;
        this.listPoints = listPoints;

        initialize();

    }

    public Context getContext() {
        return context;
    }

    private TextPaint initializeDefaultTextPaint() {
        TextPaint textPaint = new TextPaint();
        textPaint.setColor( getContext().getResources().getColor( android.R.color.white ) );
        textPaint.setShadowLayer( 2.0f, 0, 2.0f, android.R.color.black );
        // http://stackoverflow.com/questions/3061930/how-to-set-unit-for-paint-settextsize
        textPaint.setTextSize( TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, DEFAULT_FONT_SIZE, getContext().getResources().getDisplayMetrics() ) );
        return textPaint;
    }

    @SuppressWarnings("deprecation")
    private void initialize() {
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        Display display = wm.getDefaultDisplay();
        if ( Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2 ) {
            Point outSize = new Point();
            display.getSize( outSize );
            screenHeight = outSize.y;
            screenWidth = outSize.x;
        } else {
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }
        if ( context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ) {
            maxTextWidth = screenWidth / 2;
        } else {
            maxTextWidth = screenWidth / 3;
        }
        margin = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, TEXT_MARGIN, context.getResources().getDisplayMetrics() );
    }

    @Override
    public int getPointsCount() {
        return listPoints.size();
    }

    @Override
    public Drawable getDrawableAt( int position ) {
        Point point = listPoints.get( position );
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        drawable.setBounds( point.x - width / 2, point.y - height / 2, point.x + width / 2, point.y + height / 2 );
        return drawable;
    }

    @Override
    public Layout getTextLayoutAt( int position ) {
        String text = listPoints.get( position ).getText();
        Rect bounds = new Rect();
        textPaint.getTextBounds( text, 0, text.length(), bounds );

        int width = Math.min( bounds.width(), maxTextWidth );
        StaticLayout staticLayout = new StaticLayout( text, textPaint, width, Alignment.ALIGN_CENTER, 1, 0, false );
        return staticLayout;
    }

    @Override
    public Point getTextPointAt( int position ) {
        Drawable drawable = getDrawableAt( position );
        Layout textLayout = getTextLayoutAt( position );
        Point point = listPoints.get( position );
        final int marginX = drawable.getIntrinsicWidth() / 4 + margin;
        final int marginY = drawable.getIntrinsicHeight() / 4 + margin;
        int textX = point.x > screenWidth / 2 ? point.x - marginX - textLayout.getWidth() : point.x + marginX;
        int textY = point.y > screenHeight / 2 ? point.y - marginY - textLayout.getHeight() : point.y + marginY;
        return new Point( textX, textY );
    }
}