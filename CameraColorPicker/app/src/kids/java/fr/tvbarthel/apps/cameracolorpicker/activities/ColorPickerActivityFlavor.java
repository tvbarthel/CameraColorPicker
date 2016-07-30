package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.os.Bundle;

/**
 * Static methods for specific flavor behaviour of {@link ColorPickerActivity}.
 */
/* package */
final class ColorPickerActivityFlavor {

    /**
     * Called when {@link ColorPickerActivity#onCreate(Bundle)} is called.
     * <p/>
     * <b>Note</b> this should be the last methods called in {@link ColorPickerActivity#onCreate(Bundle)}.
     *
     * @param activity the {@link ColorDetailActivity}.
     */
    /* package */
    static void onCreate(ColorPickerActivity activity) {
        // For the kid version, set the title to null.
        //noinspection ConstantConditions
        activity.getSupportActionBar().setTitle(null);
    }

    private ColorPickerActivityFlavor() {
        // non-instantiable
    }
}
