package com.jiang.shanwe.db;

import java.util.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.jiang.shanwe.Config;

/**
 *
 * @author Jiang.Shanwe
 * 2015-11-5 下午11:17:44
 *
 */
public class OpenHelper extends SQLiteOpenHelper {

    private static final String CREATE_USER = "create table User("
            + "id integer primary key autoincrement," + "phoneNum text," + "name text,"
            + "sex integer," + "numberPwd text," + "picturePwd text," + "email text,"
            + "registerDate integer," + "lastloginDate integer," + "updatedTime integer,"
            + "birthday integer," + "status integer," + "syncStatus integer,"
            + "icon text," + "stoken text)";
    private static final String CREATE_TAG = "create table Tag("
            + "id integer primary key autoincrement," + "name text,"
            + "createrId integer," + "createdTime integer," + "updatedTime integer,"
            + "icon text," + "status integer," + "syncStatus integer)";
    private static final String CREATE_RECORD = "create table Record("
            + "id integer primary key autoincrement," + "ownerId integer,"
            + "count real," + "comments text," + "consumeDate integer,"
            + "createdTime integer," + "updatedTime integer," + "status integer,"
            + "syncStatus integer)";
    private static final String CREATE_DIARY = "create table Diary("
            + "id integer primary key autoincrement," + "ownerId integer,"
            + "content text," + "diaryDate integer," + "createdTime integer,"
            + "updatedTime integer," + "status integer," + "syncStatus integer)";

    private static final String CREATE_RECORD_TAG = "create table Record_Tag("
            + "id integer primary key autoincrement," + "recordId integer,"
            + "tagId integer," + "createdTime integer," + "updatedTime integer,"
            + "status integer," + "syncStatus integer)";

    private static final String CREATE_WEATHER_CITY = "create table WeatherCity("
            + "id integer primary key autoincrement," + "ownerId integer,"
            + "cityName text," + "createdTime integer," + "updatedTime integer,"
            + "selectCount integer," + "lastSelectTime integer," + "status integer,"
            + "syncStatus integer)";
    private static final String INIT_USER = "insert into user(id,registerDate) values(1, current_timestamp)";
    private static final String INIT_TAG = "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)";
    private static final String INIT_WEATHER_CITY = "insert into WeatherCity(ownerId,cityName,createdTime,updatedTime,selectCount,lastSelectTime,status,syncStatus) "
            + "values(?,?,?,?,?,?,?,?)";

    private static final String DROP_USER = "drop table if exists User";
    private static final String DROP_TAG = "drop table if exists Tag";
    private static final String DROP_RECORD = "drop table if exists Record";
    private static final String DROP_DIARY = "drop table if exists Diary";
    private static final String DROP_RECORD_TAG = "drop table if exists Record_Tag";

    public OpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_TAG);
        db.execSQL(CREATE_RECORD);
        db.execSQL(CREATE_DIARY);
        db.execSQL(CREATE_RECORD_TAG);
        initTags(db);
        initCities(db);
    }

    private void initCities(SQLiteDatabase db) {
        db.execSQL(CREATE_WEATHER_CITY);
        db.execSQL(INIT_WEATHER_CITY, new String[] {
                Config.DB_VALUE_SYSTEM_CREATERID + "", "杭州", new Date().getTime() + "",
                null, 1 + "", new Date().getTime() + "",
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_WEATHER_CITY, new String[] {
                Config.DB_VALUE_SYSTEM_CREATERID + "", "武汉", new Date().getTime() + "",
                null, 0 + "", null, Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_WEATHER_CITY, new String[] {
                Config.DB_VALUE_SYSTEM_CREATERID + "", "南通", new Date().getTime() + "",
                null, 0 + "", null, Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_WEATHER_CITY, new String[] {
                Config.DB_VALUE_SYSTEM_CREATERID + "", "重庆", new Date().getTime() + "",
                null, 0 + "", null, Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_WEATHER_CITY, new String[] {
                Config.DB_VALUE_SYSTEM_CREATERID + "", "安庆", new Date().getTime() + "",
                null, 0 + "", null, Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
    }

    private void initTags(SQLiteDatabase db) {
        db.execSQL(INIT_TAG, new String[] { "早餐", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "中餐", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "晚餐", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "零食", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "交通", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "娱乐", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "聚会", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "通讯", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "住房", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "文教", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "日化", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "衣服", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "数码", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "水电煤", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_TAG, new String[] { "其它", Config.DB_VALUE_SYSTEM_CREATERID + "",
                new Date().getTime() + "", null, null,
                Config.DB_VALUE_STATUS_USABLE + "",
                Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(INIT_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
        case 2:

        case 3:

        default:
            break;
        }
    }

}
