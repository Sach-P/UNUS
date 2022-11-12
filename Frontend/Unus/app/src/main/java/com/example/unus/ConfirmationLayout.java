package com.example.unus;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

/**
 * This popup view will be displayed when the user does an action
 * that may need to be confirmed. Such as logging out or removing
 * a friend or changing a user/password
 */

import org.java_websocket.client.WebSocketClient;

public class ConfirmationLayout {
    private Button confirm;
    private Button back;
    private View popupView;
    private boolean result;
    private boolean press;

    public ConfirmationLayout(View view) {

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.confirmation_layout, null);
        confirm = popupView.findViewById(R.id.confirm);
        back = popupView.findViewById(R.id.back);
        result = false;
        press = false;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 700, 700, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.LEFT, 400, -300);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = true;
                press = true;
                popupWindow.dismiss();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                press = true;
                popupWindow.dismiss();
            }
        });
    }

    public boolean confirm() {
        return result;
    }

    public boolean pressed() {
        return press;
    }
}
