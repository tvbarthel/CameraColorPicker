package fr.tvbarthel.apps.cameracolorpicker.wrappers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;

/**
 * A wrapper of {@link RecyclerView} that displays {@link ColorItem}s.
 */
public abstract class ColorItemListWrapper {

    protected final RecyclerView mRecyclerView;
    protected final ColorItemListWrapperListener mListener;

    protected ColorItemListWrapper(RecyclerView recyclerView, ColorItemListWrapperListener callback) {
        mRecyclerView = recyclerView;
        mListener = callback;
    }

    /**
     * Install a {@link RecyclerView.LayoutManager}
     * and a {@link RecyclerView.Adapter} on the {@link RecyclerView}.
     *
     * @return the {@link RecyclerView.Adapter} installed on the {@link RecyclerView}.
     */
    public abstract RecyclerView.Adapter installRecyclerView();

    /**
     * Set the {@link ColorItem}s.
     *
     * @param items a {@link List} of {@link ColorItem}s.
     */
    public abstract void setItems(List<ColorItem> items);

    /**
     * Add new {@link ColorItem}.
     *
     * @param colorJustAdded color item to add.
     */
    public void addItems(List<ColorItem> colorJustAdded) {
        mRecyclerView.scrollToPosition(0);
    }

    /**
     * An interface for listening to {@link ColorItemListWrapper}.
     */
    public interface ColorItemListWrapperListener {

        /**
         * Called when a {@link ColorItem} has just been clicked.
         *
         * @param colorItem    the {@link ColorItem} that has just been clicked.
         * @param colorPreview the {@link View} associated with the {@link ColorItem}. (for transition purpose)
         */
        void onColorItemClicked(@NonNull ColorItem colorItem, @NonNull View colorPreview);
    }


}
