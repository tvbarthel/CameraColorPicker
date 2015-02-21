package fr.tvbarthel.apps.cameracolorpicker.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.TextureView;

/**
 * A simple {@link android.view.TextureView} used to render camera preview.
 */
public class CameraColorPickerPreview extends TextureView implements TextureView.SurfaceTextureListener, Camera.PreviewCallback {

    /**
     * A tag for logging.
     */
    private static final String TAG = CameraColorPickerPreview.class.getCanonicalName();

    /**
     * The size of the pointer (in PIXELS).
     */
    protected static final int POINTER_RADIUS = 5;

    /**
     * The {@link android.hardware.Camera} used for getting a preview frame.
     */
    protected Camera mCamera;

    /**
     * The {@link android.hardware.Camera.Size} of the preview.
     */
    protected Camera.Size mPreviewSize;

    /**
     * An array of 3 integers representing the color being selected.
     */
    protected int[] mSelectedColor;

    /**
     * An {@link fr.tvbarthel.apps.cameracolorpicker.views.CameraColorPickerPreview.OnColorSelectedListener} that will be called each time a new color is being selected.
     */
    protected OnColorSelectedListener mOnColorSelectedListener;

    public CameraColorPickerPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mCamera.getParameters().getPreviewFormat();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        this.setSurfaceTextureListener(this);

        mPreviewSize = mCamera.getParameters().getPreviewSize();
        mSelectedColor = new int[3];
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewTexture(surface);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }


    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mOnColorSelectedListener != null) {
            final int midX = mPreviewSize.width / 2;
            final int midY = mPreviewSize.height / 2;

            // Reset the selected color.
            mSelectedColor[0] = 0;
            mSelectedColor[1] = 0;
            mSelectedColor[2] = 0;

            // Compute the average selected color.
            for (int i = 0; i <= POINTER_RADIUS; i++) {
                for (int j = 0; j <= POINTER_RADIUS; j++) {
                    addColorFromYUV420(data, mSelectedColor, (i * POINTER_RADIUS + j + 1),
                            (midX - POINTER_RADIUS) + i, (midY - POINTER_RADIUS) + j,
                            mPreviewSize.width, mPreviewSize.height);
                }
            }

            mOnColorSelectedListener.onColorSelected(Color.rgb(mSelectedColor[0], mSelectedColor[1], mSelectedColor[2]));
        }
    }

    protected void addColorFromYUV420(byte[] data, int[] averageColor, int count, int x, int y, int width, int height) {
        // The code converting YUV420 to rgb format is highly inspired from this post http://stackoverflow.com/a/10125048
        final int size = width * height;
        final int Y = data[y * width + x] & 0xff;
        final int xby2 = x / 2;
        final int yby2 = y / 2;

        final float V = (float) (data[size + 2 * xby2 + yby2 * width] & 0xff) - 128.0f;
        final float U = (float) (data[size + 2 * xby2 + 1 + yby2 * width] & 0xff) - 128.0f;

        // Do the YUV -> RGB conversion
        float Yf = 1.164f * ((float) Y) - 16.0f;
        int red = (int) (Yf + 1.596f * V);
        int green = (int) (Yf - 0.813f * V - 0.391f * U);
        int blue = (int) (Yf + 2.018f * U);

        // Clip rgb values to [0-255]
        red = red < 0 ? 0 : red > 255 ? 255 : red;
        green = green < 0 ? 0 : green > 255 ? 255 : green;
        blue = blue < 0 ? 0 : blue > 255 ? 255 : blue;

        averageColor[0] += (red - averageColor[0]) / count;
        averageColor[1] += (green - averageColor[1]) / count;
        averageColor[2] += (blue - averageColor[2]) / count;
    }


    /**
     * Set a {@link fr.tvbarthel.apps.cameracolorpicker.views.CameraColorPickerPreview.OnColorSelectedListener} that will be called each time a new color is selected.
     *
     * @param onColorSelectedListener the {@link fr.tvbarthel.apps.cameracolorpicker.views.CameraColorPickerPreview.OnColorSelectedListener} that will be called.
     */
    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        mOnColorSelectedListener = onColorSelectedListener;
    }


    /**
     * An interface for callback.
     */
    public interface OnColorSelectedListener {

        /**
         * Called when a new color has just been selected.
         *
         * @param newColor the new color that has just been selected.
         */
        void onColorSelected(int newColor);
    }

}