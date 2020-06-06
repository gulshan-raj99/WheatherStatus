package com.example.wheatherstatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView resultTextView;
    public void findWeather(View view) {
        Log.i("cityName", cityName.getText().toString());

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);


        DownloadTask task =new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString());


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName= (EditText)findViewById(R.id.cityName);
        resultTextView=(TextView)findViewById(R.id.resultTextView);


    }
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result= "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url= new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in =urlConnection.getInputStream();
                InputStreamReader reader =new InputStreamReader(in);
                int data = reader.read();
                while (data!= -1) {
                    char current =(char) data;
                    result += current;
                    data= reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                String message = "";
                JSONObject jsonObject = new JSONObject(result);
                String WheatherInfo= jsonObject.getString("Wheather");
                Log.i("website content ", WheatherInfo);
                JSONArray arr = new JSONArray(WheatherInfo);
                for(int i=0; i<arr.length();i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main = "";
                    String description = "";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if(main != "" && description != "") {
                        message += main + ": " + description + "\r\n";

                    }

                }
                if(message != "") {
                    resultTextView.setText(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
