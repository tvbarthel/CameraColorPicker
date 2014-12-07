package fr.tvbarthel.apps.cameracolorpicker.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.WindowManager;

import java.util.List;

/**
 * Static utils methods for dealing with {@link android.hardware.Camera}.
 */
public final class Cameras {

    private static final double ASPECT_TOLERANCE = 0.15;

    /**
     * Calculate the optimal size of camera preview according to layout used to display this preview
     *
     * @param sizes        Available camera preview sizes
     * @param layoutWidth  width of layout used to display the preview
     * @param layoutHeight height of the layout used to display the preview
     * @param isPortrait   indicate if current orientation is portrait since the camera sizes are all in landscape mode
     * @return best camera preview size which fit layout used to display the preview
     */
    public static Camera.Size getBestPreviewSize(List<Camera.Size> sizes, int layoutWidth, int layoutHeight, boolean isPortrait) {
        if (isPortrait) {
            // Inverse surfaceWidth and surfaceHeight since the sizes are all in landscape mode.
            layoutHeight = layoutHeight + layoutWidth;
            layoutWidth = layoutHeight - layoutWidth;
            layoutHeight = layoutHeight - layoutWidth;
        }
        double targetRatio = (double) layoutWidth / layoutHeight;
        Camera.Size optimalSize = null;
        double optimalArea = 0;

        // Try to find the size that matches the target ratio and has the max area.
        for (Camera.Size candidateSize : sizes) {
            double candidateRatio = (double) candidateSize.width / candidateSize.height;
            double candidateArea = candidateSize.width * candidateSize.height;
            double ratioDifference = Math.abs(candidateRatio - targetRatio);
            if (ratioDifference < ASPECT_TOLERANCE && candidateArea > optimalArea) {
                optimalSize = candidateSize;
                optimalArea = candidateArea;
            }
        }

        // Cannot find a size that matches the target ratio.
        // Try to find the size that has the max area.
        if (optimalSize == null) {
            optimalArea = 0;
            for (Camera.Size candidateSize : sizes) {
                double candidateArea = candidateSize.width * candidateSize.height;
                if (candidateArea > optimalArea) {
                    optimalSize = candidateSize;
                    optimalArea = candidateArea;
                }
            }
        }

        return optimalSize;
    }

    /**
     * calculate proportional layout dimension for displaying a camera preview according to a given camera preview size
     *
     * @param size       Camera preview size used {@see CameraUtils.getBestPreviewSize}
     * @param targetW    current width of the layout which will host camera preview
     * @param targetH    current height of the layout which will host camera preview
     * @param isPortrait indicate if current orientation is portrait since the camera sizes are all in landscape mode
     * @return dimension which matches camera preview size ratio
     */
    public static int[] getProportionalDimension(Camera.Size size, int targetW, int targetH, boolean isPortrait) {
        int[] adaptedDimension = new int[2];
        double previewRatio;

        if (isPortrait) {
            previewRatio = (double) size.height / size.width;
        } else {
            previewRatio = (double) size.width / size.height;
        }

        if (((double) targetW / targetH) > previewRatio) {
            adaptedDimension[0] = targetW;
            adaptedDimension[1] = (int) (adaptedDimension[0] / previewRatio);
        } else {
            adaptedDimension[1] = targetH;
            adaptedDimension[0] = (int) (adaptedDimension[1] * previewRatio);
        }

        return adaptedDimension;
    }

    /**
     * Adapt the {@link android.hardware.Camera} display orientaiton to the current device orientation.
     *
     * @param context a {@link android.content.Context} to retrieve device orientation.
     * @param camera  the {@link android.hardware.Camera} to be adapted.
     */
    public static void setCameraDisplayOrientation(Context context, android.hardware.Camera camera) {
        final Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int degrees = 0;
        final int currentRotation = ((WindowManager) context.getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch (currentRotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int displayOrientation = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            displayOrientation = (info.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;  // compensate the mirror
        } else {  // back-facing
            displayOrientation = (info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(displayOrientation);
    }


    //Non instantiable class.
    private Cameras() {
    }
}
