package fr.tvbarthel.apps.cameracolorpicker.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;

/**
 * A simple {@link ArrayAdapter} of {@link ColorItem}s that binds each {@link ColorItem} to a {@link R.layout#row_color_item}.
 */
public class ColorItemAdapter extends ArrayAdapter<ColorItem> {

    public ColorItemAdapter(Context context) {
        super(context, R.layout.row_color_item);
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
     * Create a {@link View} associated with a {@link fr.tvbarthel.apps.cameracolorpicker.adapters.ColorItemAdapter.ViewHolder}
     * that can be bound to a {@link ColorItem}.
     *
     * @param parent the parent {@link ViewGroup}.
     * @return the newly created {@link View}.
     */
    protected View createView(ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_color_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /**
     * Bind a {@link fr.tvbarthel.apps.cameracolorpicker.adapters.ColorItemAdapter.ViewHolder}
     * to the {@link ColorItem} at the given position.
     *
     * @param viewHolder the {@link fr.tvbarthel.apps.cameracolorpicker.adapters.ColorItemAdapter.ViewHolder}.
     * @param position the position of the {@link ColorItem} in the underlying data set.
     */
    protected void bindViewHolder(ViewHolder viewHolder, int position) {
        final ColorItem colorItem = getItem(position);
        viewHolder.mColorPreview.getBackground().setColorFilter(colorItem.getColor(), PorterDuff.Mode.MULTIPLY);
        if (!TextUtils.isEmpty(colorItem.getName())) {
            viewHolder.mColorText.setText(colorItem.getName());
        } else {
            viewHolder.mColorText.setText(colorItem.getHexString());
        }
    }

    /**
     * A simple class for the view holder pattern associated with {@link R.layout#row_color_item}.
     */
    private static class ViewHolder {

        /**
         * The {@link View} to show a preview of the color item.
         */
        public View mColorPreview;

        /**
         * The {@link TextView} to display the hexadecimal code of the color item.
         */
        public TextView mColorText;

        public ViewHolder(View view) {
            mColorPreview = view.findViewById(R.id.row_color_item_preview);
            mColorText = (TextView) view.findViewById(R.id.row_color_item_text);
        }
    }
}
