package com.octo.android.robodemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.Display;
import android.view.View;
import android.view.View.OnLayoutChangeListener;

/**
 * A pojo class that wraps all information needed to display a point on screen. {@link LabeledPoint} embed a position
 * and a text to draw. The {@link Drawable} that illustrates this point will be provided by
 * {@link DrawView#getDrawViewAdapter()}. This class implements {@link Parcelable} so that it can be passed between the
 * Activity to illustrate and the {@link DemoActivity}.
 * 
 * @author sni
 * @author ericharlow
 */
public class LabeledPoint extends Point implements Parcelable {

    /** The text associated to this point. */
    private String text;

    /**
     * Creates an empty {@link LabeledPoint}.
     */
    public LabeledPoint() {
    	text="";
    }

    /**
     * Creates a {@link LabeledPoint} at a given location, with a null text.
     * 
     * @param x
     *            the new x coordinate of the point.
     * @param y
     *            the new y coordinate of the point.
     */
    public LabeledPoint( int x, int y ) {
        this( x, y, null );
    }

    /**
     * Creates a {@link LabeledPoint} at a given location, with a given text.
     * 
     * @param x
     *            the new x coordinate of the point.
     * @param y
     *            the new y coordinate of the point.
     * @param text
     *            then new text of the point.
     */
    public LabeledPoint( int x, int y, String text ) {
        this.x = x;
        this.y = y;
        setText( text );
    }

    /**
     * Creates a {@link LabeledPoint} at a given location, with a null text.
     * 
     * @param src
     *            a point to get the coordinates from.
     */
    public LabeledPoint( Point src ) {
        this( src, null );
    }

    /**
     * Creates a {@link LabeledPoint} at a given location, with a given text.
     * 
     * @param src
     *            a point to get the coordinates from.
     * @param text
     *            the new text of the point.
     */
    public LabeledPoint( Point src, String text ) {
        super( src );
        setText( text );
    }

    /**
     * Creates a {@link LabeledPoint} located at the center of a given view, with a null text.
     * 
     * @param view
     *            the view on which to center the point.
     */
    public LabeledPoint( View v ) {
        this( v, 50, 50, null );
    }

    /**
     * Creates a {@link LabeledPoint} located at the center of a given view, with a given text.
     * 
     * @param view
     *            the view on which to center the point.
     * @param text
     *            the new text of the point.
     */
    public LabeledPoint( View v, String text ) {
        this( v, 50, 50, text );
    }

    /**
     * Creates a {@link LabeledPoint} positioned relatively to a given view, with no given text.
     * 
     * @param view
     *            the view on which to center the point.
     * @param widthPercent
     *            the percent of the view width at which to place the new point.
     * 
     * @param heightPercent
     *            the percent of the view height at which to place the new point.
     * 
     */
    public LabeledPoint( View v, float widthPercent, float heightPercent ) {
        this( v, widthPercent, heightPercent, null );
    }

    /**
     * Creates a {@link LabeledPoint} positioned relatively to a given view, with a given text.
     * 
     * @param view
     *            the view on which to center the point.
     * @param widthPercent
     *            the percent of the view width at which to place the new point.
     * 
     * @param heightPercent
     *            the percent of the view height at which to place the new point.
     * 
     * @param text
     *            the new text of the point.
     */
    public LabeledPoint( View v, final float widthPercent, final float heightPercent, String text ) {
        if (v != null) {
        	setMeasuredLocation(widthPercent, heightPercent, v);
        	v.addOnLayoutChangeListener(new OnLayoutChangeListener() {
				
				@Override
				public void onLayoutChange(View v, int left, int top, int right,
						int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
					setMeasuredLocation(widthPercent, heightPercent, v);
		            v.removeOnLayoutChangeListener(this);
				}
			});
        }
        setText( text );
    }
    
    /**
     * Creates a {@link LabeledPoint} positioned relatively to a given activity, with a given text.
     * @param activity - the view on which to center the point.
     * @param widthPercent - the percent of the view width at which to place the new point.
     * @param heightPercent - the percent of the view height at which to place the new point.
     * @param stringID - reference to the new text of the point.
     */
    public LabeledPoint(Activity activity, float widthPercent, float heightPercent, int stringID) {
    	this(activity, widthPercent, heightPercent, activity.getString(stringID));
    }

    /**
     * Creates a {@link LabeledPoint} positioned relatively to a given activity, with a given text.
     * 
     * @param activity
     *            the view on which to center the point.
     * @param widthPercent
     *            the percent of the view width at which to place the new point.
     * 
     * @param heightPercent
     *            the percent of the view height at which to place the new point.
     * 
     * @param text
     *            the new text of the point.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	@SuppressWarnings("deprecation")
    public LabeledPoint( Activity activity, float widthPercent, float heightPercent, String text ) {

        Display display = activity.getWindowManager().getDefaultDisplay();
        int screenWidth = 0;
        int screenHeight = 0;
        if ( Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2 ) {
            Point outSize = new Point();
            display.getSize( outSize );
            screenHeight = outSize.y;
            screenWidth = outSize.x;
        } else {
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }
        x = (int) ( screenWidth * widthPercent );
        y = (int) ( screenHeight * heightPercent );
        setText( text );
    }

    /**
     * Creates a {@link LabeledPoint} positioned relatively to a given view, with a given text.
     * 
     * @param view
     *            the view on which to center the point.
     * @param widthPercent
     *            the percent of the view width at which to place the new point.
     * 
     * @param heightPercent
     *            the percent of the view height at which to place the new point.
     * 
     */
    public LabeledPoint( Activity activity, float widthPercent, float heightPercent ) {
        this( activity, widthPercent, heightPercent, null );
    }
    
    /**
     * Creates a {@link LabeledPoint} located at the center of a given view, with a given text.
     * @param activity - the context for the view.
     * @param referenceID - the resource id for the view.
     * @param stringID - the resource id for the string.
     */
    public LabeledPoint(Activity activity, int referenceID, int stringID) {
    	this(activity.findViewById(referenceID), activity.getString(stringID));
    }
    
    /**
     * Creates a {@link LabeledPoint} at a given location, with a null text.
     * 
     * @param activity - the view on which to center the point.
     * @param x - the new x coordinate of the point.
     * @param y - the new y coordinate of the point.
     * @param stringID - reference to the new text of the point.
     */
    public LabeledPoint(Activity activity, int x, int y, int stringID ) {
        this( x, y, activity.getString(stringID) );
    }
    
    /**
     * Creates a {@link LabeledPoint} positioned relatively to a given view, with a given text.
     * @param activity - the context for the view.
     * @param widthPercent - the percent of the view width at which to place the new point.
     * @param heightPercent - the percent of the view height at which to place the new point.
     * @param referenceID - the resource id for the view.
     * @param stringID - the resource id for the string.
     */
    public LabeledPoint(Activity activity, float widthPercent, float heightPercent, int referenceID, int stringID) {
    	this(activity.findViewById(referenceID), widthPercent, heightPercent, activity.getString(stringID));
    }

    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }

    /**
     * Write this point to the specified parcel. To restore a point from a parcel, use readFromParcel()
     * 
     * @param out
     *            The parcel to write the point's coordinates into
     */
    @Override
    public void writeToParcel( Parcel out, int flags ) {
        out.writeInt( x );
        out.writeInt( y );
        out.writeString( text );
    }

    /**
     * Parcel creator for points.
     */
    public static final Parcelable.Creator< LabeledPoint > CREATOR = new Parcelable.Creator< LabeledPoint >() {
        /**
         * Return a new point from the data in the specified parcel.
         */
        @SuppressLint("NewApi")
		@Override
        public LabeledPoint createFromParcel( Parcel in ) {
            LabeledPoint r = new LabeledPoint();
            r.readFromParcel( in );
            return r;
        }

        /**
         * Return an array of rectangles of the specified size.
         */
        @Override
        public LabeledPoint[] newArray( int size ) {
            return new LabeledPoint[ size ];
        }
    };

    /**
     * Set the point's coordinates from the data stored in the specified parcel. To write a point to a parcel, call
     * writeToParcel().
     * 
     * @param in
     *            The parcel to read the point's coordinates from
     */
    public void readFromParcel( Parcel in ) {
        x = in.readInt();
        y = in.readInt();
        text = in.readString();
    }

	private void setMeasuredLocation(final float widthPercent,
			final float heightPercent, View v) {
		int[] location = new int[ 2 ];
		v.getLocationOnScreen( location );
		x = location[ 0 ] + Math.round( widthPercent * v.getMeasuredWidth() / 100 );
		y = location[ 1 ] + Math.round( heightPercent * v.getMeasuredHeight() / 100 );
	}
}