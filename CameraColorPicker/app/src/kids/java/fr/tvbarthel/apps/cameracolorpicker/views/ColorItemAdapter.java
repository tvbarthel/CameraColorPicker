package fr.tvbarthel.apps.cameracolorpicker.views;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;

/**
 * A {@link android.support.v7.widget.RecyclerView.Adapter} that adapts {@link ColorItem}s
 * into {@link fr.tvbarthel.apps.cameracolorpicker.R.layout#row_color_item}
 */
/* package */
class ColorItemAdapter extends RecyclerView.Adapter<ColorItemAdapter.ColorItemHolder> {

    private final List<ColorItem> mItems;
    private final ColorItemAdapterListener mListener;

    /* package */
    ColorItemAdapter(ColorItemAdapterListener listener) {
        this.mListener = listener;
        this.mItems = new ArrayList<>();
    }

    @Override
    public ColorItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_color_item, parent, false);
        return new ColorItemHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(ColorItemHolder holder, int position) {
        final ColorItem colorItem = mItems.get(position);
        holder.bind(colorItem);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /* package */
    void setItems(List<ColorItem> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    /* package */
    void addItems(List<ColorItem> colorJustAdded) {
        for (int i = colorJustAdded.size() - 1; i >= 0; i--) {
            mItems.add(0, colorJustAdded.get(i));
        }
        notifyItemRangeInserted(0, colorJustAdded.size());
    }

    /**
     * An interface for listening to {@link ColorItemAdapter} callbacks.
     */
    /* package */
    interface ColorItemAdapterListener {

        /**
         * Called when a {@link ColorItem} has just been clicked.
         *
         * @param colorItem    the {@link ColorItem}.
         * @param colorPreview the color preview.
         */
        void onColorItemClicked(@NonNull ColorItem colorItem, @NonNull View colorPreview);
    }

    /**
     * A simple {@link RecyclerView.ViewHolder} associated with {@link R.layout#row_color_item}.
     */
    public static class ColorItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /**
         * The {@link View} to show a preview of the color item.
         */
        private final View mColorPreview;

        /**
         * The underlying {@link View}
         */
        private final View mUnderlyingView;

        /**
         * A {@link ColorItemAdapterListener} for callback.
         */
        private final ColorItemAdapterListener mListener;

        /**
         * The {@link ColorItem} bound to this {@link ColorItemHolder}.
         */
        private ColorItem mColorItem;

        public ColorItemHolder(View view, ColorItemAdapterListener listener) {
            super(view);
            mListener = listener;
            mUnderlyingView = view;
            mColorPreview = view.findViewById(R.id.row_color_item_preview);
            view.setOnClickListener(this);
        }

        public void bind(ColorItem colorItem) {
            mColorItem = colorItem;
            mColorPreview.getBackground().setColorFilter(colorItem.getColor(), PorterDuff.Mode.MULTIPLY);
        }

        @Override
        public void onClick(View v) {
            if (v != mUnderlyingView) {
                throw new IllegalArgumentException("Unsupported view clicked. Found: " + v);
            }

            if (mColorItem != null) {
                mListener.onColorItemClicked(mColorItem, mColorPreview);
            }
        }
    }
}
