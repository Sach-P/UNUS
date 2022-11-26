package com.example.unus;

import android.content.res.ColorStateList;
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
import android.widget.Space;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Fragment used to handle the pre-game lobby
 *
 * @author Isaac Blandin
 */
public class GameLobbyFragment extends Fragment {

    View view;
    ArrayList<Integer> playerIds;
    int gameLobbyId;

    TextView playerCountDisp;

    boolean isHost;
    boolean prefill = false;

    MainActivity mainActivity;

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

        if (this.getArguments() != null){
            Bundle bundle = this.getArguments();
            gameLobbyId = bundle.getInt("lobbyId");
            isHost = bundle.getBoolean("isHost");
        } else {
            gameLobbyId = -1;
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
        }

        //declare a main activity to call the web socket
        mainActivity = (MainActivity)getActivity();
        mainActivity.connectWebSocket(gameLobbyId);

        playerIds = new ArrayList<Integer>();
        playerIds.add(UserData.getInstance().getUserID());

        addPlayer(UserData.getInstance().getUserID());

        for (int i = 0; i < playerIds.size(); i++){
            if (playerIds.get(i) != UserData.getInstance().getUserID()){
                addPlayer(playerIds.get(i));
            }
        }

        playerCountDisp = view.findViewById(R.id.player_count);
        playerCountDisp.setText(getString(R.string.player_count, playerIds.size()));

        TextView gameCodeDisp = view.findViewById(R.id.game_code);
        gameCodeDisp.setText(getString(R.string.game_code, Integer.toString(gameLobbyId)));

        //add listener to leave button
        ImageView leaveButton = (ImageView) view.findViewById(R.id.leave_lobby);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveGame();
            }
        });

        //add listener to chat button
        View chatButton = view.findViewById(R.id.lobby_chat);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatLayout cl = new ChatLayout(view);
            }
        });

        //add listener to start game button
        Button startButton = view.findViewById(R.id.start_ready_game_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new GamePlayFragment(), "gameScreen").commit();
            }
        });


        return view;
    }

    /**
     * Helper method for addPlayer() that adds a display plate for a user into the game lobby UI which displays the username, a view profile button, and a kick button if you are the host.
     *
     * @param playerID id number of the player to be added to the UI
     * @param username username of the player to be aded to the UI
     */
    private void addPlayerPlate(int playerID, String username){
        //create horizontal linear layout
        LinearLayout plate = new LinearLayout(view.getContext());
        plate.setTag("plate"+playerID);
        plate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        plate.setOrientation(LinearLayout.HORIZONTAL);
        //create grey box behind plate
        plate.setPadding(30,10,30,10);
        plate.setBackgroundColor(view.getContext().getResources().getColor(R.color.dark_gray));

        //add username display to plate
        TextView usernameText = new TextView(view.getContext());
        usernameText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        usernameText.setText(username);
        usernameText.setTextColor(view.getContext().getResources().getColor(R.color.white));
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
        viewUser.setTextColor(view.getContext().getResources().getColor(R.color.yellow ));
        viewUser.setBackgroundTintList(ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.purple_500)));
        viewUser.setText("view");
        viewUser.setTextSize(12);
        viewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPopup(playerID);
            }
        });
        plate.addView(viewUser);

        //add spacing
        Space boxSpacing = new Space(view.getContext());
        boxSpacing.setTag("space"+playerID);

        //add kick button if host
        if (playerID != UserData.getInstance().getUserID() && isHost){
            //add kick player button to the plate
            Button kickUser = new Button(view.getContext());
            kickUser.setLayoutParams(new ViewGroup.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT));
            kickUser.setTextColor(view.getContext().getResources().getColor(R.color.yellow ));
            kickUser.setBackgroundTintList(ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.purple_500)));
            kickUser.setText("kick");
            kickUser.setTextSize(12);
            kickUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        kickPlayer(plate, boxSpacing, playerID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            plate.addView(kickUser);
        }

        //add plate to screen
        LinearLayout playerDisp = view.findViewById(R.id.player_display);
        playerDisp.addView(plate);
        //add space between plates
        boxSpacing.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
        playerDisp.addView(boxSpacing);
    }

    /**
     * calls other helper methods for adding a player to the lobby based on user data retrieved from the server
     *
     * @param id id number for the player to be added
     */
    public void addPlayer(int id){

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.remote_server_url, "user", Integer.toString(id)),
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

    /**
     * creates a popup with an account summary for a user based on their id
     *
     * @param id id number of account to be viewed
     */
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
                            ((TextView) popupView.findViewById(R.id.user_id)).setText(getString(R.string.id_display, response.getInt("id")));
                            ((TextView) popupView.findViewById(R.id.games_played)).setText(getString(R.string.games_played_display, response.getInt("gamesPlayed")));
                            ((TextView) popupView.findViewById(R.id.games_won)).setText(getString(R.string.games_won_display, response.getInt("gamesWon")));
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

    /**
     * disconnects the user from the websocket and sends back to the main menu
     */
    public void leaveGame(){
        mainActivity.disconnectWebSocket();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
    }

    /**
     * disconnects the user from the websocket and sends them back to the main menu. Adds argument to create popup on the main menu
     */
    private void kicked(){
        mainActivity.disconnectWebSocket();

        MainMenuFragment frag = new MainMenuFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean("kicked", true);

        frag.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, frag).commit();

    }

    /**
     * Removed the player from the lobby websocket and removes associated UI elements
     *
     * @param plate UI plate to be deleted
     * @param space UI space to be deleted
     * @param playerID id number of the user that is being kicked
     * @throws JSONException
     */
    private void kickPlayer(View plate, Space space, int playerID) throws JSONException {
        deletePlayerArray(playerID);
        mainActivity.kickUser(playerID);
    }

    /**
     * public method called by the web socket controller in mainActivity. responds to recieved messages
     *
     * @param s String of message received from web socket
     * @throws JSONException
     */
    public void onMessage(String s) throws JSONException {

        if (!s.isEmpty()) {

            JSONObject json = new JSONObject(s);

            if (json.has("joined") && json.getInt("joined") != UserData.getInstance().getUserID()) {
                addPlayer(json.getInt("joined"));
                playerIds.add(json.getInt("joined"));

                if (isHost) {
                    mainActivity.updateLobby(playerIds);
                }
            } else if (json.has("left")) {
                LinearLayout playerDisp = view.findViewById(R.id.player_display);
                deletePlayerArray(json.getInt("left"));
                playerDisp.removeView(view.findViewWithTag("plate" + json.getInt("left")));
                playerDisp.removeView(view.findViewWithTag("space" + json.getInt("left")));
            } else if (json.has("ids") && !isHost && !prefill) {

                JSONArray array = json.getJSONArray("ids");
                for (int i = 0; i < array.length(); i++) {
                    if ((int) array.get(i) != UserData.getInstance().getUserID()) {
                        addPlayer((int) array.get(i));
                        playerIds.add((int) array.get(i));
                    }
                }
                prefill = true;
            } else if (json.has("kicked") && json.getInt("kicked") == UserData.getInstance().getUserID()) {
                kicked();
            }

            playerCountDisp.setText(getString(R.string.player_count, playerIds.size()));
        }
    }

    /**
     * Iterates through array of user ids and removes by the given id.
     *
     * @param id id number of user to be removed from the array
     */
    private void deletePlayerArray(int id){
        for (int i = 0; i < playerIds.size(); i++){
            if (playerIds.get(i).equals(id)){
                playerIds.remove(i);
            }
        }
    }

}