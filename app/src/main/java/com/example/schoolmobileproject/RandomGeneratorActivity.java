package com.example.schoolmobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class RandomGeneratorActivity extends AppCompatActivity {

    private LinearLayout scrollLayout;

    private EditText editTextAdet, editTextMin, editTextMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_generator);

        scrollLayout = findViewById(R.id.scrollLayout);
        editTextAdet = findViewById(R.id.editTextAdet);
        editTextMin = findViewById(R.id.editTextMin);
        editTextMax = findViewById(R.id.editTextMax);

        Button btnGenerate = findViewById(R.id.btnGenerate);

        addTextWatchers();

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adetStr = editTextAdet.getText().toString();
                if (!adetStr.isEmpty()) {
                    generateRandomValues(Integer.parseInt(adetStr));
                } else {
                    Toast.makeText(RandomGeneratorActivity.this, "Lütfen adet girin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (!editTextAdet.getText().toString().isEmpty() &&
                !editTextMin.getText().toString().isEmpty() &&
                !editTextMax.getText().toString().isEmpty()) {
            generateRandomValues(Integer.parseInt(editTextAdet.getText().toString()));
        }
    }


    private void addTextWatchers(){
        editTextAdet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                generateIfValuesAreNotEmpty();
            }
        });

        editTextMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                generateIfValuesAreNotEmpty();
            }
        });

        editTextMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                generateIfValuesAreNotEmpty();
            }
        });
    }

    private void generateIfValuesAreNotEmpty(){
        String adetStr = editTextAdet.getText().toString();
        String minStr = editTextMin.getText().toString();
        String maxStr = editTextMax.getText().toString();

        if (!adetStr.isEmpty() && !minStr.isEmpty() && !maxStr.isEmpty()){
            int min = Integer.parseInt(minStr);
            int max = Integer.parseInt(maxStr);

            if (max >= min){
                generateRandomValues(Integer.parseInt(adetStr));
            }
            else{
                Toast.makeText(RandomGeneratorActivity.this, "Max değer min değerden küçük olamaz", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generateRandomValues(int adet) {
        scrollLayout.removeAllViews();

        int minInput = Integer.parseInt(editTextMin.getText().toString());
        int maxInput = Integer.parseInt(editTextMax.getText().toString());

        if (adet <= 0){
            Toast.makeText(RandomGeneratorActivity.this, "Geçersiz adet değeri", Toast.LENGTH_SHORT).show();
            return;
        }

        if (maxInput <= minInput){
            Toast.makeText(RandomGeneratorActivity.this, "Max değer min değerden küçük veya eşit olamaz", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < adet; i++){
            int newMin = generateRandomNumber(minInput, maxInput -1);

            int newMax = generateRandomNumber(newMin + 1, maxInput);

            int selectedValue = generateRandomNumber(newMin, newMax);

            int progressBarMax = newMax - newMin;
            int progressBarValue = selectedValue - newMin;

            addRandomValueComponent(newMin, newMax, selectedValue, progressBarMax, progressBarValue);
        }
    }

    private int generateRandomNumber(int min, int max){
        if (max < min){
            throw new IllegalArgumentException("Max değer min değerden küçük olamaz");
        }

        Random random = new Random();

        int randomValue = random.nextInt((max-min) + 1) + min;

        return randomValue;
    }

    private  void addRandomValueComponent(int min, int max, int selectedValue, int progressBarMax, int progressBarValue){
        View itemView = LayoutInflater.from(this).inflate(R.layout.random_value_item, scrollLayout, false);
        TextView textMin = itemView.findViewById(R.id.textMin);
        TextView textMax = itemView.findViewById(R.id.textMax);
        ProgressBar progressBar = itemView.findViewById(R.id.progressBar);
        TextView textSelectedValue = itemView.findViewById(R.id.textSelectedValue);

        textMin.setText(String.valueOf(min));
        textMax.setText(String.valueOf(max));
        progressBar.setMax(progressBarMax);
        progressBar.setProgress(progressBarValue);
        textSelectedValue.setText(selectedValue + " = %" + calculatePercentage(min, max, selectedValue));

        scrollLayout.addView(itemView);
    }

    private int calculatePercentage(int min, int max, int selectedValue) {
        return ((selectedValue - min) * 100) / (max - min);
    }
}