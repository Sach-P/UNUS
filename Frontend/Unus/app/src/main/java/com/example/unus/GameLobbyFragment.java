package com.example.unus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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



        return view;
    }

    //create the UI display of a player in the lobby
    private void addPlayerPlate(int playerID, String username){
        //create horizontal linear layout
        LinearLayout plate = new LinearLayout(view.getContext());
        plate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        plate.setOrientation(LinearLayout.HORIZONTAL);
        plate.setPadding(30,10,30,30);
        plate.setBackgroundColor(getResources().getColor(R.color.dark_gray));
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
}