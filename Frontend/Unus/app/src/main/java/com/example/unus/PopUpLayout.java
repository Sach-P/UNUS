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

public class PopUpLayout extends Fragment {

    private View view;
    private TextView username;
    private TextView password;
    private Button delete_user;

    public PopUpLayout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.pop_up_layout, container, false);
        username = (TextView) view.findViewById(R.id.user_prompt);
        password = (TextView) view.findViewById(R.id.pass_prompt);
        delete_user = (Button) view.findViewById(R.id.delete_user);


        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToMainMenu();
                deleteUser("hell", "o");
            }
        });

        return view;

    }

    private void deleteUser(String username, String password) {
        try {
            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", username);
            requestBody.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.DELETE,
                    "https://b82a18c1-e947-44cf-bf54-6d24cab1848c.mock.pstmn.io/delete_user/",
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //check for passed or failed verification in the response
                                if (response.getString("results").equals("deleted")) {

                                    navigateToMainMenu();
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

        } catch (JSONException ex) {
            //loginHeader.setText(getString(R.string.login_error));
        }
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new LogInScreenFragment()).commit();
    }


    private void navigateToMainMenu(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
    }
}
