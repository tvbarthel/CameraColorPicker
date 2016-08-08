package fr.tvbarthel.apps.cameracolorpicker.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;
import fr.tvbarthel.apps.cameracolorpicker.views.PaletteView;

/**
 * Static class for flavor specific behaviour of {@link DeletePaletteDialogFragment}.
 */
/* package */
final class DeletePaletteDialogFragmentFlavor {

    /**
     * Create a {@link android.app.AlertDialog} for asking the user to confirm the deletion of a {@link Palette}.
     *
     * @param context         a {@link Context}.
     * @param paletteToDelete the {@link Palette} to delete.
     * @param callback        the {@link fr.tvbarthel.apps.cameracolorpicker.fragments.DeletePaletteDialogFragment.Callback} that will be notify.
     * @return a {@link AlertDialog}
     */
    /* package */
    static AlertDialog createDialog(Context context,
                                    final Palette paletteToDelete,
                                    final DeletePaletteDialogFragment.Callback callback) {
        final View view = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_delete_palette, null);
        final PaletteView paletteView = (PaletteView) view.findViewById(R.id.fragment_dialog_delete_palette_preview);
        final TextView messageTextView = (TextView) view.findViewById(R.id.fragment_dialog_delete_palette_message);

        paletteView.setPalette(paletteToDelete);
        messageTextView.setText(context.getString(R.string.fragment_dialog_delete_palette_message, paletteToDelete.getName()));

        return new AlertDialog.Builder(context).setView(view)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onPaletteDeletionConfirmed(paletteToDelete);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();

    }

    private DeletePaletteDialogFragmentFlavor() {
        // Non-instantiable
    }
}
