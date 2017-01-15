package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.os.Bundle;
import android.view.Menu;

/**
 * Static methods for specific flavor behaviour of {@link ColorDetailActivity}.
 */
/* package */
final class ColorDetailActivityFlavor {

    /**
     * Called when {@link ColorDetailActivity#onCreate(Bundle)} is called.
     * <p/>
     * <b>Note</b> this should be the last methods called in {@link ColorDetailActivity#onCreate(Bundle)}.
     *
     * @param activity the {@link ColorDetailActivity}.
     */
    /* package */
    static void onCreate(ColorDetailActivity activity) {
        // nothing specific for the adult flavor.
    }

    /**
     * Called when {@link ColorDetailActivity#onCreateOptionsMenu(Menu)} is called.
     *
     * @param menu the {@link Menu} created.
     */
    /* package */
    static void onCreateOptionsMenu(Menu menu) {
        // nothing specific for the adult flavor.
    }

    private ColorDetailActivityFlavor() {
        // non-instantiable.
    }
}
