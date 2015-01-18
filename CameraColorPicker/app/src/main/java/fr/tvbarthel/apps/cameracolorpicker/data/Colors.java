package fr.tvbarthel.apps.cameracolorpicker.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Static methods for dealing with colors.
 * <p/>
 * TODO comment.
 */
public final class Colors {

    private static final String COLOR_DELIMITER = ",";
    private static final String KEY_SAVED_COLORS = "Colors.Keys.SAVED_COLORS";
    private static final String KEY_LAST_PICKED_COLOR = "Colors.Keys.LAST_PICKED_COLOR";
    private static final int DEFAULT_LAST_PICKED_COLOR = Color.WHITE;

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getLastPickedColor(Context context) {
        return getPreferences(context).getInt(KEY_LAST_PICKED_COLOR, DEFAULT_LAST_PICKED_COLOR);
    }

    public static boolean saveLastPickedColor(Context context, int lastPickedColor) {
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(KEY_LAST_PICKED_COLOR, lastPickedColor);
        return editor.commit();
    }

    public static List<Integer> getSavedColors(Context context) {
        final String intArray = getPreferences(context).getString(KEY_SAVED_COLORS, "");
        if (intArray.equals("")) {
            return new ArrayList<>(0);
        }

        String[] strInts = intArray.split(COLOR_DELIMITER);
        final List<Integer> ints = new ArrayList<>(strInts.length);
        for (String strInt : strInts) {
            ints.add(Integer.valueOf(strInt));
        }

        return ints;
    }

    public static boolean saveColor(Context context, int colorToSave) {
        final SharedPreferences preferences = getPreferences(context);
        final String intArray = preferences.getString(KEY_SAVED_COLORS, "");
        String intArrayToSave;
        if (intArray.equals("")) {
            intArrayToSave = String.valueOf(colorToSave);
        } else {
            intArrayToSave = intArray + COLOR_DELIMITER + colorToSave;
        }

        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_SAVED_COLORS, intArrayToSave);
        return editor.commit();
    }

    // Non-instantibility
    private Colors() {
    }
}
