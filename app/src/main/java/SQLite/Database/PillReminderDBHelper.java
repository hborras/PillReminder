package SQLite.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import SQLite.Model.Day;
import SQLite.Model.Meal;
import SQLite.Model.Pill;
import SQLite.Model.PillReminder;

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
    private static final String KEY_IMAGE =                         "image";

    // PILL_REMINDER Table - column names
    private static final String KEY_REMINDER_ID =                   "reminder_id";
    private static final String KEY_PILL_REMINDER_PILL_ID =         "pill_id";
    private static final String KEY_DATE_START =                    "date_start";
    private static final String KEY_DATE_FINISH =                   "date_finish";
    private static final String KEY_PILL_REMINDER_DAY_ID =          "day_id";
    private static final String KEY_PILL_REMINDER_MEAL_ID =         "meal_id";
    private static final String KEY_PILL_REMINDER_EVERY_HOURS =     "every_hours";
    private static final String KEY_PILL_REMINDER_STATUS =          "status";

    // Table Create Statements
    // Pill table create statement
    private static final String CREATE_TABLE_PILL = "CREATE TABLE "
            + TABLE_PILL + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME   + " TEXT,"
            + KEY_IMAGE + " INTEGER,"
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
            + KEY_REMINDER_ID + " INTEGER,"
            + KEY_PILL_REMINDER_PILL_ID   + " INTEGER,"
            + KEY_PILL_REMINDER_DAY_ID + " INTEGER,"
            + KEY_PILL_REMINDER_MEAL_ID + " INTEGER,"
            + KEY_PILL_REMINDER_EVERY_HOURS + " INTEGER,"
            + KEY_DATE_START + " DATETIME,"
            + KEY_DATE_FINISH + " DATETIME,"
            + KEY_CREATED_AT + " DATETIME,"
            + KEY_PILL_REMINDER_STATUS + " INTEGER)";

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
        values.put(KEY_IMAGE, pill.getmImage());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long pill_id = db.insert(TABLE_PILL, null, values);

        return pill_id;
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
        Pill pill = new Pill();
        if (c != null) {
            c.moveToFirst();
            pill.setmId(c.getInt(c.getColumnIndex(KEY_ID)));
            pill.setmName((c.getString(c.getColumnIndex(KEY_NAME))));
            pill.setmImage((c.getInt(c.getColumnIndex(KEY_IMAGE))));
            pill.setmCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        }
        return pill;
    }

    /*
     * getting all Pills
     */
    public List<Pill> getAllPills(){
        List<Pill> pills = new ArrayList<Pill>();
        String selectQuery = "SELECT  * FROM " + TABLE_PILL;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Pill pill = new Pill();
                pill.setmId(c.getInt((c.getColumnIndex(KEY_ID))));
                pill.setmName((c.getString(c.getColumnIndex(KEY_NAME))));
                pill.setmImage((c.getInt(c.getColumnIndex(KEY_IMAGE))));
                pill.setmCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to pill list
                pills.add(pill);
            } while (c.moveToNext());
        }

        return pills;
    }

    /*
     * Updating a pill
     */
    public int updatePill(Pill pill) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, pill.getmName());
        values.put(KEY_IMAGE, pill.getmImage());

        // updating row
        return db.update(TABLE_PILL, values, KEY_ID + " = ?",
                new String[] { String.valueOf(pill.getmId()) });
    }

    /*
     * Deleting a pill
     */
    public void deletePill(long pill_id, boolean should_delete_all_pill_reminders) {
        SQLiteDatabase db = this.getWritableDatabase();

        // before deleting pill
        // check if pill_reminders under this pills should also be deleted
        if (should_delete_all_pill_reminders) {
            // get all pill_reminders under this tag
            List<PillReminder> allPillPillReminders = getAllPillRemindersByPill(pill_id,PillReminder.STATE_NO_STATUS);

            // delete all pill_reminders
            for (PillReminder pillReminder : allPillPillReminders) {
                // delete PillReminder
                deletePillReminder(pillReminder.getmId());
            }
        }

        db.delete(TABLE_PILL, KEY_ID + " = ?",
                new String[] { String.valueOf(pill_id) });
    }

    /*
     * Creating a Day
     */
    public long createDay(Day day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, day.getmName());

        // insert row
        long day_id = db.insert(TABLE_DAY, null, values);

        return day_id;
    }

    /*
     * get single day
     */
    public Day getDay(long day_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DAY + " WHERE "
                + KEY_ID + " = " + day_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Day day = new Day();
        if (c != null) {
            c.moveToFirst();
            day.setmId(c.getInt(c.getColumnIndex(KEY_ID)));
            day.setmName((c.getString(c.getColumnIndex(KEY_NAME))));
        }
        return day;
    }

    /*
     * getting all days
     */
    public List<Day> getAllDays(){
        List<Day> days = new ArrayList<Day>();
        String selectQuery = "SELECT  * FROM " + TABLE_DAY;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Day day = new Day();
                day.setmId(c.getInt((c.getColumnIndex(KEY_ID))));
                day.setmName((c.getString(c.getColumnIndex(KEY_NAME))));

                // adding to day list
                days.add(day);
            } while (c.moveToNext());
        }

        return days;
    }

    /*
     * Updating a day
     */
    public int updateDay(Day day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, day.getmName());

        // updating row
        return db.update(TABLE_DAY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(day.getmId()) });
    }

    /*
     * Deleting a day
     */
    public void deleteDay(long day_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DAY, KEY_ID + " = ?",
                new String[] { String.valueOf(day_id) });
    }

    /*
     * Creating a meal
     */
    public long createMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, meal.getmName());

        // insert row
        long meal_id = db.insert(TABLE_MEAL, null, values);

        return meal_id;
    }

    /*
     * get single meal
     */
    public Meal getMeal(long meal_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_MEAL + " WHERE "
                + KEY_ID + " = " + meal_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        Meal meal = new Meal();
        if (c != null) {
            c.moveToFirst();
            meal.setmId(c.getInt(c.getColumnIndex(KEY_ID)));
            meal.setmName((c.getString(c.getColumnIndex(KEY_NAME))));
        }
        return meal;
    }

    /*
     * getting all meals
     */
    public List<Meal> getAllMeals(){
        List<Meal> meals = new ArrayList<Meal>();
        String selectQuery = "SELECT  * FROM " + TABLE_MEAL;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Meal meal = new Meal();
                meal.setmId(c.getInt((c.getColumnIndex(KEY_ID))));
                meal.setmName((c.getString(c.getColumnIndex(KEY_NAME))));

                // adding to meal list
                meals.add(meal);
            } while (c.moveToNext());
        }

        return meals;
    }

    /*
     * Updating a Meal
     */
    public int updateMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, meal.getmName());

        // updating row
        return db.update(TABLE_MEAL, values, KEY_ID + " = ?",
                new String[] { String.valueOf(meal.getmId()) });
    }

    /*
     * Deleting a meal
     */
    public void deleteMeal(long meal_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEAL, KEY_ID + " = ?",
                new String[] { String.valueOf(meal_id) });
    }

    /*
     * Creating a PillReminder
     */
    public long createPillReminder(PillReminder pillReminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_REMINDER_ID, pillReminder.getmReminderId());
        values.put(KEY_PILL_REMINDER_PILL_ID, pillReminder.getmPillId());
        values.put(KEY_PILL_REMINDER_DAY_ID, pillReminder.getmDayId());
        values.put(KEY_PILL_REMINDER_MEAL_ID, pillReminder.getmMealId());
        values.put(KEY_PILL_REMINDER_EVERY_HOURS, pillReminder.getmEveryHours());
        values.put(KEY_DATE_START,pillReminder.getmDateStart());
        values.put(KEY_DATE_FINISH,pillReminder.getmDateFinish());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_PILL_REMINDER_STATUS,pillReminder.getmStatus());

        // insert row
        long pill_reminder_id = db.insert(TABLE_PILL_REMINDER, null, values);

        return pill_reminder_id;
    }

    /*
     * Updating a PillReminder
     */

    public int updatePillReminder(PillReminder pillReminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_REMINDER_ID, pillReminder.getmReminderId());
        values.put(KEY_PILL_REMINDER_PILL_ID, pillReminder.getmPillId());
        values.put(KEY_PILL_REMINDER_DAY_ID, pillReminder.getmDayId());
        values.put(KEY_PILL_REMINDER_MEAL_ID, pillReminder.getmMealId());
        values.put(KEY_PILL_REMINDER_EVERY_HOURS, pillReminder.getmEveryHours());
        values.put(KEY_DATE_START,pillReminder.getmDateStart());
        values.put(KEY_DATE_FINISH,pillReminder.getmDateFinish());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_PILL_REMINDER_STATUS,pillReminder.getmStatus());

        // updating row
        return db.update(TABLE_PILL_REMINDER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(pillReminder.getmId()) });
    }

    public List<PillReminder> getAllPillRemindersByPill(long pill_id,long status_id){
        List<PillReminder> pillReminders = new ArrayList<PillReminder>();
        String selectQuery = "SELECT  * FROM " + TABLE_PILL_REMINDER
                            + " WHERE " + KEY_PILL_REMINDER_PILL_ID + "=" + pill_id;
        if (status_id != PillReminder.STATE_NO_STATUS){
            selectQuery = selectQuery + " AND " + KEY_PILL_REMINDER_STATUS + "="
                                        + status_id;
        }
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PillReminder pillReminder = new PillReminder();
                pillReminder.setmReminderId(c.getInt(c.getColumnIndex(KEY_REMINDER_ID)));
                pillReminder.setmId(c.getInt((c.getColumnIndex(KEY_ID))));
                pillReminder.setmPillId(c.getInt(c.getColumnIndex(KEY_PILL_REMINDER_DAY_ID)));
                pillReminder.setmDayId(c.getInt(c.getColumnIndex(KEY_PILL_REMINDER_DAY_ID)));
                pillReminder.setmMealId(c.getInt(c.getColumnIndex(KEY_PILL_REMINDER_MEAL_ID)));
                pillReminder.setmStatus(c.getInt(c.getColumnIndex(KEY_PILL_REMINDER_STATUS)));
                pillReminder.setmEveryHours(c.getInt(c.getColumnIndex(KEY_PILL_REMINDER_EVERY_HOURS)));
                pillReminder.setmCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                pillReminder.setmDateStart(c.getString(c.getColumnIndex(KEY_DATE_START)));
                pillReminder.setmDateFinish(c.getString(c.getColumnIndex(KEY_DATE_FINISH)));

                // adding to pillReminders list
                pillReminders.add(pillReminder);
            } while (c.moveToNext());
        }

        return pillReminders;
    }

    /*
     * Delete a PillReminder
     */

    public void deletePillReminder(long pillReminder_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PILL_REMINDER, KEY_ID + " = ?",
                new String[] { String.valueOf(pillReminder_id) });
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /*
     * get datetime
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}

