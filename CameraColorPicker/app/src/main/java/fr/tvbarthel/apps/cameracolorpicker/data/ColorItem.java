package fr.tvbarthel.apps.cameracolorpicker.data;

/**
 * A simple data class representing a color.
 */
public class ColorItem {

    /**
     * The id of the color.
     */
    protected final long mId;

    /**
     * An int representing the value of the color.
     */
    protected final int mColor;

    /**
     * A long representing the creation time of the color. (In milliseconds).
     */
    protected final long mCreationTime;

    /**
     * Create a new {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem} with an id and a color.
     *
     * @param id    the id of the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}. Should be unique.
     * @param color the color value.
     */
    public ColorItem(long id, int color) {
        mId = id;
        mColor = color;
        mCreationTime = System.currentTimeMillis();
    }

    /**
     * Get the id of the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     *
     * @return the id of the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     */
    public long getId() {
        return mId;
    }

    /**
     * Get the value of the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     *
     * @return an integer representing the color value of the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     */
    public int getColor() {
        return mColor;
    }


    /**
     * Get the creation time of the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     *
     * @return a long representing the creation time in milliseconds.
     */
    public long getCreationTime() {
        return mCreationTime;
    }

}
