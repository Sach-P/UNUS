package com.example.unus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSettingsFragment extends Fragment {

    private View view;
    private TextView username;
    private Button delete_user;

    public UserSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_settings, container, false);
        username = (TextView) view.findViewById(R.id.username);
        delete_user = (Button) view.findViewById(R.id.delete_user);
        username.setText("hello");


        return view;
    }

    private void deleteUser(String username, String password) {
        try{

            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", username);
            requestBody.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.DELETE,
                    getString(R.string.postman_mock_server_url),
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //check for passed or failed verification in the response
                                if (response.getString("verification").equals("passed")){
                                    //change fragment to main menu
                                    //navigateToMainMenu();
                                } else {
                                    //notify user of failed login
                                    //loginHeader.setText(getString(R.string.login_failed));
                                }
                            } catch (JSONException ex) {
                                //loginHeader.setText(getString(R.string.login_error));
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

        } catch(JSONException ex) {
            //loginHeader.setText(getString(R.string.login_error));
        }
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new LogInScreenFragment()).commit();
    }

}

