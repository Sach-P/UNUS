package com.example.unus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class HandFragment extends Fragment {

    View view;

    LinearLayout hand;
    LinearLayout discard;

    public HandFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_hand, container, false);

        hand = view.findViewById(R.id.cards);
        discard = view.findViewById(R.id.discard);

        Button draw = view.findViewById(R.id.draw);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCard();
            }
        });

        return view;
    }

    private void createCard(){
        LinearLayout card = new LinearLayout(view.getContext());
        card.setLayoutParams(new LinearLayout.LayoutParams(200, 360));
        card.setPadding(10,10,10,10);
        Card card1 = new Card();
        CardColor color = card1.getColor();
        switch (color){
            case WILD:
                card.setBackgroundColor(getResources().getColor(R.color.dark_gray ));
                break;
            case YELLOW:
                card.setBackgroundColor(getResources().getColor(R.color.yellow));
                break;
            case GREEN:
                card.setBackgroundColor(getResources().getColor(R.color.teal_200 ));
                break;
            case RED:
                card.setBackgroundColor(getResources().getColor(R.color.red ));
                break;
            case BLUE:
                card.setBackgroundColor(getResources().getColor(R.color.teal_700 ));
                break;
        }

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hand.removeView(card);
                discard.removeAllViews();
                discard.addView(card);
            }
        });
        hand.addView(card);
    }
}