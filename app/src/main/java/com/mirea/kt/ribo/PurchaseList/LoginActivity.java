package com.mirea.kt.ribo.PurchaseList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    int result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        EditText login = findViewById(R.id.login);
        EditText password = findViewById(R.id.password);
        Button btnStart = findViewById(R.id.logInButton);
        btnStart.setOnClickListener(view -> {
            String loginToInsert = login.getText().toString();
            String passwordToInsert = password.getText().toString();
            String server = "https://android-for-students.ru";
            String serverPath = "/coursework/login.php ";
            HashMap<String, String> map = new HashMap<>();
            map.put("lgn", loginToInsert);
            map.put("pwd", passwordToInsert);
            map.put("g", "RIBO-04-22");
            HTTPRunnable1 httpRunnable = new HTTPRunnable1(server + serverPath, map);
            Thread th = new Thread(httpRunnable);
            th.start();
            JSONObject jSONObject = new JSONObject();
            try {
                th.join();
            } catch (InterruptedException ex) {
                Toast.makeText(this, "thread has interrupted the current thread.", Toast.LENGTH_SHORT).show();
            }
            finally {
                try {
                    jSONObject = new JSONObject(httpRunnable.getResponseBody());
                } catch (JSONException e) {
                    Toast.makeText(this, "Parsing request failed", Toast.LENGTH_SHORT).show();
                }
                try {
                    result = jSONObject.getInt("result_code");
                } catch (JSONException e) {
                    Toast.makeText(this, "Parsing request failed", Toast.LENGTH_SHORT).show();
                }
            }
            if (result != -1 ) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else Toast.makeText(this, "Неправильное имя пользователя или пароль", Toast.LENGTH_SHORT).show();
        });
    }
}
