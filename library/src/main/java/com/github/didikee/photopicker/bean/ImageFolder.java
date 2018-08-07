package com.github.didikee.photopicker.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by didikee on 2017/12/7.
 */

public class ImageFolder implements Serializable {

    public String name;  //当前文件夹的名字
    public String path;  //当前文件夹的路径
    public ImageItem cover;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    public ArrayList<ImageItem> images;  //当前文件夹下所有图片的集合

    public boolean check; //当前文件夹是否选中

    /** 只要文件夹的路径和名字相同，就认为是相同的文件夹 */
    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }

    public ArrayList<ImageItem> getSelectImages() {
        ArrayList<ImageItem> temp = new ArrayList<>();
        for (ImageItem image : images) {
            if (image.check) {
                temp.add(image);
            }
        }
        return temp;
    }
}
