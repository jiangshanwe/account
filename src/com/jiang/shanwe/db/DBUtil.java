package com.jiang.shanwe.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jiang.shanwe.Config;
import com.jiang.shanwe.model.Diary;
import com.jiang.shanwe.model.Record;
import com.jiang.shanwe.model.Tag;
import com.jiang.shanwe.model.User;
import com.jiang.shanwe.util.DateUtil;

public class DBUtil {

    public static final String DB_NAME = "love_account.db";

    public static final String TABLE_NAME_USER = "User";
    public static final String TABLE_NAME_TAG = "Tag";
    public static final String TABLE_NAME_RECORD = "Record";
    public static final String TABLE_NAME_DIARY = "Diary";
    public static final String TABLE_NAME_RECORD_TAG = "Record_Tag";

    public static final int VERSION = 1;

    private static DBUtil dbUtil;
    private SQLiteDatabase db;

    private String dietSelectSql = "SELECT re.id, re.count FROM Record_Tag AS reta "
            + "JOIN Record AS re ON reta.recordId = re.id " + "JOIN Tag AS ta ON ta.id = reta.tagId "
            + "WHERE re.consumeDate BETWEEN ? AND ? AND re.status = 1 AND re.ownerId = ? AND ta.id = ?";

    private DBUtil(Context context) {
        OpenHelper openHelper = new OpenHelper(context, DB_NAME, null, VERSION);
        db = openHelper.getWritableDatabase();
    }

    public synchronized static DBUtil getInstance(Context context) {
        if (dbUtil == null) {
            dbUtil = new DBUtil(context);
        }
        return dbUtil;
    }

    public void saveOrUpdateDiary(Diary diary) {
        long[] dateRange = DateUtil.getDateRange(diary.getDiaryDate());
        Cursor cursor = db.rawQuery(
                "SELECT id FROM Diary WHERE ownerId = ? and status = 1 AND diaryDate BETWEEN ? AND ? ", new String[] {
                        diary.getOwnerId() + "", dateRange[0] + "", dateRange[1] + "" });
        if (cursor.moveToFirst()) {
            int existId = cursor.getInt(cursor.getColumnIndex("id"));
            db.execSQL("UPDATE Diary SET content = ?, updatedTime = ?, syncStatus = 0 WHERE id = ?", new String[] {
                    diary.getContent(), new Date().getTime() + "", existId + "" });
        } else {
            ContentValues values = new ContentValues();
            values.put("ownerId", diary.getOwnerId());
            values.put("content", diary.getContent());
            values.put("diaryDate", diary.getDiaryDate().getTime());
            values.put("createdTime", new Date().getTime());
            values.put("status", Config.DB_VALUE_STATUS_USABLE);
            values.put("syncStatus", Config.DB_VALUE_SYNC_STATUS_NOT);
            db.insert(TABLE_NAME_DIARY, null, values);
        }
        cursor.close();
    }

    public Diary getDiary(Date date, int ownerId) {
        Diary diary = null;
        long[] dateRange = DateUtil.getDateRange(date);
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Diary WHERE diaryDate BETWEEN ? and ? and ownerId = ? and status = 1", new String[] {
                        dateRange[0] + "", dateRange[1] + "", ownerId + "" });
        if (cursor.moveToFirst()) {
            diary = new Diary();
            diary.setId(cursor.getInt(cursor.getColumnIndex("id")));
            diary.setContent(cursor.getString(cursor.getColumnIndex("content")));
        }
        cursor.close();
        return diary;
    }

    public void saveOrUpdateRecord(Record record) {
        long recordId = record.getId();
        if (record != null) {
            // 新建
            if (record.getId() == Config.DEFAULT_EXIST_ID) {
                ContentValues values = new ContentValues();
                values.put("ownerId", record.getOwnerId());
                values.put("count", record.getCount());
                values.put("comments", record.getComments());
                values.put("consumeDate", record.getConsumeDate().getTime());
                values.put("createdTime", new Date().getTime());
                values.put("status", Config.DB_VALUE_STATUS_USABLE);
                values.put("syncStatus", Config.DB_VALUE_SYNC_STATUS_NOT);
                recordId = db.insert(TABLE_NAME_RECORD, null, values);
            } else {
                String updateRecordSql = "update Record set count = ?, comments = ?, updatedTime = ?, status = 1, syncStatus = 0 where id = ? ";
                db.execSQL(
                        updateRecordSql,
                        new String[] { record.getCount() + "", record.getComments(), new Date().getTime() + "",
                                record.getId() + "" });
                db.execSQL("update Record_Tag set status = 0, syncStatus = 0 where recordId = ?",
                        new String[] { record.getId() + "" });

            }
            if (record.getTagIds() != null && record.getTagIds().size() > 0) {
                for (Integer i : record.getTagIds()) {
                    ContentValues values = new ContentValues();
                    values.clear();
                    values.put("recordId", recordId);
                    values.put("tagId", i);
                    values.put("createdTime", new Date().getTime());
                    values.put("status", Config.DB_VALUE_STATUS_USABLE);
                    values.put("syncStatus", Config.DB_VALUE_SYNC_STATUS_NOT);
                    db.insert(TABLE_NAME_RECORD_TAG, null, values);
                }
            }
        }
    }

    public void deleteRecord(long recordId) {
        db.execSQL("update Record set status = 0 where id = ?", new String[] { recordId + "" });
        db.execSQL("update Record_Tag set status = 0 where recordId = ?", new String[] { recordId + "" });
    }

    public void saveOrUpdateDiet(double count, int dietTag, Date consumeDate, int ownerId) {
        String dietUpdateSql = "UPDATE Record SET count = ?, updatedTime = ?, status=1, syncStatus = 0 WHERE id = ?";
        long[] dateRange = DateUtil.getDateRange(consumeDate);
        Cursor cursor = db.rawQuery(dietSelectSql, new String[] { dateRange[0] + "", dateRange[1] + "", ownerId + "",
                dietTag + "" });
        if (cursor.moveToFirst()) {
            int existId = cursor.getInt(cursor.getColumnIndex("id"));
            db.execSQL(dietUpdateSql, new String[] { count + "", new Date().getTime() + "", existId + "" });
        } else {
            ContentValues values = new ContentValues();
            values.put("ownerId", ownerId);
            values.put("count", count);
            values.put("consumeDate", consumeDate.getTime());
            values.put("createdTime", new Date().getTime());
            values.put("status", Config.DB_VALUE_STATUS_USABLE);
            values.put("syncStatus", Config.DB_VALUE_SYNC_STATUS_NOT);
            long dietId = db.insert(TABLE_NAME_RECORD, null, values);

            values.clear();
            values.put("recordId", dietId);
            values.put("tagId", dietTag);
            values.put("createdTime", new Date().getTime());
            values.put("status", Config.DB_VALUE_STATUS_USABLE);
            values.put("syncStatus", Config.DB_VALUE_SYNC_STATUS_NOT);
            db.insert(TABLE_NAME_RECORD_TAG, null, values);
        }
        cursor.close();
    }

    public void saveTag(Tag tag) {
        if (tag != null) {
            ContentValues values = new ContentValues();
            values.put("name", tag.getName());
            values.put("createrId", tag.getCreaterId());
            values.put("createdTime", new Date().getTime());
            values.put("updatedTime", "");
            values.put("icon", tag.getIcon());
            values.put("status", tag.getStatus());
            values.put("syncStatus", 0);
        }
    }

    public void saveUser(User user) {
        if (user != null) {
            ContentValues values = new ContentValues();
            values.put("phoneNum", user.getPhoneNum());
            values.put("name", user.getName());
            values.put("sex", user.getSex());
            values.put("numberPwd", user.getNumberPwd());
            values.put("picturePwd", user.getPicturePwd());
            values.put("email", user.getEmail());
            values.put("registerDate", new Date().getTime());
            values.put("lastloginDate", user.getLastLoginTime().getTime());
            values.put("birthday", user.getBithday().getTime());
            values.put("status", user.getStatus());
            values.put("syncStatus", 0);
            values.put("icon", user.getIcon());
            values.put("stoken", user.getToken());
            values.put("updatedTime", "");
            db.insert(TABLE_NAME_USER, null, values);
        }
    }

    public double[] getDailyDiet(Date date, int ownerId) {
        double[] dailyDiet = { 0, 0, 0 };
        long[] dateRange = DateUtil.getDateRange(date);
        Cursor cursor = db.rawQuery(dietSelectSql, new String[] { dateRange[0] + "", dateRange[1] + "", ownerId + "",
                Config.DB_VALUE_TAG_ID_BREAKFAST + "" });
        if (cursor.moveToFirst()) {
            dailyDiet[0] = cursor.getDouble(cursor.getColumnIndex("count"));
        }
        cursor = db.rawQuery(dietSelectSql, new String[] { dateRange[0] + "", dateRange[1] + "", ownerId + "",
                Config.DB_VALUE_TAG_ID_LUNCH + "" });
        if (cursor.moveToFirst()) {
            dailyDiet[1] = cursor.getDouble(cursor.getColumnIndex("count"));
        }
        cursor = db.rawQuery(dietSelectSql, new String[] { dateRange[0] + "", dateRange[1] + "", ownerId + "",
                Config.DB_VALUE_TAG_ID_DINNER + "" });
        if (cursor.moveToFirst()) {
            dailyDiet[2] = cursor.getDouble(cursor.getColumnIndex("count"));
        }
        cursor.close();
        return dailyDiet;
    }

    public Record getRecord(int recordId) {
        Record record = new Record();
        String selectRecordSql = "select * from Record where id = ? and status = 1";
        Cursor cursor = db.rawQuery(selectRecordSql, new String[] { recordId + "" });
        if (cursor.moveToFirst()) {
            record.setId(recordId);
            record.setId(recordId);
            record.setCount(cursor.getDouble(cursor.getColumnIndex("count")));
            record.setComments(cursor.getString(cursor.getColumnIndex("comments")));
        }
        record.setTagIds(getRecordTagIds(recordId));
        cursor.close();
        return record;
    }

    private List<Integer> getRecordTagIds(int recordId) {
        List<Integer> tagIds = new ArrayList<Integer>();
        String selectTagIdsSql = "select tagId from Record_Tag where recordId = ? and status = 1";
        Cursor cursor = db.rawQuery(selectTagIdsSql, new String[] { recordId + "" });
        if (cursor.moveToFirst()) {
            do {
                tagIds.add(cursor.getInt(cursor.getColumnIndex("tagId")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tagIds;
    }

    public List<Record> getDailyRecord(Date date, int ownerId) {
        List<Record> dayRecordList = new ArrayList<Record>();
        long[] dateRange = DateUtil.getDateRange(date);
        Cursor cursor = db
                .rawQuery(
                        "SELECT re.id AS recordId ,ta.id AS tagId, count, comments, ta.name AS tagName "
                                + "FROM Record_Tag as reta JOIN Record AS re ON reta.recordId = re.id "
                                + "JOIN Tag AS ta ON ta.id = reta.tagId WHERE re.consumeDate "
                                + "BETWEEN ? and ? and re.ownerId = ? and re.status = 1 and reta.status = 1 and ta.id > 3 ORDER BY recordId DESC",
                        new String[] { dateRange[0] + "", dateRange[1] + "", ownerId + "" });

        int tempRecordId = -1;
        boolean isFirstCursor = true;

        Record tempRecord = new Record();
        List<Tag> tempTags = new ArrayList<Tag>();

        if (cursor.moveToFirst()) {
            do {
                int recordId = cursor.getInt(cursor.getColumnIndex("recordId"));

                if (recordId != tempRecordId) {

                    if (recordId != -1 && !isFirstCursor) {
                        tempRecord.setTags(tempTags);
                        dayRecordList.add(tempRecord);
                        tempTags = new ArrayList<Tag>();
                    }

                    tempRecord = new Record();
                    tempRecord.setId(recordId);
                    tempRecord.setCount(cursor.getDouble(cursor.getColumnIndex("count")));
                    tempRecord.setComments(cursor.getString(cursor.getColumnIndex("comments")));
                    tempTags.add(new Tag(cursor.getInt(cursor.getColumnIndex("tagId")), cursor.getString(cursor
                            .getColumnIndex("tagName"))));
                    tempRecordId = recordId;
                } else {
                    tempTags.add(new Tag(cursor.getInt(cursor.getColumnIndex("tagId")), cursor.getString(cursor
                            .getColumnIndex("tagName"))));
                }
                isFirstCursor = false;
            } while (cursor.moveToNext());
            tempRecord.setTags(tempTags);
            dayRecordList.add(tempRecord);
        }
        cursor = db.rawQuery(
                "SELECT * FROM Record WHERE Record.id NOT IN (SELECT recordId FROM Record_Tag) AND consumeDate "
                        + "BETWEEN ? and ? AND ownerId = ? AND status = 1  ORDER BY createdTime DESC", new String[] {
                        dateRange[0] + "", dateRange[1] + "", ownerId + "" });
        if (cursor.moveToFirst()) {
            do {
                Record noTagRecord = new Record();
                noTagRecord.setTags(new ArrayList<Tag>());
                noTagRecord.setId(cursor.getInt(cursor.getColumnIndex("id")));
                noTagRecord.setCount(cursor.getDouble(cursor.getColumnIndex("count")));
                noTagRecord.setComments(cursor.getString(cursor.getColumnIndex("comments")));
                dayRecordList.add(noTagRecord);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dayRecordList;
    }
}