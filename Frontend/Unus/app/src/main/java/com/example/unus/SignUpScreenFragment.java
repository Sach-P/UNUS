package com.example.unus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A fragment designed to allow the user to create a new account if they do not currently
 * have an account for the app. It will require the user to pick a username along with a password.
 * The password will have to be confirmed by the user. The credentials will be sent to the server
 * to be checked whether it is already used.
 */
public class SignUpScreenFragment extends Fragment {

    TextView header;
    EditText usernameField;
    EditText passwordField;
    EditText confirmPasswordField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up_screen, container, false);
    }
}