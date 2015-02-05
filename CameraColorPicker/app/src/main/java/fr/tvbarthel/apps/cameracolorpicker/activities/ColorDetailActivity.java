package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.utils.ClipDatas;

public class ColorDetailActivity extends ActionBarActivity implements View.OnClickListener {

    /**
     * A key for passing a color item as extra.
     */
    protected static final String EXTRA_COLOR_ITEM = "ColorDetailActivity.Extras.EXTRA_COLOR_ITEM";

    /**
     * A key for passing the global visible rect of the clicked color preview clicked.
     */
    protected static final String EXTRA_START_BOUNDS = "ColorDetailActivity.Extras.EXTRA_START_BOUNDS";

    public static void startWithColorItem(Context context, ColorItem colorItem, View colorPreviewClicked) {
        final boolean isActivity = context instanceof Activity;
        final Rect startBounds = new Rect();
        colorPreviewClicked.getGlobalVisibleRect(startBounds);

        final Intent intent = new Intent(context, ColorDetailActivity.class);
        intent.putExtra(EXTRA_COLOR_ITEM, colorItem);
        intent.putExtra(EXTRA_START_BOUNDS, startBounds);

        if (!isActivity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

        if (isActivity) {
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    /**
     * A {@link android.view.View} for showing the color preview being translated during the start animation from the clicked position to the right location.
     */
    protected View mTranslatedPreview;

    /**
     * A {@link android.view.View} for showing the color preview being scaled during the start animation to fill the preview container.
     */
    protected View mScaledPreview;

    /**
     * A {@link android.widget.TextView} for showing the hexadecimal value of the color.
     */
    protected TextView mHex;

    /**
     * A {@link android.widget.TextView} for showing the RGB value of the color.
     */
    protected TextView mRgb;

    /**
     * A {@link android.widget.TextView} for showing the HSV value of the color.
     */
    protected TextView mHsv;

    /**
     * A reference to the current {@link android.widget.Toast}.
     * <p/>
     * Used for hiding the current {@link android.widget.Toast} before showing a new one or the activity is paused.
     * {@link }
     */
    protected Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_detail);

        // ensure correct extras.
        final Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_COLOR_ITEM) || !intent.hasExtra(EXTRA_START_BOUNDS)) {
            throw new IllegalStateException("Missing extras. Please use startWithColorItem.");
        }

        // Retrieve the extras.
        final ColorItem colorItem = intent.getParcelableExtra(EXTRA_COLOR_ITEM);
        final Rect startBounds = intent.getParcelableExtra(EXTRA_START_BOUNDS);

        // Create a rect that will be used to retrieve the stop bounds.
        final Rect stopBounds = new Rect();

        // Find the views.
        mTranslatedPreview = findViewById(R.id.activity_color_detail_preview_translating);
        mScaledPreview = findViewById(R.id.activity_color_detail_preview_scaling);
        mHex = (TextView) findViewById(R.id.activity_color_detail_hex);
        mRgb = (TextView) findViewById(R.id.activity_color_detail_rgb);
        mHsv = (TextView) findViewById(R.id.activity_color_detail_hsv);

        // Set the click listeners.
        mHex.setOnClickListener(this);
        mRgb.setOnClickListener(this);
        mHsv.setOnClickListener(this);

        // Display the color item data.
        mTranslatedPreview.getBackground().setColorFilter(colorItem.getColor(), PorterDuff.Mode.MULTIPLY);
        mScaledPreview.getBackground().setColorFilter(colorItem.getColor(), PorterDuff.Mode.MULTIPLY);
        mHex.setText(colorItem.getHexString());
        mRgb.setText(colorItem.getRgbString());
        mHsv.setText(colorItem.getHsvString());

        final View previewContainer = findViewById(R.id.activity_color_detail_preview_container);
        final ViewTreeObserver viewTreeObserver = previewContainer.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    previewContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                    mTranslatedPreview.getGlobalVisibleRect(stopBounds);
                    final int deltaX = startBounds.left - stopBounds.left;
                    final int deltaY = startBounds.top - stopBounds.top;
                    final AnimatorSet translationAnimatorSet = new AnimatorSet();
                    translationAnimatorSet.play(ObjectAnimator.ofFloat(mTranslatedPreview, View.TRANSLATION_X, deltaX, 0))
                            .with(ObjectAnimator.ofFloat(mTranslatedPreview, View.TRANSLATION_Y, deltaY, 0));
                    translationAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mScaledPreview.setVisibility(View.VISIBLE);
                            mHex.setVisibility(View.VISIBLE);
                            mRgb.setVisibility(View.VISIBLE);
                            mHsv.setVisibility(View.VISIBLE);
                            final float maxContainerSize = (float) Math.sqrt(Math.pow(previewContainer.getWidth(), 2) + Math.pow(previewContainer.getHeight(), 2));
                            final float maxSize = Math.max(mScaledPreview.getWidth(), mScaledPreview.getHeight());
                            final float scaleRatio = maxContainerSize / maxSize;
                            final AnimatorSet scaleAnimatorSet = new AnimatorSet();
                            scaleAnimatorSet.play(ObjectAnimator.ofFloat(mScaledPreview, View.SCALE_X, 1, scaleRatio))
                                    .with(ObjectAnimator.ofFloat(mScaledPreview, View.SCALE_Y, 1, scaleRatio))
                                    .with(ObjectAnimator.ofFloat(mHex, View.ALPHA, 0, 1))
                                    .with(ObjectAnimator.ofFloat(mRgb, View.ALPHA, 0, 1))
                                    .with(ObjectAnimator.ofFloat(mHsv, View.ALPHA, 0, 1));
                            scaleAnimatorSet.start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    translationAnimatorSet.start();

                    return true;
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideToast();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();

        switch (viewId) {
            case R.id.activity_color_detail_hex:
                clipColor(R.string.color_clip_color_label_hex, mHex.getText());
                break;

            case R.id.activity_color_detail_rgb:
                clipColor(R.string.color_clip_color_label_rgb, mRgb.getText());
                break;

            case R.id.activity_color_detail_hsv:
                clipColor(R.string.color_clip_color_label_hsv, mHsv.getText());
                break;

            default:
                throw new IllegalArgumentException("Unsupported view clicked. Found: " + view);
        }
    }


    protected void clipColor(int labelResourceId, CharSequence colorString) {
        ClipDatas.clipPainText(this, getString(labelResourceId), colorString);
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
        mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        mToast.show();
    }

}
