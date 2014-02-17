package org.smartquotation.app;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.smartquotation.app.R;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QuotationEdit extends Activity {

    private EditText mItemText;
    private EditText mPriceText;
    private EditText mQuantityText;
    private TextView mTotalPriceText;
    private Long mRowId;
    private QuotationDbAdapter mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new QuotationDbAdapter(this);
        mDbHelper.open();

        setContentView(R.layout.quotation_edit);
        
        mItemText = (EditText) findViewById(R.id.item);
        mPriceText = (EditText) findViewById(R.id.unitPrice);
        mQuantityText = (EditText) findViewById(R.id.unitQuantity);
        mTotalPriceText = (TextView) findViewById(R.id.unitTotalPrice);
        mPriceText.setText("0.00");
        mQuantityText.setText("0");

        mPriceText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				if(s.length() > count || count != 0){
					String price_text = mPriceText.getText().toString();
					String quantity_text = mQuantityText.getText().toString();
					mTotalPriceText.setText(setTotalPrice(price_text, quantity_text));
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
        
        
        mQuantityText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length() > count || count != 0){
				    String price_text = mPriceText.getText().toString();
				    String quantity_text = mQuantityText.getText().toString();
					mTotalPriceText.setText(setTotalPrice(price_text, quantity_text));
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
 
        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(QuotationDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(QuotationDbAdapter.KEY_ROWID)
									: null;
		}

		populateFields();
		
		final CheckBox cancel_item = (CheckBox) findViewById(R.id.cancel);
        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
			public void onClick(View view) {
            	
            	if (mItemText.getText().length() != 0 && mPriceText.getText().length() != 0 && mQuantityText.getText().length() != 0 ) {
	                
            		setResult(RESULT_OK);
	                finish();
	                if (cancel_item.isChecked()) {
	                	mDbHelper.deleteItem(mRowId);
	                }
            	} else {
            		Toast.makeText(QuotationEdit.this, "Please complete the required fields!", Toast.LENGTH_SHORT).show();
            	}
            }
        });
        
    }

    
    private void populateFields() {
        if (mRowId != null) {
            Cursor particular = mDbHelper.fetchItems(mRowId);
            startManagingCursor(particular);
            mItemText.setText(particular.getString(
                    particular.getColumnIndexOrThrow(QuotationDbAdapter.KEY_ITEM)));
            mPriceText.setText(particular.getString(
                    particular.getColumnIndexOrThrow(QuotationDbAdapter.KEY_PRICE)));
            mQuantityText.setText(particular.getString(
                    particular.getColumnIndexOrThrow(QuotationDbAdapter.KEY_QUANTITY)));
            mTotalPriceText.setText(particular.getString(
                    particular.getColumnIndexOrThrow(QuotationDbAdapter.KEY_TOTAL_PRICE)));
            
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(QuotationDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
       super.onPause();
       saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String item_text = mItemText.getText().toString();
        String price_text = mPriceText.getText().toString();
        String quantity_text = mQuantityText.getText().toString();
        String total_price_text = mTotalPriceText.getText().toString();
        
        
        if (mRowId == null) {
        	long id = mDbHelper.createQuotation(item_text, price_text, quantity_text, total_price_text);
            if (id > 0) {
                mRowId = id;
            }
            
        } else {
            mDbHelper.updateQuotation(mRowId, item_text, price_text, quantity_text, total_price_text);
        }
        
    }
    
    public String setTotalPrice(String price, String quantity){
    	BigDecimal bd_price = new BigDecimal(price);
    	BigDecimal bd_quantity = new BigDecimal(quantity);
    	BigDecimal bd_total = bd_price.multiply(bd_quantity);
    	NumberFormat nf = NumberFormat.getCurrencyInstance();
    	String str_total = nf.format(bd_total);
        return str_total;
    }
    

    
}
