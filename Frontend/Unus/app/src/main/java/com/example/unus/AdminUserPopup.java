package com.example.unus;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AdminUserPopup {

    private View popupView;
    private TextView username;
    private TextView userID;
    private EditText played;
    private EditText won;
    private Button change;

    public AdminUserPopup(View view, Friend user) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.admin_user_popup, null);

        username = (TextView) popupView.findViewById(R.id.username);
        userID = (TextView) popupView.findViewById(R.id.user_id);
        played = (EditText) popupView.findViewById(R.id.played);
        won = (EditText) popupView.findViewById(R.id.won);
        change = (Button) popupView.findViewById(R.id.change);

        username.setText(user.getUsername());
        userID.setText(Integer.toString(user.getUserID()));
        played.setText(Integer.toString(user.getGamesPlayed()));
        won.setText(Integer.toString(user.getGamesWon()));

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 1500, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }
}
