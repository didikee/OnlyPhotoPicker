package com.github.didikee.photopicker.demo;

import com.github.didikee.photopicker.MultiPicPickerActivity;
import com.github.didikee.photopicker.helper.OnMultiActivityActionListener;

public class SelectActivity extends MultiPicPickerActivity {
    @Override
    protected OnMultiActivityActionListener setActivityActionListener() {
        return new OnMultiActivityActionListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onNext() {

            }
        };
    }




}
