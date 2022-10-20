package com.example.unus;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSearchFragment extends Fragment {

    private View view;
    private Button back;
    private Button search;
    private EditText id;

    public UserSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_search_fragment, container, false);
        back = (Button) view.findViewById(R.id.backbutton);
        search = (Button) view.findViewById(R.id.search_button);
        id = (EditText) view.findViewById(R.id.searchbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserProfileFragment()).commit();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(id.getText().toString());
            }
        });
        return view;
    }
    private void search(String id) {
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.GET,
                    getString(R.string.remote_server_url, "user")+Integer.parseInt(id),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            TextView user = new TextView(view.getContext());
                            Button viewProf = new Button(view.getContext());
                            Button sendReq = new Button(view.getContext());
                            user.setTextSize(30);
                            viewProf.setTextSize(20);
                            viewProf.setText("View");
                            sendReq.setTextSize(20);
                            sendReq.setText("Send Friend Request");
                            try {
                                user.setText(response.getString("username"));
                                viewProf.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
                                        View popupView = inflater.inflate(R.layout.profile_view_layout, null);

                                        //Make Inactive Items Outside Of PopupWindow
                                        boolean focusable = true;

                                        //Create a window with our parameters
                                        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1000, focusable);
                                        popupWindow.showAtLocation(view, Gravity.CENTER, 50, 30);

                                        try {
                                            ((TextView) popupView.findViewById(R.id.username)).setText(response.getString("username"));
                                            ((TextView) popupView.findViewById(R.id.user_id)).setText(response.getString("id"));
                                            ((TextView) popupView.findViewById(R.id.games_played)).setText("Games Played: "+response.getString("games_played"));
                                            ((TextView) popupView.findViewById(R.id.games_won)).setText("Games Won: "+response.getString("games_won"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                sendReq.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sendReq.setText("Request Sent");
                                        sendFriendRequest(Integer.parseInt(id));
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ((LinearLayout)view.findViewById(R.id.results)).addView(user);
                            ((LinearLayout)view.findViewById(R.id.results)).addView(viewProf);
                            ((LinearLayout)view.findViewById(R.id.results)).addView(sendReq);
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
    }

    private void sendFriendRequest(int id) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                "https://3d5d7b90-cdb8-41bc-b45b-cffb50951687.mock.pstmn.io/sendFreindReq/"+id,
                null,
                null, null);

    }
}
