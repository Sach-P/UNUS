package com.example.unus;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameTwoPlayerFragment extends Fragment {

    final int INITIAL_CARDS = 5;

    View view;

    ArrayList<Card> cards;

    double dpConversionFactor;

    Card topDiscard;

    LinearLayout discardPile;

    MainActivity mainActivity;

    boolean isHost;

    public GameTwoPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_game_two_player, container, false);

        dpConversionFactor = getContext().getResources().getDisplayMetrics().density;

        mainActivity = (MainActivity)getActivity();

        if (getArguments() != null){
            Bundle bundle = getArguments();
            isHost = bundle.getBoolean("isHost");
        }

        discardPile = view.findViewById(R.id.discard_pile);

        if (isHost) {
            Card firstCard = new Card(getContext());
            discard(firstCard);
            topDiscard = firstCard;
        }

        cards = new ArrayList<Card>();

        for (int i = 0; i < INITIAL_CARDS; i++){
            Card draw = new Card(getContext());
            addCard(draw, cards.size());
            cards.add(draw);
        }

        ImageView drawPile = view.findViewById(R.id.draw_pile);
        drawPile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card draw = new Card(getContext());
                addCard(draw, cards.size());
                cards.add(draw);
            }
        });


        return view;
    }

    public void addCard(Card card, int index){
        LinearLayout hand = view.findViewById(R.id.hand_two);

        ImageView plate = new ImageView(getContext());
        plate.setImageDrawable(card.getImage());
        plate.setLayoutParams(new LinearLayout.LayoutParams((int)(80 * dpConversionFactor), (int)(120 * dpConversionFactor)));
        plate.setAdjustViewBounds(true);

        plate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.cardPlayable(topDiscard)) {
                    plate.setVisibility(View.GONE);
                    discard(cards.get(index));
                }
            }
        });

        hand.addView(plate);

    }

    public void discard(Card card){

        ImageView plate = new ImageView(getContext());
        plate.setImageDrawable(card.getImage());
        plate.setLayoutParams(new LinearLayout.LayoutParams((int)(120 * dpConversionFactor), (int)(180 * dpConversionFactor)));
        plate.setAdjustViewBounds(true);

        if (card.getColor() == CardColor.WILD){
            createColorPopup(card.getRank());
        }

        topDiscard = card;
        discardPile.removeAllViews();
        discardPile.addView(plate);
    }

    private void createColorPopup(CardRank rank){

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.color_popup, null);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1000, focusable);
        popupWindow.setOutsideTouchable(false);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //set up buttons
        Button yellow = popupView.findViewById(R.id.yellow_button);
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWild(rank,CardColor.YELLOW);
                popupWindow.dismiss();
            }
        });

        Button red = popupView.findViewById(R.id.red_button);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWild(rank,CardColor.RED);
                popupWindow.dismiss();
            }
        });

        Button purple = popupView.findViewById(R.id.purple_button);
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWild(rank,CardColor.PURPLE);
                popupWindow.dismiss();
            }
        });

        Button green = popupView.findViewById(R.id.green_button);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWild(rank,CardColor.GREEN);
                popupWindow.dismiss();
            }
        });
    }

    private void setWild(CardRank rank, CardColor color){
        discard(new Card(rank, color, getContext()));
    }

    public void onMessage(String s) throws JSONException {
        JSONObject obj = new JSONObject(s);
        if (obj.getInt("id") != UserData.getInstance().getUserID() && obj.has("id")){
            CardColor color;
            CardRank rank;
        }
    }

}