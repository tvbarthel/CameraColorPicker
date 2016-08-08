package fr.tvbarthel.apps.cameracolorpicker.views;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.wrappers.PaletteListWrapper;

/**
 * A flavor {@link PaletteListWrapper}.
 */
public class FlavorPaletteListWrapper extends PaletteListWrapper {

    /**
     * Create a {@link FlavorPaletteListWrapper}.
     *
     * @param recyclerView the {@link RecyclerView} that will be wrapped.
     * @param listener     the {@link PaletteListWrapperListener} that will be notified of the clicks.
     * @return a newly created {@link FlavorPaletteListWrapper}.
     */
    public static FlavorPaletteListWrapper create(RecyclerView recyclerView, PaletteListWrapperListener listener) {
        final PaletteAdapter paletteAdapter = new PaletteAdapter(listener);
        int spanCount = recyclerView.getContext().getResources().getInteger(R.integer.list_horizontal_span);
        final LinearLayoutManager linearLayoutManager = new GridLayoutManager(recyclerView.getContext(), spanCount);
        return new FlavorPaletteListWrapper(recyclerView, listener, paletteAdapter, linearLayoutManager);
    }

    protected FlavorPaletteListWrapper(RecyclerView recyclerView, PaletteListWrapperListener listener, Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        super(recyclerView, listener, adapter, layoutManager);
    }

    /**
     * A {@link Adapter}
     * of {@link PaletteHolder}.
     * <p/>
     * It adapts {@link Palette} into {@link R.layout#row_palette}
     */
    private static class PaletteAdapter extends Adapter<PaletteHolder> {

        protected PaletteAdapter(PaletteListWrapperListener listener) {
            super(listener);
        }

        @Override
        public PaletteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_palette, parent, false);
            return new PaletteHolder(view, mListener);
        }

        @Override
        public void onBindViewHolder(PaletteHolder holder, int position) {
            final Palette palette = get(position);
            holder.bind(palette);
        }

    }

    /**
     * A {@link RecyclerView.ViewHolder} used by {@link PaletteAdapter}.
     */
    private static class PaletteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final View mUnderlyingView;
        private final PaletteListWrapperListener mListener;
        private final PaletteView mPaletteRoundedThumbnail;
        private Palette mPalette;

        public PaletteHolder(View view, PaletteListWrapperListener listener) {
            super(view);
            mUnderlyingView = view;
            mListener = listener;
            mPaletteRoundedThumbnail = (PaletteView) view.findViewById(R.id.row_color_palette_thumbnail);

            view.setOnClickListener(this);
        }

        public void bind(Palette palette) {
            mPalette = palette;
            mPaletteRoundedThumbnail.setPalette(palette);
        }

        @Override
        public void onClick(View v) {
            if (v != mUnderlyingView) {
                throw new IllegalArgumentException("Unsupported view clicked. Found: " + v);
            }

            if (mPalette != null) {
                mListener.onPaletteClicked(mPalette, mPaletteRoundedThumbnail);
            }
        }
    }
}
