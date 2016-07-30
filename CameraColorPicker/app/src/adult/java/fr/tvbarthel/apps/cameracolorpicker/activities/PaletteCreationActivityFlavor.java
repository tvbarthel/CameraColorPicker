package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.os.Bundle;

/**
 * Static methods for specific flavor behaviour of {@link PaletteCreationActivity}.
 */
/* package */
final class PaletteCreationActivityFlavor {

    /**
     * Called when {@link PaletteCreationActivity#onCreate(Bundle)} is called.
     * <p/>
     * <b>Note</b> this should be the last methods called in {@link PaletteCreationActivity#onCreate(Bundle)}.
     *
     * @param activity the {@link PaletteCreationActivity}.
     */
    /* package */
    static void onCreate(PaletteCreationActivity activity) {
        // nothing specific for the adult flavor.
    }
}
