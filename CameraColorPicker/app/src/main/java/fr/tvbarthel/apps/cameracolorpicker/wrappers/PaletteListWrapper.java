package fr.tvbarthel.apps.cameracolorpicker.wrappers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.data.Palette;

/**
 * A wrapper of {@link RecyclerView} that displays {@link fr.tvbarthel.apps.cameracolorpicker.data.Palette}s.
 */
public abstract class PaletteListWrapper {

    protected final RecyclerView mRecyclerView;
    protected final PaletteListWrapperListener mListener;
    protected final Adapter mAdapter;
    protected final android.support.v7.widget.RecyclerView.LayoutManager mLayoutManager;

    protected PaletteListWrapper(RecyclerView recyclerView, PaletteListWrapperListener listener, Adapter adapter, android.support.v7.widget.RecyclerView.LayoutManager layoutManager) {
        mRecyclerView = recyclerView;
        mListener = listener;
        mAdapter = adapter;
        mLayoutManager = layoutManager;
    }

    /**
     * Install a {@link RecyclerView.LayoutManager}
     * and a {@link RecyclerView.Adapter} on the {@link RecyclerView}.
     *
     * @return the {@link RecyclerView.Adapter} installed on the {@link RecyclerView}.
     */
    public PaletteListWrapper.Adapter installRecyclerView() {
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return mAdapter;
    }

    /**
     * An interface for listening to {@link PaletteListWrapper}.
     */
    public interface PaletteListWrapperListener {

        /**
         * Called when a {@link Palette} has just been clicked.
         *
         * @param palette        the {@link Palette} that has just been clicked.
         * @param palettePreview the {@link View} associated with the {@link Palette}. (for transition purpose)
         */
        void onPaletteClicked(@NonNull Palette palette, @NonNull View palettePreview);
    }

    /**
     * A {@link android.support.v7.widget.RecyclerView.Adapter} backed by a {@link List}
     * of {@link Palette}
     *
     * @param <T> a {@link android.support.v7.widget.RecyclerView.ViewHolder}
     */
    public static abstract class Adapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

        private final List<Palette> mPalettes;
        protected PaletteListWrapperListener mListener;

        protected Adapter(PaletteListWrapperListener listener) {
            mListener = listener;
            mPalettes = new ArrayList<>();
        }

        @Override
        public int getItemCount() {
            return mPalettes.size();
        }

        /**
         * Set the {@link Palette}s.
         *
         * @param palettes a {@link List} of {@link Palette}s.
         */
        public void setPalettes(List<Palette> palettes) {
            mPalettes.clear();
            mPalettes.addAll(palettes);
            notifyDataSetChanged();
        }

        /**
         * Get a {@link Palette} at a given position.
         *
         * @param position the position of the palette.
         * @return the {@link Palette} at the given position.
         */
        protected Palette get(int position) {
            return mPalettes.get(position);
        }
    }
}
