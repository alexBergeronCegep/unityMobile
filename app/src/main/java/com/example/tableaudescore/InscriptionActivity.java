package com.example.tableaudescore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InscriptionActivity extends AppCompatActivity {

    EditText etUser, etPassword;
    TextView tvErreur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        etUser = findViewById(R.id.etUserInscrip);
        etPassword = findViewById(R.id.etPasswordInscrip);
        tvErreur = findViewById(R.id.tvErreur);
    }

    public void onClic(View view)
    {
        String regExUser = "^[a-zA-Z0-9_-]{3,15}$";

        CharSequence inputStrUser = etUser.getText().toString();

        Pattern patternUser = Pattern.compile(regExUser,Pattern.UNICODE_CASE);
        Matcher matcherUser = patternUser.matcher(inputStrUser);

        String regExPwd = "^[a-zA-Z0-9_\\-\\$\\#\\!\\*\\&\\?]{3,15}$";

        CharSequence inputStrPwd = etPassword.getText().toString();

        Pattern patternPwd = Pattern.compile(regExPwd,Pattern.UNICODE_CASE);
        Matcher matcherPwd = patternPwd.matcher(inputStrPwd);

        if (view.getId() == R.id.btValider) {
            if(!matcherUser.matches() && !matcherPwd.matches()) {
                tvErreur.setVisibility(View.VISIBLE);
                tvErreur.setText("le nom et le mot de passe doivent les caractères suivants: a à Z, 0 à 9, _ - et pour le mot de passe les caractères suivants: $ # ! * & ? entre 3 et 15 fois");
            }
            else if(!matcherUser.matches())
            {
                tvErreur.setVisibility(View.VISIBLE);
                tvErreur.setText("le nom doit contenir les caractères suivants: a à Z, 0 à 9, _ - entre 3 et 15 fois");
            } else if (!matcherPwd.matches()) {
                tvErreur.setVisibility(View.VISIBLE);
                tvErreur.setText("le mot de passe doit contenir les caractères suivants: a à Z, 0 à 9, _ - $ # ! * & ? entre 3 et 15 fois");
            } else {
                interfaceServeur interfaceServeur = RetrofitInstance.getInstance().create(interfaceServeur.class);
                Call<String> call = interfaceServeur.getInscription("inscription", etUser.getText().toString().trim(), etPassword.getText().toString().trim());

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body().equals("23000")) {
                            tvErreur.setVisibility(View.VISIBLE);
                            tvErreur.setText("ce nom d'utilisateur est déjà utilisé");
                        }
                        else {
                            setResult(RESULT_OK, null);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        String s = t.getMessage();
                        tvErreur.setVisibility(View.VISIBLE);
                        tvErreur.setText("une erreur est survenue");
                    }
                });
            }
        }
    }
}