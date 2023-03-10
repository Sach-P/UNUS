package com.example.unus;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This fragment displays the stats of the user along with the users friends,
 * from here the user can navigate to the user Settings
 * You can also navigate to the user search fragment and friend requests
 * fragment
 * you can also remove friends on this page
 *
 * @author Abe Demo
 */

public class UserProfileFragment extends Fragment {

    private View view;
    private TextView username;
    private TextView userID;
    private TextView games;
    private TextView wins;
    private Button settings;
    private Button back;
    private Button teams;
    private Button addFriends;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        username = (TextView) view.findViewById(R.id.username);
        userID = (TextView) view.findViewById(R.id.userID);
        games = (TextView) view.findViewById(R.id.games);
        wins = (TextView) view.findViewById(R.id.wins);
        settings = (Button) view.findViewById(R.id.user_settings);
        back = (Button) view.findViewById(R.id.back);
        teams = (Button) view.findViewById(R.id.teams);
        username.setText(UserData.getInstance().getUsername());
        userID.setText(getString(R.string.user_id_display, UserData.getInstance().getUserID()));
        games.setText(getString(R.string.games_played_display, UserData.getInstance().getGamesPlayed()));
        wins.setText(getString(R.string.games_won_display, UserData.getInstance().getGamesWon()));
        getFriends();

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserSettingsFragment()).commit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
            }
        });

        teams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new TeamsFragment()).commit();
            }
        });


        return view;
    }

    /**
     * This function will iterate thorugh the users list of friends and display them
     * in the profile with options to view users and remove them from your friends
     */
    public void getFriends() {
        LinearLayout flayout = view.findViewById(R.id.friend_buttons);
        Button make_friends = new Button(view.getContext());
        make_friends.setLayoutParams(new ViewGroup.LayoutParams(175,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        make_friends.setTextSize(15);
        make_friends.setText("Add");
        flayout.addView(make_friends);
        make_friends.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
        make_friends.setTextColor(this.getResources().getColor(R.color.yellow));
        make_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserSearchFragment()).commit();
            }
        });

        Space sp = new Space(view.getContext());
        sp.setLayoutParams(new ViewGroup.LayoutParams(40, ViewGroup.LayoutParams.WRAP_CONTENT));
        flayout.addView(sp);

        Button pending_reqs = new Button(view.getContext());
        pending_reqs.setLayoutParams(new ViewGroup.LayoutParams(300,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        pending_reqs.setTextSize(15);
        pending_reqs.setText("Requests");
        flayout.addView(pending_reqs);
        pending_reqs.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
        pending_reqs.setTextColor(this.getResources().getColor(R.color.yellow));
        pending_reqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new FriendRequestFragment()).commit();

            }
        });
        for( int i = 0; i < UserData.getInstance().getFriendsList().length; i++) {
            LinearLayout layout = view.findViewById(R.id.scrollList);
            LinearLayout newLayout = new LinearLayout(view.getContext());
            newLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            Button button = new Button(view.getContext());
            button.setLayoutParams(new ViewGroup.LayoutParams(400,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setText(UserData.getInstance().getFriendsList()[i].getUsername());
            button.setTextSize(20);
            button.setAllCaps(false);
            button.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
            button.setTextColor(this.getResources().getColor(R.color.yellow));

            Button remove = new Button(view.getContext());
            remove.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            remove.setText("del");
            remove.setTextSize(10);
            remove.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
            remove.setTextColor(this.getResources().getColor(R.color.yellow));
            int finalIndex = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getUser(UserData.getInstance().getFriendsList()[finalIndex].getUserID());
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        unfriend(UserData.getInstance().getFriendsList()[finalIndex].getUserID(), UserData.getInstance().getFriendsList()[finalIndex].getUsername());
                }
            });
            Space sp2 = new Space(view.getContext());
            sp2.setLayoutParams(new ViewGroup.LayoutParams(20, ViewGroup.LayoutParams.WRAP_CONTENT));
            newLayout.addView(button);
            newLayout.addView(sp2);
            newLayout.addView(remove);
            Space sp3 = new Space(view.getContext());
            sp3.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, 20));
            layout.addView(newLayout);
            layout.addView(sp3);
        }
    }

    /**
     * this function will get a user from the database based on the ID of the user
     * and it will display the users information in a popup window
     * @param id
     */
    public void getUser(int id) {
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
                            ((TextView) popupView.findViewById(R.id.user_id)).setText("ID: "+ response.getInt("id"));
                            ((TextView) popupView.findViewById(R.id.games_played)).setText("Games Played: "+ response.getInt("gamesPlayed"));
                            ((TextView) popupView.findViewById(R.id.games_won)).setText("Games Won: "+ response.getInt("gamesWon"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //loginHeader.setText(getString(R.string.login_error));
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);

    }

    /**
     * this function will remove the user from the logged in users friend list and it will also remove the
     * current user from the other users friend list by removing them in the backend
     * @param id
     * @param name
     */
    private void unfriend(int id, String name) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                "http://coms-309-029.class.las.iastate.edu:8080/user/" + UserData.getInstance().getUserID() + "/friends/remove-friend?friendId="+id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //loginHeader.setText(getString(R.string.login_error));
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);

        Friend[] newFriends = new Friend[UserData.getInstance().getFriendsList().length - 1];
        for(int i = 0, j = 0; i < UserData.getInstance().getFriendsList().length; i++) {
            if(UserData.getInstance().getFriendsList()[i].getUserID() != id) {
                newFriends[j] = UserData.getInstance().getFriendsList()[i];
                j++;
            }
        }
        UserData.getInstance().setFriendsList(newFriends);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserProfileFragment()).commit();


    }

}
