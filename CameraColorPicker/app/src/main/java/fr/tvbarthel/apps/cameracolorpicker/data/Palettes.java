package fr.tvbarthel.apps.cameracolorpicker.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import fr.tvbarthel.apps.cameracolorpicker.views.ColorItemListPage;

/**
 * Static methods for dealing with {@link Palette}s.
 */
public final class Palettes {

    /**
     * A key for saving the {@link Palette}s of the user.
     */
    private static final String KEY_SAVED_COLOR_PALETTES = "ColorPalettes.Keys.KEY_SAVED_COLOR_PALETTES";

    /**
     * A {@link com.google.gson.Gson} for serializing/deserializing objects.
     */
    private static final Gson GSON = new Gson();

    /**
     * A {@link java.lang.reflect.Type} instance of a {@link java.util.List} of {@link Palette}s.
     */
    private static final Type COLOR_PALETTE_LIST_TYPE = new TypeToken<List<Palette>>() {
    }.getType();

    /**
     * A {@link java.util.Comparator} for sorting {@link Palette}s in chronological order of creation.
     */
    public static final Comparator<Palette> CHRONOLOGICAL_COMPARATOR = new Comparator<Palette>() {
        @Override
        public int compare(Palette lhs, Palette rhs) {
            return (int) (rhs.getId() - lhs.getId());
        }
    };

    /**
     * Get the {@link Palette}s of the user.
     *
     * @param context a {@link android.content.Context}.
     * @return a {@link java.util.List} of {@link Palette}s.
     */
    public static List<Palette> getSavedColorPalettes(Context context) {
        return getSavedColorPalettes(getPreferences(context));
    }

    /**
     * Save a {@link Palette}s.
     *
     * @param context     a {@link android.content.Context}.
     * @param paletteToSave the {@link Palette} to save.
     * @return Returns true if the new {@link Palette} was successfully written to persistent storage.
     */
    public static boolean saveColorPalette(Context context, Palette paletteToSave) {
        if (paletteToSave == null) {
            throw new IllegalArgumentException("Can't save a null palette.");
        }

        final List<Palette> savedColorsPalettes = getSavedColorPalettes(context);
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        final List<Palette> palettes = new ArrayList<>(savedColorsPalettes.size() + 1);

        // Add the saved palette except the one with the same ID. It will be overridden.
        final int size = savedColorsPalettes.size();
        for (int i = 0; i < size; i++) {
            final Palette candidate = savedColorsPalettes.get(i);
            if (candidate.getId() != paletteToSave.getId()) {
                palettes.add(candidate);
            }
        }

        // Add the new palette to save.
        palettes.add(paletteToSave);

        editor.putString(KEY_SAVED_COLOR_PALETTES, GSON.toJson(palettes));

        return editor.commit();
    }

    /**
     * Delete a {@link Palette}.
     *
     * @param context           a {@link android.content.Context}.
     * @param paletteToDelete the {@link Palette} to be deleted.
     * @return Returns true if the palette was successfully deleted from persistent storage.
     */
    public static boolean deleteColorPalette(Context context, Palette paletteToDelete) {
        if (paletteToDelete == null) {
            throw new IllegalArgumentException("Can't delete a null color palette");
        }

        final SharedPreferences sharedPreferences = getPreferences(context);
        final List<Palette> savedColorsPalettes = getSavedColorPalettes(sharedPreferences);

        for (Iterator<Palette> it = savedColorsPalettes.iterator(); it.hasNext(); ) {
            final Palette candidate = it.next();
            if (candidate.getId() == paletteToDelete.getId()) {
                it.remove();
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_SAVED_COLOR_PALETTES, GSON.toJson(savedColorsPalettes));
                return editor.commit();
            }
        }

        return false;
    }

    /**
     * Register a {@link OnPaletteChangeListener}.
     * <p/>
     * <b>Caution</b>: No strong references to the listener are currently stored. You must store a strong reference to the listener, or it will be susceptible to garbage collection.
     *
     * @param context                   a {@link android.content.Context}.
     * @param onPaletteChangeListener the {@link OnPaletteChangeListener} to register.
     */
    public static void registerListener(Context context, OnPaletteChangeListener onPaletteChangeListener) {
        getPreferences(context).registerOnSharedPreferenceChangeListener(onPaletteChangeListener);
    }

    /**
     * Unregister a {@link OnPaletteChangeListener}.
     *
     * @param context                   a {@link android.content.Context}.
     * @param onPaletteChangeListener the {@link OnPaletteChangeListener} to unregister.
     */
    public static void unregisterListener(Context context, OnPaletteChangeListener onPaletteChangeListener) {
        getPreferences(context).unregisterOnSharedPreferenceChangeListener(onPaletteChangeListener);
    }

    /**
     * A simple class for listening to the changes of the {@link Palette}s of the user.
     */
    public abstract static class OnPaletteChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (KEY_SAVED_COLOR_PALETTES.equals(key)) {
                onColorPaletteChanged(getSavedColorPalettes(sharedPreferences));
            }
        }

        /**
         * Called when the {@link Palette}s of the user have just changed.
         *
         * @param palettes the current {@link java.util.List} of the {@link Palette}s of the user.
         */
        public abstract void onColorPaletteChanged(List<Palette> palettes);
    }

    /**
     * Get the {@link android.content.SharedPreferences} used for saving/restoring data.
     *
     * @param context a {@link android.content.Context}.
     * @return the {@link android.content.SharedPreferences} used for saving/restoring data.
     */
    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Get the {@link Palette}s of the user.
     *
     * @param sharedPreferences a {@link android.content.SharedPreferences} from which the {@link Palette}s will be retrieved.
     * @return a {@link java.util.List} of {@link Palette}s.
     */
    @SuppressWarnings("unchecked")
    private static List<Palette> getSavedColorPalettes(SharedPreferences sharedPreferences) {
        final String jsonColorPalettes = sharedPreferences.getString(KEY_SAVED_COLOR_PALETTES, "");

        // No saved colors were found.
        // Return an empty list.
        if ("".equals(jsonColorPalettes)) {
            return Collections.EMPTY_LIST;
        }

        // Parse the json into colorItems.
        final List<Palette> palettes = GSON.fromJson(jsonColorPalettes, COLOR_PALETTE_LIST_TYPE);

        // Sort the color items chronologically.
        Collections.sort(palettes, CHRONOLOGICAL_COMPARATOR);
        return palettes;
    }

    // Non instantiability.
    private Palettes() {
    }
}
