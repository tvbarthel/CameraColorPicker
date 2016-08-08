package fr.tvbarthel.apps.cameracolorpicker.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.wrappers.ColorItemListWrapper;

/**
 * A flavor implementation of {@link ColorItemListWrapper}.
 */
public class FlavorColorItemListWrapper extends ColorItemListWrapper implements ColorItemAdapter.ColorItemAdapterListener {

    private final ColorItemAdapter mAdapter;
    private final int mSpanCount;

    public FlavorColorItemListWrapper(RecyclerView recyclerView, ColorItemListWrapperListener callback) {
        super(recyclerView, callback);
        mAdapter = new ColorItemAdapter(this);
        mSpanCount = recyclerView.getContext().getResources().getInteger(R.integer.list_horizontal_span);
    }

    @Override
    public void onColorItemClicked(@NonNull ColorItem colorItem, @NonNull View colorPreview) {
        mListener.onColorItemClicked(colorItem, colorPreview);
    }

    @Override
    public RecyclerView.Adapter installRecyclerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mRecyclerView.getContext(), mSpanCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
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
}
