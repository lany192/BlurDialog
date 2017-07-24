package com.github.lany192.blurdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

public abstract class BlurDialogFragment extends DialogFragment {
    private BlurEngine mBlurEngine;
    private Toolbar mToolbar;
    private boolean mDimmingEffect;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mBlurEngine != null) {
            mBlurEngine.onAttach(activity); // re attached
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlurEngine = new BlurEngine(getActivity());
        if (mToolbar != null) {
            mBlurEngine.setToolbar(mToolbar);
        }
        int radius = getBlurRadius();
        if (radius <= 0) {
            throw new IllegalArgumentException("Blur radius must be strictly positive. Found : " + radius);
        }
        float factor = getDownScaleFactor();
        if (factor <= 1.0) {
            throw new IllegalArgumentException("Down scale must be strictly greater than 1.0. Found : " + factor);
        }
        mBlurEngine.setBlurActionBar(isActionBarBlurred());
        mBlurEngine.setBlurRadius(radius);
        mBlurEngine.setDownScaleFactor(factor);
        mDimmingEffect = isDimmingEnable();
    }

    @Override
    public void onStart() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            // enable or disable dimming effect.
            if (!mDimmingEffect) {
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBlurEngine.onResume(getRetainInstance());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mBlurEngine.onDismiss();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBlurEngine.onDetach();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public void setToolbar(Toolbar toolBar) {
        mToolbar = toolBar;
        if (mBlurEngine != null) {
            mBlurEngine.setToolbar(toolBar);
        }
    }

    protected float getDownScaleFactor() {
        return BlurEngine.DEFAULT_BLUR_DOWN_SCALE_FACTOR;
    }

    protected int getBlurRadius() {
        return BlurEngine.DEFAULT_BLUR_RADIUS;
    }

    protected boolean isDimmingEnable() {
        return BlurEngine.DEFAULT_DIMMING_POLICY;
    }

    protected boolean isActionBarBlurred() {
        return BlurEngine.DEFAULT_ACTION_BAR_BLUR;
    }
}
