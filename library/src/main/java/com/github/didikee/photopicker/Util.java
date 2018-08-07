package com.github.didikee.photopicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.WindowManager;

/**
 * Created by didik on 2016/7/30.
 * 关于dp px sp 转换
 */
public class Util {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param context context
     *            （DisplayMetrics类中属性density）
     * @return float cast to int
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param context context
     *            （DisplayMetrics类中属性density）
     * @return float cast to int
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px单位转换为sp
     *
     * @param pxValue need px
     * @param context context
     *         DisplayMetrics类中属性scaledDensity
     * @return float cast to int
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue need sp
     * @param context context
     *         DisplayMetrics类中属性scaledDensity
     * @return float cast to int
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取系统状态栏高度
     * @param context context
     * @return statusBar height
     */
    public static int getSystemStatusBarHeight(@NonNull Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取屏幕的宽高
     * @param context
     * @return
     */
    public static Pair<Integer, Integer> getWindowPixels(@NonNull Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics =new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return new Pair<Integer,Integer>(displayMetrics.widthPixels,displayMetrics.heightPixels);
    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
}
