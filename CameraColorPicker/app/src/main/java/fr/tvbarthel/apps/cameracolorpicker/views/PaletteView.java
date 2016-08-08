package fr.tvbarthel.apps.cameracolorpicker.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;

/**
 * A simple {@link View} that can display a {@link Palette}.
 * <p/>
 * All {@link ColorItem}s of the {@link Palette} are represented horizontally with a rectangle
 * of their respective color.
 */
public class PaletteView extends View {

    /**
     * The {@link Palette} being displayed.
     */
    private Palette mPalette;

    /**
     * A {@link RectF} used for drawing the {@link ColorItem}s.
     */
    private RectF mRect;

    /**
     * A {@link RectF} for computing the bounds of the drawing area.
     */
    private RectF mBounds;

    /**
     * A {@link Paint} for drawing the {@link ColorItem}s.
     */
    private Paint mColorPaint;

    public PaletteView(Context context) {
        super(context);
        init();
    }

    public PaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaletteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PaletteView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * Set the {@link Palette} that will be displayed.
     *
     * @param palette the {@link Palette} to display.
     */
    public void setPalette(Palette palette) {
        mPalette = palette;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int left = getPaddingLeft();
        final int top = getPaddingTop();
        final int right = getMeasuredWidth() - getPaddingRight();
        final int bottom = getMeasuredHeight() - getPaddingBottom();

        // Compute the drawing area.
        // The drawing area corresponds to the size of the palette view minus its padding.
        mBounds.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPalette == null) {
            // If there is no palette to draw, simply draw a white rect.
            mColorPaint.setColor(Color.WHITE);
            canvas.drawRect(mBounds, mColorPaint);
        } else {
            // Draw a rec for each color items in the palette.
            final List<ColorItem> colorItems = mPalette.getColors();
            final float colorWidth = mBounds.width() / colorItems.size();
            mRect.set(mBounds.left, mBounds.top, mBounds.left + colorWidth, mBounds.bottom);

            for (ColorItem colorItem : colorItems) {
                mColorPaint.setColor(colorItem.getColor());
                canvas.drawRect(mRect, mColorPaint);
                mRect.left = mRect.right;
                mRect.right += colorWidth;
            }
        }
    }

    /**
     * Initialize the palette view.
     * <p/>
     * Must be called once in each constructor.
     */
    private void init() {
        mBounds = new RectF();
        mRect = new RectF();

        mColorPaint = new Paint();
        mColorPaint.setStyle(Paint.Style.FILL);
    }
}
