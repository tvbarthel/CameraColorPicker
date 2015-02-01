package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItems;
import fr.tvbarthel.apps.cameracolorpicker.utils.Cameras;
import fr.tvbarthel.apps.cameracolorpicker.views.CameraColorPickerPreview;


/**
 * TODO comment.
 */
public class ColorPickerActivity extends ActionBarActivity implements CameraColorPickerPreview.OnColorSelectedListener, View.OnClickListener {

    protected static final String TAG = ColorPickerActivity.class.getSimpleName();

    /**
     * The name of the property that animates the 'picked color'.
     * <p/>
     * Used by {@link fr.tvbarthel.apps.cameracolorpicker.activities.ColorPickerActivity#mPickedColorProgressAnimator}.
     */
    protected static final String PICKED_COLOR_PROGRESS_PROPERTY_NAME = "pickedColorProgress";

    /**
     * The name of the property that animates the 'save completed'.
     * <p/>
     * Used by {@link fr.tvbarthel.apps.cameracolorpicker.activities.ColorPickerActivity#mSaveCompletedProgressAnimator}.
     */
    protected static final String SAVE_COMPLETED_PROGRESS_PROPERTY_NAME = "saveCompletedProgress";

    /**
     * A safe way to get an instance of the back {@link android.hardware.Camera}.
     */
    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return c;
    }

    /**
     * An instance of the {@link android.hardware.Camera} used for displaying the preview.
     */
    protected Camera mCamera;
    protected boolean mIsPortrait;
    protected FrameLayout mPreviewContainer;
    protected FrameLayout.LayoutParams mPreviewParams;
    protected CameraColorPickerPreview mCameraPreview;
    protected CameraAsyncTask mCameraAsyncTask;

    protected int mSelectedColor;
    protected int mLastPickedColor;

    protected View mColorPreview;
    protected View mColorPreviewAnimated;
    protected ObjectAnimator mPickedColorProgressAnimator;

    protected TextView mColorPreviewText;

    protected View mPointerRing;
    protected float mTranslationDeltaX;
    protected float mTranslationDeltaY;

    protected View mSaveCompletedIcon;
    protected View mSaveButton;
    protected ObjectAnimator mSaveCompletedProgressAnimator;
    protected float mSaveCompletedProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        initPickedColorProgressAnimator();
        initSaveEnableProgressAnimator();
        initViews();
        initTranslationDeltas();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup the camera asynchronously.
        mCameraAsyncTask = new CameraAsyncTask();
        mCameraAsyncTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cancel the Camera AsyncTask.
        mCameraAsyncTask.cancel(true);

        // Release the camera.
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }

        // Remove the camera preview
        if (mCameraPreview != null) {
            mPreviewContainer.removeView(mCameraPreview);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        boolean handled;
        switch (itemId) {
            case android.R.id.home:
                finish();
                handled = true;
                break;

            default:
                handled = super.onOptionsItemSelected(item);
        }

        return handled;
    }

    @Override
    public void onColorSelected(int color) {
        mSelectedColor = color;
        mPointerRing.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onClick(View v) {
        if (v == mCameraPreview) {
            animatePickedColor(mSelectedColor);
        } else if (v.getId() == R.id.activity_color_picker_save_button) {
            ColorItems.saveColorItem(this, new ColorItem(mLastPickedColor));
            setSaveCompleted(true);
        }
    }

    protected void initViews() {
        mIsPortrait = getResources().getBoolean(R.bool.is_portrait);
        mPreviewContainer = (FrameLayout) findViewById(R.id.activity_color_picker_preview_container);
        mColorPreview = findViewById(R.id.activity_color_picker_color_preview);
        mColorPreviewAnimated = findViewById(R.id.activity_color_picker_animated_preview);
        mColorPreviewText = (TextView) findViewById(R.id.activity_color_picker_color_preview_text);
        mPointerRing = findViewById(R.id.activity_color_picker_pointer_ring);
        mSaveCompletedIcon = findViewById(R.id.activity_color_picker_save_completed);
        mSaveButton = findViewById(R.id.activity_color_picker_save_button);
        mSaveButton.setOnClickListener(this);

        mLastPickedColor = ColorItems.getLastPickedColor(this);
        applyPreviewColor(mLastPickedColor);
    }

    protected void initTranslationDeltas() {
        ViewTreeObserver vto = mPointerRing.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver vto = mPointerRing.getViewTreeObserver();
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                        vto.removeGlobalOnLayoutListener(this);
                    } else {
                        vto.removeOnGlobalLayoutListener(this);
                    }

                    final Rect pointerRingRect = new Rect();
                    final Rect colorPreviewAnimatedRect = new Rect();
                    mPointerRing.getGlobalVisibleRect(pointerRingRect);
                    mColorPreviewAnimated.getGlobalVisibleRect(colorPreviewAnimatedRect);

                    mTranslationDeltaX = pointerRingRect.left - colorPreviewAnimatedRect.left;
                    mTranslationDeltaY = pointerRingRect.top - colorPreviewAnimatedRect.top;
                }
            });
        }
    }


    protected void initPickedColorProgressAnimator() {
        mPickedColorProgressAnimator = ObjectAnimator.ofFloat(this, PICKED_COLOR_PROGRESS_PROPERTY_NAME, 1f, 0f);
        mPickedColorProgressAnimator.setDuration(400);
        mPickedColorProgressAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mColorPreviewAnimated.setVisibility(View.VISIBLE);
                mColorPreviewAnimated.getBackground().setColorFilter(mSelectedColor, PorterDuff.Mode.SRC_ATOP);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ColorItems.saveLastPickedColor(ColorPickerActivity.this, mLastPickedColor);
                applyPreviewColor(mLastPickedColor);
                mColorPreviewAnimated.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mColorPreviewAnimated.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    protected void initSaveEnableProgressAnimator() {
        mSaveCompletedProgressAnimator = ObjectAnimator.ofFloat(this, SAVE_COMPLETED_PROGRESS_PROPERTY_NAME, 1f, 0f);
    }

    protected void applyPreviewColor(int previewColor) {
        setSaveCompleted(false);
        mColorPreview.getBackground().setColorFilter(previewColor, PorterDuff.Mode.SRC_ATOP);
        mColorPreviewText.setText(ColorItem.makeHexString(previewColor));
    }

    protected void animatePickedColor(int pickedColor) {
        mLastPickedColor = pickedColor;
        if (mPickedColorProgressAnimator.isRunning()) {
            mPickedColorProgressAnimator.cancel();
        }
        mPickedColorProgressAnimator.start();
    }

    protected void setSaveCompleted(boolean isSaveCompleted) {
        mSaveButton.setEnabled(!isSaveCompleted);
        mSaveCompletedProgressAnimator.cancel();
        mSaveCompletedProgressAnimator.setFloatValues(mSaveCompletedProgress, isSaveCompleted ? 0f : 1f);
        mSaveCompletedProgressAnimator.start();
    }

    protected void setPickedColorProgress(float progress) {
        final float fastOppositeProgress = (float) Math.pow(1 - progress, 0.3f);
        final float translationX = (float) (mTranslationDeltaX * Math.pow(progress, 2f));
        final float translationY = mTranslationDeltaY * progress;

        mColorPreviewAnimated.setTranslationX(translationX);
        mColorPreviewAnimated.setTranslationY(translationY);
        mColorPreviewAnimated.setScaleX(fastOppositeProgress);
        mColorPreviewAnimated.setScaleY(fastOppositeProgress);
    }

    protected void setSaveCompletedProgress(float progress) {
        mSaveButton.setScaleX(progress);
        mSaveButton.setRotation(45 * (1 - progress));
        mSaveCompletedIcon.setScaleX(1 - progress);
        mSaveCompletedProgress = progress;
    }

    /**
     * Async task used to configure and start camera
     */
    private class CameraAsyncTask extends AsyncTask<Void, Void, Camera> {

        @Override
        protected Camera doInBackground(Void... params) {
            Camera camera = getCameraInstance();
            if (camera == null) {
                ColorPickerActivity.this.finish();
            } else {
                //configure Camera parameters
                Camera.Parameters cameraParameters = camera.getParameters();

                //get optimal camera preview size according to the layout used to display it
                Camera.Size bestSize = Cameras.getBestPreviewSize(
                        cameraParameters.getSupportedPreviewSizes()
                        , mPreviewContainer.getWidth()
                        , mPreviewContainer.getHeight()
                        , mIsPortrait);
                //set optimal camera preview
                cameraParameters.setPreviewSize(bestSize.width, bestSize.height);
                cameraParameters.setPictureSize(bestSize.width, bestSize.height);
                camera.setParameters(cameraParameters);

                //set camera orientation to match with current device orientation
                Cameras.setCameraDisplayOrientation(ColorPickerActivity.this, camera);

                //get proportional dimension for the layout used to display preview according to the preview size used
                int[] adaptedDimension = Cameras.getProportionalDimension(
                        bestSize
                        , mPreviewContainer.getWidth()
                        , mPreviewContainer.getHeight()
                        , mIsPortrait);

                //set up params for the layout used to display the preview
                mPreviewParams = new FrameLayout.LayoutParams(adaptedDimension[0], adaptedDimension[1]);
                mPreviewParams.gravity = Gravity.CENTER;
            }
            return camera;
        }

        @Override
        protected void onPostExecute(Camera camera) {
            super.onPostExecute(camera);

            // Check if the task is cancelled before trying to use the camera.
            if (!isCancelled()) {
                mCamera = camera;
                if (mCamera == null) {
                    ColorPickerActivity.this.finish();
                } else {
                    //set up camera preview
                    mCameraPreview = new CameraColorPickerPreview(ColorPickerActivity.this, mCamera);
                    mCameraPreview.setOnColorSelectedListener(ColorPickerActivity.this);
                    mCameraPreview.setOnClickListener(ColorPickerActivity.this);

                    //add camera preview
                    mPreviewContainer.addView(mCameraPreview, 0, mPreviewParams);
                }
            }
        }

        @Override
        protected void onCancelled(Camera camera) {
            super.onCancelled(camera);
            if (camera != null) {
                camera.release();
            }
        }
    }
}
