package SQLite.Model;

/**
 * Created by plagueis on 7/05/14.
 */
public class Pill {

    private int mId;
    private String mName;
    private int mImage;
    private String mCreatedAt;

    public Pill() {
    }

    public Pill( String mName, int mImage) {
        this.mName = mName;
        this.mImage = mImage;

    }

    public Pill(int mId, String mName, int mImage) {
        this.mId = mId;
        this.mName = mName;
        this.mImage = mImage;
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

    public int getmImage() {
        return mImage;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }

    public String getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }
}
