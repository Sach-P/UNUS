package com.example.unus;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * this is the global chat for the app.
 * It works with a websocket that will both send and receive messages
 * It will not save messages if the global chat is closed nor will it
 * continue to receive them if it's not open
 */

public class ChatLayout extends Activity {

    private EditText newMessage;
    private Button sendMessage;
    private Button back;
    private View popupView;
    private LinearLayout messageBoard;
    private WebSocketClient ws;
    private String lastUser = "";

    public ChatLayout(View view) {
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.chat_layout, null);

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, 1000, 3000, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.LEFT, 0, 0);

        connectWebSocket();

        newMessage = popupView.findViewById(R.id.new_message);
        sendMessage = popupView.findViewById(R.id.send);
        messageBoard = popupView.findViewById(R.id.messages);
        back = popupView.findViewById(R.id.back);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!newMessage.getText().toString().equals(""))
                    sendMessage(newMessage.getText().toString());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ws.close();
                popupWindow.dismiss();
            }
        });

    }

    /*
    this is where the messages that we get from the websocket will be assessed and displayed
    on the chat screen, it will differtiate between the username and password as well
     */
    private void receivedMessage(String s) throws JSONException {
        try {
            JSONObject object = new JSONObject(s);
            if(!object.get("username").equals(UserData.getInstance().getUsername())) {
                TextView nextMessage = new TextView(popupView.getContext());
                LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textLayoutParams.setMargins(50, 10, 100, 0);
                nextMessage.setLayoutParams(textLayoutParams);
                nextMessage.setPadding(20, 10, 20, 10);
                nextMessage.setText(object.getString("message"));
                nextMessage.setTextColor(popupView.getResources().getColor(R.color.yellow));
                nextMessage.setBackgroundColor(popupView.getResources().getColor(R.color.purple_500));
                nextMessage.setTextSize(20);

                TextView sender = new TextView(popupView.getContext());
                LinearLayout.LayoutParams sendLayoutParams = new LinearLayout.LayoutParams(900, ViewGroup.LayoutParams.WRAP_CONTENT);
                sendLayoutParams.setMargins(40, 0, 10, 0);
                sender.setLayoutParams(sendLayoutParams);
                sender.setPadding(20, 0, 20, 0);
                sender.setText(object.get("username").toString());
                sender.setTextColor(popupView.getResources().getColor(R.color.yellow));
                //sender.setBackgroundColor(popupView.getResources().getColor(R.color.bright_purple));
                sender.setTextSize(15);
                sender.setGravity(Gravity.LEFT);

                if(lastUser.equals(object.getString("username"))){
                    messageBoard.addView(nextMessage, 1);
                } else {
                    messageBoard.addView(nextMessage, 0);
                    messageBoard.addView(sender, 0);
                }
                lastUser = object.getString("username");
            }
        } catch (JSONException ex) {

        }
    }

    private void sendMessage(String message) {
            TextView nextMessage = new TextView(popupView.getContext());
            LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(900, ViewGroup.LayoutParams.WRAP_CONTENT);
            textLayoutParams.setMargins(50, 10, 50, 0);
            nextMessage.setLayoutParams(textLayoutParams);
            nextMessage.setPadding(20, 10, 20, 10);
            nextMessage.setText(message);
            nextMessage.setTextColor(popupView.getResources().getColor(R.color.yellow));
            nextMessage.setBackgroundColor(popupView.getResources().getColor(R.color.purple_500));
            nextMessage.setTextSize(20);
            nextMessage.setGravity(Gravity.RIGHT);
            newMessage.setText("");

            ws.send(message);

            TextView sender = new TextView(popupView.getContext());
            LinearLayout.LayoutParams sendLayoutParams = new LinearLayout.LayoutParams(900, ViewGroup.LayoutParams.WRAP_CONTENT);
            sendLayoutParams.setMargins(10, 0, 10, 0);
            sender.setLayoutParams(sendLayoutParams);
            sender.setPadding(20, 0, 20, 0);
            sender.setText(message);
            sender.setText(UserData.getInstance().getUsername());
            sender.setTextColor(popupView.getResources().getColor(R.color.yellow));
            //sender.setBackgroundColor(popupView.getResources().getColor(R.color.bright_purple));
            sender.setTextSize(15);
            sender.setGravity(Gravity.RIGHT);

            if(lastUser.equals(UserData.getInstance().getUsername())){
                messageBoard.addView(nextMessage, 1);
            } else {
                messageBoard.addView(nextMessage, 0);
                messageBoard.addView(sender, 0);
            }
            lastUser = UserData.getInstance().getUsername();
    }

    private void connectWebSocket() {
        URI uri;
        try {
            //uri = new URI("wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self");
            uri = new URI("ws://coms-309-029.class.las.iastate.edu:8080/global/"+UserData.getInstance().getUserID());

                //will be getString(R.string.fake_websocket)) eventually;
        } catch (URISyntaxException e) {
            TextView nextMessage = new TextView(popupView.getContext());
            nextMessage.setText("Couldn't Connect to Messages"); //will be getString(R.string.message_error)); eventually
            nextMessage.setTextColor(popupView.getResources().getColor(R.color.yellow));
            nextMessage.setBackgroundColor(popupView.getResources().getColor(R.color.purple_500));
            nextMessage.setTextSize(20);

            messageBoard.addView(nextMessage, 0);
            e.printStackTrace();
            return;
        }

        ws = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                //ws.send("Hello from android");
            }

            @Override
            public void onMessage(String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            receivedMessage(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
            }

            @Override
            public void onError(Exception e) {
                TextView nextMessage = new TextView(popupView.getContext());
                nextMessage.setText(""+e);
                nextMessage.setTextColor(popupView.getResources().getColor(R.color.yellow));
                nextMessage.setBackgroundColor(popupView.getResources().getColor(R.color.bright_purple));
                nextMessage.setTextSize(25);

                messageBoard.addView(nextMessage, 0);
            }
        };
        ws.connect();
    }
}