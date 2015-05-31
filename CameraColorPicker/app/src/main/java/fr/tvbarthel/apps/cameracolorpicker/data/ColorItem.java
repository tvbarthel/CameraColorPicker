package fr.tvbarthel.apps.cameracolorpicker.data;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple data class representing a color.
 */
public class ColorItem implements Parcelable {

    /**
     * The id of the color.
     */
    protected final long mId;

    /**
     * An int representing the value of the color.
     */
    protected int mColor;

    /**
     * An optional name that the user can give to a color.
     */
    protected String mName;

    /**
     * A long representing the creation time of the color. (In milliseconds).
     */
    protected final long mCreationTime;

    /**
     * A human readable string representation of the hexadecimal value of the color.
     */
    protected transient String mHexString;

    /**
     * A human readable string representation of the RGB value of the color.
     */
    protected transient String mRgbString;

    /**
     * A human readable string representation of the HSV value of the color.
     */
    protected transient String mHsvString;

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
     * Create a new {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem} from a {@link android.os.Parcel}.
     * <p/>
     * Used by the parcelable creator.
     * {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem#CREATOR}.
     *
     * @param in the {@link android.os.Parcel}.
     */
    private ColorItem(Parcel in) {
        this.mId = in.readLong();
        this.mColor = in.readInt();
        this.mCreationTime = in.readLong();
        this.mName = in.readString();
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
            mRgbString = makeRgbString(mColor);
            mHsvString = makeHsvString(mColor);
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
     * Get a human readable representation of the RGB value of the color.
     *
     * @return a human readable representation of the RGB value.
     */
    public String getRgbString() {
        if (mRgbString == null) {
            mRgbString = makeRgbString(mColor);
        }
        return mRgbString;
    }

    /**
     * Get a human readable representation of the HSV value of the color.
     *
     * @return a human readable representation of the HSV value.
     */
    public String getHsvString() {
        if (mHsvString == null) {
            mHsvString = makeHsvString(mColor);
        }
        return mHsvString;
    }

    /**
     * Get the name of the color.
     *
     * @return the name of the color.
     */
    public String getName() {
        return mName;
    }

    /**
     * Set the name of the color.
     *
     * @param name the new name of the color.
     */
    public void setName(String name) {
        mName = name;
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

    /**
     * Make a human readable representation of the RGB value of a color.
     *
     * @param value the integer representation the color.
     * @return a human readable representation of the RGB value.
     */
    public static String makeRgbString(int value) {
        return "rgb(" + Color.red(value) + ", " + Color.green(value) + ", " + Color.blue(value) + ")";
    }

    /**
     * Make a human readable representation of the HSV value of a color.
     *
     * @param value the integer representation the color.
     * @return a human readable representation of the HSV value.
     */
    public static String makeHsvString(int value) {
        float[] hsv = new float[3];
        Color.colorToHSV(value, hsv);
        return "hsv(" + (int) hsv[0] + "°, " + (int) (hsv[1] * 100) + "%, " + (int) (hsv[2] * 100) + "%)";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeInt(this.mColor);
        dest.writeLong(this.mCreationTime);
        dest.writeString(this.mName);
    }

    /**
     * A {@link android.os.Parcelable.Creator} for creating {@link fr.tvbarthel.apps.cameracolorpicker.data.ColorItem} from {@link android.os.Parcel}.
     */
    public static final Creator<ColorItem> CREATOR = new Creator<ColorItem>() {
        public ColorItem createFromParcel(Parcel source) {
            return new ColorItem(source);
        }

        public ColorItem[] newArray(int size) {
            return new ColorItem[size];
        }
    };
}
