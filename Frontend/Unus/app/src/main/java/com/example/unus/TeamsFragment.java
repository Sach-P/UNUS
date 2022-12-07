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
    private TextView teamText;
    private Button back;
    private Button currTeams;
    private Button newTeams;
    private Button create;
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
        teamText = (TextView) view.findViewById(R.id.friend_reqs);
        displayList = (LinearLayout) view.findViewById(R.id.results);
        back = (Button) view.findViewById(R.id.backbutton);
        currTeams = (Button) view.findViewById(R.id.users);
        newTeams = (Button) view.findViewById(R.id.lobbies);
        create = (Button) view.findViewById(R.id.create);
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
                displayTeams(currTeamList);
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

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTeamPopup();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayList.removeAllViews();
                if (id.getText().toString().length() != 0) {
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
                            for (int i = 0; i < response.length(); i++) {
                                if (!response.getJSONObject(i).getBoolean("privacy")) {
                                    List<Friend> temp = new ArrayList<Friend>();
                                    for (int j = 0; j < response.getJSONObject(i).getJSONArray("players").length(); j++) {
                                        temp.add(new Friend(response.getJSONObject(i).getJSONArray("players").getJSONObject(j).getInt("id"),
                                                response.getJSONObject(i).getJSONArray("players").getJSONObject(j).getString("username")));
                                    }
                                    Team team = new Team(response.getJSONObject(i).getInt("id"), response.getJSONObject(i).getString("teamName"),
                                            new Friend(response.getJSONObject(i).getJSONObject("leader").getInt("id"),
                                                    response.getJSONObject(i).getJSONObject("leader").getString("username")), temp);
                                    newTeamList.add(team);
                                    for(Friend player : team.getPlayers()) {
                                        if(player.getUserID() == UserData.getInstance().getUserID())
                                            currTeamList.add(team);
                                    }
                                    if(team.getLeader().getUserID() == UserData.getInstance().getUserID())
                                        currTeamList.add(team);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        displayTeams(currTeamList);
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
        for (int i = 0; i < list.size(); i++) {
            LinearLayout layout = new LinearLayout(view.getContext());
            TextView tv = new TextView(view.getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(500, 100));
            tv.setText(list.get(i).getName());
            tv.setTextColor(view.getResources().getColor(R.color.yellow));
            tv.setTextSize(25);

            Space sp = new Space(view.getContext());
            sp.setLayoutParams(new ViewGroup.LayoutParams(125, 100));

            int finalI = i;

            Button stats = new Button(view.getContext());
            stats.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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


            layout.addView(tv);
            layout.addView(sp);
            layout.addView(stats);
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
        for (int i = 0; i < id.length(); i++) {
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
                            if (!response.getString("role").equals("admin"))
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
                getString(R.string.remote_server_url, "teams/leave-team", id + "?userId=" + UserData.getInstance().getUserID()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new TeamsFragment()).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new TeamsFragment()).commit();
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }


    public void teamPopup(Team team) {
        boolean inTeam = false;
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.team_popup, null);

        TextView username = (TextView) popupView.findViewById(R.id.username);
        TextView userID = (TextView) popupView.findViewById(R.id.user_id);
        Button join = (Button) popupView.findViewById(R.id.join);

        username.setText(team.getName());
        userID.setText(Integer.toString(team.getId()));

        LinearLayout layout = (LinearLayout) popupView.findViewById(R.id.playerList);
        TextView display_leader = new TextView(popupView.getContext());
        display_leader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        display_leader.setText(team.getLeader().getUsername() + ": Leader");
        display_leader.setTextColor(this.getResources().getColor(R.color.yellow));
        display_leader.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
        display_leader.setTextSize(20);
        layout.addView(display_leader);
        if (team.getLeader().getUserID() == UserData.getInstance().getUserID()) {
            inTeam = true;
        }

        for (int i = 0; i < team.getPlayers().size(); i++) {

            TextView display_player = new TextView(popupView.getContext());
            display_player.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            display_player.setText(team.getPlayers().get(i).getUsername());
            display_player.setTextColor(this.getResources().getColor(R.color.yellow));
            display_player.setTextSize(20);

            if (team.getPlayers().get(i).getUserID() == UserData.getInstance().getUserID()) {
                inTeam = true;
            }

            layout.addView(display_player, 1);
        }

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1600, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        if (inTeam) {
            join.setText("Leave Team");
        }
        if (team.getLeader().getUserID() == UserData.getInstance().getUserID()) {
            join.setText("Delete Team");
        }

        boolean finalInTeam = inTeam;
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (team.getLeader().getUserID() == UserData.getInstance().getUserID()) {
                    deleteTeam(team.getId());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new TeamsFragment()).commit();
                } else if (finalInTeam) {
                    leaveTeam(team.getId());
                } else {
                    joinTeam(team.getId());
                }
                popupWindow.dismiss();
            }
        });

    }

    public void joinTeam(int teamId) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                getString(R.string.remote_server_url, "teams/join-team/", teamId + "?userId=" + UserData.getInstance().getUserID()),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new TeamsFragment()).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new TeamsFragment()).commit();
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

    public void deleteTeam(int id) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                getString(R.string.remote_server_url, "teams/delete-team", id+"?userId="+UserData.getInstance().getUserID()),
                null,
                null,
                null
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

    public void createTeam(String name, boolean isPrivate) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("teamName", name);
        object.put("isPrivate", UserData.getInstance());
        object.put("isPrivate", isPrivate);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.remote_server_url, "teams", "create-team?userId=" + UserData.getInstance().getUserID()),
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("message").equals("success")) {
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new TeamsFragment()).commit();
                            } else {
                                teamText.setText("Already a Leader");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            teamText.setText("Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new TeamsFragment()).commit();
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);
    }

    public void createTeamPopup() {
        final boolean[] isPrivate = {false};
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.team_create_layout, null);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1000, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 50, 30);

        EditText name = popupView.findViewById(R.id.name_prompt);
        Button privacy = popupView.findViewById(R.id.privateButton);

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPrivate[0] = !isPrivate[0];
                if(isPrivate[0]) {
                    privacy.setBackgroundColor(view.getResources().getColor(R.color.purple_500));
                    privacy.setTextColor(view.getResources().getColor(R.color.yellow));
                    privacy.setText("Private(on)");
                } else {
                    privacy.setBackgroundColor(view.getResources().getColor(R.color.yellow));
                    privacy.setTextColor(view.getResources().getColor(R.color.purple_500));
                    privacy.setText("Private(off)");
                }
            }
        });

        Button create = (Button) popupView.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(name.getText().toString() != null) {
                        createTeam(name.getText().toString(), isPrivate[0]);
                        popupWindow.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}