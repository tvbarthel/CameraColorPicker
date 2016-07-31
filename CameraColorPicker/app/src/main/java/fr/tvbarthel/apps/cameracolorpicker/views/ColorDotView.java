package fr.tvbarthel.apps.cameracolorpicker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import fr.tvbarthel.apps.cameracolorpicker.utils.BackgroundUtils;

/**
 * Simple view used to render a color dot.
 */
public class ColorDotView extends View {
    /**
     * Simple view used to render a color dot.
     *
     * @param context holding context.
     */
    public ColorDotView(Context context) {
        this(context, null);
    }

    /**
     * Simple view used to render a color dot.
     *
     * @param context holding context.
     * @param attrs   attr from xml.
     */
    public ColorDotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Simple view used to render a color dot.
     *
     * @param context      holding context.
     * @param attrs        attr from xml.
     * @param defStyleAttr style.
     */
    public ColorDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        int measureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        setMeasuredDimension(measureSpec, measureSpec);
    }

    /**
     * Initialize internal component.
     *
     * @param context holding context.
     */
    private void initialize(Context context) {
        if (!isInEditMode()) {
            BackgroundUtils.setBackground(this, new ColorDotDrawable(context));
        }
    }
}
