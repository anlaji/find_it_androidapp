package at.mms.hot_cold;

import java.io.File;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Picture extends Activity {
	public final int CAMERA_RESULT = 0;
	public final int TAKE_PICTURE = 1;
	private String pictureFilePath;
	private Button savePictureButton;
	private Button takePictureButton;
	private ImageView imageView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture);
		 
		pictureFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/tmp_image.jpg";
		
		takePictureButton= (Button) findViewById(R.id.buttonTakePicture);
		savePictureButton= (Button) findViewById(R.id.buttonSavePicture);
		ImageView imageView= (ImageView)findViewById(R.id.imageView);
				
		takePictureButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, TAKE_PICTURE);
			}
		});
		
		savePictureButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Uri imageFileUri = Uri.fromFile(new File(pictureFilePath));
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
				startActivityForResult(intent, CAMERA_RESULT);
			}
		});

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
			switch (requestCode) {
				case CAMERA_RESULT:
					if (resultCode == RESULT_OK) {
						Bitmap bmp = BitmapFactory.decodeFile(pictureFilePath);
					}
					break;
				case TAKE_PICTURE:
					if (intent !=null)
						if (intent.hasExtra("data")){
							Bitmap bmp = intent.getParcelableExtra("data");
							imageView.setImageBitmap(bmp);
		
						}
				}
		}
	
}
