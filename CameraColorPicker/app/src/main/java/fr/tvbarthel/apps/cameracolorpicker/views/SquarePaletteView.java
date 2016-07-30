package fr.tvbarthel.apps.cameracolorpicker.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * {@link PaletteView} which will always be rendered as square.
 */
public class SquarePaletteView extends PaletteView {
    /**
     * {@link PaletteView} which will always be rendered as square.
     *
     * @param context holding context.
     */
    public SquarePaletteView(Context context) {
        super(context);
    }

    /**
     * {@link PaletteView} which will always be rendered as square.
     *
     * @param context holding context.
     * @param attrs   attr from xml.
     */
    public SquarePaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@link PaletteView} which will always be rendered as square.
     *
     * @param context      holding context.
     * @param attrs        attr from xml.
     * @param defStyleAttr style from xml.
     */
    public SquarePaletteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        int measureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(measureSpec, measureSpec);
    }
}
