package fr.tvbarthel.apps.cameracolorpicker.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.activities.ColorDetailActivity;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItems;
import fr.tvbarthel.apps.cameracolorpicker.wrappers.ColorItemListWrapper;

/**
 * A simple {@link FrameLayout} used in the {@link android.support.v4.view.ViewPager} of the {@link fr.tvbarthel.apps.cameracolorpicker.activities.MainActivity}
 * to display the list of the {@link ColorItem}s that the user created.
 */
public class ColorItemListPage extends FrameLayout implements ColorItemListWrapper.ColorItemListWrapperListener {

    /**
     * A {@link ColorItemListWrapper}.
     */
    private ColorItemListWrapper mColorItemListWrapper;

    /**
     * A {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItems.OnColorItemChangeListener} for listening the creation of new {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}s.
     */
    private ColorItems.OnColorItemChangeListener mOnColorItemChangeListener;

    /**
     * Internal listener used to catch view events.
     */
    private OnClickListener internalListener;

    /**
     * Current listener used to catch view events.
     */
    private Listener listener;

    /**
     * Current colors displayed.
     */
    private List<ColorItem> currentColors;

    /**
     * Color added while the holding activity was paused.
     */
    private ArrayList<ColorItem> colorJustAdded;

    /**
     * Listener used to catch holding activity resume event.
     */
    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;

    /**
     * Used to kno if the holding activity is paused.
     */
    private boolean isHoldingActivityPaused;

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
        ((Application) getContext().getApplicationContext()).registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    @Override
    protected void onDetachedFromWindow() {
        ColorItems.unregisterListener(getContext(), mOnColorItemChangeListener);
        ((Application) getContext().getApplicationContext()).unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        super.onDetachedFromWindow();
    }

    /**
     * Listener used to catch view events.
     *
     * @param listener listener used to catch view events.
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void init(Context context) {

        initLifeCycleListener();

        initInternalListener();

        final View view = LayoutInflater.from(context).inflate(R.layout.view_color_item_list_page, this, true);
        final View emptyView = view.findViewById(R.id.view_color_item_list_page_empty_view);
        emptyView.setOnClickListener(internalListener);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.view_color_item_list_page_list_view);
        mColorItemListWrapper = new FlavorColorItemListWrapper(recyclerView, this);

        final RecyclerView.Adapter adapter = mColorItemListWrapper.installRecyclerView();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                emptyView.setVisibility(adapter.getItemCount() == 0 ? VISIBLE : GONE);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                emptyView.setVisibility(adapter.getItemCount() == 0 ? VISIBLE : GONE);
            }
        });

        colorJustAdded = new ArrayList<>();
        currentColors = ColorItems.getSavedColorItems(context);
        mColorItemListWrapper.setItems(currentColors);
        mOnColorItemChangeListener = new ColorItems.OnColorItemChangeListener() {
            @Override
            public void onColorItemChanged(List<ColorItem> colorItems) {
                if (!isHoldingActivityPaused) {
                    mColorItemListWrapper.setItems(colorItems);
                } else {
                    if (colorItems.size() < currentColors.size()) {
                        // color deleted, reload full list.
                        mColorItemListWrapper.setItems(colorItems);
                    } else {
                        // color added.
                        colorJustAdded.clear();
                        for (int i = 0; i < colorItems.size() - currentColors.size(); i++) {
                            colorJustAdded.add(colorItems.get(i));
                        }
                    }
                }
            }
        };

    }

    private void initLifeCycleListener() {
        isHoldingActivityPaused = false;
        activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (getContext() == activity) {
                    onHoldingActivityResumed();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (getContext() == activity) {
                    onHoldingActivityPaused();
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

    private void onHoldingActivityResumed() {
        isHoldingActivityPaused = false;
        if (colorJustAdded.size() > 0) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mColorItemListWrapper.addItems(colorJustAdded);
                    currentColors.addAll(colorJustAdded);
                    colorJustAdded.clear();
                }
            }, 500);
        }
    }

    private void onHoldingActivityPaused() {
        isHoldingActivityPaused = true;
    }

    /**
     * Initialize listener used internally to avoid exposing onXXX to user of {@link ColorItemListPage}
     */
    private void initInternalListener() {
        internalListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEmphasisOnAddColorActionRequested();
                }
            }
        };
    }


    @Override
    public void onColorItemClicked(@NonNull ColorItem colorItem, @NonNull View colorPreview) {
        ColorDetailActivity.startWithColorItem(getContext(), colorItem, colorPreview);
    }

    /**
     * Listener used to catch view events.
     */
    public interface Listener {
        /**
         * Called when the user request emphasis on the add color action.
         * <p/>
         * Currently, when user touch the empty view.
         */
        void onEmphasisOnAddColorActionRequested();
    }
}
