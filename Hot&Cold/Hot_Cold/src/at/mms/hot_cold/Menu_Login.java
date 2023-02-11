package at.mms.hot_cold;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class Menu_Login extends ActionBarActivity {
	private Button playButton;
	private Button createMapButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_login);

		
		playButton= (Button) findViewById(R.id.buttonPlay);
		
        playButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "toast clicked", Toast.LENGTH_SHORT).show();
        			Intent intent =
        			new Intent(getApplicationContext(), Game.class);
        			startActivity(intent);
            }
           }); 
        createMapButton= (Button) findViewById(R.id.buttonCreateMap);
		
        createMapButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
				//Toast.makeText(getApplicationContext(), "toast clicked", Toast.LENGTH_SHORT).show();
        			Intent intent =
        			new Intent(getApplicationContext(), Create_Game.class);
        			startActivity(intent);
            }
           }); 
	}

}
