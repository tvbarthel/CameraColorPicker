package fr.tvbarthel.apps.cameracolorpicker.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import fr.tvbarthel.apps.cameracolorpicker.R;


public class ColorQuantityBar extends View {

    protected static final float DEFAULT_BAR_WIDTH = 6;
    protected static final float DEFAULT_BAR_OVER_WIDTH = 6;
    protected static final float DEFAULT_THUMB_RADIUS = 7;
    protected static final float DEFAULT_VALUE = 0.6f;


    protected float mBarWidth;
    protected int mBarColor;
    protected Paint mBarPaint;

    protected float mBarOverWidth;
    protected int mBarOverColor;
    protected Paint mBarOverPaint;

    protected float mThumbRadius;
    protected int mThumbColor;
    protected Paint mThumbPaint;
    protected PointF mThumbCenter;

    protected RectF mBounds;
    protected float mValue;

    public ColorQuantityBar(Context context) {
        super(context);
        initView(context);
    }

    public ColorQuantityBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ColorQuantityBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorQuantityBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    protected void initView(Context context) {
        mBarColor = ContextCompat.getColor(context, R.color.color_quantity_bar_default_bar_color);
        mBarOverColor = ContextCompat.getColor(context, R.color.color_quantity_bar_default_bar_over_color);
        mThumbColor = ContextCompat.getColor(context, R.color.color_quantity_bar_default_bar_thumb_color);

        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        mBarWidth = dipToPx(displayMetrics, DEFAULT_BAR_WIDTH);
        mBarOverWidth = dipToPx(displayMetrics, DEFAULT_BAR_OVER_WIDTH);

        mBarPaint = new Paint();
        mBarPaint.setColor(mBarColor);
        mBarPaint.setStyle(Paint.Style.STROKE);
        mBarPaint.setStrokeWidth(mBarWidth);

        mBarOverPaint = new Paint();
        mBarOverPaint.setColor(mBarOverColor);
        mBarOverPaint.setStyle(Paint.Style.STROKE);
        mBarOverPaint.setStrokeWidth(mBarOverWidth);

        mThumbRadius = dipToPx(displayMetrics, DEFAULT_THUMB_RADIUS);
        mThumbPaint = new Paint();
        mThumbPaint.setColor(mThumbColor);
        mThumbPaint.setStyle(Paint.Style.FILL);
        mThumbPaint.setAntiAlias(true);


        mBounds = new RectF(0, 0, 0, 0);
        mThumbCenter = new PointF(0, 0);
        mValue = DEFAULT_VALUE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int left = (int) (getPaddingLeft() + mThumbRadius);
        final int right = (int) (getMeasuredWidth() - getPaddingRight() - mThumbRadius);
        final int top = getPaddingTop();
        final int bottom = getMeasuredHeight() - getPaddingBottom();

        mBounds.set(left, top, right, bottom);
        updateThumbPosition();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBar(canvas);
        drawThumb(canvas);
    }

    /**
     * Set the color of the over bar part. (the progress).
     *
     * @param barOverColor the new color.
     */
    public void setBarOverColor(int barOverColor) {
        mBarOverColor = barOverColor;
        mBarOverPaint.setColor(mBarOverColor);
    }

    /**
     * Set the color of the bar part. (the background).
     *
     * @param barColor the new color.
     */
    public void setBarColor(int barColor) {
        mBarColor = barColor;
        mBarPaint.setColor(mBarColor);
    }

    /**
     * Set the color of the thumb.
     *
     * @param thumbColor the new color.
     */
    public void setThumbColor(int thumbColor) {
        mThumbColor = thumbColor;
        mThumbPaint.setColor(mThumbColor);
    }

    /**
     * Set the value of this {@link ColorQuantityBar}.
     *
     * @param value a value between 0f and 1f
     */
    public void setValue(float value) {
        mValue = value;
        updateThumbPosition();
        invalidate();
    }

    private void drawThumb(Canvas canvas) {
        canvas.drawCircle(mThumbCenter.x,
                mThumbCenter.y,
                mThumbRadius,
                mThumbPaint);
    }

    private void drawBar(Canvas canvas) {
        canvas.drawLine(mBounds.left, mBounds.centerY(), mBounds.right, mBounds.centerY(), mBarPaint);
        canvas.drawLine(0, mBounds.centerY(), mBounds.width() * mValue, mBounds.centerY(), mBarOverPaint);
    }

    private void updateThumbPosition() {
        mThumbCenter.x = mBounds.left + mBounds.width() * mValue;
        mThumbCenter.y = mBounds.centerY();
    }

    private static int dipToPx(DisplayMetrics displayMetrics, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
    }
}
