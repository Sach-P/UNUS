package com.example.unus;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
        getFriends();

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

    public void getFriends() {
        LinearLayout layout = view.findViewById(R.id.friends);
        TextView tv = new TextView(view.getContext());
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText("programmatically created TextView1");
        tv.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
        tv.setPadding(20, 20, 20, 20); // in pixels (left, top, right, bottom)
        layout.addView(tv);
    }
}
