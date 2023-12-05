package com.example.schoolmobileproject;

import static com.example.schoolmobileproject.R.layout.activity_sms_sender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SmsSenderActivity extends AppCompatActivity {

    private EditText editTextTel, editTextMessage;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_sms_sender);

        editTextTel = findViewById(R.id.editTextTel);
        editTextMessage = findViewById(R.id.editTextMessage);
        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetAvailable()){
                    new GetRandomSentenceTask().execute();
                }
                else {
                    Toast.makeText(SmsSenderActivity.this, "Internet bağlantısı yok", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private  boolean isInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void sendSms(String sentence){
        String phoneNumber = editTextTel.getText().toString();
        String message = editTextMessage.getText().toString() + " " + sentence;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else{
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
                    sendSmsWithSimSelection(message, phoneNumber);
                }
                else {
                    sendSmsDefault(message,phoneNumber);
                }
            }
            catch (Exception e){
                Toast.makeText(this, "SMS gönderilemedi", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new GetRandomSentenceTask().execute();
                }
                else {
                    Toast.makeText(this, "Telefon durumu izni verilmedi", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class GetRandomSentenceTask extends  AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect("https://kur.doviz.com/serbest-piyasa/amerikan-dolari").get();
                Elements elements = doc.select("div.text-xl.font-semibold.text-white");

                if (elements.size() > 0) {
                    return "\nDolar: " + elements.first().text();
                }
                else {
                    return null;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String sentence){
            if (sentence != null){
                sendSms(sentence);
            }
            else {
                String customMessage = editTextMessage.getText().toString();
                sendSms(customMessage);
                Toast.makeText(SmsSenderActivity.this, "Cümle alınamadı, girilen metin gönderildi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSmsWithSimSelection(String message, String phoneNumber) {
        final ArrayList<Integer> simCardList = new ArrayList<>();
        SubscriptionManager subscriptionManager = SubscriptionManager.from(SmsSenderActivity.this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            return;
        }

        final List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        if (subscriptionInfoList != null) {
            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                int subscriptionId = subscriptionInfo.getSubscriptionId();
                simCardList.add(subscriptionId);
            }

            if (!simCardList.isEmpty()){
                int smsToSendFrom = simCardList.get(0);
                SmsManager.getSmsManagerForSubscriptionId(smsToSendFrom).sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(SmsSenderActivity.this, "SMS gönderildi", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(SmsSenderActivity.this, "SIM kart bulunamadı", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(SmsSenderActivity.this, "Aktif SIM kart bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSmsDefault(String message, String phoneNumber) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(SmsSenderActivity.this, "SMS gönderildi", Toast.LENGTH_SHORT).show();
    }
}