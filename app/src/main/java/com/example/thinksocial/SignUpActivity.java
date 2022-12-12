package com.example.thinksocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    public Database.DataSource myDB;
    TextInputEditText etFullname;
    TextInputEditText etGender;
    TextInputEditText etUsername;
    TextInputEditText etPassword;
    TextInputEditText etConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        DB_OpenDB("ThinkSocial.db");
        etFullname = (TextInputEditText) findViewById(R.id.etFullname);
        etGender = (TextInputEditText) findViewById(R.id.etGender);
        etUsername = (TextInputEditText) findViewById(R.id.etUsername);
        etPassword = (TextInputEditText) findViewById(R.id.etPassword);
        etConfirm = (TextInputEditText) findViewById(R.id.etConfirm);

        setEvent_AgreeBtn();
        setEvent_CancelBtn();
    }

    private void setEvent_CancelBtn(){
        Button cancel = (Button) findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private void setEvent_AgreeBtn(){
        Button agree = (Button) findViewById(R.id.agreeBtn);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInformation()) {
                    addUserToDB();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                }
            }
        });
    }

    private boolean checkInformation(){
        if (!checkEmpty()){
            Toast.makeText(this, "Please fill all boxes", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkGender()) {
            Toast.makeText(this, "Gender should be Male or Female", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Query.checkDuplicateInfor(myDB, etUsername.getText().toString(), "User", "Username")){
            Toast.makeText(this, "This username already exists!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkConfirmPassword(etPassword, etConfirm)) {
            Toast.makeText(this, "Password confirm not as same", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addUserToDB(){
        int nextId = Query.getNextId(myDB, "User", "userId");
        boolean status = Query.addUser (myDB, nextId,
                                        etFullname.getText().toString(),
                                        etGender.getText().toString(),
                                        etUsername.getText().toString(),
                                        etPassword.getText().toString());
        if (!status) Toast.makeText(this, "Add user failed!", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "Add user successfully!", Toast.LENGTH_SHORT).show();
    }

    private boolean checkEmpty(){
        if (etFullname.getText().toString().equals(""))
            return false;
        if (etGender.getText().toString().equals(""))
            return false;
        if (etUsername.getText().toString().equals(""))
            return false;
        if (etPassword.getText().toString().equals(""))
            return false;
        if (etConfirm.getText().toString().equals(""))
            return false;
        return true;
    }

    private boolean checkGender(){
        String gen = etGender.getText().toString();
        if (gen.equals("Male") || gen.equals("Female")) return true;
        else return false;
    }

    private boolean checkConfirmPassword(TextInputEditText pass, TextInputEditText confirm){
        if (pass.getText().toString().equals(confirm.getText().toString())) return true;
        else return false;
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