package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

/**
 * Created by Dimuthu on 2015-12-04.
 */
public class DBHandler implements Serializable {

    private static final String KEY_ACCOUNT_NO = "account_no";
    private static final String KEY_BANK_NAME = "bank_name";
    private static final String KEY_ACCOUNT_HOLDER_NAME = "account_holder_name";
    private static final String KEY_BALANCE = "balance";

    private static final String KEY_ACCOUNT_NO_TRANSACTION_ID = "transaction_id";
    private static final String KEY_ACCOUNT_NO_TRANSACTION = "account_no";
    private static final String KEY_DATE = "date";
    private static final String KEY_EXPENSE_TYPE = "expense_type";
    private static final String KEY_AMOUNT = "amount";

    private static final String DATABASE_NAME = "130323V";
    private static final String DATABASE_TABLE_ACCOUNT = "account";
    private static final String DATABASE_TABLE_TRANSACTION = "transactions";
    private static final int DATABASE_VERSION = 1;

    private DBHelper helper;
    private final Context context;
    private SQLiteDatabase database;

    public DBHandler(Context c) {
        this.context = c;
    }

    private static class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_TABLE_ACCOUNT +
                    " (" +KEY_ACCOUNT_NO+" TEXT PRIMARY KEY, "+
                    KEY_BANK_NAME+" TEXT NOT NULL, "+
                    KEY_ACCOUNT_HOLDER_NAME+ " TEXT NOT NULL, "+
                    KEY_BALANCE+" DOUBLE NOT NULL);"
            );

            db.execSQL("CREATE TABLE " + DATABASE_TABLE_TRANSACTION +
                            " (" +KEY_ACCOUNT_NO_TRANSACTION_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                            KEY_ACCOUNT_NO_TRANSACTION+" TEXT NOT NULL, "+
                            KEY_DATE+" DATE NOT NULL, "+
                            KEY_EXPENSE_TYPE+ " TEXT NOT NULL, "+
                            KEY_AMOUNT+" DOUBLE NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_ACCOUNT);
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_TRANSACTION);
            onCreate(db);
        }
    }

    public DBHandler open(){
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close(){
        helper.close();
    }

    public long createEntry(ContentValues cv,String table){
        return database.insert(table,null,cv);
    }

    public Cursor getData(String[] columns,String table,String where){
        Cursor c = database.query(table,columns,where,null,null,null,null);
        return c;
    }

    public long updateEntry(ContentValues cv,String table,String where){
        return database.update(table,cv,where,null);
    }

    public void deleteEntry(String table,String where){
        database.delete(table,where,null);
    }
}
