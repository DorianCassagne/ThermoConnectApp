package me.dcal.thermoconnectapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class FilePicker extends AppCompatActivity  {
    private static final int REQUEST_WRITE_PERMISSION = 786;
    List<Uri> UriTabDoc;
    Uri finalimage;



    public void  openFilePicker(List<Uri> UriTabDoc, Uri finalimage){
        this.UriTabDoc =UriTabDoc;
        this.finalimage = finalimage;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        try{
            startActivityForResult(intent, 7);
        } catch (ActivityNotFoundException e){
            Toast.makeText(this, "There are no file explorer clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 7) { // for file selection
//                try {
                Uri fileuri = data.getData();

                //final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                //final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String name = getFileName(fileuri);
                LinearLayout addedfiles = (LinearLayout) findViewById(R.id.addedfile);
                TextView tv1 = new TextView(this.getApplicationContext());
                tv1.append(name);
                addedfiles.addView(tv1);
                this.UriTabDoc.add(fileuri);
                //



            }else {// for image selection
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ImageView image_view = (ImageView) findViewById(R.id.imgView);
                    image_view.setImageBitmap(selectedImage);
                    this.finalimage = imageUri;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
