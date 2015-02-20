package fr.tvbarthel.apps.cameracolorpicker.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.utils.Versions;

/**
 * A simple {@link android.support.v4.app.DialogFragment} for displaying some information about this application.
 */
public class AboutDialogFragment extends DialogFragment {

    /**
     * Create a new instance of {@link fr.tvbarthel.apps.cameracolorpicker.fragments.AboutDialogFragment}.
     *
     * @return the newly created {@link fr.tvbarthel.apps.cameracolorpicker.fragments.AboutDialogFragment}.
     */
    public static AboutDialogFragment newInstance() {
        return new AboutDialogFragment();
    }

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public AboutDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(R.string.fragment_about_title)
                .setMessage(getString(R.string.fragment_about_message, Versions.getVersionName(activity)))
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, null);

        return builder.create();
    }
}
