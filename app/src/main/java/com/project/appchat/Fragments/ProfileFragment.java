package com.project.appchat.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.project.appchat.MainActivity;
import com.project.appchat.Model.User;
import com.project.appchat.R;
import com.project.appchat.RegisterActivity;
import com.project.appchat.StartActivity;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    CircleImageView image_profile;
    EditText username, email, phone;
    RadioButton male;
    RadioButton female;
    Button btn_updateProfile, btn_delete;

    DatabaseReference reference;
    FirebaseUser fuser;

    StorageReference storageReference;
    private static final  int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;


    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile = view.findViewById(R.id.profile_image1);
        username = view.findViewById(R.id.username_pro);

        email = view.findViewById(R.id.editText_email);
        phone = view.findViewById(R.id.editText_phone);
        male = view.findViewById(R.id.radioButton_Male);
        female = view.findViewById(R.id.radioButton_Female);
        btn_updateProfile = view.findViewById(R.id.btn_update);
        btn_delete = view.findViewById(R.id.btn_delete);

        auth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        // Hiển thị thông tin cá nhân
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                username.setText(user.getUsername());
                email.setText(fuser.getEmail());
                if(user.getSex().equalsIgnoreCase("Male")){
                    male.setChecked(true);
                } else {
                    female.setChecked(true);
                }
                phone.setText(user.getPhone());

                if(user.getImageURL().equals("default")) {
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    if(getActivity() == null) {
                        return;
                    }
                    Glide.with(getActivity()).load(user.getImageURL()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Thêm hình ảnh
        image_profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        // Cập nhật thông tin cá nhân
        btn_updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Uploading");
                pd.show();

                if(fuser != null){
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("username", username.getText().toString());
                    String user = username.getText().toString();
                    Log.d("user", user);
                    hashMap.put("phone", phone.getText().toString());
                    if(male.isChecked()){
                        hashMap.put("sex", "Male");
                    }else {
                        hashMap.put("sex", "Female");
                    }
                    hashMap.put("search", username.getText().toString().toLowerCase());
                    reference.updateChildren(hashMap);
                    Toast.makeText(getActivity(), "Upload Successful", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            }
        });

        // Xóa tài khoản
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // đăng xuất và trở về StartActivity
                startActivity(new Intent(getContext(), StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        return view;
    }

    // Mở thư viện ảnh
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // Upload hình ảnh
    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    } else{
                        Toast.makeText(getContext(),"Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else{
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if(uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else{
                uploadImage();
            }
        }
    }

}