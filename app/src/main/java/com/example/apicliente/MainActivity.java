package com.example.apicliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {

    Button btn_register;
    TextView nombres,apellidos,telefono,btn_re;
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_register = findViewById(R.id.btn_register);
        btn_re = findViewById(R.id.btn_re);
        nombres = findViewById(R.id.nombres);
        apellidos = findViewById(R.id.apellidos);
        telefono = findViewById(R.id.telefono);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrar();
            }
        });

        btn_re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
    }

    public void Registrar(){
        String nm = nombres.getText().toString().trim();
        String ape = apellidos.getText().toString().trim();
        String tel = telefono.getText().toString().trim();
        String ema = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String pwd2 = password.getText().toString().trim();

        // Verificar que los campos no estén vacíos
        if (nm.isEmpty() || ape.isEmpty() || tel.isEmpty() || ema.isEmpty() || pwd.isEmpty() || pwd2.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Construir la URL y los datos a enviar
        String urls = "https://yazgovu.nyc.dom.my.id/api/Registro";
        String datos = "nombres=" + nm + "&apellidos=" + ape + "&telefono=" + tel + "&email=" + ema + "&password=" + pwd + "&password_confirmation=" + pwd2;

        // Ejecutar la solicitud HTTP en un hilo separado
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urls);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    // Escribir los datos en el cuerpo de la solicitud
                    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(datos);
                    writer.flush();
                    writer.close();

                    // Leer la respuesta del servidor
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_CREATED) {
                        // Registro exitoso
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        final String responseMessage = response.toString();

                        // Mostrar mensaje al usuario en el hilo de la interfaz de usuario principal
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Exito", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this,Home.class);
                                intent.putExtra("datos",responseMessage);
                                startActivity(intent);
                            }
                        });
                    } else {
                        // Error en la solicitud
                        final String errorMessage = "Error en el servidor: " + responseCode;
                        // Mostrar mensaje al usuario en el hilo de la interfaz de usuario principal
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    // Error de conexión
                    e.printStackTrace();
                    final String errorMessage = "Error de conexión: " + e.getMessage();
                    // Mostrar mensaje al usuario en el hilo de la interfaz de usuario principal
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}