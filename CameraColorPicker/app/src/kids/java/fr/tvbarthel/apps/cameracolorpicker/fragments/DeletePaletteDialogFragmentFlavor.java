package fr.tvbarthel.apps.cameracolorpicker.fragments;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

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
        ((PaletteView) view.findViewById(R.id.fragment_dialog_delete_palette_preview)).setPalette(paletteToDelete);
        ((PaletteView) view.findViewById(R.id.fragment_dialog_delete_palette_preview_small)).setPalette(paletteToDelete);

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(view)
                .setCancelable(true)
                .create();

        view.findViewById(R.id.fragment_dialog_delete_palette_btn_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

        view.findViewById(R.id.fragment_dialog_delete_palette_btn_validate)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onPaletteDeletionConfirmed(paletteToDelete);
                        dialog.dismiss();
                    }
                });


        return dialog;
    }

    private DeletePaletteDialogFragmentFlavor() {
        // Non-instantiable
    }
}
