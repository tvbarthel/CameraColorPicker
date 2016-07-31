package fr.tvbarthel.apps.cameracolorpicker.activities;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.data.Palettes;
import fr.tvbarthel.apps.cameracolorpicker.views.PaletteMakerView;

/**
 * The flavor implementation of {@link PaletteCreationBaseActivity}.
 *
 * In the kids version, a default name is used.
 */
public class PaletteCreationActivity extends PaletteCreationBaseActivity {

    @Override
    protected void createPalette(PaletteMakerView paletteMakerView) {
        final Palette newPalette = paletteMakerView.make(getString(R.string.activity_palette_creation_default_palette_name, System.currentTimeMillis()));
        if (Palettes.saveColorPalette(this, newPalette)) {
            finish();
        }
    }

}
