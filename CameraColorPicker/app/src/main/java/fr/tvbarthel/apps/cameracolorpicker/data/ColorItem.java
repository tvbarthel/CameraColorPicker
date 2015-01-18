package fr.tvbarthel.apps.cameracolorpicker.data;

/**
 * TODO comment.
 */
public class ColorItem {

    protected final long mId;
    protected int mColor;
    protected long mCreationTime;


    public ColorItem(long id, int color) {
        mId = id;
        mColor = color;
        mCreationTime = System.currentTimeMillis();
    }

    public long getId() {
        return mId;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public long getCreationTime() {
        return mCreationTime;
    }

    public void setCreationTime(long creationTime) {
        mCreationTime = creationTime;
    }
}
