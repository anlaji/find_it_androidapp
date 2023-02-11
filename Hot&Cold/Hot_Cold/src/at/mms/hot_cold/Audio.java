package at.mms.hot_cold;
import java.io.IOException;
import java.net.UnknownHostException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import at.mms.hot_cold.tools.Client;

public class Audio extends Activity {
	 private MediaRecorder myAudioRecorder;
	 private String outputFile = null, filePath = null;
	 private ImageButton start,stop,play;
	 private Button saveAudio,importAudio;
	 public final int TAKE_AUDIO = 1,External_Audio_Content = 2;
	 Boolean accepted=false;
	 
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		 	super.onCreate(savedInstanceState);
			setContentView(R.layout.audio);
		     
	    	  start = (ImageButton)findViewById(R.id.imageButtonrecord);
		      stop = (ImageButton)findViewById(R.id.imageButtonrecord_stop);
		      play = (ImageButton)findViewById(R.id.imageButtonrecord_play);
		      saveAudio = (Button)findViewById(R.id.AudioSave);
		      importAudio = (Button)findViewById(R.id.AudioImport);
		     
		      if(hasMicrophone()){
		    	  play.setEnabled(false);
		    	  stop.setEnabled(false);
		      } else {
		    	  play.setEnabled(false);
	              stop.setEnabled(false);
	              start.setEnabled(false);
	          }
		      
		      start.setOnClickListener(new View.OnClickListener() {
					@Override	
					public void onClick(View v) {
						accepted=true;
						start(v);	
					}
				});
			    
			    stop.setOnClickListener(new View.OnClickListener() {
					@Override								
					public void onClick(View v) {
						stop(v);	
					}
				});
			    
			    play.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							play(v);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
				});	 
			    
			    saveAudio.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(accepted==true){
		      			Intent returnIntent =	new Intent(getApplicationContext(), Create_Game.class); 
		      			returnIntent.putExtra("paths",outputFile);
		      			setResult(RESULT_OK,returnIntent);
		      			finish();  
						finish();}
						else{
				  	         Toast.makeText(getApplicationContext(), "You need to add same audio!", Toast.LENGTH_LONG).show();
						}
					}
				});	 
			    
			    importAudio.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						 Intent intentExt = new Intent(Intent.ACTION_PICK,
							        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);		        	    
							      	startActivityForResult(intentExt, External_Audio_Content);
							      	finish();
					}
				});	 
	 
	 }

	 
		   public void start(View view){
		      try {
		    	 settings();
		         myAudioRecorder.prepare();
		         myAudioRecorder.start();
		      } catch (IllegalStateException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         e.printStackTrace();
		      }
		      start.setEnabled(false);
		      stop.setEnabled(true);
		      
		      Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

		   }

		   public void stop(View view){
		     if(myAudioRecorder != null){
			   myAudioRecorder.stop();
			   myAudioRecorder.release();
			   myAudioRecorder  = null;
		     }
		      stop.setEnabled(false);
		      play.setEnabled(true);
		      Toast.makeText(getApplicationContext(), "Audio recorded successfully",
		      Toast.LENGTH_LONG).show();
		   }
		  
		   @Override
		   public boolean onCreateOptionsMenu(Menu menu) {
		      // Inflate the menu; this adds items to the action bar if it is present.
		      getMenuInflater().inflate(R.menu.audio, menu);
		      return true;
		   }
		   
		   public void play(View view) throws IllegalArgumentException,   
		   SecurityException, IllegalStateException, IOException{
		   
		   MediaPlayer m = new MediaPlayer();
		   m.setDataSource(outputFile);
		   m.prepare();
		   m.start();
		   Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();

		   }
		   
			
			protected boolean hasMicrophone() {
				PackageManager pmanager = this.getPackageManager();
				return pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
			}
			
			private void settings(){
				  outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myrecording.3gp";
			      myAudioRecorder = new MediaRecorder();
			      myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.THREE_GPP);
			      myAudioRecorder.setOutputFile(outputFile);
			}
			
			 private void sendToList(String path){
				 Intent intent =new Intent(getApplicationContext(), Create_Hints.class);
				 intent.putExtra("AUDIO_HINT", path);		 
				 startActivity(intent); 
	         }
			
			 @Override
			 protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		          if(resultCode == RESULT_OK && requestCode == External_Audio_Content){			
						   Uri audioUri = data.getData();
						   String path = getRealPathFromURI(this, audioUri);
						   sendToList(path);
						   
		          }
		      }
			
			 public String getRealPathFromURI(Context context, Uri contentUri) {
				  Cursor cursor = null;
				  try { 
				    String[] proj = { MediaStore.Images.Media.DATA };
				    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
				    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				    cursor.moveToFirst();
				    return cursor.getString(column_index);
				  } finally {
				    if (cursor != null) {
				      cursor.close();
				    }
				  }
				}		
	            
			 
		}
