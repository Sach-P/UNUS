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
    private TextView userID;
    private TextView games;
    private TextView wins;
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
        userID = (TextView) view.findViewById(R.id.userID);
        games = (TextView) view.findViewById(R.id.games);
        wins = (TextView) view.findViewById(R.id.wins);
        settings = (Button) view.findViewById(R.id.user_settings);
        back = (Button) view.findViewById(R.id.back);
        username.setText(UserData.getInstance().getUsername());
        userID.setText("UserID: "+ UserData.getInstance().getUserID());
        games.setText("Games Played: "+ UserData.getInstance().getGamesPlayed());
        wins.setText("Games Won: "+ UserData.getInstance().getGamesWon());


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
}
