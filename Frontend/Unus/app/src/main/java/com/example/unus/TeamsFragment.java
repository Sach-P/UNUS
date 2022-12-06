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

public class TeamsFragment extends Fragment {

    private View view;
    private List<Team> currTeamList;
    private List<Team> newTeamList;
    private LinearLayout displayList;
    private Button back;
    private Button currTeams;
    private Button newTeams;
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
        view = inflater.inflate(R.layout.fragment_teams, container, false);
        currTeamList = new ArrayList<Team>();
        newTeamList = new ArrayList<Team>();
        displayList= (LinearLayout) view.findViewById(R.id.results);
        back = (Button) view.findViewById(R.id.backbutton);
        currTeams = (Button) view.findViewById(R.id.users);
        newTeams = (Button) view.findViewById(R.id.lobbies);
        search = (Button) view.findViewById(R.id.search_button);
        clear = (Button) view.findViewById(R.id.clear_button);
        id = (EditText) view.findViewById(R.id.searchbar);

        getTeams();

        currTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currTeams.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
                currTeams.setTextColor(view.getResources().getColor(R.color.yellow));
                newTeams.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                newTeams.setTextColor(view.getResources().getColor(R.color.purple_500));
                displayTeams(newTeamList);
            }
        });

        newTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTeams.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
                newTeams.setTextColor(view.getResources().getColor(R.color.yellow));
                currTeams.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                currTeams.setTextColor(view.getResources().getColor(R.color.purple_500));
                displayTeams(newTeamList);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayList.removeAllViews();
                if(id.getText().toString().length() != 0) {
                    search(id.getText().toString());
                } else {
                    displayTeams(newTeamList);
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayList.removeAllViews();
                displayTeams(newTeamList);
                id.setText("");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserProfileFragment()).commit();
            }
        });

        return view;
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
                            for( int i = 0; i < response.length(); i++) {
                                if(!response.getJSONObject(i).getBoolean("privacy"))
                                    newTeamList.add(new Team(response.getJSONObject(i).getInt("id"), response.getJSONObject(i).getString("teamName")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        displayTeams(newTeamList);
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
    private void displayTeams(List<Team> list) {
        displayList.removeViews(0, displayList.getChildCount());
        for(int i = 0; i < list.size(); i++) {
            LinearLayout layout = new LinearLayout(view.getContext());
            TextView tv = new TextView(view.getContext());
            tv.setLayoutParams( new ViewGroup.LayoutParams(500, 100));
            tv.setText(list.get(i).getName());
            tv.setTextColor(view.getResources().getColor(R.color.yellow));
            tv.setTextSize(25);

            Space sp = new Space(view.getContext());
            sp.setLayoutParams( new ViewGroup.LayoutParams(100, 100));
            Space sp2 = new Space(view.getContext());
            sp2.setLayoutParams( new ViewGroup.LayoutParams(50, 100));

            int finalI = i;

            Button stats = new Button(view.getContext());
            stats.setLayoutParams( new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            stats.setText("view");
            stats.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
            stats.setTextColor(view.getResources().getColor(R.color.yellow));
            stats.setTextSize(15);
            stats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //AdminUserPopup popup = new AdminUserPopup(view, list.get(finalI));
                    teamPopup(list.get(finalI));
                }
            });

            Button del = new Button(view.getContext());
            del.setLayoutParams( new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            del.setText("leave");
            del.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
            del.setTextColor(view.getResources().getColor(R.color.yellow));
            del.setTextSize(15);
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leaveTeam(list.get(finalI).getId());
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
                            //displayTeams(list);
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
    private void leaveTeam(int id) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                getString(R.string.remote_server_url, "teams/leave-team", id+"?userId="+UserData.getInstance().getUserID()),
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

    public void teamPopup(Team team) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.team_popup, null);

        TextView username = (TextView) popupView.findViewById(R.id.username);
        TextView userID = (TextView) popupView.findViewById(R.id.user_id);
        Button join = (Button) popupView.findViewById(R.id.join);

        username.setText(team.getName());
        userID.setText(Integer.toString(team.getId()));

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1600, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinTeam(team.getId());
                popupWindow.dismiss();
            }
        });

    }

    public void joinTeam(int teamId) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                getString(R.string.remote_server_url, "teams/join-team/", teamId+"?userId="+UserData.getInstance().getUserID()),
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

/*
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
 */
/*
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
 */
}
