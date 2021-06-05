package me.dcal.thermoconnectapp;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;

import me.dcal.thermoconnectapp.Modeles.BodyAnimal;
import me.dcal.thermoconnectapp.Services.API;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class FileActivity extends AppCompatActivity  implements Serializable {
    PDFView pdfView;
    FloatingActionButton download;
    FloatingActionButton returnbtn;
    InputStream data ;
    InputStream save;
    File outputDir;
    File outputFile;
    String docname;
    ImageView docimg;
    FloatingActionButton deleteDocument;
    BodyAnimal bodyanimal;
    ByteArrayOutputStream baos;

    private static final int REQUEST_WRITE_PERMISSION = 786;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        bodyanimal  = (BodyAnimal) getIntent().getSerializableExtra("bodyanimal");
        docname = bodyanimal.getDocuments().get(0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        pdfView =  findViewById(R.id.pdfview);
        download = (FloatingActionButton) findViewById(R.id.download);
        returnbtn = (FloatingActionButton) findViewById(R.id.returnbtn);
        docimg = (ImageView)findViewById(R.id.docimg);
        deleteDocument = (FloatingActionButton) findViewById(R.id.delete);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                save = new ByteArrayInputStream(baos.toByteArray());
                //IOUtils.write(String.valueOf(save), fileOutputStream);

                try (FileOutputStream fileOS = new FileOutputStream(path+"/"+docname)) {
                    byte data[] = new byte[1024];
                    int byteContent;
                    while ((byteContent = save.read(data, 0, 1024)) != -1) {
                        fileOS.write(data, 0, byteContent);
                    }
                    Toast toast = Toast.makeText(getApplicationContext(), "Téléchargement effectué", Toast.LENGTH_SHORT);
                    toast.show();
                } catch (IOException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Erreur de téléchargement", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        deleteDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FileActivity.this);
                builder.setMessage("Suppression du document ")//R.string.confirm_dialog_message
                        // .setTitle("Suppression de la fiche de " + bodyanimal.getName())//R.string.confirm_dialog_title
                        .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() { //R.string.confirm
                            public void onClick(DialogInterface dialog, int id) {
                                bodyanimal.setDocuments(Collections.singletonList(docname));
                                Call<Integer> retour = API.getInstance().simpleService.deleteDocument(bodyanimal);
                                retour.enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        if (response.body()>=0){
                                            Intent i=new Intent(getApplicationContext(), AnimalActivity.class);
                                            i.putExtra("deletefile", docname);
                                            setResult(-1, i);
                                            finish();
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

        loadDoc(bodyanimal);

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

    public void loadDoc(BodyAnimal bd){
        Call<ResponseBody> reponse= API.getInstance().simpleService.getFile(bd);
        reponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast toast = Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT);
                toast.show();
                try {
                    data = response.body().byteStream();

                    baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = data.read(buffer)) > -1 ) {
                        baos.write(buffer, 0, len);
                    }
                    baos.flush();

                    if (docname.endsWith(".pdf")){
                        try{
                            pdfView.setVisibility(View.VISIBLE);
                            docimg.setVisibility(View.GONE);
                            pdfView.fromStream(new ByteArrayInputStream(baos.toByteArray()))
                                    .defaultPage(1)
                                    .enableSwipe(true)
                                    .load();
                        }catch (Exception e){
                            AlertDialog.Builder builder = new AlertDialog.Builder(FileActivity.this);
                            builder.setMessage("Fichier corrompu")//R.string.confirm_dialog_message
                                    .setTitle("Erreur lors du la lecture du document")//R.string.confirm_dialog_title
                                    .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() { //R.string.confirm
                                        public void onClick(DialogInterface dialog, int id) {

                                            bodyanimal.setDocuments(Collections.singletonList(docname));
                                            Call<Integer> retour = API.getInstance().simpleService.deleteDocument(bodyanimal);
                                            retour.enqueue(new Callback<Integer>() {
                                                @Override
                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                    if (response.body()>=0){
                                                        Intent i=new Intent(getApplicationContext(), AnimalActivity.class);
                                                        i.putExtra("deletefile", docname);
                                                        setResult(-1, i);
                                                        finish();
                                                    }else{
                                                        Toast toasts = Toast.makeText(getApplicationContext(), "Erreur durant la mise à jour veuillez essayer plus tard", Toast.LENGTH_SHORT);
                                                        finish();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Integer> call, Throwable t) {
                                                    System.out.println("KO");
                                                }
                                            });
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

                    }else { //if(docname.endsWith(".jpg"))
                        pdfView.setVisibility(View.GONE);
                        docimg.setVisibility(View.VISIBLE);
                        Bitmap docpic = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));
                        docimg.setImageBitmap(docpic);

                    }
                    

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

    public void dowloadDocument(View v) throws IOException {

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        save = new ByteArrayInputStream(baos.toByteArray());
        //IOUtils.write(String.valueOf(save), fileOutputStream);

        try (FileOutputStream fileOS = new FileOutputStream(path+"/"+docname)) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = save.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            // handles IO exceptions
        }
    }

}
