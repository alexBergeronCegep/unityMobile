package com.example.tableaudescore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapterScoreBoard extends RecyclerView.Adapter<adapterScoreBoard.MonViewHolder>{

    private List<User> liste;


    public adapterScoreBoard(List<User> l)
    {
        liste = l;
    }

    @NonNull
    @Override
    public MonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.scoreboard, parent, false);
        return new MonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonViewHolder holder, int position) {
        holder.tvUser.setText(liste.get(position).getUser());
        holder.tvScore.setText(liste.get(position).heure());
    }

    @Override
    public int getItemCount() {
        return liste.size();
    }


    public class MonViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvUser, tvScore;
        public MonViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUser = itemView.findViewById(R.id.tvUser);
            tvScore = itemView.findViewById(R.id.tvScore);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }
}
