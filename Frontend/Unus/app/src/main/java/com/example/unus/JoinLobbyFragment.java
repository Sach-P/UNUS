package com.example.unus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class JoinLobbyFragment extends Fragment {

    View view;

    public JoinLobbyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_join_lobby, container, false);

        EditText editText = view.findViewById(R.id.lobby_code);

        Button button = view.findViewById(R.id.join_lobby_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameLobbyFragment frag = new GameLobbyFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("lobbyId", Integer.parseInt(editText.getText().toString()));
                bundle.putBoolean("isHost", false);

                frag.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, frag, "gameLobby").commit();
                getActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });

        return view;
    }


}