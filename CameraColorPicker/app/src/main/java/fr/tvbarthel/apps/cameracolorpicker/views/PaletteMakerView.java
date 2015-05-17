package fr.tvbarthel.apps.cameracolorpicker.views;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;

/**
 * A {@link View} for creating {@link Palette}s.
 * <p/>
 * You can add {@link ColorItem} using {@link PaletteMakerView#addColor(ColorItem)}.
 * You can remove the last added {@link ColorItem} using {@link PaletteMakerView#removeLastColor()}.
 * You can call {@link PaletteMakerView#make(String)} to create the {@link Palette} with the {@link ColorItem} you added.
 */
public class PaletteMakerView extends View {

    /**
     * The name of the property used for animating the width of the items.
     */
    private static final String PROPERTY_NAME_ITEM_WIDTH = "itemWidth";

    /**
     * The default duration of the animation of the item width.
     */
    private static final int DEFAULT_ITEM_WIDTH_ANIMATION_DURATION = 300;

    /**
     * A {@link List} of the {@link ColorItem}s choose by the user to make a new {@link Palette}.
     */
    private List<ColorItem> mColorItems;

    /**
     * The width of each items.
     */
    private float mItemWidth;

    /**
     * A {@link RectF} used for drawing the color items.
     */
    private RectF mRectF;

    /**
     * An {@link ObjectAnimator} used for animating the width of the items.
     */
    private ObjectAnimator mItemWidthAnimator;

    /**
     * A {@link Paint} used for drawing the items.
     */
    private Paint mItemPaint;

    /**
     * The last size of {@link PaletteMakerView#mColorItems}.
     */
    private int mLastSize;

    public PaletteMakerView(Context context) {
        super(context);
        init();
    }

    public PaletteMakerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaletteMakerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PaletteMakerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * Add a {@link ColorItem} to the {@link List} of {@link ColorItem}s that will be used to make the {@link Palette}.
     *
     * @param colorItem the {@link ColorItem} to add.
     */
    public void addColor(ColorItem colorItem) {
        if (colorItem == null) {
            throw new IllegalArgumentException("Color item can't be null.");
        }

        mLastSize = mColorItems.size();
        mColorItems.add(colorItem);
        animateItemWidth();
    }

    /**
     * Remove the last added {@link ColorItem} from the {@link List} of {@link ColorItem}s that will be used to make the {@link Palette}.
     *
     * @return the {@link ColorItem} that was removed, or null if no {@link ColorItem}s were removed.
     */
    @Nullable
    public ColorItem removeLastColor() {
        if (mColorItems.isEmpty()) {
            return null;
        }

        mLastSize = mColorItems.size();
        final ColorItem colorItemRemoved = mColorItems.remove(mColorItems.size() - 1);
        animateItemWidth();
        return colorItemRemoved;
    }

    /**
     * Check if there is any {@link ColorItem}s added to this {@link PaletteMakerView}.
     *
     * @return Returns true is there is a least one {@link ColorItem} added, false otherwise.
     */
    public boolean isEmpty() {
        return mColorItems.isEmpty();
    }

    /**
     * Get the number of {@link ColorItem}s added to this {@link PaletteMakerView}.
     *
     * @return the number of {@link ColorItem}s added.
     */
    public int size() {
        return mColorItems.size();
    }

    /**
     * Make a {@link Palette} with the {@link ColorItem}s that were added.
     *
     * @param name the name of the {@link Palette} to make.
     * @return the newly created {@link Palette}.
     */
    public Palette make(String name) {
        return new Palette(name, mColorItems);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mRectF.bottom = getMeasuredHeight();

        if (mColorItems.isEmpty()) {
            mItemWidth = 0f;
        } else {
            mItemWidth = getMeasuredWidth() / (float) mColorItems.size();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mColorItems.isEmpty()) {
            if (mColorItems.size() == 1) {
                // There is only one item to draw.
                // If the last size was greater, then we draw the item from the left.
                // Else we draw the item from the right.
                if (mLastSize > mColorItems.size()) {
                    mRectF.left = 0;
                    mRectF.right = mItemWidth;
                } else {
                    mRectF.right = getMeasuredWidth();
                    mRectF.left = mRectF.right - mItemWidth;
                }

                final ColorItem colorItem = mColorItems.get(0);
                mItemPaint.setColor(colorItem.getColor());
                canvas.drawRect(mRectF, mItemPaint);
            } else {
                // There are more than one item to draw.
                // We draw each items except the last one with the same width: mItemWidth.
                // The last item takes the extra space.
                final int size = mColorItems.size() - 1;
                mRectF.left = 0;
                mRectF.right = mItemWidth;

                for (int i = 0; i < size; i++) {
                    final ColorItem colorItem = mColorItems.get(i);
                    mItemPaint.setColor(colorItem.getColor());
                    canvas.drawRect(mRectF, mItemPaint);

                    mRectF.left = mRectF.right;
                    mRectF.right = mRectF.left + mItemWidth;
                }

                final ColorItem lastColorItem = mColorItems.get(size);
                mItemPaint.setColor(lastColorItem.getColor());
                mRectF.right = getMeasuredWidth();
                canvas.drawRect(mRectF, mItemPaint);
            }
        }

    }

    /**
     * Animate the width of the items.
     */
    private void animateItemWidth() {
        float newItemWidth;

        if (mColorItems.isEmpty()) {
            newItemWidth = 0f;
        } else {
            newItemWidth = getMeasuredWidth() / (float) mColorItems.size();
        }

        if (mItemWidthAnimator.isRunning()) {
            mItemWidthAnimator.cancel();
        }

        mItemWidthAnimator.setFloatValues(mItemWidth, newItemWidth);
        mItemWidthAnimator.start();
    }

    /**
     * Initialize this {@link PaletteMakerView}.
     * <p/>
     * Must be called once in each constructor.
     */
    private void init() {
        mRectF = new RectF();
        mItemWidthAnimator = ObjectAnimator.ofFloat(this, PROPERTY_NAME_ITEM_WIDTH, 0f, 1f)
                .setDuration(DEFAULT_ITEM_WIDTH_ANIMATION_DURATION);
        mItemPaint = new Paint();
        mItemPaint.setStyle(Paint.Style.FILL);
        mColorItems = new ArrayList<>();
    }

    /**
     * The setters used by {@link PaletteMakerView#mItemWidthAnimator} to animate the width of the items.
     *
     * @param itemWidth the width of the items.
     */
    @SuppressWarnings("unused")
    private void setItemWidth(float itemWidth) {
        mItemWidth = itemWidth;
        invalidate();
    }
}
