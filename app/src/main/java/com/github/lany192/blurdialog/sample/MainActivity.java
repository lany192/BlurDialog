package com.github.lany192.blurdialog.sample;

import android.os.Bundle;

import com.lany.box.activity.BaseActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected boolean hasBackBtn() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle bundle) {

    }

    @OnClick(R.id.button)
    void buttonClicked() {
        SampleDialogFragment dialogFragment = SampleDialogFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), TAG);
    }

    @OnClick(R.id.button2)
    void button2Clicked() {
        SampleBottomDialogFragment dialogFragment = SampleBottomDialogFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), TAG);
    }
}
