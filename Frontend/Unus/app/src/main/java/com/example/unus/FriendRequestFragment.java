package com.example.unus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendRequestFragment extends Fragment {

    private View view;
    private Button back;
    private TextView friend_reqs;

    public FriendRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend_request, container, false);
        back = (Button) view.findViewById(R.id.backbutton);
        friend_reqs = (TextView) view.findViewById(R.id.friend_reqs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserProfileFragment()).commit();
            }
        });
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.remote_server_url, "user")+UserData.getInstance().getUserID()+"/pending-friend-requests",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        friend_reqs.setText("Friend Requests ("+response.length()+"): ");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        friend_reqs.setText("error");
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);


        return view;
    }
}
