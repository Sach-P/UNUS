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
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendRequestFragment extends Fragment {

    private View view;
    private Button back;
    private TextView friend_reqs;

    public FriendRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend_request, container, false);
        back = (Button) view.findViewById(R.id.backbutton);
        friend_reqs = (TextView) view.findViewById(R.id.friend_reqs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserProfileFragment()).commit();
            }
        });
        try {
        JSONObject object = new JSONObject();
            object.put("username", UserData.getInstance().getUsername());
            object.put("password", UserData.getInstance().getPassword());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://coms-309-029.class.las.iastate.edu:8080/login",
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            friend_reqs.setText("Friend Requests ("+response.getJSONObject("user").getJSONArray("receivedFriendRequests").length() +"): ");
                            for(int i = 0; i < response.getJSONObject("user").getJSONArray("receivedFriendRequests").length(); i++) {
                                createRequest(response.getJSONObject("user").getJSONArray("receivedFriendRequests").getJSONObject(i).getString("username"), response.getJSONObject("user").getJSONArray("receivedFriendRequests").getJSONObject(i).getInt("friendId"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        friend_reqs.setText("error");
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

    private void createRequest(String username, int id) {
        LinearLayout newLayout = new LinearLayout(view.getContext());
        newLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView user = new TextView(view.getContext());
        Button viewProf = new Button(view.getContext());
        Button accept = new Button(view.getContext());
        Button decline = new Button(view.getContext());
        user.setTextSize(30);
        user.setText(username);
        viewProf.setTextSize(20);
        viewProf.setText("View");
        accept.setTextSize(20);
        accept.setText("Accept");
        decline.setTextSize(20);
        decline.setText("Decline");

        viewProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser(id);
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Friend newFriend = new Friend(id, username);
                Friend[] temp = new Friend[UserData.getInstance().getFriendsList().length+1];
                for(int i = 0; i < UserData.getInstance().getFriendsList().length; i++) {
                    temp[i]= UserData.getInstance().getFriendsList()[i];
                }
                temp[UserData.getInstance().getFriendsList().length] = newFriend;
                UserData.getInstance().setFriendsList(temp);
                accept_decline(true, username, id); //true will equate to accepting the request
                newLayout.setVisibility(View.GONE);
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accept_decline(false, username, id); //false will equate to declining the request
                newLayout.setVisibility(View.GONE);
            }
        });


        newLayout.addView(user);
        newLayout.addView(viewProf);
        newLayout.addView(accept);
        newLayout.addView(decline);
        ((LinearLayout)view.findViewById(R.id.results)).addView(newLayout);
    }

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
                "http://coms-309-029.class.las.iastate.edu:8080/user/"+Integer.toString(id),
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

    private void accept_decline(boolean b, String username, int id) {
        try {
            JSONObject object = new JSONObject();
            object.put("username", username);
            object.put("friendId", id);
            object.put("status", (b)? "accepted":"declined");

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    "http://coms-309-029.class.las.iastate.edu:8080/user/" + UserData.getInstance().getUserID()+"/pending-friend-requests",
                    object,
                    null, null
            );
            Volley.newRequestQueue(requireContext()).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}


