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
        for( int i = 0; i < UserData.getInstance().getFriendsList().length; i++) {
            LinearLayout layout = view.findViewById(R.id.friends);
            LinearLayout newLayout = new LinearLayout(view.getContext());
            newLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tv = new TextView(view.getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setTextSize(20);
            tv.setText(UserData.getInstance().getFriendsList()[i].getUsername());
            newLayout.addView(tv);
            Button button = new Button(view.getContext());
            button.setLayoutParams(new ViewGroup.LayoutParams(250,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setText("view");
            button.setBackgroundColor(this.getResources().getColor(R.color.red));
            button.setTextColor(this.getResources().getColor(R.color.yellow));
            int finalIndex = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getUser(UserData.getInstance().getFriendsList()[finalIndex].getUserID());
                }
            });
            newLayout.addView(button);
            layout.addView(newLayout);
        }
        LinearLayout layout = view.findViewById(R.id.friends);
        Button make_friends = new Button(view.getContext());
        make_friends.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        make_friends.setTextSize(20);
        make_friends.setText("Add Friends");
        layout.addView(make_friends);
        make_friends.setBackgroundColor(this.getResources().getColor(R.color.red));
        make_friends.setTextColor(this.getResources().getColor(R.color.yellow));
        make_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void getUser(int id) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.profile_view_layout, null);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1000, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 50, 30);

        try {
            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("userID", id);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://3d5d7b90-cdb8-41bc-b45b-cffb50951687.mock.pstmn.io/get_user/"+id,
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //check for passed or failed verification in the response
                                if (response.getString("verification").equals("passed")) {
                                    ((TextView) popupView.findViewById(R.id.username)).setText(response.getJSONObject("user").getString("username"));
                                    //((TextView) popupView.findViewById(R.id.user_id)).setText(response.getJSONObject("user").getInt("id"));
                                    ((TextView) popupView.findViewById(R.id.games_played)).setText("Games Played: "+response.getJSONObject("user").getString("games_played"));
                                    ((TextView) popupView.findViewById(R.id.games_won)).setText("Games Won: "+response.getJSONObject("user").getString("games_won"));
                                } else {
                                }
                            } catch (JSONException ex) {
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
