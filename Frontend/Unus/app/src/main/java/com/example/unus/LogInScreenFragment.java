package com.example.unus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * First Fragment you see when opening a screen. Prompts a user for their username and password. Verifies credentials with the remote server
 *
 * @author Isaac Blandin
 */
public class LogInScreenFragment extends Fragment {

    private TextView loginHeader;
    private EditText usernameField;
    private EditText passwordField;

    public LogInScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in_screen, container, false);

        //initialize login screen views
        loginHeader = (TextView) view.findViewById(R.id.login_header);
        usernameField = (EditText) view.findViewById(R.id.username_field);
        passwordField = (EditText) view.findViewById(R.id.password_field);

        if (this.getArguments() != null){
            Bundle bundle = this.getArguments();
            String username = bundle.getString("username");
            usernameField.setText(username);
        }

        //set up login button action
        Button loginButton = (Button)view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginHeader.setText(getString(R.string.validatingMessage));
                sendLoginPostRequest(usernameField.getText().toString(), passwordField.getText().toString());
            }
        });

        //set up account creation button action
        Button createAccountButton = (Button)view.findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               navigateToSignup();
            }
        });

        //set up hide/show password button action
        Button showHideButton = (Button)view.findViewById(R.id.show_hide_button);
        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHideButton.getText().toString().equals("show")){
                    showHideButton.setText(getString(R.string.hidePassword));
                    passwordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    showHideButton.setText(getString(R.string.showPassword));
                    passwordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        return view;
    }

    /**
     * sends a post request with Strings for username and password to a
     * url stored in the strings xml.
     *
     * @param username username to be sent to the server
     * @param password password to be sent to the server
     */
    private void sendLoginPostRequest(String username, String password){
        try{

            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", username);
            requestBody.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    getString(R.string.remote_server_url, "login", ""),
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //check for passed or failed verification in the response
                                if (response.getString("verification").equals("passed")){

                                    //transfer user data received from http response to userData singleton
                                    UserData userData= UserData.getInstance();

                                    userData.setUserID(response.getJSONObject("user").getInt("id"));
                                    userData.setUsername(response.getJSONObject("user").getString("username"));
                                    userData.setPassword(password);
                                    userData.setRole(response.getJSONObject("user").getString("role"));

                                    int numFriends = response.getJSONObject("user").getJSONArray("friends").length();
                                    Friend[] friendList = new Friend[numFriends];
                                    for (int i = 0; i < numFriends; i++){
                                        JSONObject friendObj = response.getJSONObject("user").getJSONArray("friends").getJSONObject(i);
                                        friendList[i] = new Friend(friendObj.getInt("friendId"), friendObj.getString("username"));
                                    }
                                    userData.setFriendsList(friendList);

                                    int numSentRequests = response.getJSONObject("user").getJSONArray("sentFriendRequests").length();
                                    Friend[] sentRequests = new Friend[numSentRequests];
                                    for (int i = 0; i < numSentRequests; i++){
                                        JSONObject friendObj = response.getJSONObject("user").getJSONArray("sentFriendRequests").getJSONObject(i);
                                        sentRequests[i] = new Friend(friendObj.getInt("friendId"), friendObj.getString("username"));
                                    }
                                    userData.setSentRequestsList(sentRequests);

                                    int numReceivedRequests = response.getJSONObject("user").getJSONArray("receivedFriendRequests").length();
                                    Friend[] receivedRequests = new Friend[numReceivedRequests];
                                    for (int i = 0; i < numReceivedRequests; i++){
                                        JSONObject friendObj = response.getJSONObject("user").getJSONArray("receivedFriendRequests").getJSONObject(i);
                                        receivedRequests[i] = new Friend(friendObj.getInt("friendId"), friendObj.getString("username"));
                                    }
                                    userData.setReceivedRequestsList(receivedRequests);

                                    userData.setGamesWon(response.getJSONObject("user").getInt("gamesPlayed"));
                                    userData.setGamesPlayed(response.getJSONObject("user").getInt("gamesWon"));

                                    //change fragment to main menu
                                    navigateToMainMenu();
                                } else {
                                    //notify user of failed login
                                    loginHeader.setText(getString(R.string.login_failed));
                                }
                            } catch (JSONException ex) {
                                loginHeader.setText(getString(R.string.login_error));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loginHeader.setText(getString(R.string.login_error));
                        }
                    }
            );

            Volley.newRequestQueue(requireContext()).add(request);

        } catch(JSONException ex) {
            loginHeader.setText(getString(R.string.login_error));
        }
    }

    /**
     * method which changes fragment in container to the main menu fragment
     */
    private void navigateToMainMenu(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
    }

    /**
     * changes the fragment in the contariner to the signup fragment
     */
    private void navigateToSignup(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new SignUpScreenFragment()).commit();
    }
}