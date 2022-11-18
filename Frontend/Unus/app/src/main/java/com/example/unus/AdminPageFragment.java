package com.example.unus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * this class acts as the admin page for the app. you will have features such as deleting a user
 * and lobby as well as changing their account information, including stats and friends
 */

public class AdminPageFragment extends Fragment {

    private View view;
    private List<Friend> userList;
    private LinearLayout displayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_page, container, false);
        userList = new ArrayList<Friend>();
        displayList= (LinearLayout) view.findViewById(R.id.results);

        getUsers();

        return view;
    }

    /**
     * gets all of the users in the database and puts them all into a list of Users
     */
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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        displayUsers(userList);
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
     * displays the list of users by username and stat on the screen
     * It will display The name followed by either games played or games won
     * This function is called every time any button is hit in the leaderboard
     * screen
     *
     * @param list
     */
    private void displayUsers(List<Friend> list) {
        displayList.removeViews(0, displayList.getChildCount());
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
            stats.setText(""+list.get(i).getGamesWon());
            stats.setTextColor(view.getResources().getColor(R.color.yellow));
            stats.setTextSize(25);

            layout.addView(tv);
            layout.addView(sp);
            layout.addView(stats);
            displayList.addView(layout);
        }
    }

}
