package com.example.exoli.a4inarow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.exoli.a4inarow.classes.Game;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameFragment extends Fragment {

    private View gameView;
    private RecyclerView gameList;

    private DatabaseReference gamesRef;
    private FirebaseAuth auth;
    private String currentUserEmail;

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         gameView = inflater.inflate(R.layout.fragment_game, container, false);

         gameList = (RecyclerView) gameView.findViewById(R.id.rv_game);
         gameList.setLayoutManager(new LinearLayoutManager(getContext()));

         auth = FirebaseAuth.getInstance();
         currentUserEmail = auth.getCurrentUser().getEmail();
         gamesRef = FirebaseDatabase.getInstance().getReference().child("games");

         return gameView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Game>()
                .setQuery(gamesRef, Game.class)
                .build();

        FirebaseRecyclerAdapter<Game, GameViewHolder> adapter = new FirebaseRecyclerAdapter<Game, GameViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final GameViewHolder holder, int position, @NonNull Game model) {
                String game = getRef(position).getKey();

                gamesRef.child(game).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String winner = dataSnapshot.child("winner").getValue().toString();
                        String loser = dataSnapshot.child("loser").getValue().toString();

                        holder.txt_winner.setText(winner);
                        holder.txt_loser.setText(loser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_display_layout, parent, false);
                 GameViewHolder gameViewHolder = new GameViewHolder(view);
                 return gameViewHolder;
            }
        };
        gameList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {

        TextView txt_winner;
        TextView txt_loser;

        public GameViewHolder(View itemView) {
            super(itemView);

            txt_winner = itemView.findViewById(R.id.txt_winner);
            txt_loser = itemView.findViewById(R.id.txt_loser);
        }
    }
}
