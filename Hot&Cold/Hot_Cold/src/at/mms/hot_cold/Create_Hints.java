package at.mms.hot_cold;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.Settings;

public class Create_Hints extends ActionBarActivity {
	private Button fixButton;
	private ImageButton audioButton;
	private Button createAHintButton;
	private ImageButton pictureButton;
	private ImageButton textButton;
	private EditText Text, TextLatitude, TextLongitude;
	public final int CAMERA_RESULT = 0;
	public final int TAKE_PICTURE = 1;
	public final int TAKE_AUDIO = 3;
	public final int External_Content = 2;
	private static String latitude, longitude;
	String hintPath = "";
	String strName="", path;
	Boolean accepted=false;
	Boolean acceptedAudio=false;
	final Context variable=this;
	TextView textTargetUri ;
    ImageView targetImage;
	ImageView imageView;
    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
		 TextView audioHint = (TextView) findViewById(R.id.audioHint);

    	 
         setContentView(R.layout.activity_create__hints);
         final TextView storyHint = (TextView) findViewById(R.id.textHint);
         final TextView hintName = (TextView) findViewById(R.id.HintName);
         final TextView coordinates = (TextView) findViewById(R.id.Coordinates);
    	 
         Intent in = getIntent();
         String tv1= in.getExtras().getString("Game");
         try {
             hintName.setText(tv1);hintPath = hintPath +"$" +tv1;
 		} catch (Exception e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 		}
    	 
         // CREATING THE BUTTONS
         imageView = (ImageView)findViewById(R.id.imageViewHint);
         fixButton= (Button) findViewById(R.id.buttonFixCoordinates);
         createAHintButton= (Button) findViewById(R.id.createNewHint);
         audioButton = (ImageButton) findViewById(R.id.imageButtonAudio);
         pictureButton = (ImageButton) findViewById(R.id.imageButtonPicture);
         textButton = (ImageButton) findViewById(R.id.imageButtonText);
       
         //LOCATION
        fixButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		AlertDialog.Builder builder = new AlertDialog.Builder(variable);
        		
				// Get the layout inflater
				
			    LayoutInflater inflater = getLayoutInflater();
				View view=inflater.inflate(R.layout.dialog_coord, null);
			    // Inflate and set the layout for the dialog
			    // Pass null as the parent view because its going in the dialog layout
			    builder.setView(view);
			    TextLatitude = (EditText) view.findViewById(R.id.latitude);
			    TextLongitude = (EditText) view.findViewById(R.id.longitude);
				
				
				// Add the buttons
				builder.setPositiveButton(R.string.getcoor, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	try{accepted=true;
				        		latitude = TextLatitude.getText().toString();
				        		longitude = TextLongitude.getText().toString();
				        		
				        	if(latitude.length()<=0 || longitude.length()<=0 ){
				        		
				        		getLocation();
				        		
					     		String location = latitude +"$"+ longitude;
					        	coordinates.setText(location,TextView.BufferType.EDITABLE);
					        	hintPath = hintPath + "$la" + location;			        	
				     		} else {
				     			//TODO verify if the coordinates are valid
				     			String location = latitude +"$"+ longitude;
				     			coordinates.setText(latitude +"$"+ longitude);
				     			hintPath = hintPath + "$la" + location;
				     		}
				           } catch(Exception e) {
				        	   
				               dialog.dismiss();
				           }}
				       });
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   dialog.cancel();}
        	});
				//3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
             
        	}
        	
        });
        
        //Create A New Hint Button
        createAHintButton.setOnClickListener(new View.OnClickListener() {

        	@Override
        	public void onClick(View v) {
        		if(accepted==true){
      			Intent returnIntent =	new Intent(getApplicationContext(), Create_Game.class); 
      			returnIntent.putExtra("paths", hintPath);
      			setResult(RESULT_OK,returnIntent);
      			finish();}
        		else{
         	         Toast.makeText(getApplicationContext(), "Try to add some coordinates", Toast.LENGTH_LONG).show();
        		}
	     		}
    	});
      
      //AUDIO
        audioButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent = new Intent(getApplicationContext(), Audio.class);
	        	startActivityForResult(intent,TAKE_AUDIO);	  
	        	//finish();
        	}
        });
        
      
      //PICTURE

        pictureButton.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
              
				AlertDialog.Builder builder = new AlertDialog.Builder(variable);
				// Get the layout inflater
			    LayoutInflater inflater = getLayoutInflater();
			    // Inflate and set the layout for the dialog
			    // Pass null as the parent view because its going in the dialog layout
			    builder.setView(inflater.inflate(R.layout.dialog_picture, null));
				// Add the buttons
				builder.setPositiveButton(R.string.picture, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				        	   
				        	   // Ensure that there's a camera activity to handle the intent
				        	    if (intent.resolveActivity(getPackageManager()) != null) {
				        	        // Create the File where the photo should go
				        	        File photoFile = null;
				        	        try {
				        	            photoFile = createImageFile();
				        	        } catch (IOException ex) {
				        	            // Error occurred while creating the File
				        	        }
				        	        // Continue only if the File was successfully created
				        	        if (photoFile != null) {
				        	            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoFile);
										startActivityForResult(intent, TAKE_PICTURE);
				        	        }
				                    imageView.setVisibility(View.VISIBLE);
				           }}
				       });
				builder.setNegativeButton(R.string.import_, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	    Intent intentExt = new Intent(Intent.ACTION_PICK,
				        	    		android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				        	    startActivityForResult(intentExt, External_Content);
				           }	
        	});
				//3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
        	}
        });
        
        //Text

        textButton.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
              
				AlertDialog.Builder builder = new AlertDialog.Builder(variable);

				// Get the layout inflater
				
			    LayoutInflater inflater = getLayoutInflater();
				View view=inflater.inflate(R.layout.dialog, null);
			    // Inflate and set the layout for the dialog
			    // Pass null as the parent view because its going in the dialog layout
			    builder.setView(view);
			    Text = (EditText) view.findViewById(R.id.text_hint);
				
				// Add the buttons
				builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   try{
				        	 strName = Text.getText().toString();
				     		if(strName.length()<=0){

					     		Toast.makeText(getApplicationContext(),"Type a valid text", Toast.LENGTH_SHORT).show();
				     		}else
				     		
				        	storyHint.setVisibility(View.VISIBLE);
				        	storyHint.setText(strName);
				        	 hintPath = hintPath +"$txt" +strName;
				           } catch(Exception e) {
				               dialog.dismiss();
				           }}
				       });
				builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   dialog.cancel();}
        	});
				//3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
             
        	}

        });}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create__hints, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
        }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	//Toast.makeText(getApplicationContext(), requestCode, Toast.LENGTH_SHORT).show();
    	super.onActivityResult(requestCode, resultCode, intent);

    	switch (requestCode) {
    	case TAKE_AUDIO:
            if(resultCode == RESULT_OK){

            	//Adds the audio path to the ImageView so the user can see it
                String result=intent.getStringExtra("paths");
    	         hintPath = hintPath + "$3gp" + result;
                acceptedAudio=true;
    	        // audioHint.setVisibility(View.VISIBLE);
    	         //audioHint.setText(result);
               /* String newString;
         	   if (intent.hasExtra("AUDIO_HINT")) {
         	       Bundle extras = intent.getExtras();
         	       if(extras == null) {
         	           newString= null;
         	       } else {
         	         newString= extras.getString("AUDIO_HINT");
         	         audioHint.setVisibility(View.VISIBLE);
         	         audioHint.setText(newString);
         	         Toast.makeText(getApplicationContext(), "Your audio was saved!", Toast.LENGTH_LONG).show();
         	         hintPath = hintPath + "$3gp" + newString;
         	       }
         	   }*/
            }
    	case TAKE_PICTURE:

    	if (intent !=null)
    		if(intent.hasExtra("data"))
            {
                /* if the data has an extra called "data" we assume the returned data 
                 * is from the usual camera app*/

                //retrieve the bitmap from the intent
                Bitmap thumbnail = (Bitmap) intent.getExtras().get("data");
                //get the path of out picture to send it to the server
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = Images.Media.insertImage(getApplicationContext().getContentResolver(), thumbnail, "Title", null);
   		     	hintPath = hintPath +"$img" + path;
                //update the image view with the bitmap
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(thumbnail);
            }
            else if(intent.getExtras()==null)
            {
                /* if there are no extras we assume its the miui camera 
                 * (which returns the path to the image in the returned data)*/
                Toast.makeText(getApplicationContext(), "No extras to retrieve!",Toast.LENGTH_SHORT).show();

                //retrieve the path from the intent using data.getData().getPath() and create a BitmapDrawable using this path
                BitmapDrawable thumbnail = new BitmapDrawable(getResources(), intent.getData().getPath());
                //update the image view with the newly created drawable
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(thumbnail); 

            }
    	break;
    	
    	case External_Content:
    		if (resultCode == RESULT_OK){
    		     Uri targetUri = intent.getData();
    		     Bitmap bitmap;
    		     hintPath = hintPath +"$img" + targetUri.getPath();
    		     try {
    		      bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
    		     		      		
    	            //update the image view with the bitmap
    	          imageView.setVisibility(View.VISIBLE);
    		      imageView.setImageBitmap(bitmap);

    		     } catch (FileNotFoundException e) {
    		      // TODO Auto-generated catch block
    		      e.printStackTrace();
    		     }
    		    }
    		break;
    	}
    	}
    
	
    // location method
    private void getLocation(){
       LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       List<String> providers = locMan.getProviders(true);
       Location bestLocation = null;
       //look for a provider which give us coordinates
       for (String provider : providers) {
           Location location = locMan.getLastKnownLocation(provider);
           if (location == null) {
               continue;
           }
           if (bestLocation == null
                   || location.getAccuracy() < bestLocation.getAccuracy()) {
               bestLocation = location;
           }
       }
     boolean enabled = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
     // check if enabled and if not send user to the GSP settings
     // Better solution would be to display a dialog and suggesting to 
     // go to the settings
     if (!enabled) {
       Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
       startActivity(intent);
     } 
     // Initialize the location fields
        if (bestLocation != null) {
          onLocationChanged(bestLocation);
        } else {
          latitude = "Location not available";
          longitude = "Location not available";
        }
       } 
       
    public void onLocationChanged(Location location) {
    	// get latitude
        double lat = location.getLatitude();
        // get longitude
        double longit = location.getLongitude();
        // format the coordinates
        NumberFormat coordFormat = NumberFormat.getInstance();
        coordFormat.setMinimumFractionDigits(2);
        coordFormat.setMaximumFractionDigits(4);
        latitude = coordFormat.format(lat);
	    longitude = coordFormat.format(longit);  
   }
       
    
}

