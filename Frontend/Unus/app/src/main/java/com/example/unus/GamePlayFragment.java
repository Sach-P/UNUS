package com.example.unus;

import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Fragment used for gameplay when a game has two players
 *
 * @author Isaac Blandin
 */
public class GamePlayFragment extends Fragment {

    //number of cards given to user at the beginning of the game
    final int INITIAL_CARDS = 5;

    int numCards;

    boolean gameOver = false;

    View view;

    double dpConversionFactor;

    Card topDiscard;
    LinearLayout discardPile;

    MainActivity mainActivity;

    boolean isHost = true;

    ArrayList<Integer> playerIds;
    HashMap<Integer, String> usernames;
    int turnIndex = 0;
    boolean directionReversed = false;

    TextView turnIndicator;
    LinearLayout hand;

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
            playerIds = bundle.getIntegerArrayList("ids");
            usernames = (HashMap<Integer, String>) bundle.getSerializable("usernames");
        } else {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
        }

        turnIndicator = view.findViewById(R.id.turn_indicator);
        discardPile = view.findViewById(R.id.discard_pile);
        hand = view.findViewById(R.id.hand_two);

        //create player display for other players
        for (int i: playerIds){
            if (i != UserData.getInstance().getUserID()){
                createPlayerDisp(i);
            }
        }

        //add all initial cards to user's hand
        for (int i = 0; i < INITIAL_CARDS; i++){
            Card draw = new Card(getContext());
            addCard(draw);
        }

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

        //create listener for drawing a card
        ImageView drawPile = view.findViewById(R.id.draw_pile);
        drawPile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerIds.get(turnIndex) == UserData.getInstance().getUserID()) {
                    Card draw = new Card(getContext());
                        addCard(draw);
                    JSONObject message = new JSONObject();
                    try {
                        message.put("id", UserData.getInstance().getUserID());
                        message.put("numCards", numCards);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mainActivity.sendMessage(message);
                }
            }
        });

        //create listener for leaving the game
        ImageView leaveGame = view.findViewById(R.id.game_menu);
        leaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.disconnectWebSocket();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
            }
        });


        return view;
    }

    /**
     * adds a card image into the players hand in the game UI
     *
     * @param card card object to be added to the UI
     */
    public void addCard(Card card){

        numCards++;

        //create the frame for the UI display
        ImageView plate = new ImageView(getContext());
        plate.setImageDrawable(card.getImage());
        plate.setLayoutParams(new LinearLayout.LayoutParams((int)(80 * dpConversionFactor), (int)(120 * dpConversionFactor)));
        plate.setAdjustViewBounds(true);
        //add listener for card. Moves card from hand to discard
        plate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.cardPlayable(topDiscard) && playerIds.get(turnIndex) == UserData.getInstance().getUserID()) {
                    plate.setVisibility(View.GONE);
                    try {
                        numCards--;
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
        message.put("numCards", numCards);

        if (numCards == 0){
            message.put("win", true);
        } else {
            message.put("win", false);
        }

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

        if (obj.has("card")){
            Card card = new Card(obj.getJSONObject("card"), getContext());

            if (obj.getInt("id") != UserData.getInstance().getUserID()){
                discard(card, false);
            }
            nextTurn();
            CardRank rank = card.getRank();
            if (rank == CardRank.SKIP){
                nextTurn();
            } else if (rank == CardRank.REVERSE){
                directionReversed = !directionReversed;
                nextTurn();
                nextTurn();
            } else if (playerIds.get(turnIndex) == UserData.getInstance().getUserID() && (rank == CardRank.DRAW_FOUR || rank == CardRank.DRAW_TWO)){
                int draws = 0;
                if (rank == CardRank.DRAW_FOUR){
                    draws = 4;
                } else if (rank == CardRank.DRAW_TWO){
                    draws = 2;
                }
                for (int i = 0; i < draws; i++){
                    Card draw = new Card(getContext());
                    addCard(draw);
                }

                JSONObject message = new JSONObject();
                try {
                    message.put("id", UserData.getInstance().getUserID());
                    message.put("numCards", numCards);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mainActivity.sendMessage(message);
            }
        }

        if (obj.has("numCards") && obj.getInt("id") != UserData.getInstance().getUserID()){
            TextView numCardDisp = view.findViewWithTag("numCards"+obj.getInt("id"));
            numCardDisp.setText(String.valueOf(obj.getInt("numCards")));
        }

        //end game if a player leaves
        if (obj.has("left") && !gameOver){
            mainActivity.disconnectWebSocket();

            MainMenuFragment frag = new MainMenuFragment();

            Bundle bundle = new Bundle();
            bundle.putBoolean("playerLeft", true);

            frag.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, frag).commit();
        }

        //create pop up if someone won
        if(obj.has("win") && obj.getBoolean("win")){

            gameOver = true;

            String body;
            if (obj.getInt("id") == UserData.getInstance().getUserID()) {
                body = "{\"win\":\"true\"}";
            } else {
                body = "{\"win\":\"true\"}";
            }

            String mRequestBody = body.toString();

            StringRequest sr = new StringRequest(Request.Method.PUT, getString(R.string.remote_server_url, "gameEnd", Integer.toString(UserData.getInstance().getUserID())), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return body.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }

            };
            Volley.newRequestQueue(requireContext()).add(sr);

            createWinPopup(obj.getInt("id"));
        }
    }

    /**
     * Changes the turn index to the next player in line
     */
    private void nextTurn(){
        //increment or decrement turnIndex
        if (directionReversed){
            turnIndex--;
            if (turnIndex < 0){
                turnIndex = playerIds.size() - 1;
            }
        } else {
            turnIndex = (turnIndex + 1) % playerIds.size();
        }

        //update turn display text
        if (playerIds.get(turnIndex) == UserData.getInstance().getUserID()){
            turnIndicator.setText(getString(R.string.your_turn));
        } else {
            turnIndicator.setText(getString(R.string.players_turn, usernames.get(playerIds.get(turnIndex))));
        }
    }

    /**
     * inflates popup for win
     *
     * @param id id of winner
     */
    private void createWinPopup(int id){
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.win_popup, null);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;


        //Create a window with our parameters
        PopupWindow popupWindow = new PopupWindow(popupView, (int)(dpConversionFactor * 400), (int)(dpConversionFactor * 400), focusable);
        popupWindow.setOutsideTouchable(false);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        String winner;
        if (id == UserData.getInstance().getUserID()){
            winner = "You Won!";
        } else {
            winner = usernames.get(id) + "\n Won";
        }

        TextView winnerDisp = popupView.findViewById(R.id.winner);
        winnerDisp.setText(winner);

        //set listener for menu button
        Button menu = popupView.findViewById(R.id.back_to_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.disconnectWebSocket();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
                popupWindow.dismiss();
            }
        });
    }

    /**
     * creates a display for the numeber of a users cards
     *
     * @param id player's id to create the display for
     */
    private void createPlayerDisp (int id){
        //create a relative layout so the text can be overlaid on the image
        RelativeLayout plate = new RelativeLayout(view.getContext());
        plate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //create image of a blank card
        ImageView background = new ImageView(view.getContext());
        background.setLayoutParams(new ViewGroup.LayoutParams((int)(dpConversionFactor * 120), (int)(dpConversionFactor * 150)));
        background.setImageDrawable(AppCompatResources.getDrawable(view.getContext(), R.drawable.ic_blank));
        plate.addView(background);

        //add textView for the number of cards
        TextView numberCards = new TextView(view.getContext());
        numberCards.setLayoutParams(new ViewGroup.LayoutParams((int)(dpConversionFactor * 120), (int)(dpConversionFactor * 150)));
        numberCards.setText("5");
        numberCards.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80);
        numberCards.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        numberCards.setTextColor(view.getContext().getResources().getColor(R.color.yellow));
        numberCards.setTag("numCards"+id);
        plate.addView(numberCards);

        //add textView for the username
        TextView username = new TextView(view.getContext());
        username.setLayoutParams(new ViewGroup.LayoutParams((int)(dpConversionFactor * 120), (int)(dpConversionFactor * 150)));
        username.setText(usernames.get(id));
        username.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        username.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        username.setTextColor(view.getContext().getResources().getColor(R.color.yellow));
        username.setPadding(0, (int)(dpConversionFactor * 100), 0, 0);
        plate.addView(username);

        //add display to the layout
        LinearLayout workspace = view.findViewById(R.id.player_view);
        workspace.addView(plate);
    }

}