package SQLite.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import SQLite.Model.Pill;

public class PillReminderDBHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG =                               "PillReminderDBHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME =                     "pillreminder.db";

    // Table Names
    private static final String TABLE_PILL =                        "pill";
    private static final String TABLE_PILL_REMINDER =                "pillreminder";
    private static final String TABLE_DAY =                         "day";
    private static final String TABLE_MEAL =                        "meal";

    // Common column names
    private static final String KEY_ID =                            "id";
    private static final String KEY_CREATED_AT =                    "created_at";

    // PILLS Table - column names
    private static final String KEY_NAME =                          "name";
    private static final String KEY_COLOR =                         "color";

    // PILL_REMINDER Table - column names
    private static final String KEY_PILL_REMINDER_ID =              "pill_id";
    private static final String KEY_DATE_START =                    "date_start";
    private static final String KEY_DATE_FINISH =                   "date_finish";
    private static final String KEY_PILL_REMINDER_DAY_ID =          "day_id";
    private static final String KEY_PILL_REMINDER_MEAL_ID =         "meal_id";
    private static final String KEY_PILL_REMINDER_EVERY_HOURS =     "every_hours";

    // Table Create Statements
    // Pill table create statement
    private static final String CREATE_TABLE_PILL = "CREATE TABLE "
            + TABLE_PILL + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME   + " TEXT,"
            + KEY_COLOR + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // Day table create statement
    private static final String CREATE_TABLE_DAY = "CREATE TABLE "
            + TABLE_DAY + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME   + " TEXT)";

    // Meal table create statement
    private static final String CREATE_TABLE_MEAL = "CREATE TABLE "
            + TABLE_MEAL + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME   + " TEXT)";

    private static final String CREATE_TABLE_PILL_REMINDER = "CREATE TABLE "
            + TABLE_PILL_REMINDER + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PILL_REMINDER_ID   + " INTEGER,"
            + KEY_PILL_REMINDER_DAY_ID + " INTEGER,"
            + KEY_PILL_REMINDER_MEAL_ID + " INTEGER,"
            + KEY_PILL_REMINDER_EVERY_HOURS + " INTEGER,"
            + KEY_DATE_START + " DATETIME,"
            + KEY_DATE_FINISH + " DATETIME,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    public PillReminderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_PILL);
        db.execSQL(CREATE_TABLE_DAY);
        db.execSQL(CREATE_TABLE_MEAL);
        db.execSQL(CREATE_TABLE_PILL_REMINDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PILL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PILL_REMINDER);

        // create new tables
        onCreate(db);
    }

    /*
     * Creating a Pill
     */
    public long createPill(Pill pill) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pill.getmName());
        values.put(KEY_COLOR, pill.getmColor());
        values.put(KEY_CREATED_AT, getDateTime());

        return pill.getmId();
    }

    /*
     * get single Pill
     */
    public Pill getPill(long pill_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PILL + " WHERE "
                + KEY_ID + " = " + pill_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Pill pill = new Pill();
        pill.setmId(c.getInt(c.getColumnIndex(KEY_ID)));
        pill.setmName((c.getString(c.getColumnIndex(KEY_NAME))));
        pill.setmColor((c.getString(c.getColumnIndex(KEY_COLOR))));
        pill.setmCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

        return pill;
    }
}

