package at.mms.hot_cold;


import java.io.IOException;
import java.net.UnknownHostException;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import at.mms.hot_cold.tools.Client;


public class Login extends ActionBarActivity {
	private Button enterButton, newPlayerButton;
	private boolean connected =true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		enterButton= (Button) findViewById(R.id.buttonEnter);
		newPlayerButton = (Button) findViewById(R.id.new_player);
		final EditText userText = (EditText) findViewById(R.id.newUsername);
		final EditText passText = (EditText) findViewById(R.id.newPassword);
		
        enterButton.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
				
        		if(userText.getText().toString()!= null && passText.getText().toString()!=null){
        			
        			Thread t = new Thread() {
        				public void run() {
        					Client c;
        					c = new Client();
        					
        					try {
        						c.connect();
        					c.createPlayer(userText.getText().toString(), passText.getText().toString());
        						
        						System.out.println("CONNECTING");
        						
        					} catch (UnknownHostException e) {
        						connected = false;
        						e.printStackTrace();
        						return;
        					} catch (IOException e) {
        						connected = false;
        						e.printStackTrace();
        						return;
        					} catch (InterruptedException e) {
        						connected = false;
        						e.printStackTrace();
        						return;
        					}
        				}
        			};
        			t.start();
        			
        			if(!connected){
        				Toast.makeText(getApplicationContext(), "Connection problem!", Toast.LENGTH_SHORT).show();
        				connected = true;
        				return;
        			}
        			Intent intent =	new Intent( getApplicationContext(), Menu_Login.class); 
        	        startActivity(intent);
        		} else {
        			Toast.makeText(getApplicationContext(), "Invalid Input! Try Again!", Toast.LENGTH_SHORT).show();
        		}
    			
            }
           }); 
        
       
        newPlayerButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        			Intent intent =
        			new Intent(getApplicationContext(), CreatePlayer.class);
        			startActivity(intent);
            }
           }); 
	
	}

//	 @Override
//     public void onBackPressed() {
//         new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
//                 .setMessage("Are you sure you want to exit?")
//                 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                     @Override
//                     public void onClick(DialogInterface dialog, int which) {
//                    	 finish();
//                    	 Intent intent = new Intent(Intent.ACTION_MAIN);
//                         intent.addCategory(Intent.CATEGORY_HOME);
//                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                         startActivity(intent);
//                         
//                     }
//                 }).setNegativeButton("No", null).show();
//     }
	 
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				System.exit(0);
				return true;
			}
			return super.onKeyDown(keyCode, event);		
		}

}
