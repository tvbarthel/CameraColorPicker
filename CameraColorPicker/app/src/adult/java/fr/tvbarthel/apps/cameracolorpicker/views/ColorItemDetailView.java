package fr.tvbarthel.apps.cameracolorpicker.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.utils.ClipDatas;

/**
 * A simple view displaying some details about a {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}
 */
public class ColorItemDetailView extends LinearLayout implements View.OnClickListener {

    /**
     * A {@link android.widget.TextView} for showing the hexadecimal value of the color.
     */
    private TextView mHex;

    /**
     * A {@link android.widget.TextView} for showing the RGB value of the color.
     */
    private TextView mRgb;

    /**
     * A {@link android.widget.TextView} for showing the HSV value of the color.
     */
    private TextView mHsv;

    /**
     * A reference to the current {@link android.widget.Toast}.
     * <p/>
     * Used for hiding the current {@link android.widget.Toast} before showing a new one or when the {@link ColorItemDetailView}
     * is detached from the window.
     */
    private Toast mToast;

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

    @Override
    protected void onDetachedFromWindow() {
        hideToast();
        super.onDetachedFromWindow();
    }

    /**
     * Set the {@link ColorItem}.
     *
     * @param colorItem the {@link ColorItem}.
     */
    public void setColorItem(ColorItem colorItem) {
        mHex.setText(colorItem.getHexString());
        mRgb.setText(colorItem.getRgbString());
        mHsv.setText(colorItem.getHsvString());
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();

        switch (viewId) {
            case R.id.view_color_item_detail_hex:
                clipColor(R.string.color_clip_color_label_hex, mHex.getText());
                break;

            case R.id.view_color_item_detail_rgb:
                clipColor(R.string.color_clip_color_label_rgb, mRgb.getText());
                break;

            case R.id.view_color_item_detail_hsv:
                clipColor(R.string.color_clip_color_label_hsv, mHsv.getText());
                break;

            default:
                throw new IllegalArgumentException("Unsupported view clicked. Found: " + view);
        }
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

        mHex = (TextView) view.findViewById(R.id.view_color_item_detail_hex);
        mRgb = (TextView) view.findViewById(R.id.view_color_item_detail_rgb);
        mHsv = (TextView) view.findViewById(R.id.view_color_item_detail_hsv);

        // Set the click listeners.
        mHex.setOnClickListener(this);
        mRgb.setOnClickListener(this);
        mHsv.setOnClickListener(this);
    }

    private void clipColor(int labelResourceId, CharSequence colorString) {
        final Context context = getContext();
        ClipDatas.clipPainText(context, context.getString(labelResourceId), colorString);
        showToast(R.string.color_clip_success_copy_message);
    }

    /**
     * Hide the current {@link android.widget.Toast}.
     */
    protected void hideToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    /**
     * Show a toast text message.
     *
     * @param resId The resource id of the string resource to use.
     */
    protected void showToast(int resId) {
        hideToast();
        mToast = Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
