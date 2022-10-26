package com.example.unus;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSearchFragment extends Fragment {

    private View view;
    private Button back;
    private Button search;
    private EditText id;
    private String name;
    private LinearLayout resultLayout;

    public UserSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_search_fragment, container, false);
        back = (Button) view.findViewById(R.id.backbutton);
        search = (Button) view.findViewById(R.id.search_button);
        id = (EditText) view.findViewById(R.id.searchbar);
        resultLayout = (LinearLayout) view.findViewById(R.id.results);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserProfileFragment()).commit();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultLayout.removeAllViews();
                search(id.getText().toString());
            }
        });
        return view;
    }
    private void search(String id) {
        boolean friend = false;
        boolean sentReq = false;
        boolean receivedReq = false;
        if(Integer.parseInt(id) != UserData.getInstance().getUserID() && Integer.parseInt(id) > 0) {
            for(int i = 0; i < UserData.getInstance().getFriendsList().length; i++) {
                if (Integer.parseInt(id) == UserData.getInstance().getFriendsList()[i].getUserID())
                    friend = true;
            }
            for(int i = 0; i < UserData.getInstance().getSentRequests().length; i++) {
                if (Integer.parseInt(id) == UserData.getInstance().getSentRequests()[i].getUserID())
                    sentReq = true;
            }
            for(int i = 0; i < UserData.getInstance().getReceivedRequests().length; i++) {
                if (Integer.parseInt(id) == UserData.getInstance().getReceivedRequests()[i].getUserID())
                    receivedReq = true;
            }

            boolean finalFriend = friend;
            boolean finalSentReq = sentReq;
            boolean finalReceivedReq = receivedReq;
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://coms-309-029.class.las.iastate.edu:8080/user/" + Integer.parseInt(id),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            TextView user = new TextView(view.getContext());
                            Button viewProf = new Button(view.getContext());
                            Button sendReq = new Button(view.getContext());
                            user.setTextSize(30);
                            viewProf.setTextSize(15);
                            viewProf.setText("View");
                            sendReq.setTextSize(15);
                            if(finalFriend) {
                                sendReq.setText("Already Friends");
                            } else if (finalSentReq) {
                                sendReq.setText("Request Sent");
                            } else if (finalReceivedReq) {
                                sendReq.setText("Accept Request");
                            } else {
                                sendReq.setText("Send Friend Request");
                            }
                            try {
                                user.setText(response.getString("username"));
                                viewProf.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
                                        View popupView = inflater.inflate(R.layout.profile_view_layout, null);

                                        //Make Inactive Items Outside Of PopupWindow
                                        boolean focusable = true;

                                        //Create a window with our parameters
                                        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1000, focusable);
                                        popupWindow.showAtLocation(view, Gravity.CENTER, 50, 30);

                                        try {
                                            ((TextView) popupView.findViewById(R.id.username)).setText(response.getString("username"));
                                            ((TextView) popupView.findViewById(R.id.user_id)).setText(response.getString("id"));
                                            ((TextView) popupView.findViewById(R.id.games_played)).setText("Games Played: " + response.getString("gamesPlayed"));
                                            ((TextView) popupView.findViewById(R.id.games_won)).setText("Games Won: " + response.getString("gamesWon"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                sendReq.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(finalFriend || finalSentReq) { return; }
                                        sendReq.setText("Request Sent");
                                        try {
                                            if(finalReceivedReq) { acceptFriend(response.getString("username"), id); return; }
                                            sendFriendRequest(response.getString("username"), id);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ((LinearLayout) view.findViewById(R.id.results)).addView(user);
                            ((LinearLayout) view.findViewById(R.id.results)).addView(viewProf);
                            ((LinearLayout) view.findViewById(R.id.results)).addView(sendReq);
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
    }

    private void sendFriendRequest(String username, String id) throws JSONException {

        Friend newSent = new Friend(Integer.parseInt(id), username);
        Friend[] temp = new Friend[UserData.getInstance().getSentRequests().length+1];
        for(int i = 0; i < UserData.getInstance().getSentRequests().length; i++) {
            temp[i]= UserData.getInstance().getSentRequests()[i];
        }
        temp[UserData.getInstance().getSentRequests().length] = newSent;
        UserData.getInstance().setSentRequestsList(temp);

        JSONObject object = new JSONObject();
        object.put("username", username);
        object.put("friendId", Integer.parseInt(id));

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                "http://coms-309-029.class.las.iastate.edu:8080/user/"+UserData.getInstance().getUserID()+"/send-friend-request",
                object,
                null, null);
        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void acceptFriend(String username, String id) {

        Friend newFriend = new Friend(Integer.parseInt(id), username);
        Friend[] temp = new Friend[UserData.getInstance().getFriendsList().length+1];
        for(int i = 0; i < UserData.getInstance().getFriendsList().length; i++) {
            temp[i]= UserData.getInstance().getFriendsList()[i];
        }
        temp[UserData.getInstance().getFriendsList().length] = newFriend;
        UserData.getInstance().setFriendsList(temp);
        try {
            JSONObject object = new JSONObject();
            object.put("username", username);
            object.put("friendId", Integer.parseInt(id));
            object.put("status", "accepted");

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
