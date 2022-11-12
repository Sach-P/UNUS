package com.example.unus;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This fragment displays the user settings, for now they include
 * change username, change password and delete user but will most
 * likely be expanded in the future
 * Change user will permanently change the name of the current user
 * logged in
 * change password changes the password
 * delete user will remove the user from the database
 */

public class UserSettingsFragment extends Fragment {

    private View view;
    private TextView top_text;
    private Button delete_user;
    private Button back;
    private Button change_name;
    private Button change_pass;

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
        top_text = (TextView) view.findViewById(R.id.top_text);
        delete_user = (Button) view.findViewById(R.id.delete_user);
        back = (Button) view.findViewById(R.id.back);
        change_name = (Button) view.findViewById(R.id.change_name);
        change_pass = (Button) view.findViewById(R.id.change_password);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserProfileFragment()).commit();
            }
        });

        change_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change(true);
            }
        });

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change(false);
            }
        });

        delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteConfirmation();
            }
        });

        return view;
    }

    public void deleteConfirmation( ) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up_layout, null);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1000, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 50, 30);

        Button delete = (Button) popupView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) popupView.findViewById(R.id.user_prompt)).getText().toString();
                String password = ((EditText) popupView.findViewById(R.id.pass_prompt)).getText().toString();
                if(name.equals(UserData.getInstance().getUsername()) && password.equals(UserData.getInstance().getPassword())) {
                    popupWindow.dismiss();
                    deleteUser(UserData.getInstance().getUserID());
                } else {
                    popupWindow.dismiss();
                    top_text.setText(getString(R.string.invalid_credentials));
                }

            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void change(boolean name) {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up_layout, null);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1000, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 50, 30);
        if(name) {
            ((TextView) popupView.findViewById(R.id.username)).setText(getString(R.string.new_username));
        } else {
            ((TextView) popupView.findViewById(R.id.username)).setText(getString(R.string.new_password));
            ((TextView) popupView.findViewById(R.id.password)).setText(getString(R.string.old_password));
        }


        ((Button) popupView.findViewById(R.id.delete)).setText(getString(R.string.change_button));

        Button delete = (Button) popupView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(((EditText) popupView.findViewById(R.id.pass_prompt)).getText().toString().equals(UserData.getInstance().getPassword())) {
                    changeUser(name, ((EditText) popupView.findViewById(R.id.user_prompt)).getText().toString());
                } else {
                    top_text.setText(getString(R.string.invalid_credentials));
                }
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void deleteUser(int id) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                getString(R.string.remote_server_url, "user", Integer.toString(id)),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        navigateToLogin();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        navigateToLogin();
                    }
                }
        );

        Volley.newRequestQueue(requireContext()).add(request);

    }

    public void changeUser(boolean b, String s) {
        try {
            JSONArray friendsList = new JSONArray();
            for(int i = 0; i < UserData.getInstance().getFriendsList().length; i++) {
                JSONObject temp = new JSONObject();
                temp.put("friendId", UserData.getInstance().getFriendsList()[i].getUserID());
                temp.put("username", UserData.getInstance().getFriendsList()[i].getUsername());
                temp.put("status", "pending");
                friendsList.put(temp);
            }
            //add login credentials to the response body
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", UserData.getInstance().getUserID());
            if(b) {
                requestBody.put("username", s);
                requestBody.put("password", UserData.getInstance().getPassword());
            } else {
                requestBody.put("username", UserData.getInstance().getUsername());
                requestBody.put("password", s);
            }
            requestBody.put("friends", friendsList);
            requestBody.put("gamesPlayed", UserData.getInstance().getGamesPlayed());
            requestBody.put("gamesWon", UserData.getInstance().getGamesWon());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    getString(R.string.remote_server_url, "user", Integer.toString(UserData.getInstance().getUserID())),
                    requestBody,
                    null,
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
        navigateToLogin();
    }

    private void navigateToLogin(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new LogInScreenFragment()).commit();
    }
}

