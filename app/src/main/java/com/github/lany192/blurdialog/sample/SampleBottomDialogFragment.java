package com.github.lany192.blurdialog.sample;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.github.lany192.blurdialog.BlurBottomDialogFragment;

public class SampleBottomDialogFragment extends BlurBottomDialogFragment {

    public static SampleBottomDialogFragment newInstance() {
        return new SampleBottomDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.bottom_dialog_layout, container, false);
        return view;
    }

    @Override
    protected boolean isDimmingEnable() {
        return true;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }
//
//    @Override
//    protected float getDownScaleFactor() {
//        return 8;
//    }
//
//    @Override
//    protected int getBlurRadius() {
//        return 2;
//    }
}
