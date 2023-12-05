package com.example.schoolmobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ConverterActivity extends AppCompatActivity {

    ArrayAdapter<CharSequence> baseAdapter;
    ArrayAdapter<CharSequence> megaByteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        EditText editDecimal = findViewById(R.id.editDecimal);
        TextView textResultDecimal = findViewById(R.id.textResultDecimal);
        Spinner spinnerDecimal = findViewById(R.id.spinnerDecimal);

        baseAdapter = new CustomArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                getResources().getTextArray(R.array.base_array)
        );
        baseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDecimal = findViewById(R.id.spinnerDecimal);
        spinnerDecimal.setAdapter(baseAdapter);

        Spinner spinnerMegaByte = findViewById(R.id.spinnerMegaByte);
        megaByteAdapter = new CustomArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                getResources().getTextArray(R.array.mega_byte_array)
        );
        megaByteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMegaByte.setAdapter(megaByteAdapter);

        RadioGroup radioGroupTempeture = findViewById(R.id.radioGroupTempeture);
        TextView textResultTempeture = findViewById(R.id.textResultTempeture);

        editDecimal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateResultForDecimal();
            }
        });

        spinnerDecimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateResultForDecimal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        EditText editMegaByte = findViewById(R.id.editMegaByte);

        editMegaByte.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateResultForMegaByte();
            }
        });

        spinnerMegaByte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateResultForMegaByte();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        radioGroupTempeture.setOnCheckedChangeListener((group, checkedId) -> {
            updateResultForTempeture();
        });

        EditText editCelcius = findViewById(R.id.editCelcius);
        editCelcius.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateResultForTempeture();
            }
        });
    }


        private void updateResultForDecimal(){
            EditText editDecimal = findViewById(R.id.editDecimal);
            TextView textResultDecimal = findViewById(R.id.textResultDecimal);
            Spinner spinnerDecimal = findViewById(R.id.spinnerDecimal);

            String decimalInput = editDecimal.getText().toString();

            if (isValidDecimal(decimalInput)){
                int selectedBasePosition = spinnerDecimal.getSelectedItemPosition();
                String resultInSelectedBase = convertDecimalToBase(decimalInput, selectedBasePosition);

                textResultDecimal.setText("Sonuç: (" + spinnerDecimal.getSelectedItem() + "): " + resultInSelectedBase);
            }

            else{
                showToast("Geçerli bir sayı giriniz.");
            }
        }

        private void updateResultForMegaByte(){
            EditText editMegaByte = findViewById(R.id.editMegaByte);
            TextView textResultByte = findViewById(R.id.textResultMegaByte);
            Spinner spinnerMegaByte = findViewById(R.id.spinnerMegaByte);

            String megaByteInput = editMegaByte.getText().toString();

            if (isValidDecimal(megaByteInput)){
                int selectedUnitPosition = spinnerMegaByte.getSelectedItemPosition();
                String resultInSelectedUnit = convertMegaByteToUnit(megaByteInput,selectedUnitPosition);

                textResultByte.setText("Sonuç: (" + spinnerMegaByte.getSelectedItem() + "): " + resultInSelectedUnit);
            }
            else{
                showToast("Geçerli bir sayı giriniz");
            }
        }

        private void updateResultForTempeture(){
            RadioGroup radioGroupTemperature = findViewById(R.id.radioGroupTempeture);
            TextView textResultTemperature = findViewById(R.id.textResultTempeture);
            EditText editCelcius = findViewById(R.id.editCelcius);

            int checkedId = radioGroupTemperature.getCheckedRadioButtonId();
            if (checkedId != -1) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedOption = selectedRadioButton.getText().toString();

                String celsiusInput = editCelcius.getText().toString();

                if (isValidDecimal(celsiusInput)) {
                    double celciusValue = Double.parseDouble(celsiusInput);

                    if (celciusValue >= -273) {
                        String resultInSelectedUnit = convertCelcius(celciusValue, selectedOption);

                        textResultTemperature.setText("Sonuç (" + selectedOption + "): " + resultInSelectedUnit);
                    } else {
                        showToast("Celcius değeri en az -273 olmalıdır");
                    }
                } else {
                    showToast("Geçerli bir sayı girin");
                }
            }
    }

    private String convertCelcius(double celciusValue, String selectedOption){
        switch (selectedOption){
            case "Fahrenheit":
                double fahrenheitValue = (celciusValue * 9 / 5) + 32;
                return String.valueOf(fahrenheitValue);

            case "Kelvin":
                double kelvinValue = celciusValue + 273.15;
                return String.valueOf(kelvinValue);
            default:
                return "Geçersiz Birim";
        }
    }

    private String convertDecimalToBase(String decimalInput, int base){
        try {
            int decimalValue = Integer.parseInt(decimalInput);
            switch (base){
                case 0:
                    return Integer.toBinaryString(decimalValue);
                case 1:
                    return Integer.toOctalString(decimalValue);
                case 2:
                    return Integer.toHexString(decimalValue);
                default:
                    return "Geçersiz Taban";
            }
        }
        catch (NumberFormatException e){
            return "Geçersiz Sayı";
        }
    }

    private String convertMegaByteToUnit(String megaByteInput, int unit){
        try{
            double megaByteValue = Double.parseDouble(megaByteInput);
            switch(unit) {
                case 0:
                    return String.valueOf(megaByteValue * 1024);
                case 1:
                    return String.valueOf(megaByteValue * 1024 * 1024);
                case 2:
                    return String.valueOf(megaByteValue * 1000);
                case 3:
                    return String.valueOf(megaByteValue * 1024 * 1024 * 8);
                default:
                    return "Geçersiz Birim";
            }
        }
        catch (NumberFormatException e) {
            return "Geçersiz sayı";
        }
    }

    private boolean isValidDecimal(String input){
        try {
            Double.parseDouble(input);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }

    }

    private void showToast(String message){
        Toast.makeText(ConverterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}