package fr.tvbarthel.apps.cameracolorpicker.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.activities.ColorDetailActivity;
import fr.tvbarthel.apps.cameracolorpicker.adapters.ColorItemAdapter;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItems;
import fr.tvbarthel.apps.cameracolorpicker.utils.ClipDatas;

/**
 * A simple {@link FrameLayout} used in the {@link android.support.v4.view.ViewPager} of the {@link fr.tvbarthel.apps.cameracolorpicker.activities.MainActivity}
 * to display the list of the {@link ColorItem}s that the user created.
 */
public class ColorItemListPage extends FrameLayout {

    /**
     * A {@link ColorItemAdapter} used for adapting the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}s.
     */
    private ColorItemAdapter mColorItemAdapter;

    /**
     * A {@link android.widget.ListView} used for displaying the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}s
     */
    private ListView mListView;

    /**
     * A {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItems.OnColorItemChangeListener} for listening the creation of new {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}s.
     */
    private ColorItems.OnColorItemChangeListener mOnColorItemChangeListener;

    /**
     * The user-visible label for the clip {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     */
    private String mClipColorItemLabel;

    /**
     * A reference to the current {@link android.widget.Toast}.
     * <p/>
     * Used for hiding the current {@link android.widget.Toast} before showing a new one or the activity is paused.
     * {@link }
     */
    private Toast mToast;

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;

    public ColorItemListPage(Context context) {
        super(context);
        init(context);
    }

    public ColorItemListPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorItemListPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorItemListPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ColorItems.registerListener(getContext(), mOnColorItemChangeListener);
        ((Application) getContext().getApplicationContext()).registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    @Override
    protected void onDetachedFromWindow() {
        ColorItems.unregisterListener(getContext(), mOnColorItemChangeListener);
        ((Application) getContext().getApplicationContext()).unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        super.onDetachedFromWindow();
    }

    private void init(Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.view_color_item_list_page, this, true);

        mClipColorItemLabel = context.getString(R.string.color_clip_color_label_hex);

        mColorItemAdapter = new ColorItemAdapter(context);
        mColorItemAdapter.addAll(ColorItems.getSavedColorItems(context));
        final View emptyView = view.findViewById(R.id.view_color_item_list_page_empty_view);
        mListView = (ListView) view.findViewById(R.id.view_color_item_list_page_list_view);
        mListView.setAdapter(mColorItemAdapter);
        mListView.setEmptyView(emptyView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ColorItem colorItem = mColorItemAdapter.getItem(position);
                ColorDetailActivity.startWithColorItem(view.getContext(), colorItem,
                        view.findViewById(R.id.row_color_item_preview));
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ColorItem colorItem = mColorItemAdapter.getItem(position);
                ClipDatas.clipPainText(view.getContext(), mClipColorItemLabel, colorItem.getHexString());
                showToast(R.string.color_clip_success_copy_message);
                return true;
            }
        });


        mOnColorItemChangeListener = new ColorItems.OnColorItemChangeListener() {
            @Override
            public void onColorItemChanged(List<ColorItem> colorItems) {
                mColorItemAdapter.clear();
                mColorItemAdapter.addAll(colorItems);
                mColorItemAdapter.notifyDataSetChanged();
            }
        };

        // Create an ActivityLifecycleCallbacks to hide the eventual toast that could be displayed when the
        // hosting activity goes on pause.
        mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
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
                if (getContext() == activity) {
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
        mToast = Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
