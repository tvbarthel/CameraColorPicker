package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import fr.tvbarthel.apps.cameracolorpicker.R;

/**
 * {@link ColorPickerBaseActivity} implementation used for the kids.
 */
public class ColorPickerActivity extends ColorPickerBaseActivity {

    private Animation wiggle;
    private ColorDrawable mActionBarBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        mColorPreviewText.setVisibility(View.INVISIBLE);
        mActionBarBackground = new ColorDrawable(ContextCompat.getColor(this, R.color.color_primary));

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(null);
            supportActionBar.setBackgroundDrawable(mActionBarBackground);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void setSaveCompleted(boolean isSaveCompleted) {
        super.setSaveCompleted(isSaveCompleted);
        if (isSaveCompleted) {
            mPointerRing.setVisibility(View.INVISIBLE);
            finish();
        }
    }

    @Override
    protected void animatePickedColor(int pickedColor) {
        super.animatePickedColor(pickedColor);
        mSaveButton.postDelayed(new Runnable() {

            @Override
            public void run() {
                mSaveButton.startAnimation(wiggle);
            }
        }, 500);
    }

    @Override
    public void onColorSelected(int color) {
        super.onColorSelected(color);
        mActionBarBackground.setColor(color);
    }
}
