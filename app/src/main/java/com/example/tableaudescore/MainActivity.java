package com.example.tableaudescore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.GetChars;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    int id = -1;
    String user = "";
    RecyclerView rvListe;

    adapterScoreBoard adapterScoreBoard;

    ActivityResultLauncher<Intent> resultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        }
                    }
                });

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
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuConnexion) {
            Intent intent = new Intent(this, ConnexionActivity.class);
            resultLauncher.launch(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}