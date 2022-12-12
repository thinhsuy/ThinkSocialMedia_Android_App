package com.example.thinksocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public Database.DataSource myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DB_OpenDB("ThinkSocial.db");
        TextView underlineText = (TextView)findViewById(R.id.fogetPassText);
        underlineText.setPaintFlags(underlineText.getPaintFlags() |  Paint.UNDERLINE_TEXT_FLAG);
        Button SignIn = (Button) findViewById(R.id.signInBtn);
        Button SignUp = (Button) findViewById(R.id.signUpBtn);
        TextInputEditText etUsername = (TextInputEditText) findViewById(R.id.etUsername);
        TextInputEditText etPassword = (TextInputEditText) findViewById(R.id.etPassword);


        setEventForSignIn(SignIn, etUsername, etPassword);
        setEventForSignUp(SignUp);
    }

    private void setEventForSignUp(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void setEventForSignIn(Button button,  TextInputEditText etUsername, TextInputEditText etPassword){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idUser = Query.checkSignIn(myDB, etUsername.getText().toString(), etPassword.getText().toString());
                //CheckLogin(idUser);
                CheckLogin(1);
            }
        });
    }

    private void CheckLogin(int idUser){
        if (idUser<=0)
            Toast.makeText(this, "Login failed! Check your username or password!", Toast.LENGTH_SHORT).show();
        else {
            ((Global)this.getApplication()).setIdCurrentUser(idUser);
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
    }

    private void DB_OpenDB(String dbName){
        File file = new File(dbName);
        if (file.exists()) {
            Toast.makeText(this, "This DB is existed", Toast.LENGTH_SHORT).show();
            return;
        }
        myDB = new Database.DataSource(this, dbName);
        myDB.open();
    }
}