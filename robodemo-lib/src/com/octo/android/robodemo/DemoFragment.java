package com.octo.android.robodemo;

import static com.octo.android.robodemo.RoboDemo.BUNDLE_KEY_DEMO_ACTIVITY_ARRAY_LIST_POINTS;
import static com.octo.android.robodemo.RoboDemo.BUNDLE_KEY_DEMO_ACTIVITY_ID;
import static com.octo.android.robodemo.RoboDemo.SHARED_PREFERENCE_NAME;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;

/**
 * Base class of DemoFragments. These Fragments demonstrate the usage of a given Activity. They are transparent and
 * will get displayed on top of the activity to demonstrate. They display a list of {@link LabeledPoint} inside a
 * {@link DrawView}.
 * 
 * When subclassing this class, you only have to override {@link #getDrawViewAdapter()} and provide a custom
 * {@link DrawViewAdapter} that will act as model for the {@link DrawView}, giving it all data to draw both texts and
 * associated drawables at given locations.
 * 
 * To start subclasses of this Fragment, proceed as follow :
 * 
 * <pre>
 * boolean neverShowDemoAgain = AbstractDemoFragment.isNeverShowAgain( this, demoActivityId );
 * 
 * if ( !neverShowDemoAgain ) {
 *     //create an ArrayList<LabeledPoints> named arrayListPoints.
 * 
 *     DemoFragment f = new DemoFragment();
 *     f.show(getFragmentManager(), DemoFragment.TAG);
 * }
 * </pre>
 * 
 * @author ericharlow
 * 
 */
public abstract class DemoFragment extends DialogFragment {
	
	public static String TAG = "DemoFragment";
	
	private ArrayList< LabeledPoint > listPoints = null;
    private String demoActivityId;

    private DrawView drawView;
    private CheckBox checkBox;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, R.style.Theme_RoboDemo);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_demo, null);

        drawView = (DrawView) v.findViewById( R.id.drawView_move_content_demo );
        checkBox = (CheckBox) v.findViewById( R.id.checkbox_demo_never_again );
        
        View neverAgainText = v.findViewById(R.id.textview_demo_never_again);
        neverAgainText.setOnClickListener(createDemoNeverAgainListener());
        
        View finishButton = v.findViewById(R.id.button_demo_finish);
        finishButton.setOnClickListener(createFinishButtonListener());
        
        return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Bundle bundle = savedInstanceState != null ? savedInstanceState : getArguments();
		
		listPoints = bundle.getParcelableArrayList( BUNDLE_KEY_DEMO_ACTIVITY_ARRAY_LIST_POINTS );
	    demoActivityId = bundle.getString( BUNDLE_KEY_DEMO_ACTIVITY_ID );
        
        drawView.setAnimationListener( new DrawViewAnimationListener() );
        drawView.setDrawViewAdapter( getDrawViewAdapter() );
        drawView.setOnClickListener(createDrawViewListener());
	}

	protected DrawView getDrawView() {
        return drawView;
    }

    @Override
    public void onConfigurationChanged( Configuration newConfig ) {
        super.onConfigurationChanged( newConfig );
        // as most probably a layout change will change the underlying activity, just finish
        // current demo activity's points are not relevant anymore.
        finish(null);
    }

    /**
     * Must be overriden by any custom DemoActivity to provide a DrawViewAdapter to the {@link DrawView}.
     * 
     * @return an adapter that will be passed to the {@link DrawView}.
     */
    public abstract DrawViewAdapter getDrawViewAdapter();

    protected List< LabeledPoint > getListPoints() {
        return listPoints;
    }

    @Override
	public void onSaveInstanceState( Bundle outState ) {
        outState.putParcelableArrayList( BUNDLE_KEY_DEMO_ACTIVITY_ARRAY_LIST_POINTS, listPoints );
        outState.putString( BUNDLE_KEY_DEMO_ACTIVITY_ID, demoActivityId );
        super.onSaveInstanceState( outState );
    }

    public void onTap( View view ) {
        if ( drawView.isAnimationTerminated() ) {
            drawView.resetAnimation();
        } else {
            drawView.terminateAnimation();
        }
    }

    public void checkNeverShowAgain( View view ) {
        checkBox.setChecked( !checkBox.isChecked() );
    }

    public void finish( View view ) {
        Editor editor = getActivity().getSharedPreferences( SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE ).edit();
        if ( checkBox.isChecked() ) {
            editor = editor.putBoolean( demoActivityId, true );
        } else {
            editor = editor.remove( demoActivityId );
        }
        editor.commit();
        removeSelf(getActivity());
    }

    private void setButtonsVisible( boolean visible ) {
        final View layoutButtons = getView().findViewById( R.id.layout_demo_buttons );
        int animationResId = visible ? android.R.anim.fade_in : android.R.anim.fade_out;
        Animation animation = AnimationUtils.loadAnimation( getActivity(), animationResId );
        animation.setDuration( getResources().getInteger( android.R.integer.config_shortAnimTime ) );
        animation.setAnimationListener( new ButtonsAnimationListener( visible, layoutButtons ) );
        layoutButtons.startAnimation( animation );
    }
    
    private boolean removeSelf(Activity act) {
		if (act == null) return false;
		FragmentManager fManager = act.getFragmentManager();
		fManager.beginTransaction().remove(this).commit();
		return true;
	}
    
    private OnClickListener createDrawViewListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onTap(v);
			}
		};
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

}
