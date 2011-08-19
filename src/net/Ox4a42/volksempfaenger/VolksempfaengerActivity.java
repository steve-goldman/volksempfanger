package net.Ox4a42.volksempfaenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class VolksempfaengerActivity extends Activity implements OnClickListener {
    private Button buttonSubscriptionList;
    private Button buttonListenQueue;
    private Button buttonDownloadQueue;
    private Button buttonSettings;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        buttonSubscriptionList = (Button) findViewById(R.id.button_subscriptionlist);
        buttonListenQueue = (Button) findViewById(R.id.button_listenqueue);
        buttonDownloadQueue = (Button) findViewById(R.id.button_downloadqueue);
        buttonSettings = (Button) findViewById(R.id.button_settings);
        
        buttonSubscriptionList.setOnClickListener(this);
        buttonListenQueue.setOnClickListener(this);
        buttonDownloadQueue.setOnClickListener(this);
        buttonSettings.setOnClickListener(this);
    }

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_subscriptionlist:
			Toast.makeText(this, "SubscriptionListActivity", Toast.LENGTH_SHORT).show();
			break;
		case R.id.button_listenqueue:
			Toast.makeText(this, "ListenQueueActivity", Toast.LENGTH_SHORT).show();
			break;
		case R.id.button_downloadqueue:
			Toast.makeText(this, "DownloadQueueActivity", Toast.LENGTH_SHORT).show();
			break;
		case R.id.button_settings:
			Toast.makeText(this, "SettingsActivity", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}