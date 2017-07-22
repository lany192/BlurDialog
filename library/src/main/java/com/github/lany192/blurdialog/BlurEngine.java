package com.github.lany192.blurdialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;


/**
 * Encapsulate the whole behaviour to provide a blur effect on a DialogFragment.
 * <p/>
 * All the screen behind the dialog will be blurred except the action bar.
 * <p/>
 * Simply linked all methods to the matching lifecycle ones.
 */
class BlurEngine {

    /**
     * Since image is going to be blurred, we don't care about resolution.
     * Down scale factor to reduce blurring time and memory allocation.
     */
    static final float DEFAULT_BLUR_DOWN_SCALE_FACTOR = 4.0f;

    /**
     * Radius used to blur the background
     */
    static final int DEFAULT_BLUR_RADIUS = 8;

    /**
     * Default dimming policy.
     */
    static final boolean DEFAULT_DIMMING_POLICY = false;

    /**
     * Default action bar blurred policy.
     */
    static final boolean DEFAULT_ACTION_BAR_BLUR = false;

    /**
     * Image view used to display blurred background.
     */
    private BlurView mBlurredBackgroundView;

    /**
     * Layout params used to add blurred background.
     */
    private FrameLayout.LayoutParams mBlurredBackgroundLayoutParams;

    /**
     * Holding activity.
     */
    private Activity mHoldingActivity;

    /**
     * Allow to use a toolbar without set it as action bar.
     */
    private Toolbar mToolbar;


    /**
     * Boolean used to know if the actionBar should be blurred.
     */
    private boolean mBlurredActionBar;

    /**
     * Constructor.
     *
     * @param holdingActivity activity which holds the DialogFragment.
     */
    public BlurEngine(Activity holdingActivity) {
        mHoldingActivity = holdingActivity;
    }

    /**
     * Must be linked to the original lifecycle.
     *
     * @param activity holding activity.
     */
    public void onAttach(Activity activity) {
        mHoldingActivity = activity;
    }

    /**
     * Resume the engine.
     *
     * @param retainedInstance use getRetainInstance.
     */
    public void onResume(boolean retainedInstance) {
        if (mBlurredBackgroundView == null || retainedInstance) {
            if (mHoldingActivity.getWindow().getDecorView().isShown()) {
                blur();
                mHoldingActivity.getWindow().addContentView(
                        mBlurredBackgroundView,
                        mBlurredBackgroundLayoutParams
                );
            } else {
                mHoldingActivity.getWindow().getDecorView().getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                // dialog can have been closed before being drawn
                                if (mHoldingActivity != null) {
                                    mHoldingActivity.getWindow().getDecorView()
                                            .getViewTreeObserver().removeOnPreDrawListener(this);
                                    blur();
                                    mHoldingActivity.getWindow().addContentView(
                                            mBlurredBackgroundView,
                                            mBlurredBackgroundLayoutParams
                                    );
                                }
                                return true;
                            }
                        }
                );
            }
        }
    }

    /**
     * Must be linked to the original lifecycle.
     */
    @SuppressLint("NewApi")
    public void onDismiss() {
        if (mBlurredBackgroundView != null) {
            ViewGroup parent = (ViewGroup) mBlurredBackgroundView.getParent();
            if (parent != null) {
                parent.removeView(mBlurredBackgroundView);
            }
            mBlurredBackgroundView = null;
        }
    }

    /**
     * Must be linked to the original lifecycle.
     */
    public void onDetach() {
        mHoldingActivity = null;
    }

    /**
     * Enable / disable blurred action bar.
     * <p/>
     * When enabled, the action bar is blurred in addition of the content.
     *
     * @param enable true to blur the action bar.
     */
    public void setBlurActionBar(boolean enable) {
        mBlurredActionBar = enable;
    }

    /**
     * Set a toolbar which isn't set as action bar.
     *
     * @param toolbar toolbar.
     */
    public void setToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
    }

    public void setBlurRadius(float radius) {
        if (mBlurredBackgroundView != null) {
            mBlurredBackgroundView.setBlurRadius(radius);
        }
    }

    public void setDownSampleFactor(float factor) {
        if (mBlurredBackgroundView != null) {
            mBlurredBackgroundView.setDownSampleFactor(factor);
        }
    }

    private void blur() {
        mBlurredBackgroundLayoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        //evaluate top offset due to action bar, 0 if the actionBar should be blurred.
        int actionBarHeight;
        if (mBlurredActionBar) {
            actionBarHeight = 0;
        } else {
            actionBarHeight = getActionBarHeight();
        }
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                    || mHoldingActivity instanceof ActionBarActivity
                    || mHoldingActivity instanceof AppCompatActivity) {
                //add offset as top margin since actionBar height must also considered when we display
                // the blurred background. Don't want to draw on the actionBar.
                mBlurredBackgroundLayoutParams.setMargins(0, actionBarHeight, 0, 0);
                mBlurredBackgroundLayoutParams.gravity = Gravity.TOP;
            }
        } catch (NoClassDefFoundError e) {
            // no dependency to appcompat, that means no additional top offset due to actionBar.
            mBlurredBackgroundLayoutParams.setMargins(0, 0, 0, 0);
        }
        mBlurredBackgroundView = new BlurView(mHoldingActivity);
    }

    /**
     * Retrieve action bar height.
     *
     * @return action bar height in px.
     */
    private int getActionBarHeight() {
        int actionBarHeight = 0;

        try {
            if (mToolbar != null) {
                actionBarHeight = mToolbar.getHeight();
            } else if (mHoldingActivity instanceof ActionBarActivity) {
                ActionBar supportActionBar
                        = ((ActionBarActivity) mHoldingActivity).getSupportActionBar();
                if (supportActionBar != null) {
                    actionBarHeight = supportActionBar.getHeight();
                }
            } else if (mHoldingActivity instanceof AppCompatActivity) {
                ActionBar supportActionBar
                        = ((AppCompatActivity) mHoldingActivity).getSupportActionBar();
                if (supportActionBar != null) {
                    actionBarHeight = supportActionBar.getHeight();
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                android.app.ActionBar actionBar = mHoldingActivity.getActionBar();
                if (actionBar != null) {
                    actionBarHeight = actionBar.getHeight();
                }
            }
        } catch (NoClassDefFoundError e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                android.app.ActionBar actionBar = mHoldingActivity.getActionBar();
                if (actionBar != null) {
                    actionBarHeight = actionBar.getHeight();
                }
            }
        }
        return actionBarHeight;
    }

    /**
     * retrieve status bar height in px
     *
     * @return status bar height in px
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = mHoldingActivity.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mHoldingActivity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Retrieve offset introduce by the navigation bar.
     *
     * @return bottom offset due to navigation bar.
     */
    private int getNavigationBarOffset() {
        int result = 0;
        Resources resources = mHoldingActivity.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * Used to check if the status bar is translucent.
     *
     * @return true if the status bar is translucent.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isStatusBarTranslucent() {
        TypedValue typedValue = new TypedValue();
        int[] attribute = new int[]{android.R.attr.windowTranslucentStatus};
        TypedArray array = mHoldingActivity.obtainStyledAttributes(typedValue.resourceId, attribute);
        boolean isStatusBarTranslucent = array.getBoolean(0, false);
        array.recycle();
        return isStatusBarTranslucent;
    }
}
