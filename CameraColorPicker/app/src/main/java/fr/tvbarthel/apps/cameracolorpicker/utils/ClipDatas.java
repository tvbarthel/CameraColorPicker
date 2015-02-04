package fr.tvbarthel.apps.cameracolorpicker.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Static methods for dealing with {@link android.content.ClipData}.
 */
public final class ClipDatas {

    /**
     * Sets a text as the current primary clip on the clipboard.
     * <p/>
     * Internally gets the {@link android.content.ClipboardManager} and set a plain text {@link android.content.ClipData} as primary clip.
     *
     * @param context a {@link android.content.Context} for retrieving the {@link android.content.ClipboardManager}.
     * @param label   User-visible label for the clip data.
     * @param text    The actual text in the clip.
     */
    public static void clipPainText(Context context, String label, CharSequence text) {
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    // Non instantiability.
    private ClipDatas() {
    }
}
