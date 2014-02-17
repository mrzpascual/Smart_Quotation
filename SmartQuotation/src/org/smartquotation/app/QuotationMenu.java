package org.smartquotation.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuotationMenu extends ListActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotation_menu);
        
        Button bttn_list = (Button) findViewById(R.id.bttn_goto_list);
        bttn_list.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intnt_to_Quotation = new Intent(QuotationMenu.this, Quotation.class);
				startActivity(intnt_to_Quotation);
			}
		});
		
	}

}
