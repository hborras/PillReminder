package SQLite.Model;

/**
 * Created by plagueis on 7/05/14.
 */
public class Day {

    private int mId;
    private String mName;

    public Day() {
    }

    public Day(int mId, String mName) {
        this.mId = mId;
        this.mName = mName;
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
}
