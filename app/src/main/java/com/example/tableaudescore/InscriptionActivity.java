package com.example.tableaudescore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionActivity extends AppCompatActivity {

    EditText etUser, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        etUser = findViewById(R.id.etUserInscrip);
        etPassword = findViewById(R.id.etPasswordInscrip);
    }

    public void onClic(View view)
    {
        if (view.getId() == R.id.btValider) {
            if(etUser.getText().toString().trim().isEmpty() || etPassword.getText().toString().trim().isEmpty())
            {
                Toast.makeText(this, "le nom et le mot de passe sont obligatoire", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                interfaceServeur interfaceServeur = RetrofitInstance.getInstance().create(interfaceServeur.class);
                Call<String> call = interfaceServeur.getInscription("inscription", etUser.getText().toString().trim(), etPassword.getText().toString().trim());

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        setResult(RESULT_OK, null);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        String s = t.getMessage();
                        Toast.makeText(InscriptionActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}