package com.example.unus;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    WebSocketClient ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_main);
    }

    public GameLobbyFragment getFragment(){
        return (GameLobbyFragment) getSupportFragmentManager().findFragmentByTag("gameLobby");
    }

    @SuppressLint("DefaultLocale")
    public void connectWebSocket(int lobbyID) {
        URI uri;
        try {
            //uri = new URI("wss://demo.piesocket.com/v3/channel_123?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self");
            uri = new URI(String.format("ws://coms-309-029.class.las.iastate.edu:8080/lobbies/%d/%d", lobbyID, UserData.getInstance().getUserID()));
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
                                gameLobbyFrag.onMessage(s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                GameLobbyFragment gameLobbyFrag = (GameLobbyFragment) getSupportFragmentManager().findFragmentByTag("gameLobby");
                gameLobbyFrag.leaveGame();
            }

            @Override
            public void onError(Exception e) {

            }
        };
        ws.connect();
    }

    public void disconnectWebSocket(){
        ws.close();
    }

    public void updateLobby(ArrayList<Integer> players) throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("ids", new JSONArray(players));
        String str = obj.toString();

        ws.send(str);
    }

    public void kickUser(int id) throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("kicked", id);
        String str = obj.toString();

        ws.send(str);

    }

    public void sendMessage(JSONObject jsonObject){
        String str = jsonObject.toString();
        ws.send(str);
    }
}