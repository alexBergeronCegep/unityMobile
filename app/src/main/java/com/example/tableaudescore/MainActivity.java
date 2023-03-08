package com.example.tableaudescore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.GetChars;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    int id = -1;
    String user = "";
    int score = 63000;
    RecyclerView rvListe;

    TextView tvScore;
    MenuItem menuConnexion , menuDeconnexion, menuQR;

    adapterScoreBoard adapterScoreBoard;

    ActivityResultLauncher<Intent> resultLauncher;

    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
        {
            id = savedInstanceState.getInt("id");
            user = savedInstanceState.getString("user");
            score = savedInstanceState.getInt("score");
        }

        tvScore = findViewById(R.id.tvOwnScore);

        SharedPreferences pref = getSharedPreferences("connection", MODE_PRIVATE);
        Boolean b = pref.getBoolean("isConnected", false);
        if(b)
        {
            id = pref.getInt("id", -1);
            user = pref.getString("user", "");
            getSupportActionBar().setTitle("utilisateur " + user);
        }
        else
        {
            getSupportActionBar().setTitle("utilisateur non connecté");
        }


        rvListe = findViewById(R.id.rvScore);
        rvListe.setHasFixedSize(true);
        // Liste en horizontale/verticale         Enlever la partie apres this pour que la liste soit verticale
        rvListe.setLayoutManager(new LinearLayoutManager(this));


        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            id = result.getData().getIntExtra("id", 0);
                            user = result.getData().getStringExtra("user");
                            getSupportActionBar().setTitle("utilisateur " + user);
                            ownScore();
                        }
                    }
                });
        if(id != -1)
        {
            ownScore();
        }

        interfaceServeur interfaceServeur = RetrofitInstance.getInstance().create(interfaceServeur.class);
        Call<List<User>> call = interfaceServeur.getUsers("getUsers");

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> l = response.body();
                adapterScoreBoard = new adapterScoreBoard(l);
                rvListe.setAdapter(adapterScoreBoard);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                String s = t.getMessage();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menuConnexion = menu.findItem(R.id.menuConnexion);
        menuDeconnexion = menu.findItem(R.id.idDeconnexion);
        menuQR = menu.findItem(R.id.scan);
        if(id != -1)
        {
            menuConnexion.setVisible(false);
            menuDeconnexion.setVisible(true);
            menuQR.setVisible(true);
        }
        else
        {
            menuConnexion.setVisible(true);
            menuDeconnexion.setVisible(false);
            menuQR.setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuConnexion) {
            menuConnexion.setVisible(false);
            menuDeconnexion.setVisible(true);
            menuQR.setVisible(true);
            Intent intent = new Intent(this, ConnexionActivity.class);
            resultLauncher.launch(intent);
        } else if (item.getItemId() == R.id.idDeconnexion) {
            SharedPreferences pref = getSharedPreferences("connection",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("id", -1);
            editor.putString("user", "");
            editor.putBoolean("isConnected", false);
            editor.commit();
            id = -1;
            user = "";
            getSupportActionBar().setTitle("utilisateur non connecté");
            menuConnexion.setVisible(true);
            menuDeconnexion.setVisible(false);
            menuQR.setVisible(false);
        }
        else if(item.getItemId() == R.id.scan)
        {
            Intent intent = new Intent(this, QRCode.class);
            intent.putExtra("id", id);
            resultLauncher.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void ownScore()
    {
        interfaceServeur interfaceServeur2 = RetrofitInstance.getInstance().create(interfaceServeur.class);
        Call<String> call2 = interfaceServeur2.getScore("getScore", id);

        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s = response.body();
                score = Integer.parseInt(s);
                int heure = score / 3600;
                int min = score % 3600 / 60;
                tvScore.setText(heure + "h " + min + "m ");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String s = t.getMessage();
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("id", id);
        savedInstanceState.putString("user", user);
        savedInstanceState.putInt("score", score);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed () {
        Toast.makeText(this, "Glisser à nouveau pour quitter", Toast.LENGTH_SHORT).show();
        if(doubleBackToExitPressedOnce)
        {
            finishAndRemoveTask();
            return;
        }
        this.doubleBackToExitPressedOnce = true;

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}