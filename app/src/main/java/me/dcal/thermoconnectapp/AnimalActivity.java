package me.dcal.thermoconnectapp;

import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirection;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.dcal.thermoconnectapp.Modeles.BodyAnimal;
import me.dcal.thermoconnectapp.Modeles.BodyAnimalData;
import me.dcal.thermoconnectapp.Modeles.BodyDocument;
import me.dcal.thermoconnectapp.Modeles.TerraListData;
import me.dcal.thermoconnectapp.Services.API;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AnimalActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private final String TAG = "TextEditor";
    private TextView mTextEditor;

    private List<Uri> UriTabDoc = new ArrayList<>();
    private List<Uri> UriTabImage = new ArrayList<>();
    private HashMap<String, List<Uri>> UriTab = new HashMap<>();
    private Uri finalimage;
    private ArrayList<Entry> dataGraphWeight = new ArrayList<Entry>();
    ListView addedfiles;
    ArrayAdapter<String> arrayAdapter;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_LOAD_DOCUMENT = 2;
    private static int RESULT_DELETE_DOCUMENT = 3;
    private static int RESULT_ALIMENTATION = 4;

    private static final int REQUEST_WRITE_PERMISSION = 786;
    BodyAnimal bodyanimal;
    Boolean type;
    LineChart pieChart;
    TextView newWeight;
    TextView name;
    TextView sexe;
    TextView descriptionauto;
    ImageView imgView;
    TextView descriptionperso;
    TextView changedescription;
    Button save_button;
    TextView dateofBirth;
    Spinner changeSexe;
    ImageView animalimage;
    Button buttonLoadFile;
    List<String> initFileList;
    TextView species;
    SwipeActionAdapter mAdapter;
    Button suivialimentaire;
    Bitmap defaultPic;
    Bitmap newPic = null;
    Boolean picChange = false;
    Boolean deleteImage = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);

        newWeight = (TextView) findViewById(R.id.nouveaupoids);
        addedfiles = (ListView) findViewById(R.id.docview);
        name = (TextView) findViewById(R.id.name);
        sexe = (TextView) findViewById(R.id.sexe);
        descriptionauto = (TextView) findViewById(R.id.descriptionauto);
        imgView = (ImageView) findViewById(R.id.animalimage);
        descriptionperso = (TextView) findViewById(R.id.descriptionperso);
        changedescription = (TextView) findViewById(R.id.changedescription);
        dateofBirth = (TextView) findViewById(R.id.naissance);
        changeSexe = (Spinner) findViewById(R.id.changesexe);
        animalimage = (ImageView) findViewById(R.id.animalimage);
        pieChart = (LineChart) findViewById(R.id.barchart);
        save_button = (Button) findViewById(R.id.save);
        buttonLoadFile = (Button) findViewById(R.id.addfiles);
        species = (TextView) findViewById(R.id.species);
        suivialimentaire = (Button) findViewById(R.id.suivialimentaire);

        this.bodyanimal  = (BodyAnimal) getIntent().getSerializableExtra("Animal");
        initFileList = this.bodyanimal.getDocuments();
        this.bodyanimal.setBodyConnexion(API.getBodyConnexion(getApplicationContext()));

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        name.setText(this.bodyanimal.getName());
        try{
            sexe.setText((this.bodyanimal.getSex() == true ? "Male" : this.bodyanimal.getSex() == false ? "Femelle" : "NC"));
        }catch (NullPointerException e){
            sexe.setText("NC");
        }

        descriptionauto.setText(this.bodyanimal.getSpecies().getDescription());
        dateofBirth.setText(this.bodyanimal.getDateOfBirth());
        species.setText(this.bodyanimal.getSpecies().toString());

        descriptionperso.setText(this.bodyanimal.getDescription());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSexe();
        loadimage();
        setchart();
        doclist(this.bodyanimal.getDocuments());

        suivialimentaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlimentationActivity.class);
                intent.putExtra("bodyanimal", bodyanimal);
                startActivityForResult(intent, RESULT_ALIMENTATION);
            }
        });

        XAxis xAxis = pieChart.getXAxis();
        xAxis.setGranularity(1);
        xAxis.setValueFormatter(new ValueFormatter(){
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yy", Locale.FRANCE);
            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        KeyboardVisibilityEvent.setEventListener( this,new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean b) {
                if (changedescription.getVisibility()==View.VISIBLE && !isKeyboardShown(changedescription.getRootView())) {
                    descriptionperso.setText(changedescription.getText().toString());
                    changedescription.setVisibility(View.GONE);
                    descriptionperso.setVisibility(View.VISIBLE);

                    verification();
                }
            }
        });

        addedfiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List<String> doc = new ArrayList<String>();
                doc.add(parent.getItemAtPosition(position).toString());
                bodyanimal.setDocuments(doc);
                Intent intent = new Intent(getApplicationContext(), FileActivity.class);
                intent.putExtra("bodyanimal", bodyanimal);
                startActivityForResult(intent, RESULT_DELETE_DOCUMENT);
                //loadDocument((String)parent.getItemAtPosition(position));
            }
        });



        animalimage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AnimalActivity.this);
                builder.setMessage("Modification de la photo "+bodyanimal.getName()+"")//R.string.confirm_dialog_message
                       // .setTitle("Suppression de la fiche de " + bodyanimal.getName())//R.string.confirm_dialog_title
                        .setPositiveButton("Changer l'image", new DialogInterface.OnClickListener() { //R.string.confirm
                            public void onClick(DialogInterface dialog, int id) {
                                type = true;
                                requestPermission();
                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {//R.string.cancel
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();




            }
        });
       changeSexe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                sexe.setText(parentView.getItemAtPosition(position).toString());

                sexe.setVisibility(View.VISIBLE);
                changeSexe.setVisibility(View.GONE);
                verification();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //updateInfoVue(v);
                if (!changedescription.getText().toString().isEmpty() && !(descriptionperso.getText().equals(changedescription.getText()))){
                    bodyanimal.setDescription(changedescription.getText().toString());
                }

                Boolean sex;
                if (picChange){
                    if (finalimage != null){
                        UriTabImage.add(finalimage);
                        UriTab.put("picture", UriTabImage);
                        defaultPic = newPic;
                    }
                }


                if (changeSexe.getSelectedItem().toString().equals("Male")){
                    sex = true;
                }else if (changeSexe.getSelectedItem().toString().equals("Femelle")){
                    sex = false;
                }else{
                    sex = null;
                }

                List<MultipartBody.Part> part = new ArrayList<>();

                if (UriTab.size() > 0){
                    part = uploadFile(UriTab);
                }

                bodyanimal.setSex(sex);
                Call<Integer> retour = API.getInstance().simpleService.modifAnimal(bodyanimal,part);
                retour.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if (response.body()>=0){
                            save_button.setVisibility(View.GONE);
                        }else{
                            Toast toasts = Toast.makeText(getApplicationContext(), "Erreur durant la mise à jour veuillez essayer plus tard", Toast.LENGTH_SHORT);
                            toasts.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        System.out.println("KO");
                    }
                });
            }
        });



        buttonLoadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                type = false;
                requestPermission();

            }
        });
    }

    private boolean isKeyboardShown(View rootView) {
        /* 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft keyboard */
        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128;

        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        /* heightDiff = rootView height - status bar height (r.top) - visible frame height (r.bottom - r.top) */
        int heightDiff = rootView.getBottom() - r.bottom;
        /* Threshold size: dp to pixels, multiply with display density */
        boolean isKeyboardShown = heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD * dm.density;

        Log.d(TAG, "isKeyboardShown ? " + isKeyboardShown + ", heightDiff:" + heightDiff + ", density:" + dm.density
                + "root view height:" + rootView.getHeight() + ", rect:" + r);

        return isKeyboardShown;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.deconnexion:
                API.setBodyConnexion(getApplicationContext(),null);
                Intent i=new Intent(this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement

    }

    public void loadimage(){
        Call<ResponseBody> reponse= API.getInstance().simpleService.getImage(this.bodyanimal);
        reponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    defaultPic = BitmapFactory.decodeStream(response.body().byteStream());
                    imgView.setImageBitmap(defaultPic);

                    //imgView.setImageBitmap(this.bodyanimal.get());
                }
                catch (Exception ex){
                    Toast toasts = Toast.makeText(getApplicationContext(), "Erreur durant le chargement de l'image", Toast.LENGTH_SHORT);
                    toasts.show();
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
    public Boolean imagesAreEqual(Bitmap i1, Bitmap i2)
    {
        if (i1.getHeight() != i2.getHeight()) return false;
        if (i1.getWidth() != i2.getWidth()) return false;

        for (int y = 0; y < i1.getHeight(); ++y)
            for (int x = 0; x < i1.getWidth(); ++x)
                if (i1.getPixel(x, y) != i2.getPixel(x, y)) return false;

        return true;
    }

    public void modifDescription(View v){
        changedescription.setText(descriptionperso.getText().toString());
        descriptionperso.setVisibility(View.GONE);
        changedescription.setVisibility(View.VISIBLE);
    }


    public void setSexe(){String[] arraySpinner = new String[] {
            "Male", "Femelle", "NC"
    };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        changeSexe.setAdapter(adapter);

    }

    public void modifSexe(View v){
        sexe.setVisibility(View.GONE);
        changeSexe.setVisibility(View.VISIBLE);
    }


    public void verification(){
        Boolean sex;

        if (changeSexe.getSelectedItem().toString().equals("Male")){
            sex = true;
        }else if (changeSexe.getSelectedItem().toString().equals("Femelle")){
            sex = false;
        }else{
            sex = null;
        }


        if (newPic != null){
            picChange = !newPic.sameAs(defaultPic);
            //newpic = imagesAreEqual(newPic,defaultPic);
        }

        if( deleteImage || picChange || !(descriptionperso.getText().toString().equals(this.bodyanimal.getDescription())) || (sex == null ? sex != this.bodyanimal.getSex() : !(sex.equals(this.bodyanimal.getSex())) )) {

            save_button.setVisibility(View.VISIBLE);
        }
        else{

            save_button.setVisibility(View.INVISIBLE);
        }

    }


    public void addWeight(View v){

        Double weight = Double.parseDouble(newWeight.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date())+" 00:00:00.0";

        BodyAnimalData body = new BodyAnimalData(this.bodyanimal.getBodyConnexion(),this.bodyanimal.getIdAnimal(), currentDateandTime,weight);
        ArrayList<Entry> data = new ArrayList<Entry>();

        Call<Integer> reponse= API.getInstance().simpleService.setAllAnimalData(body);
        reponse.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                long now = TimeUnit.MILLISECONDS.toHours( timestampToFloat(Timestamp.valueOf(currentDateandTime)));
                //dataGraphWeight.add(new Entry(now, Float.parseFloat(newWeight.getText().toString())));
                setchart();
               // pieChart.setData();
                Toast toast = Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                call.request().url();
                Toast toast = Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    public void doclist(List<String> listdocs){
        //DocumentAdapter<String> arrayAdapter = new DocumentAdapter<String>(this, R.layout.custom_added_files);
        arrayAdapter.clear();
        if (listdocs.size()==0){
            addedfiles.setVisibility(View.GONE);
        }else{
            addedfiles.setVisibility(View.VISIBLE);
        }
        for (String doc : listdocs){
            arrayAdapter.add(doc);
        }

        if (arrayAdapter.getCount()>0){
            View item = arrayAdapter.getView(0, null, addedfiles);
            item.measure(0, 0);
            int nbitem = arrayAdapter.getCount()>3 ? 3 : arrayAdapter.getCount();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (nbitem * item.getMeasuredHeight()));
            addedfiles.setLayoutParams(params);
        }


        addedfiles.setAdapter(arrayAdapter);
    }


    public void  openFilePicker(){
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
                startActivityForResult(intent, RESULT_LOAD_DOCUMENT);
            } catch (ActivityNotFoundException e){
                Toast.makeText(this, "There are no file explorer clients installed.", Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_DOCUMENT) { // for file selection
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

                bodyanimal.addDocument(tv1.getName());
                doclist(bodyanimal.getDocuments());

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


            }else if (requestCode == RESULT_DELETE_DOCUMENT){
                bodyanimal.setDocuments(initFileList);
                List<String> doc = bodyanimal.getDocuments();
                doc.remove(data.getStringExtra("deletefile"));

                bodyanimal.setDocuments(doc);
                doclist(bodyanimal.getDocuments());

            } else if (requestCode == RESULT_ALIMENTATION){
                bodyanimal.setFood(data.getStringExtra("alimentation"));
            }else{// for image selection
                try {
                    Uri imageUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    animalimage.setImageBitmap(selectedImage);
                    finalimage = imageUri;
                    newPic = selectedImage;
                    verification();

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
                String folder = uri.getAuthority().split("\\.",4)[3];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory().getPath() + "/" + split[1];
                }
                if ("home".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory().getPath() + "/" + folder +"/"+split[1];
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

            if (value == null){

            }else{
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


    public void deleteAnimal(View v){
       // updateInfoVue(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(AnimalActivity.this);
        builder.setMessage("Voulez vous vraiment supprimer la fiche de "+bodyanimal.getName()+" ?\n Ceci entrainera la suppression de toutes les données.")//R.string.confirm_dialog_message
                .setTitle("Suppression de la fiche de " + bodyanimal.getName())//R.string.confirm_dialog_title
                .setPositiveButton("Confimer", new DialogInterface.OnClickListener() { //R.string.confirm
                    public void onClick(DialogInterface dialog, int id) {
                        Call<Integer> retour = API.getInstance().simpleService.deleteAnimal(bodyanimal);
                        retour.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {//R.string.cancel
                    public void onClick(DialogInterface dialog, int id) {
                        // CANCEL
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();


    }

    public void setchart(){


        dataGraphWeight.clear();

        Call<List<BodyAnimalData>> data = API.getInstance().simpleService.getAllAnimalData(this.bodyanimal);
        data.enqueue(new Callback<List<BodyAnimalData>>() {
            @Override
            public void onResponse(Call<List<BodyAnimalData>> call, Response<List<BodyAnimalData>> response) {

                List<BodyAnimalData> listdata = response.body();
                Collections.sort(listdata);
                int i = 1;

                for(BodyAnimalData bd : listdata){

                    String time = bd.getDateAnimalData()+" 00:00:00.0";
                    long now = TimeUnit.MILLISECONDS.toHours( timestampToFloat(Timestamp.valueOf(time)));
                    dataGraphWeight.add(new Entry(now, Float.parseFloat(bd.getWeight().toString())));
                }

                if (dataGraphWeight.size() != 0){
                    LineDataSet datasetWeight= new LineDataSet(dataGraphWeight, "Evolution du poids(g)");
                    datasetWeight.setColor(Color.BLUE);
                    LineData data = new LineData(datasetWeight);
                    pieChart.setVisibility(View.VISIBLE);
                    pieChart.setData(data);
                    pieChart.animateXY(0, 0);
                }else{
                    pieChart.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<BodyAnimalData>> call, Throwable t) {
                API.launchShortToast(getApplicationContext(), "KO");
            }
        });
    }

    public long timestampToFloat(Timestamp tm){
        long time =  tm.getTime();
        return time;
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


}