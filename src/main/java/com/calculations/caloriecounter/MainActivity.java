package com.calculations.caloriecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    EditText editText;
    EditText Qnt;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.getCalories);
        editText = findViewById(R.id.input_food);
        textView = findViewById(R.id.textView);
        Qnt = findViewById(R.id.quantity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(new getCalorieValue().execute().getStatus().toString());
            }
        });
    }

    private class getCalorieValue extends AsyncTask<Void , Void , Void> {
        float output;
        String input;
        float quantity;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            input = editText.getText().toString();
            quantity = Integer.parseInt(Qnt.getText().toString());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "https://www.google.com/search?q=%s+calories";
                String product = input;
                String URL = String.format(url, product);
                Document document = Jsoup.connect(URL).get();
                Elements cals = document.select("div[aria-live]");
                ArrayList<String>calories = new ArrayList<>();
                for(Element i : cals) {
                    calories.add(i.text());
                }
//                output = calories.get(1);
                String inTEXT = calories.get(1);
                inTEXT = inTEXT.replace(" calories" , "");

                float inNumRes = Float.parseFloat(inTEXT);
                inNumRes = inNumRes / 100;
                inNumRes = inNumRes * quantity;
                output = inNumRes;

            }
            catch (IOException e) {
                Toast.makeText(MainActivity.this , e.toString() , Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            textView.setText("number of calories : " + output + " cal");
        }
    }
}