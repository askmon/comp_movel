package usp.ime.movel.ouvidoria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Registrar extends Activity implements OnClickListener {

	private Intent intent;
	private String username;
	private Button mPicture;
	private EditText location;
	private EditText description;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registrar);
		intent = getIntent();
		username = intent.getStringExtra("username");
		TextView user = (TextView)findViewById(R.id.textView1);
		user.setText("Usuário: " + username);
		description = (EditText) findViewById(R.id.description);
		location = (EditText) findViewById(R.id.location);
		mPicture = (Button) findViewById(R.id.picture);
		mPicture.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.picture:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File f = new File(android.os.Environment.getExternalStorageDirectory(), "ouvidoria.jpg");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			System.out.println("Primeiro " + Uri.fromFile(f));
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString() + "/ouvidoria.jpg");
                System.out.println("Segundo " + Uri.fromFile(f));
                try {
                    Bitmap bm;
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath());
                    if (bm == null) System.out.println("bm ta null");
                    System.out.println(f.getAbsolutePath());
                    bm = Bitmap.createScaledBitmap(bm, 140, 140, true);
                    ImageView image = (ImageView) findViewById(R.id.ivImage);
                    image.setImageBitmap(bm);
 
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
             
        }
    }

}
