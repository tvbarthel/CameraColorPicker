package fr.tvbarthel.apps.cameracolorpicker.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;

import fr.tvbarthel.apps.cameracolorpicker.utils.Views;

/**
 * A simple {@link android.view.TextureView} used to render camera preview.
 */
public class CameraColorPickerPreview extends TextureView implements TextureView.SurfaceTextureListener, Camera.PreviewCallback {

    private static final String TAG = CameraColorPickerPreview.class.getCanonicalName();

    /**
     * The size of the pointer (in DIP).
     */
    protected static final float POINTER_SIZE = 10;

    protected Camera mCamera;
    protected int mPointerSize;
    protected Camera.Size mPreviewSize;
    protected OnColorPickedListener mOnColorPickedListener;
    protected int mWidth;
    protected int mHeight;

    public CameraColorPickerPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mCamera.getParameters().getPreviewFormat();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        this.setSurfaceTextureListener(this);

        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mPointerSize = Views.dipToPx(displayMetrics, POINTER_SIZE);
        mPreviewSize = mCamera.getParameters().getPreviewSize();
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
        if (mOnColorPickedListener != null) {
            final int midX = mPreviewSize.width / 2;
            final int midY = mPreviewSize.height / 2;
            mOnColorPickedListener.onColorPicked(getColorFromYUV420(data, midX, midY, mPreviewSize.width, mPreviewSize.height));
        }

    }

    protected int getColorFromYUV420(byte[] data, int x, int y, int width, int height) {
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

        // Clip rgb values to 0-255
        red = red < 0 ? 0 : red > 255 ? 255 : red;
        green = green < 0 ? 0 : green > 255 ? 255 : green;
        blue = blue < 0 ? 0 : blue > 255 ? 255 : blue;

        return Color.rgb(red, green, blue);
    }


    public void setOnColorPickedListener(OnColorPickedListener onColorPickedListener) {
        mOnColorPickedListener = onColorPickedListener;
    }

    public interface OnColorPickedListener {
        void onColorPicked(int color);
    }

}