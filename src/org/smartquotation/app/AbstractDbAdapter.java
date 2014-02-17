package org.smartquotation.app;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class AbstractDbAdapter {
	protected static final String TAG = "AbstractDbAdapter";
    protected DatabaseHelper mDbHelper;
    protected SQLiteDatabase mDb;
    
    private static final String TABLE_QUOTATION = "quotation"; 
    private static final String TABLE_CREATE_QUOTATION = "create table quotation (_id integer primary key autoincrement, " 
    													 + "key_item text not null, key_price text not null, key_quantity text not null, key_total_price text not null );";
    
    private static final String DATABASE_NAME = "quotationDB";
    private static final int DATABASE_VERSION = 2;
    private final Context mCtx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
    	
    	DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
    	
    	@Override
    	public void onCreate(SQLiteDatabase db){
    		db.execSQL(TABLE_CREATE_QUOTATION);
    	}
    	
    	@Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTATION);
            
            onCreate(db);
        }
    }
    
    public AbstractDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    
    public AbstractDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
    	mDbHelper.close();
    }
}
