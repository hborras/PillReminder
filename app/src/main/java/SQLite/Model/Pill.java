package SQLite.Model;

import android.util.Log;

public class Pill {

    private static final String LOG = "PillModel";

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

    public void setmCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public void LogPill(){
        Log.e(LOG,"Id: "         + this.mId);
        Log.e(LOG,"Name: "       + this.mName);
        Log.e(LOG,"Image: "      + this.mImage);
        Log.e(LOG,"Created At: " + this.mCreatedAt);
    }
}
