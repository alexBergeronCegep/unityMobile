package com.example.tableaudescore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnexionActivity extends AppCompatActivity {

    EditText etUser, etPassword;

    CheckBox souvenir;

    ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        etUser = findViewById(R.id.etUserCon);
        etPassword = findViewById(R.id.etPasswordCon);
        souvenir = findViewById(R.id.souvenir);


        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Toast.makeText(ConnexionActivity.this, "inscription accomplie avec succès", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onClick(View view)
    {
        if(view.getId() == R.id.btInscription)
        {
            Intent intent = new Intent(this, InscriptionActivity.class);
            resultLauncher.launch(intent);
        }
        else if (view.getId() == R.id.btConnexion)
        {
            interfaceServeur interfaceServeur = RetrofitInstance.getInstance().create(interfaceServeur.class);
            Call<String> call = interfaceServeur.getConnexion("connexion", etUser.getText().toString(), etPassword.getText().toString());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body() == "false")
                    {
                        Toast.makeText(ConnexionActivity.this, "votre nom d'utilisateur ou votre mot de passe est invalide", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(souvenir.isChecked())
                        {
                            SharedPreferences pref = getSharedPreferences("connection",MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("id", Integer.parseInt(response.body()));
                            editor.putString("user", etUser.getText().toString());
                            editor.putBoolean("isConnected", true);
                            editor.commit();
                            Intent intent = new Intent();
                            intent.putExtra("id", Integer.parseInt(response.body()));
                            intent.putExtra("user", etUser.getText().toString());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        else
                        {
                            SharedPreferences pref = getSharedPreferences("connection",MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putInt("id", -1);
                            editor.putString("user", "");
                            editor.putBoolean("isConnected", false);
                            editor.commit();
                            Intent intent = new Intent();
                            intent.putExtra("id", Integer.parseInt(response.body()));
                            intent.putExtra("user", etUser.getText().toString());
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    String s = t.getMessage();
                    Toast.makeText(ConnexionActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}