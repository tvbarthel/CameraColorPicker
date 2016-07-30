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
        // nothing specific for the adult flavor.
    }

    private ColorPickerActivityFlavor() {
        // non-instantiable
    }
}
