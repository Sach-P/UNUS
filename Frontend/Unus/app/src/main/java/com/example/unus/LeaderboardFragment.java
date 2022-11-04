package com.example.unus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LeaderboardFragment extends Fragment {

    private Button back;
    private Button global;
    private Button friends;
    private Button played;
    private Button won;
    private List<Friend> userList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userList = new ArrayList<Friend>();

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        back = (Button) view.findViewById(R.id.back);
        global = (Button) view.findViewById(R.id.global_list);
        friends = (Button) view.findViewById(R.id.friends_list);
        played = (Button) view.findViewById(R.id.games_played);
        won = (Button) view.findViewById(R.id.games_won);


        getUsers();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
            }
        });

        won.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back.setText(userList.get(0).getUsername());
            }
        });


        return view;
    }

    private void getUsers() {

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-029.class.las.iastate.edu:8080/user",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for( int i = 0; i < response.length(); i++) {
                                userList.add(new Friend(response.getJSONObject(i).getInt("id"), response.getJSONObject(i).getString("username")));
                            }
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
