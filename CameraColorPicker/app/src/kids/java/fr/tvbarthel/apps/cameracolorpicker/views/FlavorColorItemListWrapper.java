package fr.tvbarthel.apps.cameracolorpicker.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.wrappers.ColorItemListWrapper;

/**
 * A flavor implementation of {@link ColorItemListWrapper}.
 */
public class FlavorColorItemListWrapper extends ColorItemListWrapper implements ColorItemAdapter.ColorItemAdapterListener {

    private final ColorItemAdapter mAdapter;

    public FlavorColorItemListWrapper(RecyclerView recyclerView, ColorItemListWrapperListener callback) {
        super(recyclerView, callback);
        mAdapter = new ColorItemAdapter(this);
    }

    @Override
    public void onColorItemClicked(@NonNull ColorItem colorItem, @NonNull View colorPreview) {
        mListener.onColorItemClicked(colorItem, colorPreview);
    }

    @Override
    public RecyclerView.Adapter installRecyclerView() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mRecyclerView.getContext(), 5);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }

    @Override
    public void setItems(List<ColorItem> items) {
        mAdapter.setItems(items);
    }
}
