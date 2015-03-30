package fr.tvbarthel.apps.cameracolorpicker.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

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

    /**
     * Credit goes to Cyril Mottier.
     * https://plus.google.com/+CyrilMottier/posts/FABaJhRMCuy
     *
     * @param view the {@link View} to animate.
     * @return an {@link ObjectAnimator} that will play a 'nope' animation.
     */
    public static ObjectAnimator nopeAnimation(View view, int delta) {
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500);
    }

}