package fr.tvbarthel.apps.cameracolorpicker.activities;

import android.view.View;

/**
 * {@link ColorPickerBaseActivity} implementation used for the adults.
 */
public class ColorPickerActivity extends ColorPickerBaseActivity {

    @Override
    protected void setSaveCompleted(boolean isSaveCompleted) {
        super.setSaveCompleted(isSaveCompleted);
        if (isSaveCompleted) {
            mConfirmSaveMessage.setVisibility(View.VISIBLE);
            mConfirmSaveMessage.animate().translationY(0).setDuration(DURATION_CONFIRM_SAVE_MESSAGE)
                    .setInterpolator(mConfirmSaveMessageInterpolator).start();
            mConfirmSaveMessage.removeCallbacks(mHideConfirmSaveMessage);
            mConfirmSaveMessage.postDelayed(mHideConfirmSaveMessage, DELAY_HIDE_CONFIRM_SAVE_MESSAGE);
        }
    }
}
