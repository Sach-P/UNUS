package com.example.unus;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Fragment used for the primary navigation around the menu screens. Very graphical
 *
 * @author Isaac Blandin
 */
public class MainMenuFragment extends Fragment {

    int GameLobbyId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        //add user's data to the profile preview
        TextView username = (TextView) view.findViewById(R.id.username_display);
        username.setText(UserData.getInstance().getUsername());
        TextView id = (TextView) view.findViewById(R.id.id_display) ;
        id.setText(getString(R.string.id_display, UserData.getInstance().getUserID()));

        //add clickable interaction to the avatar image
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar_pic);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserProfileFragment()).commit();
            }
        });

        //add clickable interaction to join game button
        Button joinGame = (Button) view.findViewById(R.id.join_game_button);
        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new JoinLobbyFragment()).commit();
            }
        });

        //add clickable interaction to host game button
        Button hostGame = (Button) view.findViewById(R.id.host_game_button);
        hostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createLobby();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startHostedLobby();
            }
        });

        //this is the global chat button and it takes you to global chat
        ((ImageView) view.findViewById(R.id.global_chat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatLayout cl = new ChatLayout(view);

            }
        });

        //add clickable interaction to leaderboard button
                Button leaderboard = (Button) view.findViewById(R.id.leaderboard_button);
                leaderboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new LeaderboardFragment()).commit();
                    }
        });

        //add clickable interaction to logout button
        Button logout = (Button) view.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new LogInScreenFragment()).commit();
            }
        });

        //create kicked popup if the user was kicked from a lobby
        if (getArguments() != null){
            Bundle bundle = getArguments();
            if (bundle.getBoolean("kicked")){
                View popupView = inflater.inflate(R.layout.booted_popup, null);

                //Make Inactive Items Outside Of PopupWindow
                boolean focusable = true;

                //Create a window with our parameters
                final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1000, focusable);

                //Set the location of the window on the screen
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            }
        }

        return view;
    }

    /**
     * Sends http request to create a lobby on the server
     *
     * @throws JSONException
     */
    private void createLobby() throws JSONException {
        //add login credentials to the response body
        JSONObject requestBody = new JSONObject();
        requestBody.put("private",true);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                String.format("http://coms-309-029.class.las.iastate.edu:8080/lobbies/create-lobby?userId=%d", UserData.getInstance().getUserID()),
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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
     * retrieve lobby that is being hosted by the user. Change screen to lobby
     */
    private void startHostedLobby(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                String.format("http://coms-309-029.class.las.iastate.edu:8080/user/get-lobby/%d",UserData.getInstance().getUserID()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject json = new JSONObject(response);
                            setGameLobbyId(json.getInt("id"));
                            changeToLobby(json.getInt("id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(requireContext()).add(stringRequest);

    }

    /**
     * Sets the global variable of the game lobby
     *
     * @param id id number of game lobby that is being hosted
     */
    public void setGameLobbyId(int id){
        GameLobbyId = id;
    }

    /**
     * Change fragment to game lobby based on id number
     *
     * @param id game lobby's id number
     */
    public void changeToLobby(int id){
        GameLobbyFragment frag = new GameLobbyFragment();

        //add arguments to game lobby fragment before committing
        Bundle bundle = new Bundle();
        bundle.putInt("lobbyId", id);
        bundle.putBoolean("isHost", true);
        frag.setArguments(bundle);

        //change fragments
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, frag, "gameLobby").commit();
        getActivity().getSupportFragmentManager().executePendingTransactions();
    }




}