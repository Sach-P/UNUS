package com.example.unus;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminUserPopup {

    private View popupView;
    private TextView username;
    private TextView userID;
    private EditText played;
    private EditText won;
    private Button change;
    private View view;
    private Friend user;

    public AdminUserPopup(View view, Friend user) {
        this.view = view;
        this.user = user;
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.admin_user_popup, null);

        username = (TextView) popupView.findViewById(R.id.username);
        userID = (TextView) popupView.findViewById(R.id.user_id);
        played = (EditText) popupView.findViewById(R.id.played);
        won = (EditText) popupView.findViewById(R.id.won);
        change = (Button) popupView.findViewById(R.id.change);

        username.setText(user.getUsername());
        userID.setText(Integer.toString(user.getUserID()));
        played.setText(Integer.toString(user.getGamesPlayed()));
        won.setText(Integer.toString(user.getGamesWon()));

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1500, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUser();
            }
        });

    }


    public void changeUser() {
        try {
            JSONArray friendsList = new JSONArray();
            for(int i = 0; i < UserData.getInstance().getFriendsList().length; i++) {
                JSONObject temp = new JSONObject();
                temp.put("friendId", UserData.getInstance().getFriendsList()[i].getUserID());
                temp.put("username", UserData.getInstance().getFriendsList()[i].getUsername());
                temp.put("status", "pending");
                friendsList.put(temp);
            }
            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", UserData.getInstance().getUserID());
            requestBody.put("username", UserData.getInstance().getUsername());
            requestBody.put("password", UserData.getInstance().getPassword());
            requestBody.put("friends", friendsList);
            requestBody.put("role", UserData.getInstance().getRole());
            requestBody.put("gamesPlayed", played.getText().toString());
            requestBody.put("gamesWon", won.getText().toString());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    "http://coms-309-029.class.las.iastate.edu:8080/user/"+user.getUserID(),
                    requestBody,
                    null,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //loginHeader.setText(getString(R.string.login_error));
                        }
                    }
            );

            //Volley.newRequestQueue(requireContext()).add(request);

        } catch (JSONException ex) {
        }
    }
}
