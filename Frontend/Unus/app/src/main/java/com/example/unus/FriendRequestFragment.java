package com.example.unus;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Space;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


/**
 * This fragment will get all of the incoming friend requests and
 * display them so that the user can decide to either accept or decline then
 */

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
        newLayout.setGravity(Gravity.CENTER);

        LinearLayout fullBox = new LinearLayout(view.getContext());
        fullBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        fullBox.setOrientation(LinearLayout.VERTICAL);
        fullBox.setPadding(30,10,30,30);
        fullBox.setBackgroundColor(getResources().getColor(R.color.dark_gray));

        TextView user = new TextView(view.getContext());
        Button viewProf = new Button(view.getContext());
        Button accept = new Button(view.getContext());
        Button decline = new Button(view.getContext());

        user.setTextSize(30);
        user.setText(username);
        user.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        user.setTextColor(getResources().getColor(R.color.purple_500));

        viewProf.setTextSize(20);
        viewProf.setText("View");
        viewProf.setTextColor(getResources().getColor(R.color.yellow));
        viewProf.setBackgroundColor(getResources().getColor(R.color.purple_500));

        accept.setTextSize(20);
        accept.setText("Accept");
        accept.setTextColor(getResources().getColor(R.color.yellow));
        accept.setBackgroundColor(getResources().getColor(R.color.purple_500));

        decline.setTextSize(20);
        decline.setText("Decline");
        decline.setTextColor(getResources().getColor(R.color.yellow));
        decline.setBackgroundColor(getResources().getColor(R.color.purple_500));

        viewProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser(id);
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullBox.setVisibility(View.GONE);
                Friend newFriend = new Friend(id, username);
                Friend[] temp = new Friend[UserData.getInstance().getFriendsList().length+1];
                for(int i = 0; i < UserData.getInstance().getFriendsList().length; i++) {
                    temp[i]= UserData.getInstance().getFriendsList()[i];
                }
                temp[UserData.getInstance().getFriendsList().length] = newFriend;
                UserData.getInstance().setFriendsList(temp);
                accept_decline(true, username, id); //true will equate to accepting the request
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullBox.setVisibility(View.GONE);
                accept_decline(false, username, id); //false will equate to declining the request
            }
        });

        Space space = new Space(view.getContext());
        space.setLayoutParams(new LinearLayout.LayoutParams(40,20));

        Space space2 = new Space(view.getContext());
        space2.setLayoutParams(new LinearLayout.LayoutParams(40,20));

        newLayout.addView(viewProf);
        newLayout.addView(space);
        newLayout.addView(accept);
        newLayout.addView(space2);
        newLayout.addView(decline);

        fullBox.addView(user);
        fullBox.addView(newLayout);

        Space space3 = new Space(view.getContext());
        space3.setLayoutParams(new LinearLayout.LayoutParams(0, 30));

        ((LinearLayout)view.findViewById(R.id.results)).addView(fullBox);
        ((LinearLayout)view.findViewById(R.id.results)).addView(space3);

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
            object.put("status", (b)? "accepted":"declined");
            String put =((b)? "accepted":"declined");

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    "http://coms-309-029.class.las.iastate.edu:8080/user/" + UserData.getInstance().getUserID()+"/pending-friend-requests?friendId="+id,
                    object,
                    null, null
            );
            Volley.newRequestQueue(requireContext()).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


