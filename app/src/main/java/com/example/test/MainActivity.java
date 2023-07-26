package com.example.test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    Button btExchange, btClear;
    TextView exchangeText, currencyText;
    EditText inputTextLeft;
    Spinner spinnerLeft, spinnerRight;
    String left = "", right = "";
    float currencyExchange = 1;
    ArrayList<CharSequence> countries = new ArrayList<>();
    ArrayAdapter<CharSequence> adapter;

    Hashtable<String, Currency> table = new Hashtable<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Runnable runnable = () -> {
            synchronized (this) {
                try {
                    Document document = Jsoup.connect("https://tw.stock.yahoo.com/currency-converter").get();
                    int length = document.select("li[class=List(n)]").size();
                    Elements element = document.select("li[class=List(n)]");
                    table.put("台幣", new Currency("台幣", "1", "1", "1", "1"));
                    countries.add("台幣");
                    for (int i = 0; i < length; i++) {
                        String[] info = element.get(i).text().split(" ");
                        Currency currency = new Currency(info[0], info[4], info[5], info[6], info[7]);
                        table.put(info[0], currency);
                        countries.add(info[0]);
                        System.out.println(currency);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> {
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        btExchange = findViewById(R.id.btExchange);
        btClear = findViewById(R.id.btClear);
        inputTextLeft = findViewById(R.id.inputTextLeft);
        exchangeText = findViewById(R.id.exchangeText);
        currencyText = findViewById(R.id.currencyText);
        spinnerLeft = findViewById(R.id.currencyLeft);
        spinnerRight = findViewById(R.id.currencyRight);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeft.setAdapter(adapter);
        spinnerRight.setAdapter(adapter);
        btExchange.setOnClickListener( (view) ->{
            int temp = spinnerLeft.getSelectedItemPosition();
            spinnerLeft.setSelection(spinnerRight.getSelectedItemPosition(), false);
            spinnerRight.setSelection(temp, false);
        });
        btClear.setOnClickListener( (view) -> inputTextLeft.setText(""));
        inputTextLeft.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float num;
                try {
                    num = Float.parseFloat(inputTextLeft.getText().toString());
                } catch (NumberFormatException ex){
                    exchangeText.setText("");
                    return;
                }
                exchangeText.setText(String.valueOf(num * currencyExchange));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spinnerLeft.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                left = adapterView.getSelectedItem().toString();
                if (table.containsKey(left) && table.containsKey(right)) {
                    currencyExchange = table.get(left).rate / table.get(right).rate;
                }
                currencyText.setText(String.valueOf(currencyExchange));

                float num;
                try {
                    num = Float.parseFloat(inputTextLeft.getText().toString());
                } catch (NumberFormatException ex){
                    exchangeText.setText("");
                    return;
                }
                exchangeText.setText(String.valueOf(num * currencyExchange));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerRight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                right = adapterView.getSelectedItem().toString();
                if (table.containsKey(left) && table.containsKey(right)) {
                    currencyExchange = table.get(left).rate / table.get(right).rate;
                }
                currencyText.setText(String.valueOf(currencyExchange));

                float num;
                try {
                    num = Float.parseFloat(inputTextLeft.getText().toString());
                } catch (NumberFormatException ex){
                    exchangeText.setText("");
                    return;
                }
                exchangeText.setText(String.valueOf(num * currencyExchange));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}