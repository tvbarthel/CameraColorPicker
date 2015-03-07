package fr.tvbarthel.apps.cameracolorpicker.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;

/**
 * A simple {@link android.support.v4.app.DialogFragment} used to ask the user to confirm the deletion of a {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
 */
public final class DeleteColorDialogFragment extends DialogFragment {

    /**
     * The key used to pass the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem} to delete as an argument.
     */
    private static final String ARG_COLOR_ITEM = "DeleteColorDialog.Args.ARG_COLOR_ITEM";

    /**
     * Create a new instance of a {@link DeleteColorDialogFragment} to ask the user to confirm the deletion of a {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     *
     * @param colorItemToDelete the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem} to delete.
     * @return the newly created {@link DeleteColorDialogFragment}.
     */
    public static DeleteColorDialogFragment newInstance(ColorItem colorItemToDelete) {
        final DeleteColorDialogFragment instance = new DeleteColorDialogFragment();
        final Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_COLOR_ITEM, colorItemToDelete);
        instance.setArguments(arguments);
        return instance;
    }

    /**
     * A {@link DeleteColorDialogFragment.Callback} used when the user confirms the deletion of a {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     */
    private Callback mCallback;

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public DeleteColorDialogFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callback) {
            mCallback = (Callback) activity;
        } else {
            throw new IllegalStateException("Activity must implements DeleteColorDialog#Callback.");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle arguments = getArguments();

        if (!arguments.containsKey(ARG_COLOR_ITEM)) {
            throw new IllegalStateException("Missing args. Please use the newInstance() method.");
        }

        final ColorItem colorItemToDelete = arguments.getParcelable(ARG_COLOR_ITEM);

        final Context context = getActivity();
        final View view = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_delete_color, null);
        view.findViewById(R.id.fragment_dialog_delete_color_preview).setBackgroundColor(colorItemToDelete.getColor());
        
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback.onColorDeletionConfirmed(colorItemToDelete);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    /**
     * A simple interface for callbacks.
     */
    public interface Callback {

        /**
         * Called when the deletion of a {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem} has been confirmed by the user.
         *
         * @param colorItemToDelete the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem} to delete.
         */
        void onColorDeletionConfirmed(@NonNull ColorItem colorItemToDelete);
    }
}
