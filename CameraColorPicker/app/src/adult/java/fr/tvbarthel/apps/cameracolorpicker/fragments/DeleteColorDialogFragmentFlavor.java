package fr.tvbarthel.apps.cameracolorpicker.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;

/**
 * Static class for flavor specific behaviour of {@link DeleteColorDialogFragment}.
 */
/* package */
final class DeleteColorDialogFragmentFlavor {

    /**
     * Create a {@link android.app.AlertDialog} for asking the user to confirm the deletion of a {@link Palette}.
     *
     * @param context           a {@link Context}.
     * @param callback          the {@link fr.tvbarthel.apps.cameracolorpicker.fragments.DeleteColorDialogFragment.Callback} that will be notify.
     * @param colorItemToDelete the {@link ColorItem} to delete.
     * @return a {@link AlertDialog}
     */
    /* package */
    static AlertDialog createDialog(Context context,
                                    final DeleteColorDialogFragment.Callback callback,
                                    final ColorItem colorItemToDelete) {
        final View view = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_delete_color, null);
        view.findViewById(R.id.fragment_dialog_delete_color_preview).setBackgroundColor(colorItemToDelete.getColor());

        return new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onColorDeletionConfirmed(colorItemToDelete);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }

    private DeleteColorDialogFragmentFlavor() {
        // Non-instantiable
    }
}
