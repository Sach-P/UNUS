package com.example.unus;

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

/**
 * Fragment used for gameplay when a game has two players
 *
 * @author Isaac Blandin
 */
public class GamePlayFragment extends Fragment {

    final int NUM_PLAYERS = 2;

    int readyPlayers = 0;

    //number of cards given to user at the beginning of the game
    final int INITIAL_CARDS = 5;

    View view;

    ArrayList<Card> cards;

    double dpConversionFactor;

    Card topDiscard;
    LinearLayout discardPile;

    MainActivity mainActivity;

    boolean isHost = true;

    boolean yourTurn;



    public GamePlayFragment() {
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
        view = inflater.inflate(R.layout.fragment_game_play, container, false);

        //get factor needed to make UI sizing dynamic
        dpConversionFactor = getContext().getResources().getDisplayMetrics().density;

        mainActivity = (MainActivity)getActivity();

        //detect if user is host based on arguments passed into fragment on creation
        if (getArguments() != null){
            Bundle bundle = getArguments();
            isHost = bundle.getBoolean("isHost");
        }


        discardPile = view.findViewById(R.id.discard_pile);

        //draw first card if you are the host
        if (isHost) {
            Card firstCard;
            do {
                firstCard = new Card(getContext());
            } while (firstCard.getColor() == CardColor.WILD);

            try {
                discard(firstCard, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            topDiscard = firstCard;
        }

        //add all initial cards to user's hand
        cards = new ArrayList<Card>();
        for (int i = 0; i < INITIAL_CARDS; i++){
            Card draw = new Card(getContext());
            addCard(draw, cards.size());
            cards.add(draw);
        }

        //create listener for drawing a card
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

    /**
     * adds a card image into the players hand in the game UI
     *
     * @param card card object to be added to the UI
     * @param index index of the card in the ArrayList
     */
    public void addCard(Card card, int index){

        LinearLayout hand = view.findViewById(R.id.hand_two);

        //create the frame for the UI display
        ImageView plate = new ImageView(getContext());
        plate.setImageDrawable(card.getImage());
        plate.setLayoutParams(new LinearLayout.LayoutParams((int)(80 * dpConversionFactor), (int)(120 * dpConversionFactor)));
        plate.setAdjustViewBounds(true);
        //add listener for card. Moves card from hand to discard
        plate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.cardPlayable(topDiscard)) {
                    plate.setVisibility(View.GONE);
                    try {
                        discard(card, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //add the new card to the hand UI
        hand.addView(plate);

    }

    /**
     * Moves the card from the player's hand to the discard pile
     *
     * @param card card object to be discarded
     */
    private void discard(Card card, boolean send) throws JSONException {

        //create new image view using the card's drawable
        ImageView plate = new ImageView(getContext());
        plate.setImageDrawable(card.getImage());
        plate.setLayoutParams(new LinearLayout.LayoutParams((int)(120 * dpConversionFactor), (int)(180 * dpConversionFactor)));
        plate.setAdjustViewBounds(true);

        //create a popup to choose the new color if a wild card is played
        if (card.getColor() == CardColor.WILD){
            createColorPopup(card.getRank());
        } else {
            if (send){
                sendCard(card);
            }
        }

        //replace the card on the discard pile
        topDiscard = card;
        discardPile.removeAllViews();
        discardPile.addView(plate);
    }

    /**
     * Creates a popup view to select the color after playing a wild card
     *
     * @param rank which type of wild card was played
     */
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
                try {
                    setWild(rank,CardColor.YELLOW);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                popupWindow.dismiss();
            }
        });

        Button red = popupView.findViewById(R.id.red_button);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setWild(rank,CardColor.RED);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                popupWindow.dismiss();
            }
        });

        Button purple = popupView.findViewById(R.id.purple_button);
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setWild(rank,CardColor.PURPLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                popupWindow.dismiss();
            }
        });

        Button green = popupView.findViewById(R.id.green_button);
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setWild(rank,CardColor.GREEN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                popupWindow.dismiss();
            }
        });
    }

    /**
     * create a colored wild card and place on the discard pile
     *
     * @param rank which wild card is being played
     * @param color color that is selected
     */
    private void setWild(CardRank rank, CardColor color) throws JSONException {
        Card card = new Card(rank, color, getContext());
        sendCard(card);
        discard(card, true);
    }

    /**
     * sends a jsonObject message to the websocket with a card object
     *
     * @param card
     * @throws JSONException
     */
    private void sendCard(Card card) throws JSONException {
        JSONObject message = new JSONObject();
        message.put("id", UserData.getInstance().getUserID());
        message.put("card", card.toJsonObject());

        mainActivity.sendMessage(message);
    }

    /**
     * Takes in string (should be from web socket) and makes changes accordingly
     *
     * @param s string from the websocket message
     * @throws JSONException
     */
    public void onMessage(String s) throws JSONException {
        JSONObject obj = new JSONObject(s);
        if (obj.getInt("id") != UserData.getInstance().getUserID() && obj.has("id")){
            if (obj.has("card")){
                Card card = new Card(obj.getJSONObject("card"), getContext());
                discard(card, false);
            }
        }
    }

}