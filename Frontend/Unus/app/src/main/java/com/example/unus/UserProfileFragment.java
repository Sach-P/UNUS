package com.example.unus;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
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

public class UserProfileFragment extends Fragment {

    private View view;
    private TextView username;
    private Button settings;
    private Button back;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        username = (TextView) view.findViewById(R.id.username);
        settings = (Button) view.findViewById(R.id.user_settings);
        back = (Button) view.findViewById(R.id.back);
        username.setText("username");
        getStats();


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserSettingsFragment()).commit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new MainMenuFragment()).commit();
            }
        });


        return view;
    }

    public void getStats() {
        try {
            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", "12345");

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://b82a18c1-e947-44cf-bf54-6d24cab1848c.mock.pstmn.io/get_stats",
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                ((TextView) view.findViewById(R.id.games)).setText("Games Played: " + response.getString("GamesPlayed"));
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

        } catch (JSONException ex) {
            //loginHeader.setText(getString(R.string.login_error));
        }
    }
}
