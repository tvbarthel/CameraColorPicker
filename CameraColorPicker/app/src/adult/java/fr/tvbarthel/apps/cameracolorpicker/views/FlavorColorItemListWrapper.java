package fr.tvbarthel.apps.cameracolorpicker.views;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.utils.ClipDatas;
import fr.tvbarthel.apps.cameracolorpicker.wrappers.ColorItemListWrapper;

/**
 * A flavor implementation of {@link ColorItemListWrapper}.
 */
public class FlavorColorItemListWrapper extends ColorItemListWrapper implements ColorItemAdapter.ColorItemAdapterListener {

    private final Context mContext;
    private final ColorItemAdapter mAdapter;
    private final String mClipColorItemLabel;
    private final Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;
    private Toast mToast;

    public FlavorColorItemListWrapper(RecyclerView recyclerView, ColorItemListWrapperListener callback) {
        super(recyclerView, callback);
        mContext = recyclerView.getContext();
        mAdapter = new ColorItemAdapter(this);
        mClipColorItemLabel = mContext.getString(R.string.color_clip_color_label_hex);

        // Create an ActivityLifecycleCallbacks to hide the eventual toast that could be displayed when the
        // hosting activity goes on pause.
        mActivityLifecycleCallbacks = createToastWatcher();
        recyclerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                ((Application) mContext.getApplicationContext()).registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                ((Application) mContext.getApplicationContext()).unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            }
        });
    }

    @Override
    public void onColorItemClicked(@NonNull ColorItem colorItem, @NonNull View colorPreview) {
        mListener.onColorItemClicked(colorItem, colorPreview);
    }


    @Override
    public void onColorItemLongClicked(@NonNull ColorItem colorItem) {
        ClipDatas.clipPainText(mContext, mClipColorItemLabel, colorItem.getHexString());
        showToast(R.string.color_clip_success_copy_message);
    }

    @Override
    public RecyclerView.Adapter installRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }

    @Override
    public void setItems(List<ColorItem> items) {
        mAdapter.setItems(items);
    }

    @Override
    public void addItems(List<ColorItem> colorJustAdded) {
        super.addItems(colorJustAdded);
        mAdapter.addItems(colorJustAdded);
    }

    /**
     * Hide the current {@link android.widget.Toast}.
     */
    private void hideToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    /**
     * Show a toast text message.
     *
     * @param resId The resource id of the string resource to use.
     */
    private void showToast(@StringRes int resId) {
        hideToast();
        mToast = Toast.makeText(mContext, resId, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * Create a {@link android.app.Application.ActivityLifecycleCallbacks} that will hide {@link FlavorColorItemListWrapper#mToast}
     * when the activity associated with the recycler view is paused.
     *
     * @return a newly created {@link android.app.Application.ActivityLifecycleCallbacks}
     */
    @NonNull
    private Application.ActivityLifecycleCallbacks createToastWatcher() {
        return new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (mContext == activity) {
                    hideToast();
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        };
    }
}
