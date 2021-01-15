package com.project.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.appchat.AsyncTask.SimpleAsyncTask;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        email= findViewById(R.id.editText_email);
        password = findViewById(R.id.editText_password);
    }

    public void btn_login(View view) {
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString();

        final ProgressDialog pd = new ProgressDialog(this, R.style.Theme_AppCompat_Light_Dialog);
        pd.setTitle("Loading...");
        pd.setMessage("Load data...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.show();

        if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
            Toast.makeText(LoginActivity.this, "All filed are required", Toast.LENGTH_SHORT).show();

        }else{
            // Chứng thực tài khoản
            auth.signInWithEmailAndPassword(txt_email, txt_password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                pd.setProgress(90);
                                pd.setMax(100);
                                pd.dismiss();
                            }else{
                                Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                                pd.setProgress(90);
                                pd.setMax(100);
                                pd.dismiss();
                            }
                        }
                    });
        }
    }

    public void forgot_password(View view) {
        startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
    }

    public void convert_register(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}