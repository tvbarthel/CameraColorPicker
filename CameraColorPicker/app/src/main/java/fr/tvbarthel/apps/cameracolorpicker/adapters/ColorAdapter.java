package fr.tvbarthel.apps.cameracolorpicker.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;

/**
 * TODO comment.
 */
public class ColorAdapter extends ArrayAdapter<Integer> {

    public ColorAdapter(Context context) {
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

    protected View createView(ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_color_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    protected void bindViewHolder(ViewHolder viewHolder, int position) {
        final int color = getItem(position);
        viewHolder.mColorPreview.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        viewHolder.mColorText.setText(Integer.toHexString(color));
    }

    private static class ViewHolder {
        public View mColorPreview;
        public TextView mColorText;

        public ViewHolder(View view) {
            mColorPreview = view.findViewById(R.id.row_color_item_preview);
            mColorText = (TextView) view.findViewById(R.id.row_color_item_text);
        }
    }
}
