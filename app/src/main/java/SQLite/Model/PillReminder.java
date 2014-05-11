package SQLite.Model;

/**
 * Created by plagueis on 7/05/14.
 */
public class PillReminder {

    public final static int STATE_NO_STATUS  = 0;
    public final static int STATE_ACTIVE  = 1;
    public final static int STATE_ARCHIVE = 2;
    public final static int STATE_DELETED = 3;

    private int mId;
    private int mPillId;
    private int mMealId;
    private int mDayId;
    private int mEveryHours;
    private String mDateStart;
    private String mDateFinish;
    private String mCreatedAt;
    private int mReminderId;

    public int getmReminderId() {
        return mReminderId;
    }

    public void setmReminderId(int mReminderId) {
        this.mReminderId = mReminderId;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    private int mStatus;

    public PillReminder() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmPillId() {
        return mPillId;
    }

    public void setmPillId(int mPillId) {
        this.mPillId = mPillId;
    }

    public int getmMealId() {
        return mMealId;
    }

    public void setmMealId(int mMealId) {
        this.mMealId = mMealId;
    }

    public int getmDayId() {
        return mDayId;
    }

    public void setmDayId(int mDayId) {
        this.mDayId = mDayId;
    }

    public int getmEveryHours() {
        return mEveryHours;
    }

    public void setmEveryHours(int mEveryHours) {
        this.mEveryHours = mEveryHours;
    }

    public String getmDateStart() {
        return mDateStart;
    }

    public void setmDateStart(String mDateStart) {
        this.mDateStart = mDateStart;
    }

    public String getmDateFinish() {
        return mDateFinish;
    }

    public void setmDateFinish(String mDateFinish) {
        this.mDateFinish = mDateFinish;
    }

    public String getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public PillReminder(int mId,int mReminderId, int mPillId, int mEveryHours, String mDateStart, String mDateFinish) {

        this.mId = mId;
        this.mPillId = mPillId;
        this.mEveryHours = mEveryHours;
        this.mDateStart = mDateStart;
        this.mDateFinish = mDateFinish;
        this.mReminderId = mReminderId;
    }

    public PillReminder(int mId,int mReminderId, int mPillId, int mMealId, int mDayId, String mDateStart, String mDateFinish) {

        this.mId = mId;
        this.mPillId = mPillId;
        this.mMealId = mMealId;
        this.mDayId = mDayId;
        this.mDateStart = mDateStart;
        this.mDateFinish = mDateFinish;
        this.mReminderId = mReminderId;
    }
}
