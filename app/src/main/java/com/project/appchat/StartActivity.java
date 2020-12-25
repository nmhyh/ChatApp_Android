package com.project.appchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.appchat.AsyncTask.SimpleAsyncTask;

public class StartActivity extends AppCompatActivity {
    ImageView imageView;
    SimpleAsyncTask simpleAsyncTask;
    ProgressBar progressBar;

    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imageView = findViewById(R.id.imageView);
        this.progressBar = findViewById(R.id.progress_horizontal);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if user is null
        if(firebaseUser != null) {
            simpleAsyncTask = new SimpleAsyncTask(progressBar);
            simpleAsyncTask.execute();
            if (progressBar.getMax() == 100) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else{
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
        }
    }

}