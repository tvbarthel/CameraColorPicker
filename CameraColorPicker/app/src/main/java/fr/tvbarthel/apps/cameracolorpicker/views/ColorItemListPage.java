package fr.tvbarthel.apps.cameracolorpicker.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

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
    }

    @Override
    protected void onDetachedFromWindow() {
        ColorItems.unregisterListener(getContext(), mOnColorItemChangeListener);
        super.onDetachedFromWindow();
    }

    private void init(Context context) {
        final View view = LayoutInflater.from(context).inflate(R.layout.view_color_item_list_page, this, true);
        final View emptyView = view.findViewById(R.id.view_color_item_list_page_empty_view);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.view_color_item_list_page_list_view);
        mColorItemListWrapper = new FlavorColorItemListWrapper(recyclerView, this);

        final RecyclerView.Adapter adapter = mColorItemListWrapper.installRecyclerView();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                emptyView.setVisibility(adapter.getItemCount() == 0 ? VISIBLE : GONE);
            }
        });

        mColorItemListWrapper.setItems(ColorItems.getSavedColorItems(context));
        mOnColorItemChangeListener = new ColorItems.OnColorItemChangeListener() {
            @Override
            public void onColorItemChanged(List<ColorItem> colorItems) {
                mColorItemListWrapper.setItems(colorItems);
            }
        };
    }


    @Override
    public void onColorItemClicked(@NonNull ColorItem colorItem, @NonNull View colorPreview) {
        ColorDetailActivity.startWithColorItem(getContext(), colorItem, colorPreview);
    }
}
