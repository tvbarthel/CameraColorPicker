package fr.tvbarthel.apps.cameracolorpicker.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple class representing a palette of colors.
 * <p/>
 * It's a list of {@link ColorItem}s with an id and a name.
 */
public class Palette implements Parcelable {

    /**
     * The id of this {@link Palette}.
     * <p/>
     * Should be unique.
     */
    private final long mId;

    /**
     * The name of this {@link Palette}.
     */
    private String mName;

    /**
     * The {@link ColorItem}s that compose this {@link Palette}.
     */
    private List<ColorItem> mColorItems;

    public Palette(String name) {
        mId = System.currentTimeMillis();
        mName = name;
        mColorItems = new ArrayList<>();
    }

    public Palette(String name, List<ColorItem> colorItems) {
        mId = System.currentTimeMillis();
        mName = name;
        mColorItems = new ArrayList<>(colorItems);
    }

    private Palette(Parcel parcel) {
        mColorItems = new ArrayList<>();

        mId = parcel.readLong();
        mName = parcel.readString();
        parcel.readTypedList(mColorItems, ColorItem.CREATOR);
    }

    /**
     * Add a {@link ColorItem} to this {@link Palette}.
     *
     * @param colorItem the {@link ColorItem} to add.
     */
    public void addColor(ColorItem colorItem) {
        if (colorItem == null) {
            throw new IllegalArgumentException("Color item can't be null.");
        }

        mColorItems.add(colorItem);
    }

    /**
     * Get the id of this {@link Palette}.
     *
     * @return the id of this {@link Palette}.
     */
    public long getId() {
        return mId;
    }

    /**
     * Get the name of this {@link Palette}.
     *
     * @return the name of this {@link Palette}.
     */
    public String getName() {
        return mName;
    }

    /**
     * Set the name of this {@link Palette}.
     *
     * @param name the new name of this {@link Palette}.
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Get the {@link List} of the {@link ColorItem}s that compose this {@link Palette}.
     *
     * @return the {@link List} of the {@link ColorItem}s that compose this {@link Palette}.
     */
    public List<ColorItem> getColors() {
        return new ArrayList<>(mColorItems);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mName);
        dest.writeTypedList(this.mColorItems);
    }

    /**
     * A {@link android.os.Parcelable.Creator} for creating {@link Palette} from {@link android.os.Parcel}.
     */
    public static final Creator<Palette> CREATOR = new Creator<Palette>() {
        public Palette createFromParcel(Parcel source) {
            return new Palette(source);
        }

        public Palette[] newArray(int size) {
            return new Palette[size];
        }
    };
}
