package com.example.myfirtapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Admins extends AppCompatActivity {
    private String categoryName,prodectName,prodectDiscription,prodectPrice,dateStorege,downloadUrlImage;
    private ImageView imageView;
    private EditText name,descreption,price;
    private Button addProdect;
    private Uri imageUri;
    private StorageReference storageReferenceImage;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins);
        categoryName = getIntent().getExtras().get("category").toString();
        Toast.makeText(this,categoryName,Toast.LENGTH_LONG).show();
        storageReferenceImage = FirebaseStorage.getInstance().getReference().child("Product images");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        progressDialog = new ProgressDialog(this);


        imageView = findViewById(R.id.picture);
        name = findViewById(R.id.prodect_name);
        descreption = findViewById(R.id.prodect_descreption);
        price = findViewById(R.id.prodect_price);
        addProdect = findViewById(R.id.addPro);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openGalary();
            }
        });
        addProdect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProdeectData();
            }
        });
    }

    private void validateProdeectData() {
        prodectName = name.getText().toString();
        prodectDiscription = descreption.getText().toString();
        prodectPrice = price.getText().toString();
        if (TextUtils.isEmpty(prodectName)){
            Toast.makeText(Admins.this,"name is mondatory",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(prodectDiscription)){
            Toast.makeText(Admins.this,"description is mondatory",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(prodectPrice)){
            Toast.makeText(Admins.this,"price is mondatory",Toast.LENGTH_SHORT).show();
        }else if (imageView==null){
            Toast.makeText(Admins.this,"Image is mondatory",Toast.LENGTH_SHORT).show();
        }else {
            storeImageProdect();
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void storeImageProdect() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a");
        dateStorege = formatter.format(date);
        final StorageReference storageReference = storageReferenceImage.child(imageUri.getLastPathSegment()+dateStorege + ".jpg");
        final UploadTask uploadTask = storageReference.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Admins.this,"Error"+e.toString(),Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admins.this,"picture add success ....",Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if (!task.isSuccessful()){
                           progressDialog.dismiss();
                           throw task.getException();

                       }
                       downloadUrlImage = storageReference.getDownloadUrl().toString();
                       return storageReference.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            imageUri = task.getResult();
                            Toast.makeText(Admins.this,"got product image successfully",Toast.LENGTH_SHORT).show();
                            saveProductInfo();
                        }
                    }
                });
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void saveProductInfo()
    {
        progressDialog.setTitle("add Product");
        progressDialog.setMessage("Please wait while product adding");
        progressDialog.show();
        LocalDate date1 = LocalDate.now();
        LocalTime time1 = LocalTime.now();
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        String date2 = formatterDate.format(date1);
        String time2 = formatterTime.format(time1);
        HashMap<String,Object> productMap = new HashMap<>();
        productMap.put("pId",dateStorege);
        productMap.put("date",date2);
        productMap.put("time",time2);
        productMap.put("image",imageUri.toString());
        productMap.put("category",categoryName);
        productMap.put("price",prodectPrice);
        productMap.put("discreption",prodectDiscription);
        productMap.put("name",prodectName);
        databaseReference.child(dateStorege).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent intent = new Intent(Admins.this,category.class);
                    startActivity(intent);
                    Toast.makeText(Admins.this,"product add successful",Toast.LENGTH_SHORT).show();

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(Admins.this,"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGalary() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data !=null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
