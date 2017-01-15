package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;

import fr.tvbarthel.apps.cameracolorpicker.R;

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
        // For the kid version, set the title to null.
        //noinspection ConstantConditions
        activity.getSupportActionBar().setTitle(null);
    }

    /**
     * Called when {@link PaletteDetailActivity#onCreateOptionsMenu(Menu)} is called.
     *
     * @param menu the {@link Menu} created.
     */
    /* package */
    static void onCreateOptionsMenu(Menu menu) {
        // For the kid version, hide the edit and share menu items.
        menu.removeItem(R.id.menu_palette_detail_action_edit);
        menu.removeItem(R.id.menu_palette_detail_action_share);
    }


    private PaletteDetailActivityFlavor() {
        // non-instantiable.
    }

}
