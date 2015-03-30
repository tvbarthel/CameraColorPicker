package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.adapters.ColorItemAdapter;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItems;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.data.Palettes;
import fr.tvbarthel.apps.cameracolorpicker.fragments.EditTextDialogFragment;
import fr.tvbarthel.apps.cameracolorpicker.views.PaletteMakerView;

/**
 * An {@link ActionBarActivity} for creating a {@link Palette} from the {@link ColorItem}s that the user saved.
 */
public class PaletteCreationActivity extends ActionBarActivity implements OnClickListener, EditTextDialogFragment.Callback {

    /**
     * The {@link PaletteMakerView} used for building a palette of colors.
     */
    private PaletteMakerView mPaletteMakerView;

    /**
     * An {@link ObjectAnimator} used for animating the button that removes the last color from the color palette builder.
     */
    private ObjectAnimator mRemoveLastColorBtnAnimator;

    /**
     * A reference to the last {@link Toast} so that we can cancel it during onPause and before showing a new one.
     */
    private Toast mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette_creation);

        final ColorItemAdapter colorItemAdapter = new ColorItemAdapter(this);
        final List<ColorItem> colorItems = ColorItems.getSavedColorItems(this);
        colorItemAdapter.addAll(colorItems);

        mPaletteMakerView = (PaletteMakerView) findViewById(R.id.activity_palette_creation_color_palette_builder);
        final ListView listView = (ListView) findViewById(R.id.activity_palette_creation_list_view);
        listView.setAdapter(colorItemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ColorItem colorItem = colorItemAdapter.getItem(position);
                mPaletteMakerView.addColor(colorItem);
                if (mPaletteMakerView.size() == 1) {
                    mRemoveLastColorBtnAnimator.setFloatValues(1f);
                    mRemoveLastColorBtnAnimator.start();
                }
            }
        });

        final View removeButton = findViewById(R.id.activity_palette_creation_remove_button);
        removeButton.setOnClickListener(this);
        removeButton.setScaleX(0f);
        mRemoveLastColorBtnAnimator = ObjectAnimator.ofFloat(removeButton, View.SCALE_X, 0f, 1f);

        findViewById(R.id.activity_palette_creation_fab).setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        hideToast();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        switch (viewId) {
            case R.id.activity_palette_creation_remove_button:
                mPaletteMakerView.removeLastColor();
                if (mPaletteMakerView.isEmpty()) {
                    mRemoveLastColorBtnAnimator.setFloatValues(0f);
                    mRemoveLastColorBtnAnimator.start();
                }
                break;

            case R.id.activity_palette_creation_fab:
                if (mPaletteMakerView.isEmpty()) {
                    // The color palette builder is empty.
                    // Can't create an empty color palette, show a toast with an instruction.
                    showToast(R.string.activity_palette_creation_empty_color_palette_builder);
                } else {
                    final EditTextDialogFragment editTextDialogFragment = EditTextDialogFragment.newInstance(0,
                            R.string.activity_palette_creation_edit_text_dialog_fragment_title,
                            R.string.activity_palette_creation_edit_text_dialog_fragment_positive_button,
                            android.R.string.cancel,
                            R.string.activity_palette_creation_edit_text_dialog_fragment_hint,
                            null);
                    editTextDialogFragment.show(getSupportFragmentManager(), null);
                }
                break;

            default:
                throw new IllegalArgumentException("Unsupported view clicked. Found: " + v);
        }
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

    /**
     * Hide the current {@link android.widget.Toast}.
     */
    private void hideToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    /**
     * Show a toast text message.
     *
     * @param resId The resource id of the string resource to use.
     */
    private void showToast(@StringRes int resId) {
        hideToast();
        mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
