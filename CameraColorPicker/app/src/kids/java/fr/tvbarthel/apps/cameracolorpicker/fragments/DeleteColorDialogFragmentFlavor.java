package fr.tvbarthel.apps.cameracolorpicker.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import fr.tvbarthel.apps.cameracolorpicker.R;
import fr.tvbarthel.apps.cameracolorpicker.data.ColorItem;
import fr.tvbarthel.apps.cameracolorpicker.data.Palette;

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
    static Dialog createDialog(Context context,
                               final DeleteColorDialogFragment.Callback callback,
                               final ColorItem colorItemToDelete) {
        final View view = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_delete_color, null);
        final int color = colorItemToDelete.getColor();

        view.findViewById(R.id.fragment_dialog_delete_color_preview).setBackgroundColor(color);
        view.findViewById(R.id.fragment_dialog_delete_color_preview_circle)
                .getBackground().mutate().setColorFilter(color, PorterDuff.Mode.SRC);


        final AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setView(view)
                .create();

        view.findViewById(R.id.fragment_dialog_delete_color_btn_validate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onColorDeletionConfirmed(colorItemToDelete);
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.fragment_dialog_delete_color_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }

    private DeleteColorDialogFragmentFlavor() {
        // Non-instantiable
    }
}
