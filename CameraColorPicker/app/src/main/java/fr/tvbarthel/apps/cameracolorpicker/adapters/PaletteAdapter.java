package fr.tvbarthel.apps.cameracolorpicker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.views.PaletteView;

/**
 * A simple {@link ArrayAdapter} of {@link Palette}s that binds each {@link Palette} to a {@link R.layout#row_palette}.
 */
public class PaletteAdapter extends ArrayAdapter<Palette> {

    public PaletteAdapter(Context context) {
        super(context, R.layout.row_palette);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = createView(parent);
        }

        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        bindViewHolder(viewHolder, position);

        return view;
    }

    /**
     * Create a {@link View} associated with a {@link fr.tvbarthel.apps.cameracolorpicker.adapters.PaletteAdapter.ViewHolder}
     * that can be bound to a {@link Palette}.
     *
     * @param parent the parent {@link ViewGroup}.
     * @return the newly created {@link View}.
     */
    protected View createView(ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_palette, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /**
     * Bind a {@link fr.tvbarthel.apps.cameracolorpicker.adapters.PaletteAdapter.ViewHolder}
     * to the {@link Palette} at the given position.
     *
     * @param viewHolder the {@link fr.tvbarthel.apps.cameracolorpicker.adapters.ColorItemAdapter.ViewHolder}.
     * @param position the position of the {@link Palette} in the underlying data set.
     */
    protected void bindViewHolder(ViewHolder viewHolder, int position) {
        final Palette palette = getItem(position);
        viewHolder.mPaletteRoundedThumbnail.setPalette(palette);
        viewHolder.mPaletteName.setText(palette.getName());
    }

    private static class ViewHolder {
        public PaletteView mPaletteRoundedThumbnail;
        public TextView mPaletteName;

        public ViewHolder(View view) {
            mPaletteRoundedThumbnail = (PaletteView) view.findViewById(R.id.row_color_palette_thumbnail);
            mPaletteName = (TextView) view.findViewById(R.id.row_color_palette_name);
        }
    }
}
