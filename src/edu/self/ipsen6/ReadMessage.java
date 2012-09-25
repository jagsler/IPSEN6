package edu.self.ipsen6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReadMessage extends Activity {

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_message);  
        
        Intent intent = getIntent();
        String bericht = intent.getStringExtra("BERICHT");
        
        TextView txtBericht = (TextView) findViewById(R.id.txtBericht);
        txtBericht.setText(bericht);
    }

}
