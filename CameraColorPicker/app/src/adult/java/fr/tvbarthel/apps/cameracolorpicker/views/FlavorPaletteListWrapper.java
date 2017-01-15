package fr.tvbarthel.apps.cameracolorpicker.views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
     * @param listener     the {@link fr.tvbarthel.apps.cameracolorpicker.wrappers.PaletteListWrapper.PaletteListWrapperListener} that will be notified of the clicks.
     * @return a newly created {@link FlavorPaletteListWrapper}.
     */
    public static FlavorPaletteListWrapper create(RecyclerView recyclerView, PaletteListWrapperListener listener) {
        final PaletteAdapter paletteAdapter = new PaletteAdapter(listener);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        return new FlavorPaletteListWrapper(recyclerView, listener, paletteAdapter, linearLayoutManager);
    }

    protected FlavorPaletteListWrapper(RecyclerView recyclerView, PaletteListWrapperListener listener, Adapter adapter, RecyclerView.LayoutManager layoutManager) {
        super(recyclerView, listener, adapter, layoutManager);
    }

    /**
     * A {@link fr.tvbarthel.apps.cameracolorpicker.wrappers.PaletteListWrapper.Adapter}
     * of {@link PaletteHolder}.
     * <p/>
     * It adapts {@link Palette} into {@link fr.tvbarthel.apps.cameracolorpicker.R.layout#row_palette}
     */
    private static class PaletteAdapter extends PaletteListWrapper.Adapter<PaletteHolder> {

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
     * A {@link android.support.v7.widget.RecyclerView.ViewHolder} used by {@link PaletteAdapter}.
     */
    private static class PaletteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final View mUnderlyingView;
        private final PaletteListWrapperListener mListener;
        private final PaletteView mPaletteRoundedThumbnail;
        private final TextView mPaletteName;
        private Palette mPalette;

        public PaletteHolder(View view, PaletteListWrapperListener listener) {
            super(view);
            mUnderlyingView = view;
            mListener = listener;
            mPaletteRoundedThumbnail = (PaletteView) view.findViewById(R.id.row_color_palette_thumbnail);
            mPaletteName = (TextView) view.findViewById(R.id.row_color_palette_name);

            view.setOnClickListener(this);
        }

        public void bind(Palette palette) {
            mPalette = palette;
            mPaletteRoundedThumbnail.setPalette(palette);
            mPaletteName.setText(palette.getName());
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
