package dev.leho.covidcases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView total_cases, deaths, recovered, tests;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        total_cases = findViewById(R.id.total_cases);
        deaths = findViewById(R.id.deaths);
        recovered = findViewById(R.id.recovered);
        progressBar = findViewById(R.id.progressBar);
        tests = findViewById(R.id.tests);
        AndroidNetworking.initialize(getApplicationContext());
        getStats();
    }

    private static String formatInt(int number) {
        String pattern="#,###";
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }

    public void refreshdata(View v) {
        getStats();
        Toast.makeText(getApplicationContext(), "Refreshed!", Toast.LENGTH_SHORT).show();
    }

    public void getStats() {
        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.get("https://corona.lmao.ninja/v2/all")
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response.toString());
                            total_cases.setText("😷 Total Cases: " + formatInt(jsonResponse.getInt("cases")));
                            deaths.setText("💀 Deaths: " + formatInt(jsonResponse.getInt("deaths")));
                            recovered.setText("🏥 Recovered: " + formatInt(jsonResponse.getInt("recovered")));
                            tests.setText("🧪 Tests: " + formatInt(jsonResponse.getInt("tests")));
                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            Log.e("stats", "error parsing json");
                            e.printStackTrace();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("stats", anError.getMessage());
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }


}