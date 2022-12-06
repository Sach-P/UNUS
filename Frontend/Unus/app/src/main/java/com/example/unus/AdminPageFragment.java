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
    private List<Integer> lobbyList;
    private List<Team> teamList;
    private LinearLayout displayList;
    private Button back;
    private Button users;
    private Button lobbies;
    private Button teams;
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
        lobbyList = new ArrayList<Integer>();
        teamList = new ArrayList<Team>();
        displayList= (LinearLayout) view.findViewById(R.id.results);
        back = (Button) view.findViewById(R.id.backbutton);
        users = (Button) view.findViewById(R.id.users);
        lobbies = (Button) view.findViewById(R.id.lobbies);
        teams = (Button) view.findViewById(R.id.teams);
        search = (Button) view.findViewById(R.id.search_button);
        clear = (Button) view.findViewById(R.id.clear_button);
        id = (EditText) view.findViewById(R.id.searchbar);

        getUsers();
        getLobbies();
        getTeams();

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                users.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
                users.setTextColor(view.getResources().getColor(R.color.yellow));
                lobbies.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                lobbies.setTextColor(view.getResources().getColor(R.color.purple_500));
                teams.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                teams.setTextColor(view.getResources().getColor(R.color.purple_500));
                id.setHint(R.string.searchid);
                displayUsers(userList);
            }
        });

        lobbies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lobbies.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
                lobbies.setTextColor(view.getResources().getColor(R.color.yellow));
                users.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                users.setTextColor(view.getResources().getColor(R.color.purple_500));
                teams.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                teams.setTextColor(view.getResources().getColor(R.color.purple_500));
                id.setHint(R.string.searchlobbyid);
                displayLobbies(lobbyList);
            }
        });

        teams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lobbies.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                lobbies.setTextColor(view.getResources().getColor(R.color.purple_500));
                users.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                users.setTextColor(view.getResources().getColor(R.color.purple_500));
                teams.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
                teams.setTextColor(view.getResources().getColor(R.color.yellow));
                id.setHint("Search by TeamID");
                displayTeams();
            }
        });

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
                                if(!response.getJSONObject(i).getString("role").equals("admin"))
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
     * gets all of the lobbies in the database and puts them all into a list of ints
     */
    public void getLobbies() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-029.class.las.iastate.edu:8080/lobbies",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for( int i = 0; i < response.length(); i++) {
                                lobbyList.add(response.getJSONObject(i).getInt("id"));
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

            int finalI = i;

            Button stats = new Button(view.getContext());
            stats.setLayoutParams( new ViewGroup.LayoutParams(200, 100));
            stats.setText("stats");
            stats.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
            stats.setTextColor(view.getResources().getColor(R.color.yellow));
            stats.setTextSize(15);
            stats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   //AdminUserPopup popup = new AdminUserPopup(view, list.get(finalI));
                    userPopup(list.get(finalI));
                }
            });

            Button del = new Button(view.getContext());
            del.setLayoutParams( new ViewGroup.LayoutParams(100, 100));
            del.setText("x");
            del.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
            del.setTextColor(view.getResources().getColor(R.color.yellow));
            del.setTextSize(15);
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
     * displays the list of lobbies by id where the admin can then
     * either delete the lobby or kick just one specific player in the lobby
     *
     * @param list
     */
    private void displayLobbies(List<Integer> list) {
        displayList.removeViews(0, displayList.getChildCount());
        for(int i = 0; i < list.size(); i++) {
            LinearLayout layout = new LinearLayout(view.getContext());
            TextView tv = new TextView(view.getContext());
            tv.setLayoutParams( new ViewGroup.LayoutParams(500, 100));
            tv.setText(list.get(i).toString());
            tv.setTextColor(view.getResources().getColor(R.color.yellow));
            tv.setTextSize(25);

            Space sp = new Space(view.getContext());
            sp.setLayoutParams( new ViewGroup.LayoutParams(100, 100));
            Space sp2 = new Space(view.getContext());
            sp2.setLayoutParams( new ViewGroup.LayoutParams(50, 100));

            int finalI = i;

            /*
            Button stats = new Button(view.getContext());
            stats.setLayoutParams( new ViewGroup.LayoutParams(200, 100));
            stats.setText("stats");
            stats.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
            stats.setTextColor(view.getResources().getColor(R.color.yellow));
            stats.setTextSize(15);
            stats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //AdminUserPopup popup = new AdminUserPopup(view, list.get(finalI));
                }
            });
             */

            Button del = new Button(view.getContext());
            del.setLayoutParams( new ViewGroup.LayoutParams(100, 100));
            del.setText("x");
            del.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
            del.setTextColor(view.getResources().getColor(R.color.yellow));
            del.setTextSize(15);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteLobby(list.get(finalI));
                }
            });

            layout.addView(tv);
            layout.addView(sp);
            //layout.addView(stats);
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
                    if(!response.getString("role").equals("admin"))
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

    /**
     * This function will delete an existing lobby and kick all of the players in the lobby
     */
    private void deleteLobby(int id) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                getString(R.string.remote_server_url, "lobbies/delete-lobby", id+"?userId="+UserData.getInstance().getUserID()),
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

    public void userPopup(Friend user) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.admin_user_popup, null);

        TextView username = (TextView) popupView.findViewById(R.id.username);
        TextView userID = (TextView) popupView.findViewById(R.id.user_id);
        EditText played = (EditText) popupView.findViewById(R.id.played);
        EditText won = (EditText) popupView.findViewById(R.id.won);
        Button change = (Button) popupView.findViewById(R.id.change);
        Button promote = (Button) popupView.findViewById(R.id.promote);

        username.setText(user.getUsername());
        userID.setText(Integer.toString(user.getUserID()));
        played.setText(Integer.toString(user.getGamesPlayed()));
        won.setText(Integer.toString(user.getGamesWon()));

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1600, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promote(user);
                popupWindow.dismiss();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = played.getText().toString();
                if(id.length() == 0)
                    return;
                for(int i = 0; i < id.length(); i++) {
                    if (id.charAt(i) != '0' && id.charAt(i) != '1' && id.charAt(i) != '2' && id.charAt(i) != '3' && id.charAt(i) != '4' &&
                            id.charAt(i) != '5' && id.charAt(i) != '6' && id.charAt(i) != '7' &&
                            id.charAt(i) != '8' && id.charAt(i) != '8' && id.charAt(i) != '9') {
                        return;
                    }
                }
                id = won.getText().toString();
                if(id.length() == 0)
                    return;
                for(int i = 0; i < id.length(); i++) {
                    if (id.charAt(i) != '0' && id.charAt(i) != '1' && id.charAt(i) != '2' && id.charAt(i) != '3' && id.charAt(i) != '4' &&
                            id.charAt(i) != '5' && id.charAt(i) != '6' && id.charAt(i) != '7' &&
                            id.charAt(i) != '8' && id.charAt(i) != '8' && id.charAt(i) != '9') {
                        return;
                    }
                }
                changeUser(Integer.parseInt(played.getText().toString()), Integer.parseInt(won.getText().toString()), user);
                popupWindow.dismiss();
            }
        });

    }


    public void changeUser(int played, int won, Friend user) {
        try {
            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", user.getUserID());
            requestBody.put("username", user.getUsername());
            //requestBody.put("password", user.getPassword());
            //requestBody.put("friends", friendsList);
            requestBody.put("role", "player");
            requestBody.put("gamesPlayed", played);
            requestBody.put("gamesWon", won);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    "http://coms-309-029.class.las.iastate.edu:8080/user/"+user.getUserID(),
                    requestBody,
                    null,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            );

            Volley.newRequestQueue(requireContext()).add(request);

        } catch (JSONException ex) {
        }
    }

    public void promote(Friend user) {
        try {
            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", user.getUserID());
            requestBody.put("username", user.getUsername());
            //requestBody.put("password", user.getPassword());
            //requestBody.put("friends", friendsList);
            requestBody.put("role", "admin");
            requestBody.put("gamesPlayed", user.getGamesPlayed());
            requestBody.put("gamesWon", user.getGamesWon());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    "http://coms-309-029.class.las.iastate.edu:8080/user/"+user.getUserID(),
                    requestBody,
                    null,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            );

            Volley.newRequestQueue(requireContext()).add(request);

        } catch (JSONException ex) {
        }
    }

    /**
     * gets all of the users in the database and puts them all into a list of Users
     */
    private void getTeams() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-029.class.las.iastate.edu:8080/teams",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                teamList.add(new Team(response.getJSONObject(i).getString("teamName"),
                                        response.getJSONObject(i).getInt("id"),
                                        response.getJSONObject(i).getInt("wins")));
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

    /**
     * displays the list of users by username and stat on the screen
     * It will display The name followed by either games played or games won
     * This function is called every time any button is hit in the leaderboard
     * screen
     *
     */
    private void displayTeams() {
        displayList.removeViews(0, displayList.getChildCount());
        for(int i = 0; i < teamList.size(); i++) {
            LinearLayout layout = new LinearLayout(view.getContext());
            TextView tv = new TextView(view.getContext());
            tv.setLayoutParams( new ViewGroup.LayoutParams(500, 100));
            tv.setText(teamList.get(i).getName());
            tv.setTextColor(view.getResources().getColor(R.color.yellow));
            tv.setTextSize(25);

            Space sp = new Space(view.getContext());
            sp.setLayoutParams( new ViewGroup.LayoutParams(100, 100));

            Button del = new Button(getContext());
            del.setLayoutParams( new ViewGroup.LayoutParams(100, 100));
            del.setText("x");
            del.setTextColor(view.getResources().getColor(R.color.yellow));
            del.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
            del.setTextSize(25);

            int finalI = i;
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteTeam(teamList.get(finalI).getId());
                }
            });

            layout.addView(tv);
            layout.addView(sp);
            layout.addView(del);
            displayList.addView(layout);
        }
    }

    public void deleteTeam(int id) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                getString(R.string.remote_server_url, "teams/delete-team", id + "?userId=" + UserData.getInstance().getUserID()),
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
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new AdminPageFragment()).commit();

                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }
}
