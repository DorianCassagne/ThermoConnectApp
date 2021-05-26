package me.dcal.thermoconnectapp;

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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dcal.thermoconnectapp.Modeles.BodyAnimal;
import me.dcal.thermoconnectapp.Modeles.BodyDocument;
import me.dcal.thermoconnectapp.Services.API;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AnimalActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    LineChart pieChart;
    TextView newWeight;
    private List<Uri> UriTabDoc = new ArrayList<>();
    private List<Uri> UriTabImage = new ArrayList<>();
    private HashMap<String, List<Uri>> UriTab = new HashMap<>();
    private Uri finalimage;
    ListView addedfiles;
    ArrayAdapter<String> arrayAdapter;
    private static final int REQUEST_WRITE_PERMISSION = 786;
    BodyAnimal bodyanimal;

    TextView name;
    TextView sexe;
    TextView descriptionauto;
    TextView naissance;
    ImageView imgView;
    TextView descriptionperso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        pieChart = (LineChart) findViewById(R.id.barchart);
        newWeight = (TextView) findViewById(R.id.nouveaupoids);
        addedfiles = (ListView) findViewById(R.id.docview);

        this.bodyanimal  = (BodyAnimal) getIntent().getSerializableExtra("Animal");
        this.bodyanimal.setBodyConnexion(API.getBodyConnexion(getApplicationContext()));

        addedfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List<String> doc = new ArrayList<String>();
                doc.add(parent.getItemAtPosition(position).toString());
                bodyanimal.setDocuments(doc);
                Intent intent = new Intent(getApplicationContext(), FileActivity.class);
                intent.putExtra("bodyanimal", bodyanimal);
                startActivity(intent);
                //loadDocument((String)parent.getItemAtPosition(position));

            }
        });
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);


        name = (TextView) findViewById(R.id.name);
        sexe = (TextView) findViewById(R.id.sexe);
        descriptionauto = (TextView) findViewById(R.id.descriptionauto);
        naissance = (TextView) findViewById(R.id.naissance);
        imgView = (ImageView) findViewById(R.id.animalimage);
        descriptionperso = (TextView) findViewById(R.id.descriptionperso);

        name.setText(this.bodyanimal.getName());
        sexe.setText((this.bodyanimal.getSex() == true ? "Male" : this.bodyanimal.getSex() == false ? "Femelle" : "NC"));
        descriptionauto.setText(this.bodyanimal.getSpecies().getDescription());
        naissance.setText(this.bodyanimal.getDateOfBirth());


        descriptionperso.setText(this.bodyanimal.getDescription());

        ArrayList NoOfEmp = new ArrayList();
        //loadDocument(null);
        loadimage();

        doclist(this.bodyanimal.getDocuments());

        NoOfEmp.add(new Entry(945f, 450));
        NoOfEmp.add(new Entry(1040f, 510));
        NoOfEmp.add(new Entry(1133f, 520));
        NoOfEmp.add(new Entry(1240f, 600));
        NoOfEmp.add(new Entry(1369f, 615));
        NoOfEmp.add(new Entry(1487f, 689));
        NoOfEmp.add(new Entry(1501f, 750));

        LineDataSet dataSet = new LineDataSet(NoOfEmp, "Evolution du poids");

        LineData data = new LineData(dataSet);

        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);


        Button buttonLoadFile = (Button) findViewById(R.id.addfiles);
        buttonLoadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                requestPermission();

            }
        });
    }

    public void loadimage(){
        Call<ResponseBody> reponse= API.getInstance().simpleService.getImage(this.bodyanimal);
        reponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast toast = Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT);
                toast.show();
                try {

                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    imgView.setImageBitmap(bitmap);
                    Toast ff = Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT);
                    ff.show();

                    //imgView.setImageBitmap(this.bodyanimal.get());
                }
                catch (Exception ex){
                    Toast toasts = Toast.makeText(getApplicationContext(), "KO", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.request().url();
                Toast toast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }


    public void addWeight(View v){

    }

    public void doclist(List<String> listdocs){
        for (String doc : listdocs){
            arrayAdapter.add(doc);
            addedfiles.setAdapter(arrayAdapter);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();//do your job
        }
    }

    public void  requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions( new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            openFilePicker();//do your job
        }
    }

    public void  openFilePicker(){

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

                BodyDocument tv1 = null;
                try {
                    tv1 = new BodyDocument(0, name);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                arrayAdapter.add(tv1.getName());
                addedfiles.setAdapter(arrayAdapter);
                UriTabDoc.clear();
                UriTabDoc.add(fileuri);

                UriTab.clear();
                UriTab.put("files", UriTabDoc);
                List<MultipartBody.Part> part = uploadFile(UriTab);
                Call<Integer> reponse= API.getInstance().simpleService.upload(bodyanimal, part);
                reponse.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {

                        try {


                            Toast toast = Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT);
                            toast.show();
                            //imgView.setImageBitmap(this.bodyanimal.get());
                        }
                        catch (Exception ex){
                            Toast toasts = Toast.makeText(getApplicationContext(), "KO", Toast.LENGTH_SHORT);
                            toasts.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        call.request().url();
                        Toast toast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });


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

}