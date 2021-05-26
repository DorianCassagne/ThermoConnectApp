package me.dcal.thermoconnectapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.mikephil.charting.charts.LineChart;


import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import me.dcal.thermoconnectapp.Modeles.BodyAnimal;
import me.dcal.thermoconnectapp.Services.API;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class FileActivity extends AppCompatActivity  implements Serializable {
    PDFView pdfView;
    Button download;
    Button returnbtn;
    InputStream data ;
    File outputDir;
    File outputFile;
    String docname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        BodyAnimal bodyanimal  = (BodyAnimal) getIntent().getSerializableExtra("bodyanimal");
        docname = bodyanimal.getDocuments().get(0);


        try {
            outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer
            outputFile = File.createTempFile(docname.split("\\.")[0], docname.split("\\.")[1], outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfView =  findViewById(R.id.pdfview);
        download = (Button) findViewById(R.id.download);
        returnbtn = (Button) findViewById(R.id.returnbtn);
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        loadDoc(bodyanimal);

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

                    pdfView.fromStream(data)
                            .defaultPage(1)
                            .enableSwipe(true)
                            .load();

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
        File file = new File(path, docname + ".png");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        IOUtils.write(String.valueOf(data), fileOutputStream);
    }



}
