package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.os.Bundle;

/**
 * Static methods for specific flavor behaviour of {@link PaletteCreationBaseActivity}.
 */
/* package */
final class PaletteCreationBaseActivityFlavor {

    /**
     * Called when {@link PaletteCreationBaseActivity#onCreate(Bundle)} is called.
     * <p/>
     * <b>Note</b> this should be the last methods called in {@link PaletteCreationBaseActivity#onCreate(Bundle)}.
     *
     * @param activity the {@link PaletteCreationBaseActivity}.
     */
    /* package */
    static void onCreate(PaletteCreationBaseActivity activity) {
        // nothing specific for the adult flavor.
    }
}
