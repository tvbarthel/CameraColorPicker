package fr.tvbarthel.apps.cameracolorpicker.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
        final View dialogView = LayoutInflater.from(activity).inflate(R.layout.fragment_dialog_about, null);

        ((TextView) dialogView.findViewById(R.id.fragment_dialog_about_version_name))
                .setText(getString(R.string.fragment_dialog_about_version_name,
                        Versions.getVersionName(activity)));

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, null);

        return builder.create();
    }
}
