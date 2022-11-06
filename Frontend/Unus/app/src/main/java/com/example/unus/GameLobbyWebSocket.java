package com.example.unus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class GameLobbyWebSocket extends AppCompatActivity {

    WebSocketClient ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @SuppressLint("DefaultLocale")
    private void connectWebSocket(int lobbyID) {
        URI uri;
        try {
            uri = new URI(String.format("ws://coms-309-029.class.las.iastate.edu:8080/%d/%d", lobbyID, UserData.getInstance().getUserID()));
        } catch (URISyntaxException e) {
           return;
        }

        ws = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                //nothing to see here
            }

            @Override
            public void onMessage(String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GameLobbyFragment gameLobbyFrag = (GameLobbyFragment) getSupportFragmentManager().findFragmentByTag("gameLobby");
                        try {
                            gameLobbyFrag.onMessage(new JSONObject(s));
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

            }
        };
        ws.connect();
    }
}