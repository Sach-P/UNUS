package com.example.unus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
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

/**
 * This fragment displays the leaderboard
 * leaderboards include both friend sections and global sections
 * it displays stats based on what you selected, that can be either
 * friend or global and games played or games won
 */

public class LeaderboardFragment extends Fragment {

    private View view;
    private Button back;
    private Button global;
    private Button friends;
    private Button played;
    private Button won;
    private TextView stat_name;
    public boolean isGlobal = true;
    public boolean isPlayed = true; //I know this is terrible for scaling but I'll fix it later
    private LinearLayout board;
    private List<Friend> userList;
    private List<Friend> friendList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userList = new ArrayList<Friend>();
        friendList = new ArrayList<Friend>();
        getUsers();
        /*
        Something in here is so bugged but I can't for the life of me find it, it works and that;s all that matters for now
         */

        view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        back = (Button) view.findViewById(R.id.back);
        global = (Button) view.findViewById(R.id.global_list);
        friends = (Button) view.findViewById(R.id.friends_list);
        played = (Button) view.findViewById(R.id.games_played);
        won = (Button) view.findViewById(R.id.games_won);
        board = (LinearLayout) view.findViewById(R.id.users);
        stat_name = (TextView) view.findViewById(R.id.stat_name);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
            }
        });

        won.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayed) {
                    won.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
                    won.setTextColor(view.getResources().getColor(R.color.yellow));
                    played.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                    played.setTextColor(view.getResources().getColor(R.color.purple_500));
                    isPlayed = false;
                    stat_name.setText("Games Won: ");
                    sortGamesWon();
                    displayUsers((isGlobal) ? userList : friendList, false);
                }
            }
        });

        played.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isPlayed) {
                    played.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
                    played.setTextColor(view.getResources().getColor(R.color.yellow));
                    won.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                    won.setTextColor(view.getResources().getColor(R.color.purple_500));
                    isPlayed = true;
                    stat_name.setText("Games Played: ");
                    sortGamesPlayed();
                    displayUsers((isGlobal) ? userList : friendList, true);
                }
            }
        });

        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isGlobal) {
                    global.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
                    global.setTextColor(view.getResources().getColor(R.color.yellow));
                    friends.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                    friends.setTextColor(view.getResources().getColor(R.color.purple_500));
                    isGlobal = true;
                    stat_name.setText((isPlayed) ? "Games Played: " : "Games Won: ");
                    displayUsers(userList, isPlayed);
                }
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGlobal) {
                    friends.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
                    friends.setTextColor(view.getResources().getColor(R.color.yellow));
                    global.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                    global.setTextColor(view.getResources().getColor(R.color.purple_500));
                    isGlobal = false;
                    stat_name.setText((isPlayed) ? "Games Played: " : "Games Won: ");
                    displayUsers(friendList, isPlayed);
                }
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
                                userList.add(new Friend(response.getJSONObject(i).getInt("id"), response.getJSONObject(i).getString("username"),
                                        response.getJSONObject(i).getInt("gamesPlayed"), response.getJSONObject(i).getInt("gamesWon")));
                                for(int j = 0; j < UserData.getInstance().getFriendsList().length; j++) {
                                    if(UserData.getInstance().getFriendsList()[j].getUserID() == response.getJSONObject(i).getInt("id"))
                                        friendList.add(new Friend(response.getJSONObject(i).getInt("id"), response.getJSONObject(i).getString("username"),
                                                response.getJSONObject(i).getInt("gamesPlayed"), response.getJSONObject(i).getInt("gamesWon")));
                                }
                                if(UserData.getInstance().getUserID() == response.getJSONObject(i).getInt("id"))
                                    friendList.add(new Friend(response.getJSONObject(i).getInt("id"), response.getJSONObject(i).getString("username"),
                                            response.getJSONObject(i).getInt("gamesPlayed"), response.getJSONObject(i).getInt("gamesWon")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sortGamesPlayed();
                        stat_name.setText("Games Played: ");
                        displayUsers(userList, true);
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

    private void sortGamesPlayed() {
        List<Friend> temp = new ArrayList<Friend>();
        temp.add(userList.get(0));
        for(int i = 1; i < userList.size(); i++) {
            int j;
            for (j = 0; j < temp.size() && userList.get(i).getGamesPlayed() < temp.get(j).getGamesPlayed(); j++);
            temp.add(j, userList.get(i));
        }
        userList = temp;

        List<Friend> temp2 = new ArrayList<Friend>();
        temp2.add(friendList.get(0));
        for(int i = 1; i < friendList.size(); i++) {
            int j;
            for (j = 0; j < temp2.size() && friendList.get(i).getGamesPlayed() < temp2.get(j).getGamesPlayed(); j++);
            temp2.add(j, friendList.get(i));
        }
        friendList = temp2;
    }

    private void sortGamesWon() {
        List<Friend> temp = new ArrayList<Friend>();
        temp.add(userList.get(0));
        for(int i = 1; i < userList.size(); i++) {
            int j;
            for (j = 0; j < temp.size() && userList.get(i).getGamesWon() < temp.get(j).getGamesWon(); j++);
            temp.add(j, userList.get(i));
        }
        userList = temp;

        List<Friend> temp2 = new ArrayList<Friend>();
        temp2.add(friendList.get(0));
        for(int i = 1; i < friendList.size(); i++) {
            int j;
            for (j = 0; j < temp2.size() && friendList.get(i).getGamesWon() < temp2.get(j).getGamesWon(); j++);
            temp2.add(j, friendList.get(i));
        }
        friendList = temp2;
    }

    private void displayUsers(List<Friend> list, boolean played) {
        board.removeViews(0, board.getChildCount());
        for(int i = 0; i < list.size(); i++) {
            LinearLayout layout = new LinearLayout(view.getContext());
            TextView tv = new TextView(view.getContext());
            tv.setLayoutParams( new ViewGroup.LayoutParams(500, 100));
            tv.setText(list.get(i).getUsername());
            tv.setTextColor(view.getResources().getColor(R.color.yellow));
            tv.setTextSize(25);

            Space sp = new Space(view.getContext());
            sp.setLayoutParams( new ViewGroup.LayoutParams(100, 100));

            TextView stats = new TextView(view.getContext());
            stats.setLayoutParams( new ViewGroup.LayoutParams(400, 100));
            stats.setText(""+((played)?list.get(i).getGamesPlayed():list.get(i).getGamesWon()));
            stats.setTextColor(view.getResources().getColor(R.color.yellow));
            stats.setTextSize(25);

            layout.addView(tv);
            layout.addView(sp);
            layout.addView(stats);
            board.addView(layout);
        }
    }
}
