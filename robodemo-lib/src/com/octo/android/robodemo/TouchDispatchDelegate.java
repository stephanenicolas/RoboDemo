package com.octo.android.robodemo;

import android.view.MotionEvent;

/**
 * Provide a way for Touch Events to be passed to other views.
 * @author ericharlow
 */
public interface TouchDispatchDelegate {
	
	/**
	 * Send MotionEvents to a delegate.
	 * Intend to send from a Fragment's custom semitranslucent view to the Activity beneath the Fragment.
	 * @param event - The motion event to be dispatched.
	 * @return True if the event was handled by the view, false otherwise.
	 */
	public boolean sendTouchEvent(MotionEvent event);
}
