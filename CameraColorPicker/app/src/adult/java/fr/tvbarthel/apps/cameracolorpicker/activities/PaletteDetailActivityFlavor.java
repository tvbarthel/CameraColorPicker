package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.os.Bundle;
import android.view.Menu;

/**
 * Static methods for specific flavor behaviour of {@link PaletteDetailActivity}.
 */
/* package */
final class PaletteDetailActivityFlavor {


    /**
     * Called when {@link PaletteDetailActivity#onCreate(Bundle)} is called.
     * <p/>
     * <b>Note</b> this should be the last methods called in {@link PaletteDetailActivity#onCreate(Bundle)}.
     *
     * @param activity the {@link PaletteDetailActivity}.
     */
    /* package */
    static void onCreate(PaletteDetailActivity activity) {
        // nothing specific for the adult flavor.
    }

    /**
     * Called when {@link PaletteDetailActivity#onCreateOptionsMenu(Menu)} is called.
     *
     * @param menu the {@link Menu} created.
     */
    /* package */
    static void onCreateOptionsMenu(Menu menu) {
        // nothing specific for the adult flavor.
    }


    private PaletteDetailActivityFlavor() {
        // non-instantiable.
    }

}
