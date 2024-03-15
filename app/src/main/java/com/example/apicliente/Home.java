package com.example.apicliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity {

    TextView nombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nombre = findViewById(R.id.nombre);
        String data = getIntent().getStringExtra("datos");
        try {
            JSONObject jsonData = new JSONObject(data);
            JSONObject userObject = jsonData.getJSONObject("user");
            String nombres = userObject.getString("nombres");
            nombre.setText(nombres);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}