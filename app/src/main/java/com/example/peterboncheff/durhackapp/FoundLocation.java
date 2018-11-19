package com.example.peterboncheff.durhackapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.ModelVersion;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Prediction;
import okhttp3.OkHttpClient;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class FoundLocation extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private String webpage;
    private final String API_KEY = "d7af1c69e1f8442893d4611c34c109b4";

    private final ClarifaiClient client = new ClarifaiBuilder(API_KEY).buildSync();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_location);
        init();
        clarifaiBuild();
        getFromPrevIntent();

        Log.d(MainActivity.TAG, "webpage : " +  webpage);
        this.textView.setText(getInfo());
    }

    private void init(){
        File img = new File("C:\\Users\\Peter Boncheff\\IntelliJIDEAProjects\\DurhackApp\\app\\src\\main\\java\\com\\example\\peterboncheff\\durhackapp");
        if(img.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
            this.imageView.setImageBitmap(bitmap);
        }
        this.textView = findViewById(R.id.info);

    }

    private void getFromPrevIntent(){
        Bundle bundle = getIntent().getExtras();
        //if(bundle == null) this.webpage = null;
        //else this.webpage = bundle.getString(MainActivity.URI_NAME);

    }
    private void clarifaiBuild(){
        new ClarifaiBuilder(API_KEY)
                .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                .buildSync(); // or use .build() to get a Future<ClarifaiClient>
    }

    private void clarifaiImage(String image){
//        client.getDefaultModels().generalModel().predict()
//                .withInputs(ClarifaiInput.forImage(image))
//                .executeSync();
    }

    public String getInfo(){
        ConceptModel model = client.getDefaultModels().generalModel();
        ModelVersion modelVersion = model.getVersionByID("the-version").executeSync().get();

//        ClarifaiResponse<List<ClarifaiOutput<Prediction>>> response = client.predict(model.id())
//                .withInputs(ClarifaiInput.forImage("https://samples.clarifai.com/metro-north.jpg"))
//                .withVersion("aa7f35c01e0642fda5cf400f543e7c40")
//                .executeSync();
        //this.webpage
        return "asdasda";
    }
}
