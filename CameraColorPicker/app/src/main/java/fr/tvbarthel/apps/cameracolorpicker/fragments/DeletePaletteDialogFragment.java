package fr.tvbarthel.apps.cameracolorpicker.fragments;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.views.PaletteView;

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

        final Context context = getActivity();
        final View view = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_delete_palette, null);
        final PaletteView paletteView = (PaletteView) view.findViewById(R.id.fragment_dialog_delete_palette_preview);
        final TextView messageTextView = (TextView) view.findViewById(R.id.fragment_dialog_delete_palette_message);

        paletteView.setPalette(paletteToDelete);
        messageTextView.setText(getString(R.string.fragment_dialog_delete_palette_message, paletteToDelete.getName()));

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback.onPaletteDeletionConfirmed(paletteToDelete);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();
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
