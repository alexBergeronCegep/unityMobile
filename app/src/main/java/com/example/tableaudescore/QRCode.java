package com.example.tableaudescore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;

public class QRCode extends AppCompatActivity {

    Button scanBtn;

    int id;
    String user;
    Mqtt3AsyncClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        id = getIntent().getIntExtra("id",-1);
        user = getIntent().getStringExtra("user");
        if (id == -1) {
            setResult(RESULT_CANCELED);
            finish();
        }

        client = MqttClient.builder()
                .useMqttVersion3()
                .identifier("android")
                .serverHost("172.16.207.246")
                .serverPort(1883)
                .buildAsync();

        scanBtn = findViewById(R.id.scanBtn);
    }

    public void click(View view) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null)
        {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else
            {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                client.connectWith()
                        .simpleAuth()
                        .username("test")
                        .password("test".getBytes())
                        .applySimpleAuth()
                        .send()
                        .whenComplete((connAck, throwable) -> {
                            Log.d("test", "test");
                            if (throwable != null) {
                                Log.d("test", "error connection " + throwable.getMessage());
                            } else {
                                Log.d("test", "je me connection");
                                String idString = Integer.toString(id);
                                // setup subscribes or start publishing
                                client.publishWith()
                                        .topic(intentResult.getContents())
                                        .payload(idString.getBytes())
                                        .send()
                                        .whenComplete((publish, throwable2) -> {
                                            if (throwable2 != null) {
                                                Log.d("test", "publish error");
                                            } else {
                                                Log.d("test", "publish ok");
                                            }
                                        });
                                Intent intent = new Intent();
                                intent.putExtra("id", id);
                                intent.putExtra("user", user);
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        });
            }
        } else
        {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d("test", "autre");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}