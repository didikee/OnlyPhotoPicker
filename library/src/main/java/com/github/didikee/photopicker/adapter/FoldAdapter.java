package com.github.didikee.photopicker.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.didikee.photopicker.R;
import com.github.didikee.photopicker.Util;
import com.github.didikee.photopicker.bean.ImageFolder;
import com.github.didikee.photopicker.bean.ImageItem;
import com.github.didikee.photopicker.helper.OnSampleClickListener;

import java.util.ArrayList;


/**
 * Created by didikee on 2017/12/7.
 */

public class FoldAdapter extends RecyclerView.Adapter<FoldAdapter.ViewHolder> {
    private ArrayList<ImageFolder> mImageFolders;
    private Context mContext;
    private OnSampleClickListener<ImageFolder> mItemClickListener;
    private int mLastPosition = -1;
    private GradientDrawable mCheckDrawable;
    private GradientDrawable mIndicatorDrawable;

    public void setImageFolders(ArrayList<ImageFolder> imageFolders) {
        this.mImageFolders = imageFolders;
    }

    public void setOnItemClickListener(OnSampleClickListener<ImageFolder> itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public ImageFolder getCurrentSelectedFolder() {
        if (mLastPosition >= 0 && mLastPosition < getItemCount()) {
            return mImageFolders.get(mLastPosition);
        }
        return null;
    }

    public ArrayList<String> getSelectImage() {
        ArrayList<String> selectImagePaths = null;
        if (mImageFolders != null && mImageFolders.size() > 0) {
            selectImagePaths = new ArrayList<>();
            for (int i = 0; i < mImageFolders.size(); i++) {
                ImageFolder imageFolder = mImageFolders.get(i);
                ArrayList<ImageItem> images = imageFolder.images;
                if (images != null && images.size() > 0) {
                    for (ImageItem image : images) {
                        if (image.check) {
                            selectImagePaths.add(image.path);
                        }
                    }
                }
            }
        }
        return selectImagePaths;
    }

    public int getSelectImageCount() {
        int count = 0;
        if (mImageFolders != null && mImageFolders.size() > 0) {
            for (int i = 0; i < mImageFolders.size(); i++) {
                ImageFolder imageFolder = mImageFolders.get(i);
                ArrayList<ImageItem> images = imageFolder.images;
                if (images != null && images.size() > 0) {
                    for (ImageItem image : images) {
                        if (image.check) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public int getCurrentSelectedPosition() {
        return mLastPosition;
    }

    @Override
    public FoldAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        // init drawable
        int color = mContext.getResources().getColor(R.color.pp_theme_color);
        mCheckDrawable = new GradientDrawable();
        mCheckDrawable.setStroke(Util.dp2px(mContext, 2), color);

        mIndicatorDrawable = new GradientDrawable();
        mIndicatorDrawable.setCornerRadius(Util.dp2px(mContext, 8));
        mIndicatorDrawable.setColor(color);

        View folderView = LayoutInflater.from(mContext).inflate(R.layout.mmp_item_rv_folder, parent, false);
        return new ViewHolder(folderView);
    }

    @Override
    public void onBindViewHolder(final FoldAdapter.ViewHolder holder, int position) {
        final ImageFolder imageFolder = mImageFolders.get(position);
        ImageItem cover = imageFolder.cover;
        if (cover != null) {
            Glide.with(mContext).load(cover.path).into(holder.cover);
        }
        //theme
        holder.indicator.setBackgroundDrawable(mIndicatorDrawable);
        holder.checkView.setBackgroundDrawable(mCheckDrawable);

        //count
        String itemName = mContext.getResources().getString(R.string.mmp_pic_item);
        holder.count.setText(imageFolder.images.size() + itemName);
        holder.path.setText(imageFolder.name);
        final boolean isChecked = mLastPosition == position;
        holder.checkView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        int size = imageFolder.getSelectImages().size();
        if (size > 0) {
            holder.indicator.setVisibility(View.VISIBLE);
            holder.indicator.setText(String.valueOf(size));
        } else {
            holder.indicator.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked) {
                    imageFolder.check = true;
                    return;
                }
                holder.checkView.setVisibility(View.VISIBLE);
                if (mLastPosition >= 0 && mLastPosition < getItemCount()) {
                    mImageFolders.get(mLastPosition).check = false;
                    notifyItemChanged(mLastPosition);
                }
                mLastPosition = holder.getAdapterPosition();
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(v, imageFolder);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageFolders == null ? 0 : mImageFolders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView cover;
        private View checkView;
        private TextView count;
        private TextView path;
        private TextView indicator;

        public ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            checkView = itemView.findViewById(R.id.checkView);
            count = itemView.findViewById(R.id.count);
            path = itemView.findViewById(R.id.path);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }
}
