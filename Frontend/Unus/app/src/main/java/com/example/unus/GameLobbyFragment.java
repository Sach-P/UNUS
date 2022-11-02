package com.example.unus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Fragment used to handle the game lobby menu
 */
public class GameLobbyFragment extends Fragment {

    View view;

    public GameLobbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_game_lobby, container, false);



        return view;
    }

    private void addPlayerPlate(int playerID){
        //create horizontal linear layout
        LinearLayout plate = new LinearLayout(view.getContext());
        plate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        plate.setOrientation(LinearLayout.HORIZONTAL);
        plate.setPadding(30,10,30,30);
        plate.setBackgroundColor(getResources().getColor(R.color.dark_gray));


    }
}