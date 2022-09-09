package com.example.randomnumbergenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                getNumber(v);
            }
        });
    }

    public void getNumber(View view){
        EditText value = (EditText) findViewById(R.id.editTextNumber);
        String strEntry = value.getText().toString();

        if (!TextUtils.isEmpty(strEntry)) {
            int rangeNumber = Integer.parseInt(value.getText().toString());
            Random rand = new Random();
            int num = rand.nextInt(rangeNumber+1);

            TextView result = findViewById(R.id.prompt);
            result.setText(String.valueOf(num));
        }
    }
}