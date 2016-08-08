package fr.tvbarthel.apps.cameracolorpicker.utils;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;

/**
 * Utils used to encapsulate any common behavior linked to background.
 * <p/>
 * Implementation for post lollipop devices.
 */
final class BackgroundUtilsImplPostLollipop {
    /**
     * Non instantiable class.
     */
    private BackgroundUtilsImplPostLollipop() {

    }

    /**
     * Build a background programmatically.
     *
     * @param normalColor  background color.
     * @param pressedColor color used when the state is pressed.
     * @return drawable well initialized.
     */
    static Drawable getBackground(int normalColor, int pressedColor) {
        return getPressedColorRippleDrawable(normalColor, pressedColor);
    }

    /**
     * Get a ripple drawable.
     *
     * @param normalColor  color assigned to normal state.
     * @param pressedColor color assigned to pressed state.
     * @return well initialize ripple background.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static RippleDrawable getPressedColorRippleDrawable(int normalColor, int pressedColor) {
        return new RippleDrawable(
                ColorStateList.valueOf(pressedColor),
                new ColorDrawable(normalColor),
                new ColorDrawable(Color.WHITE)
        );
    }
}
