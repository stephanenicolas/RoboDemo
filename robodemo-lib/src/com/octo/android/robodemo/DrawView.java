package com.octo.android.robodemo;

import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation.AnimationListener;

/**
 * This view will draw all {@link LabeledPoint} on its surface. It uses a {@link DrawViewAdapter } to get the content to
 * draw.
 * 
 * @author sni
 * @author ericharlow
 */
public class DrawView extends View {

    private static final int DRAW_UNDER_TEXT_CORNER_RADIUS = 7;
    private static final int DEFAULT_FONT_SIZE = 22;

    /**
     * The defaut delay between points in animation in ms.
     */
    private static final long DELAY_BETWEEN_POINTS = 2000;

    private DrawViewAdapter drawViewAdapter;
    private int handlerType;
    private AnimatorHandler handler;
    private int currentPointPositionToDisplay = 0;
    private long delayBetweenPoints;
    private boolean isShowingAllPointsAtTheEndOfAnimation;
    private boolean isDrawingOnePointAtATime;

    private AnimationListener animationListener;

    private Paint underTextPaint;

    private boolean isClearPorterDuffXfermodeEnabled = true;
    
    private TouchDispatchDelegate mTouchDispatchDelegate;

    public DrawView( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );
        
        if (attrs!=null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                                                      R.styleable.DrawView,
                                                      defStyle, 0);
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(a.getColor(R.styleable.DrawView_textColor, Color.WHITE));
            textPaint.setAntiAlias(a.getBoolean(R.styleable.DrawView_textAntiAlias, true));
            textPaint.setTextSize(a.getDimension(R.styleable.DrawView_textSize, DEFAULT_FONT_SIZE));
            textPaint.setShadowLayer(a.getFloat(R.styleable.DrawView_shadowLayerBlurRadius, 2.0f),
            		a.getFloat(R.styleable.DrawView_shadowLayerXOffset, 0),
            		a.getFloat(R.styleable.DrawView_shadowLayerYOffset, 2.0f),
            		a.getColor(R.styleable.DrawView_shadowLayerColor, Color.BLACK));
            
            Drawable drawable = a.getDrawable(R.styleable.DrawView_drawable);
            drawViewAdapter = new DefaultDrawViewAdapter(context, drawable, textPaint, null);
            
            underTextPaint = new Paint();
            underTextPaint.setColor( a.getColor(R.styleable.DrawView_underTextPaintColor, Color.DKGRAY));
            underTextPaint.setAlpha( a.getInt(R.styleable.DrawView_underTextPaintAlpha, 150) );
            
            isShowingAllPointsAtTheEndOfAnimation = a.getBoolean(R.styleable.DrawView_isShowingAllPointsAtTheEndOfAnimation, true);
            isDrawingOnePointAtATime = a.getBoolean(R.styleable.DrawView_isDrawingOnePointAtATime, false);
            delayBetweenPoints = a.getInt(R.styleable.DrawView_delayBetweenPoints, (int) DELAY_BETWEEN_POINTS);
            handlerType = a.getInt(R.styleable.DrawView_handlerType, 1);
            initializeHandler(handlerType);
            a.recycle();
          }
    }

	public DrawView( Context context, AttributeSet attrs ) {
		this(context, attrs, 0);
    }

    public DrawView( Context context ) {
    	this(context, null);
    }

    @Override
    public void onDraw( Canvas canvas ) {
        super.onDraw( canvas );

        if ( isAnimationTerminated() ) {
            if ( isShowingAllPointsAtTheEndOfAnimation ) {
                for ( int index = 0; index < getAdapterPointCount(); index++ ) {
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
        return currentPointPositionToDisplay >= getAdapterPointCount() - 1;
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
        currentPointPositionToDisplay = getAdapterPointCount() - 1;
        if ( animationListener != null ) {
            animationListener.onAnimationEnd( null );
        }
        refreshDrawableState();
        invalidate();
    }
    
    private int getAdapterPointCount() {
    	return drawViewAdapter == null ? 0 : drawViewAdapter.getPointsCount();
    }

    public void setDrawViewAdapter( DrawViewAdapter drawViewAdapter ) {
        this.drawViewAdapter = drawViewAdapter;
    }

    public DrawViewAdapter getDrawViewAdapter() {
        return drawViewAdapter;
    }
    
    /**
     * Supply the data to the Adapter.
     * @see DefaultDrawViewAdapter
     * @param listPoints - the data.
     */
    public void setDrawViewAdapterLabeledPoints(List< LabeledPoint > listPoints) {
    	((DefaultDrawViewAdapter) drawViewAdapter).setListPoints(listPoints);
    }

    /**
     * Sets the delay between animation of two points.
     * 
     * @param delayBetweenPoints
     *            the new delay of animation (in ms).
     */
    public void setDelayBetweenPoints( long delayBetweenPoints ) {
        this.delayBetweenPoints = delayBetweenPoints;
        handlerType = 1;
        initializeHandler(handlerType);
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

    private void initializeHandler(int type) {
    	handler = new AnimatorHandler( this, delayBetweenPoints ); // since there are no null checks for handler
   
		if (type == 1) {
			handler.sendEmptyMessageDelayed();
		} else if (type == 2) {
			
		}  
    }

    private void showNextPoint() {

        if ( currentPointPositionToDisplay < getAdapterPointCount() - 1 ) {
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

    @Override
	public boolean dispatchTouchEvent(MotionEvent event) {
    	if (isTouchHandler() && isTouchEventInLabeledPoint(event, drawViewAdapter.getDrawableAt( currentPointPositionToDisplay ))) {
    			if (event.getAction() == MotionEvent.ACTION_UP)
    				showNextPoint();
    			if (mTouchDispatchDelegate != null)
    				return mTouchDispatchDelegate.sendTouchEvent(event);
    			else
    				return true;
    	}
    		
    	return super.dispatchTouchEvent(event);
	}

	private boolean isTouchHandler() {
		return handlerType == 2 || isTouchDrivenAnimationHandler();
	}
	
	private boolean isTouchDrivenAnimationHandler() {
		return handlerType == 3;
	}

	private boolean isTouchEventInLabeledPoint(MotionEvent event, Drawable drawable) {
		float x = event.getX();
		float y = event.getY();
		int center_x = drawable.getBounds().centerX();
		int center_y = drawable.getBounds().centerY();
		int radius = drawable.getIntrinsicWidth() / 2 - 3;
		
		//change < to <= to include points on circle
		boolean result = Math.pow((x - center_x), 2) + Math.pow((y - center_y), 2) < Math.pow(radius, 2);
		return result;
	}

	public TouchDispatchDelegate getTouchDispatchDelegate() {
		return mTouchDispatchDelegate;
	}

	public void setTouchDispatchDelegate(TouchDispatchDelegate touchDispatchDelegate) {
		if (!isTouchDrivenAnimationHandler())
			this.mTouchDispatchDelegate = touchDispatchDelegate;
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
        }
        
        public void sendEmptyMessageDelayed() {
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