package fr.tvbarthel.apps.cameracolorpicker.utils;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Utils used to encapsulate any common behavior linked to background.
 */
public final class BackgroundUtils {

    /**
     * Non instantiable class.
     */
    private BackgroundUtils() {

    }

    /**
     * Build a background programmatically.
     *
     * @param view         view to which assign the background
     * @param normalColor  background color.
     * @param pressedColor color used when the state is pressed.
     */
    public static void buildBackgroundDrawable(View view, int normalColor, int pressedColor) {
        BackgroundUtils.setBackground(
                view,
                BackgroundUtils.buildBackgroundDrawable(normalColor, pressedColor)
        );
    }

    /**
     * Build a background programmatically.
     *
     * @param normalColor  background color.
     * @param pressedColor color used when the state is pressed.
     * @return drawable well initialized.
     */
    public static Drawable buildBackgroundDrawable(int normalColor, int pressedColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return BackgroundUtilsImplPostLollipop.getBackground(normalColor, pressedColor);
        } else {
            return BackgroundUtilsImplPreLollipop.getBackground(normalColor, pressedColor);
        }
    }

    /**
     * Proper way to set a background across android versions.
     *
     * @param view     view to which the background will be set.
     * @param drawable drawable to set as background, null to remove the current background.
     */
    public static void setBackground(@NonNull View view, @Nullable Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
