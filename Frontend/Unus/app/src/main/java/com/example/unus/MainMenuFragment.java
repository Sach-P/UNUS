package com.example.unus;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Main Menu Screen Fragment
 */
public class MainMenuFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        TextView username = (TextView) view.findViewById(R.id.username_display);
        username.setText(UserData.getInstance().getUsername());
        TextView id = (TextView) view.findViewById(R.id.id_display) ;
        id.setText(getString(R.string.id_display, UserData.getInstance().getUserID()));

        //add clickable interaction to the avatar image
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar_pic);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new UserProfileFragment()).commit();
            }
        });

        //add clickable interaction to join game button
        Button joinGame = (Button) view.findViewById(R.id.join_game_button);
        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //put navigation to join game fragment
            }
        });

        //add clickable interaction to host game button
        Button hostGame = (Button) view.findViewById(R.id.host_game_button);
        hostGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new GameLobbyFragment(), "gameLobby").commit();
                getActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });

        //this is the global chat button and it takes you to global chat
        ((ImageView) view.findViewById(R.id.global_chat)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatLayout cl = new ChatLayout(view);

            }
        });

        //add clickable interaction to leaderboard button
                Button leaderboard = (Button) view.findViewById(R.id.leaderboard_button);
                leaderboard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new LeaderboardFragment()).commit();
                    }
        });

        //add clickable interaction to logout button
        Button logout = (Button) view.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new LogInScreenFragment()).commit();
            }
        });

        return view;
    }
}