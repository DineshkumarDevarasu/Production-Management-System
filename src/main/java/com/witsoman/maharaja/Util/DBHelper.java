package com.witsoman.maharaja.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;


public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper dbHelper = null;
    private static SQLiteDatabase db = null;
    private Context mContext;
    public static String DATABASE_NAME ="chefdetail.db";
    public static int DATABASE_VERSION = 1;


    private DBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context) {

        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
            if (db == null) {
                db = dbHelper.getWritableDatabase();
            }

        }

        return dbHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBTables.CREATE_TABLE);
        Log.e("DB", "CREATED");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /* //Drop existing table*/
        db.execSQL("DROP TABLE " + DBTables.REGISTRATION_TABLE);
        Log.e("DB", "DROPPED");
        //Recreate with new columns
        db.execSQL(DBTables.CREATE_TABLE);
        Log.e("DB", "CREATED");


    }
    /*Insert value using Sqlite*/

    public void insertRegistrationTable(String ChefId,String ChefName,int OrderId) {
        try {
            ContentValues insertValues = new ContentValues();
            insertValues.put(DBConstants.CHEFID,ChefId);
            insertValues.put(DBConstants.CHEFNAME,ChefName);
            insertValues.put(DBConstants.ORDERID,OrderId);
            db.insert(DBTables.REGISTRATION_TABLE, null, insertValues);
            Log.e("Inserted", DBTables.REGISTRATION_TABLE);
            Log.e("InsertValues",""+insertValues);
        } catch (Exception e) {
            Log.e("Exception ", "insertRegistrationTable" + e.toString());
        }

    }


    /*Update value using Sqlite*/

   /* public void updateRegistrationTable(int ChefId,int OrderId){

        try{
            ContentValues updatedValues = new ContentValues();
            // Assign values for each row.
            updatedValues.put(DBConstants.CHEFID,ChefId);
            updatedValues.put(DBConstants.ORDERID,OrderId);


            Log.e("Id",""+ChefId);
            db.update(DBTables.REGISTRATION_TABLE, updatedValues, DBConstants.CHEFID + "='" + ChefId + "'", null);
            Log.e("Updated", DBTables.REGISTRATION_TABLE);
        }catch (Exception e){
            Log.e("Exception ","DB update "+e.toString());
        }

    }*/
    /*View  sqlite database value in Log Cat*/
    public void getViewDetails(){
        queryLoop("select * from " + DBTables.REGISTRATION_TABLE);
    }
    public Employee[] getViewDetails(String chefId){
        return queryLoop("select * from "+DBTables.REGISTRATION_TABLE + " where " + DBConstants.CHEFID + "='" + chefId + "'",0);
    }

    public void queryLoop(String query) {
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    int id = cursor.getInt(cursor.getColumnIndex(DBTables.UNIQUE_ID));
                    String ChefId = cursor.getString(cursor.getColumnIndex(DBConstants.CHEFID));
                    String ChefName=cursor.getString(cursor.getColumnIndex(DBConstants.CHEFNAME));
                    String OrderId = cursor.getString(cursor.getColumnIndex(DBConstants.ORDERID));


                    Log.e("Row Values", Integer.toString(id) + "," + ChefId + "," +ChefName + "," + OrderId );
                    cursor.moveToNext();
                }
                Log.e("Records: ", Integer.toString(cursor.getCount()) + " Records Found!");
            } else {
                Log.e("Records: ", "No Records Found!");
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("Exception", "Select " + e.toString());
        }
    }

    /*Retrieve sqlite database value Layout*/
    public Employee[] queryLoop(String query, int flag) {
        try {
            db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                Employee[] eArray = new Employee[cursor.getCount()];
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    Employee e = new Employee();
                    int id = cursor.getInt(cursor.getColumnIndex(DBTables.UNIQUE_ID));
                    e.ChefId = cursor.getInt(cursor.getColumnIndex(DBConstants.CHEFID));
                    e.ChefName=cursor.getString(cursor.getColumnIndex(DBConstants.CHEFNAME));
                    e.OrderId = cursor.getInt(cursor.getColumnIndex(DBConstants.ORDERID));

                    eArray[i] = e;

                    Log.e("RowValues", Integer.toString(id) + "," + e.ChefId + ","+e.ChefName+"," + e.OrderId);                 cursor.moveToNext();
                }
                Log.e("Records: ", Integer.toString(cursor.getCount()) + " Records Found!");
                cursor.close();
                return eArray;
            } else {
                Log.e("Records: ", "No Records Found!");
                return null;
            }

        } catch (Exception e) {
            Log.e("Exception", "Select " + e.toString());
        }
        return null;
    }





    /*Delete Record in sqlite*/
   public void DeleteRecord(int ChefId){

        try{
            db.delete(DBTables.REGISTRATION_TABLE, DBConstants.CHEFID + "=" + "'" + ChefId + "'", null);

            Log.e("Deleted", DBTables.REGISTRATION_TABLE);
        }catch (Exception e){
            Log.e("Exception ","DB delete "+e.toString());
        }

    }


    public void DeleteAllRecords() {
        try {
            db.delete(DBTables.REGISTRATION_TABLE, null, null);

            Log.e("Delete All Records : ", DBTables.REGISTRATION_TABLE);
        } catch (Exception e) {
            Log.e("Exception ", "DB delete All Records : " + e.toString());
        }
    }

        public boolean Login(String ChefId,String OrderId) throws SQLException
        {
            Cursor mCursor = db.rawQuery("SELECT * FROM " + DBTables.REGISTRATION_TABLE + " WHERE CHEFID =? AND ORDERID=?", new String[]{ChefId,OrderId});
            if (mCursor != null) {
                if(mCursor.getCount() > 0)
                {
                    return true;
                }
            }
            return false;
        }


    }




