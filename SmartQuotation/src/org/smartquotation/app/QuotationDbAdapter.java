package org.smartquotation.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class QuotationDbAdapter extends AbstractDbAdapter {
    public static final String KEY_ITEM = "key_item";
    public static final String KEY_PRICE = "key_price";
    public static final String KEY_QUANTITY = "key_quantity";
    public static final String KEY_TOTAL_PRICE = "key_total_price";
    public static final String KEY_ROWID = "_id";
    
    private static final String DATABASE_TABLE = "quotation";
    
	public QuotationDbAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	
    public long createQuotation(String item_key, String price_key, String quantity_key, String total_price_key ) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ITEM, item_key);
        initialValues.put(KEY_PRICE, price_key);
        initialValues.put(KEY_QUANTITY, quantity_key);
        initialValues.put(KEY_TOTAL_PRICE, total_price_key);   
        return mDb.insert(DATABASE_TABLE, null, initialValues); 
    }
    
    public boolean deleteItem(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public void deleteAllItem(){
    	mDb.delete(DATABASE_TABLE, null, null);
    }
 
    public Cursor fetchAllItems() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_ITEM,
                KEY_PRICE, KEY_QUANTITY, KEY_TOTAL_PRICE}, null, null, null, null, null);
    }

    public Cursor fetchItems(long rowId) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                         KEY_ITEM, KEY_PRICE, KEY_QUANTITY, KEY_TOTAL_PRICE}, KEY_ROWID + "=" + rowId, null,
                         null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateQuotation(long rowId, String item_key, String price_key, String quantity_key, String total_price_key) {
        ContentValues args = new ContentValues();
        args.put(KEY_ITEM, item_key);
        args.put(KEY_PRICE, price_key);
        args.put(KEY_QUANTITY, quantity_key);
        args.put(KEY_TOTAL_PRICE, total_price_key);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
