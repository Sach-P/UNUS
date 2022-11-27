package com.example.unus;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Space;
import android.widget.TextView;

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
import java.util.List;

/**
 * this class acts as the admin page for the app. you will have features such as deleting a user
 * and lobby as well as changing their account information, including stats and friends
 */

public class AdminPageFragment extends Fragment {

    private View view;
    private List<Friend> userList;
    private LinearLayout displayList;
    private Button back;
    private Button search;
    private Button clear;
    private EditText id;

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
        back = (Button) view.findViewById(R.id.backbutton);
        search = (Button) view.findViewById(R.id.search_button);
        clear = (Button) view.findViewById(R.id.clear_button);
        id = (EditText) view.findViewById(R.id.searchbar);

        getUsers();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayList.removeAllViews();
                if(id.getText().toString().length() != 0) {
                    search(id.getText().toString());
                } else {
                    displayUsers(userList);
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayList.removeAllViews();
                displayUsers(userList);
                id.setText("");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
            }
        });

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
            Space sp2 = new Space(view.getContext());
            sp2.setLayoutParams( new ViewGroup.LayoutParams(50, 100));

            Button stats = new Button(view.getContext());
            stats.setLayoutParams( new ViewGroup.LayoutParams(200, 100));
            stats.setText("stats");
            stats.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
            stats.setTextColor(view.getResources().getColor(R.color.yellow));
            stats.setTextSize(15);

            Button del = new Button(view.getContext());
            del.setLayoutParams( new ViewGroup.LayoutParams(100, 100));
            del.setText("x");
            del.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
            del.setTextColor(view.getResources().getColor(R.color.yellow));
            del.setTextSize(15);
            int finalI = i;
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteUser(list.get(finalI).getUserID());
                }
            });

            layout.addView(tv);
            layout.addView(sp);
            layout.addView(stats);
            layout.addView(sp2);
            layout.addView(del);
            displayList.addView(layout);
        }
    }


    /**
     * This function will take in the number that was put into the input field
     * and search the database to see if the user exists
     * If the user does exist it will display the user and a button that will show their stats
     * and other that will send them friend requests
     *
     * @param id
     */
    private void search(String id) {
        for(int i = 0; i < id.length(); i++) {
            if (id.charAt(i) != '0' && id.charAt(i) != '1' && id.charAt(i) != '2' && id.charAt(i) != '3' && id.charAt(i) != '4' &&
                    id.charAt(i) != '5' && id.charAt(i) != '6' && id.charAt(i) != '7' &&
                    id.charAt(i) != '8' && id.charAt(i) != '8' && id.charAt(i) != '9') {
                return;
            }
        }
        JsonObjectRequest request = new JsonObjectRequest(
        Request.Method.GET,
        "http://coms-309-029.class.las.iastate.edu:8080/user/" + Integer.parseInt(id),
        null,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String username = response.getString("username");
                    int id = response.getInt("id");
                    int games_played = response.getInt("gamesPlayed");
                    int games_won = response.getInt("gamesWon");
                    Friend temp = new Friend(id, username, games_played, games_won);
                    List<Friend> list = new ArrayList<>();
                    list.add(temp);
                    displayUsers(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(requireContext()).add(request);
    }

    /**
     * This function will delete the user logged in from the database entirely
     * including removing them from anyone else's friends list
     */
    private void deleteUser(int id) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                getString(R.string.remote_server_url, "user", Integer.toString(id)),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new AdminPageFragment()).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }
}
