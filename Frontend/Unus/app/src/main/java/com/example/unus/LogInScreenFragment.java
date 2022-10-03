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
 * A simply login screen fragment which takes the a user's username
 * and password and sends it to the server for verification
 */
public class LogInScreenFragment extends Fragment {

    private View view;
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
        view = inflater.inflate(R.layout.fragment_log_in_screen, container, false);

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
     * @param username
     * @param password
     */
    private void sendLoginPostRequest(String username, String password){
        try{

            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", username);
            requestBody.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    getString(R.string.login_url),
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //check for passed or failed verification in the response
                                if (response.getString("verification").equals("passed")){
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

    private void navigateToSignup(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new SignUpScreenFragment()).commit();
    }
}