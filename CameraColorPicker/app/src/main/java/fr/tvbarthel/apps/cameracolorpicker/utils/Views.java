package fr.tvbarthel.apps.cameracolorpicker.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Static methods for dealing with views.
 */
public final class Views {

    // Non-instantiability.
    private Views() {
    }

    /**
     * Convert a value from dip to pixel.
     *
     * @param displayMetrics the {@link android.util.DisplayMetrics} used to convert.
     * @param dip            the value in dip to convert.
     * @return a value in pixel.
     */
    public static int dipToPx(DisplayMetrics displayMetrics, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
    }

}