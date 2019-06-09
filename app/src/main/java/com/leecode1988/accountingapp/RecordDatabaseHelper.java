package com.leecode1988.accountingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.LinkedList;

/**
 * author:LeeCode
 * create:2019/2/9 21:10
 */
public class RecordDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "RecordDatabaseHelper";
    private Context mContext;

    public static final String DB_NAME = "Record";
    private static final String CREATE_RECORD_DB = "create table Record("
            + "id integer primary key autoincrement,"
            + "uuid text,"
            + "type integer,"
            + "category text,"
            + "amount real,"
            + "remark text,"
            + "time integer,"
            + "date date)";

    public RecordDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
        Log.d(TAG, "DatabaseHelp init!");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORD_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addRecord(RecordBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uuid", bean.getUuid());
        values.put("type", bean.getType());
        values.put("category", bean.getCategory());
        values.put("remark", bean.getRemark());
        values.put("amount", bean.getAmount());
        values.put("date", bean.getDate());
        values.put("time", bean.getTimeStamp());
        db.insert(DB_NAME, null, values);
        values.clear();
        Log.d(TAG, bean.getUuid() + "---->added");
    }

    public void removeRecord(String uuid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_NAME, "uuid = ?", new String[]{uuid});
    }

    public void editRecord(String uuid, RecordBean bean) {
        removeRecord(uuid);
        bean.setUuid(uuid);
        addRecord(bean);
    }
//    public void updateRecord(String uuid,Object type,Object itemValue){
//        SQLiteDatabase db=this.getWritableDatabase();
//        ContentValues values=new ContentValues();
//        values.put((String)type,  itemValue);
//        db.update(DB_NAME,values,"uuid=?",new String[]{uuid});
//    }

    /**
     * 查询特定日期的Records
     *
     * @param dateStr
     * @return LinkedList<RecordBean>
     */
    public LinkedList<RecordBean> queryRecords(String dateStr) {
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where date = ? order by time asc", new String[]{dateStr});
//        Cursor cursor1=db.query(DB_NAME,null,"where date = ?",new String[]{dateStr},null,null,"order by time");
        if (cursor.moveToFirst()) {
            do {
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                long timeStamp = cursor.getLong(cursor.getColumnIndex("time"));
                RecordBean record = new RecordBean();
                record.setUuid(uuid);
                record.setType(type);
                record.setCategory(category);
                record.setAmount(amount);
                record.setRemark(remark);
                record.setDate(date);
                record.setTimeStamp(timeStamp);

                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    /**
     * 查询时间段的账单
     *
     * @param dateStrFirst,dataStrLast
     * @return LinkedList<RecordBean>
     */
    public LinkedList<RecordBean> queryRecordsByKey(String dateStrFirst, String dataStrLast) {
        LinkedList<RecordBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record where date>=? AND date<=? order by date asc", new String[]{dateStrFirst, dataStrLast});
        if (cursor.moveToFirst()) {

            do {
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                long timeStamp = cursor.getLong(cursor.getColumnIndex("time"));
                RecordBean record = new RecordBean();
                record.setUuid(uuid);
                record.setType(type);
                record.setCategory(category);
                record.setAmount(amount);
                record.setRemark(remark);
                record.setDate(date);
                record.setTimeStamp(timeStamp);

                records.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    /**
     * 查询出账单存在的日期，且日期去重。
     *
     * @return
     */
    public LinkedList<String> getAvailableDate() {

        LinkedList<String> dates = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT * from Record order by date asc", new String[]{});
        if (cursor.moveToFirst()) {

            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                //防止添加多个重复的date 2019-2-10
                if (!dates.contains(date)) {
                    dates.add(date);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return dates;
    }
}
