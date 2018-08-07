package com.github.didikee.photopicker;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by didikee on 2017/12/7.
 */

public class MultiPicPicker {

    public static final String SHOW_GIF = "show_gif";
    public static final int RESULT_OK = 200;
    public static final int RESULT_CANCEL = 201;
    public static final String DATA = "data";

    private MultiPicPicker() {
    }

    public static class Builder {
        private boolean showGIF;
        private int requestCode = 202;
        private Activity context;

        public Builder(Activity context) {
            this.context = context;
        }

        private boolean isShowGIF() {
            return showGIF;
        }

        public Builder showGIF(boolean showGIF) {
            this.showGIF = showGIF;
            return this;
        }

        public Builder requestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public void start() {
            Intent intent = new Intent(context, MultiPicPickerActivity.class);
            intent.putExtra(SHOW_GIF, showGIF);
            context.startActivityForResult(intent, requestCode);
        }
    }
}
