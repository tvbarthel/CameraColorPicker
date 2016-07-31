package fr.tvbarthel.apps.cameracolorpicker.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Utils used to encapsulate any common behavior linked to background.
 * <p/>
 * Implementation for pre lollipop devices.
 */
final class BackgroundUtilsImplPreLollipop {

    /**
     * Non instantiable class.
     */
    private BackgroundUtilsImplPreLollipop() {

    }

    /**
     * Build a background programmatically.
     *
     * @param normalColor  background color.
     * @param pressedColor color used when the state is pressed.
     * @return drawable well initialized.
     */
    static Drawable getBackground(int normalColor, int pressedColor) {
        return getPressedColorDrawable(normalColor, pressedColor);
    }

    private static Drawable getPressedColorDrawable(int normalColor, int pressedColor) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(
                new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled},
                getColorDrawableFromColor(pressedColor)
        );
        drawable.addState(
                new int[]{android.R.attr.state_focused},
                getColorDrawableFromColor(pressedColor)
        );
        drawable.addState(
                new int[]{android.R.attr.state_activated},
                getColorDrawableFromColor(pressedColor)
        );
        drawable.addState(
                new int[]{},
                getColorDrawableFromColor(normalColor)
        );
        return drawable;
    }

    /**
     * Generate a drawable according to a color.
     *
     * @param color color of the drawable.
     * @return color drawable.
     */
    private static ColorDrawable getColorDrawableFromColor(int color) {
        return new ColorDrawable(color);
    }
}
