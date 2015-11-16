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

    public static final String CREATE_USER = "create table User(" + "id integer primary key autoincrement,"
            + "phoneNum text," + "name text," + "sex integer," + "numberPwd text," + "picturePwd text," + "email text,"
            + "registerDate integer," + "lastloginDate integer," + "updatedTime integer," + "birthday integer,"
            + "status integer," + "syncStatus integer," + "icon text," + "stoken text)";
    public static final String CREATE_TAG = "create table Tag(" + "id integer primary key autoincrement,"
            + "name text," + "createrId integer," + "createdTime integer," + "updatedTime integer," + "icon text,"
            + "status integer," + "syncStatus integer)";
    public static final String CREATE_RECORD = "create table Record(" + "id integer primary key autoincrement,"
            + "ownerId integer," + "count real," + "comments text," + "consumeDate integer," + "createdTime integer,"
            + "updatedTime integer," + "status integer," + "syncStatus integer)";
    public static final String CREATE_DIARY = "create table Diary(" + "id integer primary key autoincrement,"
            + "ownerId integer," + "content text," + "diaryDate integer," + "createdTime integer,"
            + "updatedTime integer," + "status integer," + "syncStatus integer)";

    public static final String CREATE_RECORD_TAG = "create table Record_Tag(" + "id integer primary key autoincrement,"
            + "recordId integer," + "tagId integer," + "createdTime integer," + "updatedTime integer,"
            + "status integer," + "syncStatus integer)";

    public static final String DROP_USER = "drop table if exists User";
    public static final String DROP_TAG = "drop table if exists Tag";
    public static final String DROP_RECORD = "drop table if exists Record";
    public static final String DROP_DIARY = "drop table if exists Diary";
    public static final String DROP_RECORD_TAG = "drop table if exists Record_Tag";

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
    }

    private void initTags(SQLiteDatabase db) {
        db.execSQL(
                "insert into Tag (id,name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?,?)",
                new String[] { Config.DB_VALUE_TAG_ID_BREAKFAST + "", "早餐", Config.DB_VALUE_SYSTEM_CREATERID + "",
                        new Date().getTime() + "", null, null, Config.DB_VALUE_STATUS_USABLE + "",
                        Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (id,name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?,?)",
                new String[] { Config.DB_VALUE_TAG_ID_LUNCH + "", "中餐", Config.DB_VALUE_SYSTEM_CREATERID + "",
                        new Date().getTime() + "", null, null, Config.DB_VALUE_STATUS_USABLE + "",
                        Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (id,name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?,?)",
                new String[] { Config.DB_VALUE_TAG_ID_DINNER + "", "晚餐", Config.DB_VALUE_SYSTEM_CREATERID + "",
                        new Date().getTime() + "", null, null, Config.DB_VALUE_STATUS_USABLE + "",
                        Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "零食", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "交通", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "娱乐", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "聚会", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "通讯", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "住房", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "文教", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "日化", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "衣服", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "数码", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "水电煤", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
        db.execSQL(
                "insert into Tag (name,createrId,createdTime,updatedTime,icon,status,syncStatus) values(?,?,?,?,?,?,?)",
                new String[] { "其它", Config.DB_VALUE_SYSTEM_CREATERID + "", new Date().getTime() + "", null, null,
                        Config.DB_VALUE_STATUS_USABLE + "", Config.DB_VALUE_SYNC_STATUS_ALREADY + "" });
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER);
        db.execSQL(DROP_TAG);
        db.execSQL(DROP_RECORD);
        db.execSQL(DROP_DIARY);
        db.execSQL(DROP_RECORD_TAG);
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_TAG);
        db.execSQL(CREATE_RECORD);
        db.execSQL(CREATE_DIARY);
        initTags(db);
    }

}
