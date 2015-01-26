package fr.tvbarthel.apps.cameracolorpicker.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Static methods for dealing with {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}s.
 * <p/>
 * TODO comment.
 */
public final class ColorItems {

    private static final String COLOR_DELIMITER = ",";
    private static final String KEY_SAVED_COLORS = "Colors.Keys.SAVED_COLORS";
    private static final String KEY_LAST_PICKED_COLOR = "Colors.Keys.LAST_PICKED_COLOR";
    private static final int DEFAULT_LAST_PICKED_COLOR = Color.WHITE;
    private static final ColorItem DEFAULT_LAST_PICKED_COLOR_ITEM = new ColorItem(1, Color.WHITE);
    private static final Gson GSON = new Gson();
    private static final Type COLOR_ITEM_LIST_TYPE = new TypeToken<List<ColorItem>>() {
    }.getType();

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static ColorItem getLastPickedColorItem(Context context) {
        final String colorItemString = getPreferences(context).getString(KEY_LAST_PICKED_COLOR, null);

        if (colorItemString == null) {
            return DEFAULT_LAST_PICKED_COLOR_ITEM;
        }

        return GSON.fromJson(colorItemString, ColorItem.class);
    }

    public static boolean saveLastPickedColorItem(Context context, ColorItem lastPickedColorItem) {
        final String colorItemString = GSON.toJson(lastPickedColorItem);
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(KEY_LAST_PICKED_COLOR, colorItemString);
        return editor.commit();
    }

    @SuppressWarnings("unchecked")
    public static List<ColorItem> getSavedColors(Context context) {
        final String jsonColorItems = getPreferences(context).getString(KEY_SAVED_COLORS, "");

        // No saved colors were found.
        // Return an empty list.
        if (jsonColorItems.equals("")) {
            return Collections.EMPTY_LIST;
        }

        // Parse the json into colorItems.
        final List<ColorItem> colorItems = GSON.fromJson(jsonColorItems, COLOR_ITEM_LIST_TYPE);

        // Return an unmodifiable list of the color items.
        return Collections.unmodifiableList(colorItems);
    }

    public static boolean saveColor(Context context, ColorItem colorToSave) {
        if (colorToSave == null) {
            throw new IllegalArgumentException("Can't save a null color.");
        }

        final List<ColorItem> savedColorsItems = getSavedColors(context);
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        final List<ColorItem> colorItems = new ArrayList<>(savedColorsItems.size() + 1);
        colorItems.addAll(savedColorsItems);
        colorItems.add(colorToSave);

        editor.putString(KEY_SAVED_COLORS, GSON.toJson(colorItems));

        return editor.commit();
    }

    // Non-instantibility
    private ColorItems() {
    }
}
