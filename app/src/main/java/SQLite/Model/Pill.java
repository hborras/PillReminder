package SQLite.Model;

/**
 * Created by plagueis on 7/05/14.
 */
public class Pill {

    private int mId;
    private String mName;
    private String mColor;
    private String mCreatedAt;

    public Pill() {
    }

    public Pill(int mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }

    public Pill(int mId, String mName, String mColor) {
        this.mId = mId;
        this.mName = mName;
        this.mColor = mColor;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public String getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }
}
