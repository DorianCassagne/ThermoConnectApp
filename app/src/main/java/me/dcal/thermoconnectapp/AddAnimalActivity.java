package me.dcal.thermoconnectapp;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.dcal.thermoconnectapp.Modeles.BodyAnimal;
import me.dcal.thermoconnectapp.Modeles.BodyAnimalData;
import me.dcal.thermoconnectapp.Modeles.BodySpecies;
import me.dcal.thermoconnectapp.Services.API;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddAnimalActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    private HashMap<String, List<Uri>> UriTab = new HashMap<>();
    private List<Uri> UriTabDoc = new ArrayList<>();
    private List<Uri> UriTabImage = new ArrayList<>();
    private TextView naissance;
    private TextView poids;
    private Uri finalimage;
    int idterra;
    int idanimal;
    Spinner speciesspinner;
    Spinner sexspinner;
    TextView autodescrip;
    TextView commentaire;
    HashMap<String, String> arraydescription = new HashMap<>();
    Boolean type;
    BodyAnimal bodyAnimal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_animal);
        setSpecies();
        setSexe();

        idterra  = (int) getIntent().getSerializableExtra("idterra");

        ImageView img = (ImageView) findViewById(R.id.imgView);
        TextView name = (TextView) findViewById(R.id.name);
        autodescrip = (TextView) findViewById(R.id.descriptionauto) ;
        commentaire = (TextView) findViewById(R.id.commentaire) ;
        naissance = (TextView) findViewById(R.id.naissance);
        speciesspinner = (Spinner) findViewById(R.id.spinner);
        speciesspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                autodescrip.setText(arraydescription.get(parentView.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        poids = (TextView) findViewById(R.id.poids);
        naissance = (TextView) findViewById(R.id.naissance);

        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
        naissance.append(formatter.format(new Date()));
        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                type = true;
                requestPermission();

            }
        });
        Button buttonLoadFile = (Button) findViewById(R.id.buttonLoadFile);
        buttonLoadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                type = false;
                requestPermission();
            }
        });

        Button buttoncreate = (Button) findViewById(R.id.createanimal);
        buttoncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (finalimage != null){
                    UriTabImage.add(finalimage);
                    UriTab.put("picture", UriTabImage);
                }
                if (UriTabDoc.size() >0){
                    UriTab.put("files", UriTabDoc);
                }


                List<String> documents = new ArrayList<>();
                Boolean sex = true;
                if (sexspinner.getSelectedItem().toString().equals("Male")){
                    sex = true;
                }else if (sexspinner.getSelectedItem().toString().equals("Male")){
                    sex = false;
                }else{
                    sex = null;
                }

//((TextView) findViewById(R.id.naissance)).getText().toString()
                bodyAnimal = new BodyAnimal(API.getBodyConnexion(getApplicationContext())
                                , idterra
                                , (BodySpecies) speciesspinner.getSelectedItem()
                                ,name.getText().toString()
                                ,sex
                                , ((TextView) findViewById(R.id.naissance)).getText().toString()
                        ,commentaire.getText().toString()
                        ,null
                        ,0
                , documents);

                List<MultipartBody.Part> part = new ArrayList<>();
                if (UriTab.size() > 0){
                    part = uploadFile(UriTab);
                }

                Call<Integer> reponse= API.getInstance().simpleService.ajoutAnimal(bodyAnimal,part);
                reponse.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        idanimal = response.body();

                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault());
                        String currentDateandTime = sdf.format(new Date());

                        BodyAnimalData bodydata = new BodyAnimalData(bodyAnimal.getBodyConnexion(), idanimal,currentDateandTime, Double.parseDouble(poids.getText().toString()) );
                        Call<Integer> reponse2= API.getInstance().simpleService.setAllAnimalData(bodydata);
                        reponse2.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                Integer i=response.body();
                                Toast toast = Toast.makeText(getApplicationContext(), i+"", Toast.LENGTH_SHORT);
                                toast.show();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                call.request().url();
                                Toast toast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        call.request().url();
                        Toast toast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });



            }
        });
        }

        public void changedate(View v){
            API.launchShortToast(getApplicationContext(),"onClick");
            int mYear, mMonth, mDay, mHour, mMinute;
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String date = dayOfMonth + "/" + month + "/" + year;
                            naissance.setText(date);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

    public void setSpecies(){

        Response<List<BodySpecies>> toto;
        Call<List<BodySpecies>> species=  API.getInstance().simpleService.getspecies(API.getBodyConnexion(getApplicationContext()));
        species.enqueue(new Callback<List<BodySpecies>>() {
            @Override
            public void onResponse(Call<List<BodySpecies>> call, Response<List<BodySpecies>> response) {
                BodySpecies[] arraySpinner = new BodySpecies[response.body().size()];

                int i =0;
                for (BodySpecies specie : response.body()){
                    arraySpinner[i] = specie;
                    arraydescription.put(specie.getSpecies(),specie.getDescription());
                    i++;
                }
                speciesspinner = (Spinner) findViewById(R.id.spinner);

                ArrayAdapter<BodySpecies> adapter = new ArrayAdapter<BodySpecies>(AddAnimalActivity.this,
                        android.R.layout.simple_spinner_item, arraySpinner);
// Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                speciesspinner.setAdapter(adapter);

                Toast toast = Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(Call<List<BodySpecies>> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "KO", Toast.LENGTH_SHORT);
                toast.show();
            }

        });

    }

    public void setSexe(){String[] arraySpinner = new String[] {
            "Male", "Femelle", "NC"
    };
        sexspinner = (Spinner) findViewById(R.id.sexspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        sexspinner.setAdapter(adapter);

    }
    public void openFilePicker(){

        if (type){
            Intent i = new Intent(
                    Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );

            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }else{
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            try{
                startActivityForResult(intent, 7);
            } catch (ActivityNotFoundException e){
                Toast.makeText(AddAnimalActivity.this, "There are no file explorer clients installed.", Toast.LENGTH_SHORT).show();
            }
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
                    UriTabDoc.add(fileuri);
                    //



            }else {// for image selection
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ImageView image_view = (ImageView) findViewById(R.id.imgView);
                    image_view.setImageBitmap(selectedImage);
                    finalimage = imageUri;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

        } else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();//do your job
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            openFilePicker();//do your job
        }
    }
    private List<MultipartBody.Part> uploadFile( HashMap<String, List<Uri>> filesUri) {

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        List<MultipartBody.Part> parts = new ArrayList<>();

        for (Map.Entry<String, List<Uri>> entry : filesUri.entrySet())
        {
            String key = entry.getKey();
            List<Uri> value = entry.getValue();

            for (Uri fileuri : value){
                File file = new File(getPath(getApplicationContext(), fileuri));

                // create RequestBody instance from file
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContentResolver().getType(fileuri)),
                                file
                        );

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData(key, file.getName(), requestFile);

                parts.add(body);
            }
        }


        // add another part within the multipart request

        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, API.getBodyConnexion(getApplicationContext()).toString());
        return parts;
        // finally, execute the request
        /*Call<Integer> call = API.getInstance().simpleService.upload(API.getBodyConnexion(getApplicationContext()), parts);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                API.launchShortToast(getApplicationContext(), "OK");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
               API.launchShortToast(getApplicationContext(), "KO");
            }

        });*/
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

    public static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id)
                );

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void createanimal(View view){

    }
}