package com.github.lany192.blurdialog.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.lany192.blurdialog.BlurDialogFragment;

public class SampleDialogFragment extends BlurDialogFragment {

    public static SampleDialogFragment newInstance() {
        SampleDialogFragment fragment = new SampleDialogFragment();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Burl dialog");
        builder.setMessage("Do you know?");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    @Override
    protected boolean isDimmingEnable() {
        return true;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return false;
    }

    @Override
    protected float getDownScaleFactor() {
        return 4;
    }

    @Override
    protected int getBlurRadius() {
        return 2;
    }
}
