package com.example.unus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
 * A fragment designed to allow the user to create a new account if they do not currently
 * have an account for the app. It will require the user to pick a username along with a password.
 * The password will have to be confirmed by the user. The credentials will be sent to the server
 * to be checked whether it is already used.
 *
 * @author Isaac Blandin
 */
public class SignUpScreenFragment extends Fragment {

    TextView status;
    EditText usernameField;
    EditText passwordField;
    EditText confirmPasswordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up_screen, container, false);

        //initialize sign up screen's views
        status = view.findViewById(R.id.signup_status);
        usernameField = view.findViewById(R.id.signup_username_field);
        passwordField = view.findViewById(R.id.signup_password_field);
        confirmPasswordField = view.findViewById(R.id.signup_confirm_password_field);

        //initialize signup button
                Button signupButton = (Button)view.findViewById(R.id.signup_button);
                signupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = usernameField.getText().toString();
                        String password = passwordField.getText().toString();
                        String confirmPassword = confirmPasswordField.getText().toString();

                status.setText(getString(R.string.validatingMessage));

                if (username.isEmpty()){ //check for empty username field
                    status.setText(getString(R.string.no_username));
                } else if (password.isEmpty()){ //check for empty password field
                    status.setText(getString(R.string.no_password));
                } else if (password.length() < 8 || !containsNumber(password)){ //check if password meets requirements
                    status.setText(getString(R.string.invalid_password));
                } else if (!password.equals(confirmPassword)){ //check if confirm password field matches
                    status.setText(getString(R.string.password_mismatch));
                } else { //check with server if account can be created
                    sendSignupPostRequest(username, password);
                }
            }
        });

        //initialize back button
        Button backButton = (Button)view.findViewById(R.id.back_to_login);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToLogin();
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
    private void sendSignupPostRequest(String username, String password){
        try{

            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", username);
            requestBody.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    getString(R.string.remote_server_url, "signup", ""),
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //check for passed or failed verification in the response
                                if (response.getString("message").equals("passed")){
                                    //change fragment to login
                                    navigateToLogin(username);
                                } else {
                                    //notify user of failed account creation or already taken username
                                    status.setText(getString(R.string.username_unavailable));
                                }
                            } catch (JSONException ex) {
                                status.setText(getString(R.string.signup_error));
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            status.setText(getString(R.string.signup_error));
                        }
                    }
            );

            Volley.newRequestQueue(requireContext()).add(request);

        } catch(JSONException ex) {
            status.setText(getString(R.string.signup_error));
        }
    }

    /**
     * changes screen back to the login screen with the username prefilled
     *
     * @param username username to be prefilled in the login screen
     */
    private void navigateToLogin(String username){

        Bundle bundle = new Bundle();
        bundle.putString("username", username);

        LogInScreenFragment loginFrag = new LogInScreenFragment();
        loginFrag.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, loginFrag).commit();
    }

    /**
     * changes screen back to unfilled login screen
     */
    private void navigateToLogin(){

        LogInScreenFragment loginFrag = new LogInScreenFragment();

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, loginFrag).commit();
    }

    /**
     * checks a string for a number, returns true if there is a number
     *
     * @param str string to be checked for numbers
     *
     * @return whether the string contains a number or not
     */
    private boolean containsNumber(String str){
        for (int i = 0; i < str.length(); i++){
            if (Character.isDigit(str.charAt(i))){
                return true;
            }
        }
        return false;
    }
}