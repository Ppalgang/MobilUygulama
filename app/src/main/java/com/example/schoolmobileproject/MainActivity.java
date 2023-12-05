package com.example.schoolmobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnConverter = findViewById(R.id.btnConverter);
        Button btnRandom = findViewById(R.id.btnRandom);
        Button btnSMS = findViewById(R.id.btnSMS);

        btnConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConverterActivity.class);

                startActivity(intent);
            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RandomGeneratorActivity.class);

                startActivity(intent);
            }
        });
        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SmsSenderActivity.class);

                startActivity(intent);
            }
        });
    }
}