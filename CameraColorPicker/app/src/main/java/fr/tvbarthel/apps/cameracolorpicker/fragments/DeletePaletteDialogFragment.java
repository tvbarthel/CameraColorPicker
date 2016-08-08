package fr.tvbarthel.apps.cameracolorpicker.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import fr.tvbarthel.apps.cameracolorpicker.data.Palette;

/**
 * A simple {@link android.support.v4.app.DialogFragment} used to ask the user to confirm the deletion of a {@link fr.tvbarthel.apps.cameracolorpicker.data.Palette}.
 */
public class DeletePaletteDialogFragment extends DialogFragment {

    /**
     * The key used to pass the {@link fr.tvbarthel.apps.cameracolorpicker.data.Palette} to delete as an argument.
     */
    private static final String ARG_PALETTE = "DeletePaletteDialogFragment.Args.ARG_PALETTE";

    /**
     * Create a new instance of a {@link DeletePaletteDialogFragment} to ask the user to confirm the deletion of a {@link fr.tvbarthel.apps.cameracolorpicker.data.Palette}.
     *
     * @param paletteToDelete the {@link fr.tvbarthel.apps.cameracolorpicker.data.Palette} to delete.
     * @return the newly created {@link DeletePaletteDialogFragment}.
     */
    public static DeletePaletteDialogFragment newInstance(Palette paletteToDelete) {
        final DeletePaletteDialogFragment instance = new DeletePaletteDialogFragment();
        final Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_PALETTE, paletteToDelete);
        instance.setArguments(arguments);
        return instance;
    }

    /**
     * A {@link DeletePaletteDialogFragment.Callback} used when the user confirms the deletion of a {@link fr.tvbarthel.apps.cameracolorpicker.data.Palette}.
     */
    private Callback mCallback;

    /**
     * Default Constructor.
     * <p/>
     * lint [ValidFragment]
     * http://developer.android.com/reference/android/app/Fragment.html#Fragment()
     * Every fragment must have an empty constructor, so it can be instantiated when restoring its activity's state.
     */
    public DeletePaletteDialogFragment() {
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle arguments = getArguments();

        if (!arguments.containsKey(ARG_PALETTE)) {
            throw new IllegalStateException("Missing args. Please use the newInstance() method.");
        }

        final Palette paletteToDelete = arguments.getParcelable(ARG_PALETTE);
        return DeletePaletteDialogFragmentFlavor.createDialog(getContext(), paletteToDelete, mCallback);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Callback) {
            mCallback = (Callback) activity;
        } else {
            throw new IllegalStateException("Activity must implements DeletePaletteDialogFragment#Callback.");
        }
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
         * Called when the deletion of a {@link fr.tvbarthel.apps.cameracolorpicker.data.Palette} has been confirmed by the user.
         *
         * @param paletteToDelete the {@link fr.tvbarthel.apps.cameracolorpicker.data.Palette} to delete.
         */
        void onPaletteDeletionConfirmed(@NonNull Palette paletteToDelete);
    }
}
