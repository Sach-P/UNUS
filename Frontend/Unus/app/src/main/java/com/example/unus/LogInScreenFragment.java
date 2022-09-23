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
import org.w3c.dom.Text;

import java.util.Objects;

/**
 * A simply login screen fragment which takes the a user's username
 * and password and sends it to the server for verification
 */
public class LogInScreenFragment extends Fragment {

    private View view;
    private Button loginButton;
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

        loginHeader = (TextView) view.findViewById(R.id.login_header);

        usernameField = (EditText) view.findViewById(R.id.username_field);
        passwordField = (EditText) view.findViewById(R.id.password_field);

        loginButton = (Button)view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginHeader.setText("Validating Credentials...");
                sendLoginPostRequest(usernameField.getText().toString(), passwordField.getText().toString());
            }
        });

        return view;
    }

    private void sendLoginPostRequest(String username, String password){
        try{

            JSONObject requestBody = new JSONObject();
            requestBody.put("username", username);
            requestBody.put("password", password);

            LoginStatus loginStatus;

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    getString(R.string.postman_mock_server_url),
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("verification").equals("passed")){
                                    navigateToMainMenu();
                                } else {
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

    private void navigateToMainMenu(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
    }
}