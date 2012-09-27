package com.octo.android.robodemo;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation.AnimationListener;

/**
 * This view will draw all {@link LabeledPoint} on its surface. It uses a {@link DrawViewAdapter } to get the content to
 * draw.
 * 
 * @author sni
 * 
 */
public class DrawView extends View {

    private static final int DRAW_UNDER_TEXT_CORNER_RADIUS = 7;

    /**
     * The defaut delay between points in animation in ms.
     */
    private static final long DELAY_BETWEEN_POINTS = 2000;

    private DrawViewAdapter drawViewAdapter;

    private AnimatorHandler handler;
    private int currentPointPositionToDisplay = 0;
    private long delayBetweenPoints = DELAY_BETWEEN_POINTS;
    private boolean isShowingAllPointsAtTheEndOfAnimation = true;
    private boolean isDrawingOnePointAtATime = false;

    private AnimationListener animationListener;

    private Paint underTextPaint;

    private boolean isClearPorterDuffXfermodeEnabled = true;

    public DrawView( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
        initializeHandler();
        initUnderTextPaint();
    }

    public DrawView( Context context, AttributeSet attrs ) {
        super( context, attrs );
        initializeHandler();
        initUnderTextPaint();
    }

    public DrawView( Context context ) {
        super( context );
        initializeHandler();
        initUnderTextPaint();
    }

    @Override
    public void onDraw( Canvas canvas ) {
        super.onDraw( canvas );

        if ( isAnimationTerminated() ) {
            if ( isShowingAllPointsAtTheEndOfAnimation ) {
                for ( int index = 0; index < drawViewAdapter.getPointsCount(); index++ ) {
                    drawPoint( index, canvas );
                }
            } else {
                drawPoint( currentPointPositionToDisplay, canvas );
            }
        } else {
            if ( currentPointPositionToDisplay >= 0 ) {
                if ( isDrawingOnePointAtATime ) {
                    drawPoint( currentPointPositionToDisplay, canvas );
                } else {
                    for ( int index = 0; index <= currentPointPositionToDisplay; index++ ) {
                        drawPoint( index, canvas );
                    }
                }
            }
        }
    }

    public void setIsClearPorterDuffXfermodeEnabled( boolean isClearPorterDuffXfermodeEnabled ) {
        this.isClearPorterDuffXfermodeEnabled = isClearPorterDuffXfermodeEnabled;
    }

    public void setUnderTextPaint( Paint underTextPaint ) {
        this.underTextPaint = underTextPaint;
    }

    public boolean isAnimationTerminated() {
        return currentPointPositionToDisplay >= getDrawViewAdapter().getPointsCount() - 1;
    }

    /**
     * Restarts the animation from the beginning.
     */
    public void resetAnimation() {
        handler.removeMessages( AnimatorHandler.ANIMATION_MESSAGE_ID );
        currentPointPositionToDisplay = 0;
        if ( animationListener != null ) {
            animationListener.onAnimationStart( null );
        }
        handler.sendEmptyMessageDelayed( AnimatorHandler.ANIMATION_MESSAGE_ID, delayBetweenPoints );
    }

    /**
     * Restarts the animation from the beginning.
     */
    public void terminateAnimation() {
        handler.removeMessages( AnimatorHandler.ANIMATION_MESSAGE_ID );
        currentPointPositionToDisplay = getDrawViewAdapter().getPointsCount() - 1;
        if ( animationListener != null ) {
            animationListener.onAnimationEnd( null );
        }
        refreshDrawableState();
        invalidate();
    }

    public void setDrawViewAdapter( DrawViewAdapter drawViewAdapter ) {
        this.drawViewAdapter = drawViewAdapter;
    }

    public DrawViewAdapter getDrawViewAdapter() {
        return drawViewAdapter;
    }

    /**
     * Sets the delay between animation of two points.
     * 
     * @param delayBetweenPoints
     *            the new delay of animation (in ms).
     */
    public void setDelayBetweenPoints( long delayBetweenPoints ) {
        this.delayBetweenPoints = delayBetweenPoints;
        initializeHandler();
    }

    public long getDelayBetweenPoints() {
        return delayBetweenPoints;
    }

    public boolean isShowingAllPointsAtTheEndOfAnimation() {
        return isShowingAllPointsAtTheEndOfAnimation;
    }

    /**
     * Wether or not all points are displayed at the end of all points animation.
     * 
     * @param isShowingAllPointsAtTheEndOfAnimation
     *            if true, all points will be visible at the end of animation, nothing will be displayed otherwise.
     * 
     */
    public void setShowingAllPointsAtTheEndOfAnimation( boolean isShowingAllPointsAtTheEndOfAnimation ) {
        this.isShowingAllPointsAtTheEndOfAnimation = isShowingAllPointsAtTheEndOfAnimation;
    }

    /**
     * Whether to show one point at a time or a point and all previous points.
     * 
     * @param isDrawingOnePointAtATime
     *            if true, only one point will be displayed at a time. If false, a point and all its predecessors will
     *            be displayed simultaneously.
     */
    public void setDrawingOnePointAtATime( boolean isDrawingOnePointAtATime ) {
        this.isDrawingOnePointAtATime = isDrawingOnePointAtATime;
    }

    public boolean isDrawingOnePointAtATime() {
        return isDrawingOnePointAtATime;
    }

    /**
     * Sets an {@link AnimationListener} that will be notified at the beginning and end of the animation of all points.
     * 
     * @param animationListener
     */
    public void setAnimationListener( AnimationListener animationListener ) {
        this.animationListener = animationListener;
    }

    public AnimationListener getAnimationListener() {
        return animationListener;
    }

    private void initializeHandler() {
        handler = new AnimatorHandler( this, delayBetweenPoints );
    }

    private void initUnderTextPaint() {
        underTextPaint = new Paint();
        underTextPaint.setColor( getResources().getColor( android.R.color.darker_gray ) );
        underTextPaint.setAlpha( 150 );
    }

    private void showNextPoint() {

        if ( currentPointPositionToDisplay < getDrawViewAdapter().getPointsCount() - 1 ) {
            currentPointPositionToDisplay++;

            if ( isAnimationTerminated() && animationListener != null ) {
                animationListener.onAnimationEnd( null );
            }
        }
        refreshDrawableState();
        invalidate();
    }

    /**
     * Draw the point at the position specified by {@link DrawViewAdapter#getTextPoint(int)}
     * 
     * @param position
     *            the index of the point to draw.
     * @param canvas
     *            the canvas on which to draw the point at position.
     */
    protected void drawPoint( int position, Canvas canvas ) {
        drawText( position, canvas );
        drawDrawable( position, canvas );
    }

    /**
     * Draw the text of the point at a given position specified by {@link DrawViewAdapter#getTextPoint(int)}
     * 
     * @param position
     *            the index of the point to draw.
     * @param canvas
     *            the canvas on which to draw the point at position.
     */
    protected void drawText( int position, Canvas canvas ) {
        Point point = drawViewAdapter.getTextPointAt( position );
        canvas.save();
        canvas.translate( point.x, point.y );
        Layout layout = drawViewAdapter.getTextLayoutAt( position );
        doDrawUnderTextPaint( canvas, layout );
        layout.draw( canvas );
        canvas.restore();
    }

    /**
     * Draw some surface under text. This method is called just before drawing each {@link LabeledPoint}'s text.
     * 
     * @param canvas
     *            the canvas in which we draw.
     * @param layout
     *            the {@link Layout} associated to a {@link LabeledPoint}.
     */
    protected void doDrawUnderTextPaint( Canvas canvas, Layout layout ) {
        if ( underTextPaint != null ) {
            int margin = DRAW_UNDER_TEXT_CORNER_RADIUS;
            RectF rect = new RectF( -margin, -margin, layout.getWidth() + margin * 2, layout.getHeight() + margin * 2 );
            canvas.drawRoundRect( rect, 2 * margin, 2 * margin, underTextPaint );
        }
    }

    /**
     * Draw the drawable of the point at a given position specified by {@link DrawViewAdapter#getDrawablePoint(int)}
     * 
     * @param position
     *            the index of the point to draw.
     * @param canvas
     *            the canvas on which to draw the point at position.
     */
    protected void drawDrawable( int position, Canvas canvas ) {
        Drawable drawable = drawViewAdapter.getDrawableAt( position );
        if ( drawable == null ) {
            return;
        }
        doUseClearPorterDuffXfermode( canvas, drawable );
        drawable.draw( canvas );
        /*
         * Shall we add some debug mode ? Paint paint = new Paint(); paint.setColor(
         * getContext().getResources().getColor( android.R.color.holo_orange_dark ) ); canvas.drawCircle(
         * drawable.getBounds().exactCenterX(), drawable.getBounds().exactCenterY(), 5, paint );
         */
    }

    /**
     * if PorterDuff xfermode is active, this method can be used to remove the background inside the {@link Drawable}
     * associated to a {@link LabeledPoint}. This method is called just before drawing each {@link LabeledPoint}'s
     * drawable.
     * 
     * @param canvas
     *            the canvas on which to draw the point at position.
     * @param drawable
     *            the {@link Drawable} that is going to be drawn.
     * @see #isClearPorterDuffXfermodeEnabled
     * @see #setIsClearPorterDuffXfermodeEnabled(boolean)
     */
    protected void doUseClearPorterDuffXfermode( Canvas canvas, Drawable drawable ) {
        if ( isClearPorterDuffXfermodeEnabled ) {
            Paint p = new Paint();
            p.setXfermode( new PorterDuffXfermode( Mode.CLEAR ) );
            int cx = drawable.getBounds().centerX();
            int cy = drawable.getBounds().centerY();
            int radius = drawable.getIntrinsicWidth() / 2 - 3;
            canvas.drawCircle( cx, cy, radius, p );
        }
    }

    /**
     * Animate the point to draw on screen.
     * 
     * @author sni
     * 
     */
    private static final class AnimatorHandler extends Handler {
        private static final int ANIMATION_MESSAGE_ID = 0;

        private WeakReference< DrawView > weakReference;
        private long delayBetweenPoints;

        private AnimatorHandler( DrawView drawView, long delayBetweenPoints ) {
            this.weakReference = new WeakReference< DrawView >( drawView );
            this.delayBetweenPoints = delayBetweenPoints;
            sendEmptyMessageDelayed( AnimatorHandler.ANIMATION_MESSAGE_ID, delayBetweenPoints );
        }

        @Override
        public void handleMessage( Message msg ) {
            super.handleMessage( msg );
            if ( msg.what == ANIMATION_MESSAGE_ID ) {
                if ( weakReference == null || weakReference.get() == null || weakReference.get().getDrawViewAdapter() == null ) {
                    return;
                }
                DrawView drawView = weakReference.get();
                drawView.showNextPoint();
                sendEmptyMessageDelayed( AnimatorHandler.ANIMATION_MESSAGE_ID, delayBetweenPoints );
            }

        }

    }

}