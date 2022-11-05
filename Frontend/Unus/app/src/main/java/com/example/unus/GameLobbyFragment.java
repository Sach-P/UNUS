package com.example.unus;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Space;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Fragment used to handle the game lobby menu
 */
public class GameLobbyFragment extends Fragment {

    View view;
    int[] players;


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

        String gameCode = "A6Y42";
        int[] playerIds = {24, 21, 22, 23};

        addPlayer(UserData.getInstance().getUserID());

        for (int i = 0; i < playerIds.length; i++){
            if (playerIds[i] != UserData.getInstance().getUserID()){
                addPlayer(playerIds[i]);
            }
        }

        TextView playerCountDisp = view.findViewById(R.id.player_count);
        playerCountDisp.setText(getString(R.string.player_count, playerIds.length));

        TextView gameCodeDisp = view.findViewById(R.id.game_code);
        gameCodeDisp.setText(getString(R.string.game_code, gameCode));

        return view;
    }

    //create the UI display of a player in the lobby
    private void addPlayerPlate(int playerID, String username){
        //create horizontal linear layout
        LinearLayout plate = new LinearLayout(view.getContext());
        plate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        plate.setOrientation(LinearLayout.HORIZONTAL);
        //create grey box behind plate
        plate.setPadding(30,10,30,10);
        plate.setBackgroundColor(getResources().getColor(R.color.dark_gray));

        //add username display to plate
        TextView usernameText = new TextView(view.getContext());
        usernameText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        usernameText.setText(username);
        usernameText.setTextColor(getResources().getColor(R.color.white));
        usernameText.setTextSize(20);
        usernameText.setGravity(Gravity.CENTER_VERTICAL);
        plate.addView(usernameText);

        //add spacing
        Space space = new Space(view.getContext());
        space.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        plate.addView(space);

        //add view profile button to the plate
        Button viewUser = new Button(view.getContext());
        viewUser.setLayoutParams(new ViewGroup.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
        viewUser.setTextColor(getResources().getColor(R.color.yellow ));
        viewUser.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_500)));
        viewUser.setText("view");
        viewUser.setTextSize(12);
        viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPopup(playerID);
            }
        });
        plate.addView(viewUser);

        if (playerID != UserData.getInstance().getUserID()){
            //add kick player button to the plate
            Button kickUser = new Button(view.getContext());
            kickUser.setLayoutParams(new ViewGroup.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
            kickUser.setTextColor(getResources().getColor(R.color.yellow ));
            kickUser.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_500)));
            kickUser.setText("kick");
            kickUser.setTextSize(12);
            kickUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add kick user function
                }
            });
            plate.addView(kickUser);
        }

        //add plate to screen
        LinearLayout playerDisp = view.findViewById(R.id.player_display);
        playerDisp.addView(plate);
        //add space between plates
        Space boxSpacing = new Space(view.getContext());
        boxSpacing.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
        playerDisp.addView(boxSpacing);
    }

    public void addPlayer(int id){

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                "http://coms-309-029.class.las.iastate.edu:8080/user/"+Integer.toString(id),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            addPlayerPlate(response.getInt("id"), response.getString("username"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        Volley.newRequestQueue(requireContext()).add(request);
    }

    public void userPopup(int id) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.profile_view_layout, null);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1000, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 50, 30);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.remote_server_url, "user", Integer.toString(id)),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            ((TextView) popupView.findViewById(R.id.username)).setText(response.getString("username"));
                            ((TextView) popupView.findViewById(R.id.user_id)).setText("ID: "+ response.getInt("id"));
                            ((TextView) popupView.findViewById(R.id.games_played)).setText("Games Played: "+ response.getInt("gamesPlayed"));
                            ((TextView) popupView.findViewById(R.id.games_won)).setText("Games Won: "+ response.getInt("gamesWon"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //loginHeader.setText(getString(R.string.login_error));
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);

    }
}