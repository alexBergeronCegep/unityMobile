package com.example.tableaudescore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvListe;

    adapterScoreBoard adapterScoreBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rvListe = findViewById(R.id.rvScore);
        rvListe.setHasFixedSize(true);
        // Liste en horizontale/verticale         Enlever la partie apres this pour que la liste soit verticale
        rvListe.setLayoutManager(new LinearLayoutManager(this));

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
}