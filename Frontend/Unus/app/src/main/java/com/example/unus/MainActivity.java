package com.example.unus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

/**
 * Main activity that fragments are run within. Handles web socket messages for the game
 *
 * @author Isaac Blandin
 */
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

    /**
     * Creates a connection to the lobby websocket with an id number to connect to a specific game lobby.
     *
     * @param lobbyID id number of the game lobby to connect to
     */
    @SuppressLint("DefaultLocale")
    public void connectWebSocket(int lobbyID) {
        URI uri;
        try {
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
                        GamePlayFragment gameFrag = (GamePlayFragment) getSupportFragmentManager().findFragmentByTag("gameScreen");

                        if (gameLobbyFrag != null) {
                            try {
                                gameLobbyFrag.onMessage(s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (gameFrag != null && gameFrag.isVisible()){
                            try {
                                gameFrag.onMessage(s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

    /**
     * disconnects the user from the active web socket
     */
    public void disconnectWebSocket(){
        ws.close();
    }

    /**
     * Method called by host of lobby to send an updated list of ids to the other users
     *
     * @param players ArrayList of players' id numbers in the current lobby
     * @throws JSONException
     */
    public void updateLobby(ArrayList<Integer> players) throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("ids", new JSONArray(players));
        String str = obj.toString();

        ws.send(str);
    }

    /**
     * sends a message to kick a user by id number
     *
     * @param id id number of the user to be kicked
     * @throws JSONException
     */
    public void kickUser(int id) throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("kicked", id);
        String str = obj.toString();

        ws.send(str);

    }

    /**
     * send a message as a JSON String from a JSON Object to the active websocket
     *
     * @param jsonObject
     */
    public void sendMessage(JSONObject jsonObject){
        String str = jsonObject.toString();
        ws.send(str);
    }
}