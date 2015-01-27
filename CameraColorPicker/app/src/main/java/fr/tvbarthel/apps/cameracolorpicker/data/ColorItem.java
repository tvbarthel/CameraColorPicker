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
    protected int mColor;

    /**
     * A long representing the creation time of the color. (In milliseconds).
     */
    protected final long mCreationTime;

    /**
     * A human readable string representation of the hexadecimal value of the color.
     */
    protected transient String mHexString;

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
     * Create a new {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem} with a color.
     * <p/>
     * The {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem#mId} is set to the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem#mCreationTime}.
     *
     * @param color the color value.
     */
    public ColorItem(int color) {
        mId = mCreationTime = System.currentTimeMillis();
        mColor = color;
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
     * Set the color valur of the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     *
     * @param color an integer representing the new color value of the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     */
    public void setColor(int color) {
        if (mColor != color) {
            mColor = color;
            mHexString = makeHexString(mColor);
        }
    }

    /**
     * Get the creation time of the {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem}.
     *
     * @return a long representing the creation time in milliseconds.
     */
    public long getCreationTime() {
        return mCreationTime;
    }

    /**
     * Get a human readable representation of the hexadecimal value of the color.
     *
     * @return a human readable representation of the hexadecimal value.
     */
    public String getHexString() {
        if (mHexString == null) {
            mHexString = makeHexString(mColor);
        }
        return mHexString;
    }

    /**
     * Make a human readable representation of the hexadecimal value of a color.
     *
     * @param value the integer representation the color.
     * @return a human readable representation of the hexadecimal value.
     */
    public static String makeHexString(int value) {
        return "#" + Integer.toHexString(value).substring(2);
    }

}
