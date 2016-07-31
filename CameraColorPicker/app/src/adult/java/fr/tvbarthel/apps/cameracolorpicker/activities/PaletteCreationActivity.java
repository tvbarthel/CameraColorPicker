package fr.tvbarthel.apps.cameracolorpicker.activities;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.data.Palettes;
import fr.tvbarthel.apps.cameracolorpicker.fragments.EditTextDialogFragment;
import fr.tvbarthel.apps.cameracolorpicker.views.PaletteMakerView;

/**
 * The flavor implementation of {@link PaletteCreationBaseActivity}.
 * <p/>
 * In the adult version, a name is asked to the user.
 */
public class PaletteCreationActivity extends PaletteCreationBaseActivity implements EditTextDialogFragment.Callback {


    @Override
    protected void createPalette(PaletteMakerView paletteMakerView) {
        final EditTextDialogFragment editTextDialogFragment = EditTextDialogFragment.newInstance(0,
                R.string.activity_palette_creation_edit_text_dialog_fragment_title,
                R.string.activity_palette_creation_edit_text_dialog_fragment_positive_button,
                android.R.string.cancel,
                getString(R.string.activity_palette_creation_edit_text_dialog_fragment_hint),
                null);
        editTextDialogFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onEditTextDialogFragmentPositiveButtonClick(int requestCode, String text) {
        final Palette newPalette = mPaletteMakerView.make(text);
        if (Palettes.saveColorPalette(this, newPalette)) {
            finish();
        }
    }


    @Override
    public void onEditTextDialogFragmentNegativeButtonClick(int requestCode) {
        // Do nothing when the user cancels.
    }

}
