package com.github.didikee.photopicker.helper;


import com.github.didikee.photopicker.bean.ImageItem;

/**
 * Created by didikee on 2017/12/8.
 */

public interface OnImageStatusChangedListener {
    void select(ImageItem item);
    void unselected(ImageItem item);
}
