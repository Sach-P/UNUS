package com.example.geoff.randomnumber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void getNumber(View view){
        EditText value = (EditText) findViewById(R.id.range);
        String strEntry = value.getText().toString();

        if (!TextUtils.isEmpty(strEntry)) {
            int rangeNumber = Integer.parseInt(value.getText().toString());
            Random rand = new Random();
            int num = rand.nextInt(rangeNumber+1);

            TextView result = findViewById(R.id.result);
            result.setText(String.valueOf(num));
        }
    }
}
