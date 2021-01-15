package com.project.appchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.appchat.Adapter.MessageAdapter;
import com.project.appchat.Model.Chat;
import com.project.appchat.Model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUserActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username, name, sex, phone, status;

    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;

    String userid;
    ImageButton btn_call;
    EditText txt_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;

    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        intent = getIntent();
        userid = intent.getStringExtra("userid");
        Log.d("user", userid);

        profile_image = findViewById(R.id.profile_image3);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        sex = findViewById(R.id.sex);
        phone = findViewById(R.id.phone);
        status = findViewById(R.id.status);
        btn_call = findViewById(R.id.btn_phone);


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                name.setText(user.getUsername());
                sex.setText(user.getSex());
                phone.setText(user.getPhone());
                status.setText(user.getStatus());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else{
                    // change this
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // and this
                startActivity(new Intent(ProfileUserActivity.this, MessageActivity.class). putExtra("userid", userid)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String uri = phone.getText().toString();
                Uri call = Uri.parse(uri);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: " + phone.getText().toString()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                else{
                    Log.d("ImplicitIntent", "Can't handle this!");
                }
            }
        });
    }
}