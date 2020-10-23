package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirtapp.Prevalent.Prevalent;
import com.example.myfirtapp.viewHolder.ProductViewHolder;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
       private CircleImageView profileImage;
       private TextView update, close,changeImageProfile;
       private EditText username, password,email;
       private String checker="",myUri;
       private Uri imageUri;
       private StorageReference storageReference;
       private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        profileImage = findViewById(R.id.profile_image_setting);
        update = findViewById(R.id.updateBtn);
        close = findViewById(R.id.closeBtn);
        changeImageProfile = findViewById(R.id.change_image_profile);
        username = findViewById(R.id.username_filde);
        password = findViewById(R.id.password_fild);
        email = findViewById(R.id.email_fild);
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile iamge");
        getUserInfo(profileImage,username,password,email);
        changeImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";
                CropImage.activity(imageUri).setAspectRatio(1,1)
                        .start(Settings.this);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });
    }

    private void saveUserInfo() {
        if (TextUtils.isEmpty(username.getText())||TextUtils.isEmpty(password.getText())||TextUtils.isEmpty(email.getText())){
            Toast.makeText(Settings.this, "please fill all informations", Toast.LENGTH_SHORT).show();
        }else if (checker.equals("clicked")){
            UploadImage();
        }else{
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("update info");
            progressDialog.setMessage("please wait while informations update ");
            progressDialog.show();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
            HashMap<String ,Object> usermap = new HashMap<>();
            usermap.put("name",username.getText().toString());
            usermap.put("password",password.getText().toString());
            usermap.put("email",email.getText().toString());
            ref.child(Prevalent.curentOnlineuser.getUsername()).updateChildren(usermap);
            progressDialog.dismiss();
            Intent intent = new Intent(Settings.this,Scurity_questions.class);
            intent.putExtra("from","setting");
            startActivity(intent);
            Toast.makeText(Settings.this, "Profile info are update successfully", Toast.LENGTH_SHORT).show();

        }
    }

    private void UploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("update info");
        progressDialog.setMessage("please wait while the informations update");
        progressDialog.show();
        final StorageReference storageReference1 = storageReference.child(Prevalent.curentOnlineuser.getEmail()+".jpg");
        uploadTask = storageReference1.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception
            {
            if (!task.isSuccessful()){
                throw task.getException();
            }
                return storageReference1.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    myUri = downloadUri.toString();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
                    HashMap<String ,Object> usermap = new HashMap<>();
                    usermap.put("username",username.getText().toString());
                    usermap.put("password",password.getText().toString());
                    usermap.put("email",email.getText().toString());
                    usermap.put("image",myUri);
                    ref.child(Prevalent.curentOnlineuser.getUsername()).updateChildren(usermap);
                    progressDialog.dismiss();
                    startActivity(new Intent(Settings.this,Home.class));
                    Toast.makeText(Settings.this, "Profile info are update successfully", Toast.LENGTH_SHORT).show();

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Settings.this, "something wrong is happen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){

            CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
            imageUri = activityResult.getUri();
            profileImage.setImageURI(imageUri);
        }else{
            Toast.makeText(Settings.this, "Error please try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.this,Settings.class));
            finish();
        }
    }

    private void getUserInfo(final CircleImageView profileImage, final EditText username, final EditText password, final EditText email) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.curentOnlineuser.getUsername());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("image").exists()){
                        String usern = snapshot.child("username").getValue().toString();
                        String passw = snapshot.child("password").getValue().toString();
                        String emaail1= snapshot.child("email").getValue().toString();
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profileImage);
                        username.setText(usern);
                        password.setText(passw);
                        email.setText(emaail1);
                    }else{
                        String usern = snapshot.child("username").getValue().toString();
                        String passw = snapshot.child("password").getValue().toString();
                        String emaail1= snapshot.child("email").getValue().toString();
                        username.setText(usern);
                        password.setText(passw);
                        email.setText(emaail1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
