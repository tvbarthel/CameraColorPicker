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

    private static final String KEY_SAVED_COLOR_ITEMS = "Colors.Keys.SAVED_COLOR_COLOR_ITEMS";
    private static final String KEY_LAST_PICKED_COLOR = "Colors.Keys.LAST_PICKED_COLOR";
    private static final int DEFAULT_LAST_PICKED_COLOR = Color.WHITE;
    private static final Gson GSON = new Gson();
    private static final Type COLOR_ITEM_LIST_TYPE = new TypeToken<List<ColorItem>>() {
    }.getType();

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

    public static List<ColorItem> getSavedColorItems(Context context) {
        return getSavedColorItems(getPreferences(context));
    }

    @SuppressWarnings("unchecked")
    public static List<ColorItem> getSavedColorItems(SharedPreferences sharedPreferences) {
        final String jsonColorItems = sharedPreferences.getString(KEY_SAVED_COLOR_ITEMS, "");

        // No saved colors were found.
        // Return an empty list.
        if (jsonColorItems.equals("")) {
            return Collections.EMPTY_LIST;
        }

        // Parse the json into colorItems.
        final List<ColorItem> colorItems = GSON.fromJson(jsonColorItems, COLOR_ITEM_LIST_TYPE);

        // Return a new list of with the color items.
        return new ArrayList<>(colorItems);
    }

    public static boolean saveColorItem(Context context, ColorItem colorToSave) {
        if (colorToSave == null) {
            throw new IllegalArgumentException("Can't save a null color.");
        }

        final List<ColorItem> savedColorsItems = getSavedColorItems(context);
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        final List<ColorItem> colorItems = new ArrayList<>(savedColorsItems.size() + 1);
        colorItems.addAll(savedColorsItems);
        colorItems.add(colorToSave);

        editor.putString(KEY_SAVED_COLOR_ITEMS, GSON.toJson(colorItems));

        return editor.commit();
    }

    public static void registerListener(Context context, OnColorItemChangeListener onColorItemChangeListener) {
        getPreferences(context).registerOnSharedPreferenceChangeListener(onColorItemChangeListener);
    }

    public static void unregisterListener(Context context, OnColorItemChangeListener onColorItemChangeListener) {
        getPreferences(context).unregisterOnSharedPreferenceChangeListener(onColorItemChangeListener);
    }

    public abstract static class OnColorItemChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (KEY_SAVED_COLOR_ITEMS.equals(key)) {
                onColorItemChanged(getSavedColorItems(sharedPreferences));
            }
        }

        public abstract void onColorItemChanged(List<ColorItem> colorItems);
    }

    // Non-instantiability
    private ColorItems() {
    }
}
