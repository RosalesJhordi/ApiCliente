package com.example.apicliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {
    Button btn_loginuser;
    EditText correo,password;
    TextView btn_re;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_loginuser = findViewById(R.id.btn_loginuser);
        btn_re = findViewById(R.id.btn_re);
        correo = findViewById(R.id.correo);
        password = findViewById(R.id.password);

        btn_loginuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btn_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        });
    }

    public void login() {
        String ema = correo.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        // Verificar que los campos no estén vacíos
        if (ema.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String urls = "https://yazgovu.nyc.dom.my.id/api/Login";
        String datos = "&email=" + ema + "&password=" + pwd;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urls);
                    HttpURLConnection conex = (HttpURLConnection) url.openConnection();
                    conex.setRequestMethod("POST");
                    conex.setDoOutput(true);

                    OutputStreamWriter whiter = new OutputStreamWriter(conex.getOutputStream());
                    whiter.write(datos);
                    whiter.flush();
                    whiter.close();

                    int response = conex.getResponseCode();
                    if (response == HttpURLConnection.HTTP_CREATED){
                        // Login exitoso
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conex.getInputStream()));
                        StringBuilder respon= new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            respon.append(line);
                        }
                        reader.close();
                        final String responseMessage = respon.toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this, "Exito", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this,Home.class);
                                intent.putExtra("datos", responseMessage);
                                startActivity(intent);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Login.this, "Error en la solicitud: " + response, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    conex.disconnect();
                }catch (IOException e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}