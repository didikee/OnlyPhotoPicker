package com.github.didikee.photopicker;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.github.didikee.photopicker.adapter.FoldAdapter;
import com.github.didikee.photopicker.adapter.ImageAdapter;
import com.github.didikee.photopicker.bean.ImageFolder;
import com.github.didikee.photopicker.helper.OnMultiActivityActionListener;
import com.github.didikee.photopicker.helper.OnSampleClickListener;
import com.github.didikee.photopicker.helper.OnStatusChangedListener;

import java.util.ArrayList;


/**
 * Created by didikee on 2017/12/7.
 */

public abstract class MultiPicPickerActivity extends AppCompatActivity implements ImageDataSource.OnImagesLoadedListener {
    private static final String TAG = "MultiPicPicker";

    private static final int DEFAULT_IMAGE_SIZE = 96;

    private OnMultiActivityActionListener activityActionListener;

    private RecyclerView mFoldRecyclerView;
    private RecyclerView mContentRecyclerView;
    private RecyclerView mSelectRecyclerView;
    private FoldAdapter mFolderAdapter;
    private ImageAdapter mImageAdapter;
    private int mGridSize;
    private String mSelectNamePrefix;

    private TextView folderNameTextView;
    private View folderLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpp_layout_multipicpicker);
        activityActionListener = setActivityActionListener();

        Intent intent = getIntent();
        boolean showGif = intent.getBooleanExtra(MultiPicPicker.SHOW_GIF, true);

        //actionbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mSelectNamePrefix = getResources().getString(R.string.mmp_selected);

        initView();

        new ImageDataSource(this, null, showGif, this);
    }

    private void initView() {
        mFoldRecyclerView = findViewById(R.id.recyclerViewLeft);
        mContentRecyclerView = findViewById(R.id.recyclerViewContent);

        folderNameTextView = findViewById(R.id.folder_name);
        folderLayout = findViewById(R.id.folder_layout);

        mFolderAdapter = new FoldAdapter();
        mFolderAdapter.setOnItemClickListener(folderItemClickListener);
        mFoldRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        final int offset = Util.dp2px(this, 2);
        mFoldRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (parent.getChildAdapterPosition(view) == 0) {

                }
                outRect.top = offset;
                outRect.bottom = offset;

            }
        });
        DefaultItemAnimator leftItemAnimator = new DefaultItemAnimator();
        leftItemAnimator.setSupportsChangeAnimations(false);
        mFoldRecyclerView.setItemAnimator(leftItemAnimator);
        mFoldRecyclerView.setAdapter(mFolderAdapter);


        //config image adapter
        Pair<Integer, Integer> windowPixels = Util.getWindowPixels(this);
        int phoneWidth = windowPixels.first;
        int defaultImageSize = Util.dp2px(this, DEFAULT_IMAGE_SIZE);
        mGridSize = phoneWidth / defaultImageSize;
        if (mGridSize < 2) {
            mGridSize = 2;
        }
        int rightOffsetSize = Util.dp2px(this, 2);
        int rightImageWidth = (phoneWidth / 2 - rightOffsetSize) / mGridSize;

        mImageAdapter = new ImageAdapter(rightImageWidth > defaultImageSize ? rightImageWidth : 0);
        mImageAdapter.setOnStatusChangeListener(contentAdapterStatusChangedListener);
        mContentRecyclerView.setLayoutManager(new GridLayoutManager(this, mGridSize, GridLayoutManager.VERTICAL, false));
        mContentRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int childAdapterPosition = parent.getChildAdapterPosition(view);
                outRect.top = offset;
                outRect.bottom = offset;
                outRect.left = offset;
                outRect.right = offset;
            }
        });
        mContentRecyclerView.setAdapter(mImageAdapter);
        updateTextUI();

        folderNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFolderLayout();
            }
        });
    }

    private void switchFolderLayout() {
        int height = folderLayout.getHeight();
        int contentHeight = mContentRecyclerView.getHeight();
        if (height == 0) {
            // open
            ValueAnimator openAnimator = ValueAnimator.ofInt(0, contentHeight);
            openAnimator.setDuration(250);
            openAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int height = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = folderLayout.getLayoutParams();
                    layoutParams.height = height;
                    folderLayout.setLayoutParams(layoutParams);
                }
            });
            openAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            openAnimator.start();
        } else {
            // close
            ValueAnimator openAnimator = ValueAnimator.ofInt(contentHeight, 0);
            openAnimator.setDuration(250);
            openAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int height = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = folderLayout.getLayoutParams();
                    layoutParams.height = height;
                    folderLayout.setLayoutParams(layoutParams);
                }
            });
            openAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            openAnimator.start();
        }
    }

    @Override
    public void onImagesLoaded(ArrayList<ImageFolder> imageFolders) {
        Log.d(TAG, "onImagesLoaded: " + imageFolders.size());
        mFolderAdapter.setImageFolders(imageFolders);
        mFolderAdapter.notifyDataSetChanged();

        //TODO auto click to init right side list
        mFoldRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                //fix bug: when no media viewHolder.itemView is null
                RecyclerView.ViewHolder viewHolder = mFoldRecyclerView.findViewHolderForAdapterPosition(0);
                if (viewHolder == null || viewHolder.itemView == null) {
                    Toast.makeText(getApplicationContext(), "No image here", Toast.LENGTH_LONG).show();
                } else {
                    viewHolder.itemView.performClick();
                }
            }
        });
    }

    private OnSampleClickListener<ImageFolder> folderItemClickListener = new OnSampleClickListener<ImageFolder>() {
        @Override
        public void onClick(View view, ImageFolder imageFolder) {
            mImageAdapter.setImageItems(imageFolder.images);
            //要初始化默认值
            mImageAdapter.notifyDataSetChanged();
            //关闭folder layout
            switchFolderLayout();
        }
    };


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.mmp_action, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == R.id.action_done) {
//            //TODO done
//            ArrayList<String> selectImage = mFolderAdapter.getSelectImage();
//            if (selectImage != null) {
//                Log.d(TAG, "selectImage size: " + selectImage.size());
//            }
//            finishWithResult(selectImage);
//        } else if (itemId == R.id.action_select_all) {
//            mImageAdapter.selectAll();
//            statusChanged();
//            return true;
//        } else if (itemId == R.id.action_select_none) {
//            mImageAdapter.selectNone();
//            statusChanged();
//            return true;
//        } else if (itemId == R.id.action_reverse) {
//            mImageAdapter.reverse();
//            statusChanged();
//            return true;
//        } else if (itemId == android.R.id.home) {
//            exit();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void finishWithResult(ArrayList<String> resultData) {
        if (resultData != null && resultData.size() > 0) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(MultiPicPicker.DATA, resultData);
            setResult(MultiPicPicker.RESULT_OK, resultIntent);
        } else {
            setResult(MultiPicPicker.RESULT_CANCEL);
        }
        finish();
    }

    public void updateTextUI() {
        //获取总数量
        int selectImageCount = mFolderAdapter.getSelectImageCount();
        setTitle(mSelectNamePrefix + "(" + selectImageCount + ")");
    }

    private void exit() {
        if (mFolderAdapter.getSelectImageCount() > 0) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mmp_exit_warning)
                    .setPositiveButton(R.string.mmp_discard, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.mmp_cancel, null)
                    .show();
        } else {
            finishWithResult(null);
        }

    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private OnStatusChangedListener contentAdapterStatusChangedListener =new OnStatusChangedListener() {
        @Override
        public void statusChanged() {
            int currentSelectedPosition = mFolderAdapter.getCurrentSelectedPosition();
            if (currentSelectedPosition >= 0) {
                mFolderAdapter.notifyItemChanged(currentSelectedPosition);
                updateTextUI();
            }
        }
    };

    protected abstract OnMultiActivityActionListener setActivityActionListener();
}
