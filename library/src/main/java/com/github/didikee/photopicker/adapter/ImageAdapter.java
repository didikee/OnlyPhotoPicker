package com.github.didikee.photopicker.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.didikee.photopicker.R;
import com.github.didikee.photopicker.Util;
import com.github.didikee.photopicker.bean.ImageItem;
import com.github.didikee.photopicker.helper.OnStatusChangedListener;

import java.util.ArrayList;


/**
 * Created by didikee on 2017/12/7.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private int mItemSize;
    private Context mContext;
    private ArrayList<ImageItem> mImageItems;
    private OnStatusChangedListener mStatusChangeListener;
    private Drawable mCheckedDrawable;
    private Drawable mUnCheckDrawable;


    public ImageAdapter(int itemWidth) {
        this.mItemSize = itemWidth;
    }

    public void setImageItems(ArrayList<ImageItem> imageItems) {
        this.mImageItems = imageItems;
    }

    public void setOnStatusChangeListener(OnStatusChangedListener statusChangeListener) {
        this.mStatusChangeListener = statusChangeListener;
    }

//    public void refreshConfig(ArrayList<ImageItem> imageItems) {
//        if (imageItems == null || imageItems.size() == 0) {
//            mSelectItems.clear();
//        } else {
//            mSelectItems.clear();
//            mSelectItems.addAll(imageItems);
//        }
//    }

    public void selectAll() {
        //TODO 当前文件夹中的全部图片标记为选中
        if (mImageItems != null && mImageItems.size() > 0) {
            for (int i = 0; i < mImageItems.size(); i++) {
                mImageItems.get(i).check = true;
            }
        }
        notifyDataSetChanged();
    }

    public void selectNone() {
        //TODO 当前文件夹中的全部图片标记为未选中
        if (mImageItems != null && mImageItems.size() > 0) {
            for (int i = 0; i < mImageItems.size(); i++) {
                mImageItems.get(i).check = false;
            }
        }
        notifyDataSetChanged();
    }

    public void reverse() {
        //TODO 当前文件夹中的照片状态切换（选中--> 未选中,未选中-->选中）
        if (mImageItems != null && mImageItems.size() > 0) {
            for (int i = 0; i < mImageItems.size(); i++) {
                boolean check = mImageItems.get(i).check;
                mImageItems.get(i).check = !check;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        //drawable
        int color = mContext.getResources().getColor(R.color.pp_theme_color);
        mCheckedDrawable = Util.tintDrawable(mContext.getResources().getDrawable(R.drawable.mmp_check_circle), ColorStateList.valueOf(color));
        mUnCheckDrawable = mContext.getResources().getDrawable(R.drawable.mmp_uncheck);
//        mUnCheckDrawable =new GradientDrawable();
//        mUnCheckDrawable.setShape(GradientDrawable.RING);
//        mUnCheckDrawable.setUseLevel(false);
//        int size = Util.dp2px(mContext, 24);
//        mUnCheckDrawable.setSize(size,size);
//        mUnCheckDrawable.setColor(Color.parseColor("#E4007F"));
//        mUnCheckDrawable.set

        View imageLayout = LayoutInflater.from(mContext).inflate(R.layout.mmp_item_rv_image, parent, false);
        if (mItemSize > 0) {
            View imageView = imageLayout.findViewById(R.id.image);
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = mItemSize;
            layoutParams.height = mItemSize;
            imageView.setLayoutParams(layoutParams);
        }
        return new ViewHolder(imageLayout);
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.ViewHolder holder, int position) {
        final ImageItem imageItem = mImageItems.get(position);
        if (imageItem != null) {
            Glide.with(mContext).load(imageItem.path).into(holder.imageView);
            setCheckView(holder.checkView, imageItem.check);

            holder.checkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageItem.check = !imageItem.check;
                    setCheckView(holder.checkView, imageItem.check);
//                    if (mSelectItems.contains(imageItem)) {
//                        mSelectItems.remove(imageItem);
//                        if (mStatusChangeListener != null) {
//                            mStatusChangeListener.unselected(imageItem);
//                        }
//                    } else {
//                        mSelectItems.add(imageItem);
//                        if (mStatusChangeListener != null) {
//                            mStatusChangeListener.select(imageItem);
//                        }
//                    }
                    if (mStatusChangeListener != null) {
                        mStatusChangeListener.statusChanged();
                    }

                }
            });
        }
    }

    private void setCheckView(ImageView checkView, boolean check) {
//        checkView.setImageResource(check ? R.drawable.ic_checked : R.drawable.ic_uncheck);
        checkView.setImageDrawable(check ? mCheckedDrawable : mUnCheckDrawable);

    }

    @Override
    public int getItemCount() {
        return mImageItems == null ? 0 : mImageItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageView checkView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            checkView = itemView.findViewById(R.id.checkView);
        }
    }

}
