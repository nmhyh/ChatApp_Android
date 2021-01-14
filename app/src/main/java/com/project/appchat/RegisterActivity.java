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
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText username, email, password, phone, age, confirm_password;

    RadioButton male;
    RadioButton female;
    FirebaseAuth auth;
    DatabaseReference reference;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.editText_username);
        email = findViewById(R.id.editText_email);
        password = findViewById(R.id.editText_password);
        phone = findViewById(R.id.editText_phone);
        confirm_password = findViewById(R.id.editText_comfirmpassword);
        male = findViewById(R.id.radioButton_Male);
        female = findViewById(R.id.radioButton_Female);


        auth = FirebaseAuth.getInstance();

    }

    private void register(final String username, String email, String password, final String phone, final String sex){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("phone", phone);
                            hashMap.put("sex", sex);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void btn_register(View view) {
        String txt_username = username.getText().toString();
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString();
        String txt_phone = phone.getText().toString();
        String txt_confirm_password = confirm_password.getText().toString();
        String txt_sex;
        if(male.isChecked()){
            txt_sex = "Male";
        }else {
            txt_sex = "Female";
        }

        final ProgressDialog pd = new ProgressDialog(this, R.style.Theme_AppCompat_Light_Dialog);
        pd.setTitle("Loading...");
        pd.setMessage("Register Account...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(true);
        pd.show();


        if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)
                || TextUtils.isEmpty(txt_phone) || TextUtils.isEmpty(txt_confirm_password)){
            pd.dismiss();
            Toast.makeText(RegisterActivity.this, "All files are required", Toast.LENGTH_SHORT).show();
        }else if(txt_password.length() < 6) {
            pd.dismiss();
            Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters and confirm password fail", Toast.LENGTH_SHORT).show();
        }else if(!txt_password.equals(txt_confirm_password)){
            pd.dismiss();
            Toast.makeText(RegisterActivity.this, "Password and confirm password must match", Toast.LENGTH_SHORT).show();
        }else{
            pd.setProgress(90);
            pd.setMax(100);

            register(txt_username, txt_email,txt_password, txt_phone, txt_sex);
            pd.dismiss();
        }
    }

    public void login(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}