package com.example.unus;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/*
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
 */


public class ChatLayout {

    private EditText newMessage;
    private Button sendMessage;
    private Button back;
    private View popupView;
    private LinearLayout messageBoard;

    public ChatLayout(View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.chat_layout, null);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 3000, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);

        newMessage = popupView.findViewById(R.id.new_message);
        sendMessage = popupView.findViewById(R.id.send);
        messageBoard = popupView.findViewById(R.id.messages);
        back = popupView.findViewById(R.id.back);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(newMessage.getText().toString());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }

    private void receivedMessage(String message) {

    }

    private void sendMessage(String message) {
        TextView nextMessage = new TextView(popupView.getContext());
        nextMessage.setText(message);
        nextMessage.setTextColor(popupView.getResources().getColor(R.color.yellow));
        nextMessage.setBackgroundColor(popupView.getResources().getColor(R.color.bright_purple));
        nextMessage.setTextSize(25);
        newMessage.setText("");

        messageBoard.addView(nextMessage);

    }
}