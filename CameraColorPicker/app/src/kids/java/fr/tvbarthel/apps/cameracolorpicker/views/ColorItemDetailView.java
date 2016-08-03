package fr.tvbarthel.apps.cameracolorpicker.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;

/**
 * A simple view displaying some details about a {@link ColorItem}
 */
public class ColorItemDetailView extends LinearLayout {

    private ColorQuantityBar mQuantityBarRed;
    private ColorQuantityBar mQuantityBarGreen;
    private ColorQuantityBar mQuantityBarBlue;

    public ColorItemDetailView(Context context) {
        super(context);
        init(context);
    }

    public ColorItemDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorItemDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorItemDetailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * Set the {@link ColorItem}.
     *
     * @param colorItem the {@link ColorItem}.
     */
    public void setColorItem(ColorItem colorItem) {
        final int color = colorItem.getColor();

        mQuantityBarRed.setValue(Color.red(color) / 255f);
        mQuantityBarGreen.setValue(Color.green(color) / 255f);
        mQuantityBarBlue.setValue(Color.blue(color) / 255f);
    }


    /**
     * Initialize this {@link ColorItemDetailView}.
     * <p/>
     * <b>Note:</b> should be called in every constructor.
     *
     * @param context the {@link Context} from the constructor.
     */
    private void init(Context context) {
        setOrientation(VERTICAL);

        final View view = LayoutInflater.from(context)
                .inflate(R.layout.view_color_item_detail, this);

        mQuantityBarRed = (ColorQuantityBar) view.findViewById(R.id.view_color_item_detail_quantity_bar_red);
        mQuantityBarGreen = (ColorQuantityBar) view.findViewById(R.id.view_color_item_detail_quantity_bar_green);
        mQuantityBarBlue = (ColorQuantityBar) view.findViewById(R.id.view_color_item_detail_quantity_bar_blue);
    }

}
