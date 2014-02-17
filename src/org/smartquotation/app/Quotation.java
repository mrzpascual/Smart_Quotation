package org.smartquotation.app;

import org.smartquotation.app.R;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Quotation extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int CLIENT_ID = Menu.FIRST + 1;
    private static final int CLEAR_ID = Menu.FIRST + 2;

    private QuotationDbAdapter mDbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_list);
        mDbHelper = new QuotationDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }

    private void fillData() {
        Cursor itemCursor = mDbHelper.fetchAllItems();
        startManagingCursor(itemCursor);

        // Create an array to specify the fields we want to display in the list
        String[] from = new String[]{QuotationDbAdapter.KEY_ITEM};

        // and an array of the fields we want to bind those fields to
        int[] to = new int[]{R.id.text1};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter items = 
            new SimpleCursorAdapter(this, R.layout.quotation_row, itemCursor, from, to);
        setListAdapter(items);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        menu.add(1, CLIENT_ID, 0, R.string.menu_add_client);
        menu.add(2, CLEAR_ID, 0, R.string.menu_clear);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createQuotation();
                return true;
            case CLEAR_ID:
            	clearList();
            	return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    private void createQuotation() {
        Intent i = new Intent(this, QuotationEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    private void clearList(){
    	SimpleCursorAdapter items = new SimpleCursorAdapter(this, R.layout.quotation_row, null, null, null);
    	setListAdapter(items);
    	mDbHelper.deleteAllItem();
    	
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, QuotationEdit.class);
        i.putExtra(QuotationDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
    

}
