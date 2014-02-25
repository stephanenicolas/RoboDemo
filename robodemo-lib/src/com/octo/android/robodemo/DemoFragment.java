package com.octo.android.robodemo;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;

/**
 * This Fragment demonstrates the usage of a given Activity.
 * The default view is semi-transparent and will be displayed on top of the activity being demonstrated.
 * It displays a list of {@link LabeledPoint} inside a {@link DrawView}.
 * 
 * To start this Fragment, proceed as follow :
 * 
 * <pre>
 * boolean neverShowDemoAgain = RoboDemo.isNeverShowAgain( this, demoFragmentId );
 * 
 * if ( !neverShowDemoAgain ) {
 *     //create an ArrayList<LabeledPoints> named arrayListPoints.
 * 
 *     DemoFragment f = DemoFragment.newInstance(arrayListPoints);
 *     f.show(getFragmentManager(), DemoFragment.TAG);
 * }
 * </pre>
 * 
 * @author ericharlow
 * 
 */
public class DemoFragment extends DialogFragment {
	
	public static final String TAG = "DemoFragment";
	public final static String DEMO_FRAGMENT_ID = "demo-main-fragment";
	public final static String DEMO_FRAGMENT_SHOW = "demo-fragment-show";
	
	private ArrayList< LabeledPoint > listPoints;
    private String demoFragmentId;
    private int mResourceId;

    private DrawView drawView;
    private CheckBox checkBox;
    
    /**
     * Create a DemoFragment using a list of LabeledPoints.
     * @param arrayListPoints - list of LabeledPoints to show.
     * @return The DemoFragment.
     */
    public static DemoFragment newInstance(ArrayList< LabeledPoint > arrayListPoints) {
    	return newInstance(R.layout.fragment_demo, arrayListPoints);
    }
    
    /**
     * Create a customized DemoFragment with a xml layout and supplied list of LabeledPoints.
     * @param resource - custom layout.
     * @param arrayListPoints - list of LabeledPoints to show.
     * @return The DemoFragment.
     * @see DrawView
     */
    public static DemoFragment newInstance(int resource, ArrayList< LabeledPoint > arrayListPoints) {
    	DemoFragment f = new DemoFragment();
    	f.demoFragmentId = DEMO_FRAGMENT_ID;
    	f.mResourceId = resource;
    	f.listPoints = arrayListPoints;
    	return f;
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, R.style.Theme_RoboDemo);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mResourceId == 0) {
			mResourceId = savedInstanceState.getInt(DEMO_FRAGMENT_ID);
		}
		
		View v = inflater.inflate(mResourceId, null);

		View temp = v.findViewById( R.id.drawView_move_content_demo );
		if (temp != null) {
			drawView = (DrawView) temp;
		}			
        
		temp = v.findViewById( R.id.checkbox_demo_never_again );
		if (temp != null)
			checkBox = (CheckBox) temp;
		
		temp = v.findViewById(R.id.textview_demo_never_again);
		if (temp != null)
			temp.setOnClickListener(createDemoNeverAgainListener());
        
        temp = v.findViewById(R.id.button_demo_finish);
        if (temp != null)
        	temp.setOnClickListener(createFinishButtonListener());
        
        //TODO: add a delegate, and provide the view for custom listeners to be added
        
        return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (drawView == null)
			return;
        
        drawView.setAnimationListener( new DrawViewAnimationListener() );
        drawView.setDrawViewAdapterLabeledPoints(listPoints);
        drawView.setTouchDispatchDelegate(new FragmentTouchDispatchDelegate());
	}

    /**
     * Removes any previous fragment identified by the tag.
     */
	@Override
	public void show(FragmentManager manager, String tag) {
        Fragment prev = manager.findFragmentByTag(tag);
        if (prev != null) {
        	FragmentTransaction ft = manager.beginTransaction();
            ft.remove(prev).commit();
        }
		super.show(manager, tag);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(DEMO_FRAGMENT_ID, mResourceId);
		super.onSaveInstanceState(outState);
	}

//	public void onTap( View view ) {
//        if ( drawView.isAnimationTerminated() ) {
//            drawView.resetAnimation();
//        } else {
//            drawView.terminateAnimation();
//        }
//    }

    public void checkNeverShowAgain( View view ) {
        checkBox.setChecked( !checkBox.isChecked() );
    }

    public void finish( View view ) {
        if ( checkBox.isChecked() ) {
            RoboDemo.setNeverShowAgain(getActivity(), demoFragmentId, true);
        } else {
        	RoboDemo.showAgain(getActivity(), demoFragmentId);
        }
        
        RoboDemo.setShowDemo(getActivity(), DemoFragment.DEMO_FRAGMENT_SHOW, false);       
        removeSelf();
    }

    private void setButtonsVisible( boolean visible ) {
    	View view = getView();
    	if (view == null)
    		return;
    	
        final View layoutButtons = view.findViewById( R.id.layout_demo_buttons );
        if (layoutButtons == null)
        	return;
        
        int animationResId = visible ? android.R.anim.fade_in : android.R.anim.fade_out;
        Animation animation = AnimationUtils.loadAnimation( getActivity(), animationResId );
        animation.setDuration( getResources().getInteger( android.R.integer.config_shortAnimTime ) );
        animation.setAnimationListener( new ButtonsAnimationListener( visible, layoutButtons ) );
        layoutButtons.startAnimation( animation );
    }
    
    private boolean removeSelf() {
		FragmentManager fManager = getFragmentManager();
		fManager.beginTransaction().remove(this).commit();
		return true;
	}
    
    public String getDemoFragmentId() {
		return demoFragmentId;
	}

	public void setDemoFragmentId(String demoFragmentId) {
		this.demoFragmentId = demoFragmentId;
	}
    
    private OnClickListener createFinishButtonListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish(v);
			}
		};
	}
    
    private OnClickListener createDemoNeverAgainListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkNeverShowAgain(v);
			}
		};
	}

    /**
     * Animate the buttons at the bottom of the screen.
     * 
     * @author sni
     * 
     */
    private final class DrawViewAnimationListener implements AnimationListener {

        @Override
        public void onAnimationStart( Animation animation ) {
            setButtonsVisible( false );
        }

        @Override
        public void onAnimationRepeat( Animation animation ) {

        }

        @Override
        public void onAnimationEnd( Animation animation ) {
            setButtonsVisible( true );
        }
    }

    private final class ButtonsAnimationListener implements AnimationListener {
        private final boolean visibleAtEnd;
        private final View layoutButtons;

        private ButtonsAnimationListener( boolean visibleAtEnd, View layoutButtons ) {
            this.visibleAtEnd = visibleAtEnd;
            this.layoutButtons = layoutButtons;
        }

        @Override
        public void onAnimationStart( Animation animation ) {
            layoutButtons.setVisibility( View.VISIBLE );
        }

        @Override
        public void onAnimationRepeat( Animation animation ) {
        }

        @Override
        public void onAnimationEnd( Animation animation ) {
            layoutButtons.setVisibility( visibleAtEnd ? View.VISIBLE : View.GONE );
        }
    }
    
    /**
     * Gives the Context for the {@link TouchDispatchDelegate}.
     * @author ericharlow
     */
    private class FragmentTouchDispatchDelegate implements TouchDispatchDelegate {

		@Override
		public boolean sendTouchEvent(MotionEvent event) {
			View v = getActivity().getWindow().getDecorView();
			if (v != null)
				return v.dispatchTouchEvent(event);
			else
				return false;
		}
    	
    }

}
