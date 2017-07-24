package com.github.lany192.blurdialog;

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

class BlurEngine {
    static final float DEFAULT_BLUR_DOWN_SCALE_FACTOR = 4.0f;
    static final int DEFAULT_BLUR_RADIUS = 8;
    static final boolean DEFAULT_DIMMING_POLICY = false;
    static final boolean DEFAULT_ACTION_BAR_BLUR = false;
    private BlurView mBlurView;
    private FrameLayout.LayoutParams mBlurViewLayoutParams;
    private Activity mActivity;
    private Toolbar mToolbar;
    private boolean mBlurredActionBar;

    public BlurEngine(Activity activity) {
        mActivity = activity;
    }

    public void onAttach(Activity activity) {
        mActivity = activity;
    }

    public void onResume(boolean retainedInstance) {
        if (mBlurView == null || retainedInstance) {
            if (mActivity.getWindow().getDecorView().isShown()) {
                blur();
                mActivity.getWindow().addContentView(mBlurView, mBlurViewLayoutParams);
            } else {
                mActivity.getWindow().getDecorView().getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                // dialog can have been closed before being drawn
                                if (mActivity != null) {
                                    mActivity.getWindow().getDecorView().getViewTreeObserver().removeOnPreDrawListener(this);
                                    blur();
                                    mActivity.getWindow().addContentView(mBlurView, mBlurViewLayoutParams);
                                }
                                return true;
                            }
                        }
                );
            }
        }
    }

    public void onDismiss() {
        if (mBlurView != null) {
            ViewGroup parent = (ViewGroup) mBlurView.getParent();
            if (parent != null) {
                parent.removeView(mBlurView);
            }
            mBlurView = null;
        }
    }

    public void onDetach() {
        mActivity = null;
    }

    public void setBlurActionBar(boolean enable) {
        mBlurredActionBar = enable;
    }

    public void setToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
    }

    public void setBlurRadius(float radius) {
        if (mBlurView != null) {
            mBlurView.setBlurRadius(radius);
        }
    }

    public void setDownScaleFactor(float factor) {
        if (mBlurView != null) {
            mBlurView.setDownScaleFactor(factor);
        }
    }

    private void blur() {
        mBlurViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        //evaluate top offset due to action bar, 0 if the actionBar should be blurred.
        int actionBarHeight;
        if (mBlurredActionBar) {
            actionBarHeight = 0;
        } else {
            actionBarHeight = getActionBarHeight();
        }
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                    || mActivity instanceof ActionBarActivity
                    || mActivity instanceof AppCompatActivity) {
                //add offset as top margin since actionBar height must also considered when we display
                // the blurred background. Don't want to draw on the actionBar.
                mBlurViewLayoutParams.setMargins(0, actionBarHeight, 0, 0);
                mBlurViewLayoutParams.gravity = Gravity.TOP;
            }
        } catch (NoClassDefFoundError e) {
            // no dependency to appcompat, that means no additional top offset due to actionBar.
            mBlurViewLayoutParams.setMargins(0, 0, 0, 0);
        }
        mBlurView = new BlurView(mActivity);
    }

    private int getActionBarHeight() {
        int actionBarHeight = 0;

        try {
            if (mToolbar != null) {
                actionBarHeight = mToolbar.getHeight();
            } else if (mActivity instanceof ActionBarActivity) {
                ActionBar supportActionBar = ((ActionBarActivity) mActivity).getSupportActionBar();
                if (supportActionBar != null) {
                    actionBarHeight = supportActionBar.getHeight();
                }
            } else if (mActivity instanceof AppCompatActivity) {
                ActionBar supportActionBar = ((AppCompatActivity) mActivity).getSupportActionBar();
                if (supportActionBar != null) {
                    actionBarHeight = supportActionBar.getHeight();
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                android.app.ActionBar actionBar = mActivity.getActionBar();
                if (actionBar != null) {
                    actionBarHeight = actionBar.getHeight();
                }
            }
        } catch (NoClassDefFoundError e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                android.app.ActionBar actionBar = mActivity.getActionBar();
                if (actionBar != null) {
                    actionBarHeight = actionBar.getHeight();
                }
            }
        }
        return actionBarHeight;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = mActivity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mActivity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getNavigationBarOffset() {
        int result = 0;
        Resources resources = mActivity.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isStatusBarTranslucent() {
        TypedValue typedValue = new TypedValue();
        int[] attribute = new int[]{android.R.attr.windowTranslucentStatus};
        TypedArray array = mActivity.obtainStyledAttributes(typedValue.resourceId, attribute);
        boolean isStatusBarTranslucent = array.getBoolean(0, false);
        array.recycle();
        return isStatusBarTranslucent;
    }
}
