package com.ecomway.touristo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecomway.touristo.model.UserData;
import com.ecomway.touristo.utils.SharePref;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    CircleImageView imgperson;
    CheckBox checkmale, checkfemale;
    String Gender;
    SharePref pref;
    Uri filePath;
    StorageReference ref;
    String UserType;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseAuth auth;
    private EditText editName, editEmail, editAge, editCountry;
    private DatabaseReference mDatabase, refroot;
    TextView txtsave;
    ImageView backimg;
    private final int PICK_IMAGE_REQUEST = 71;

    private Uri imageUri;
    int RESULT_LOAD_IMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        init();
        onClicked();
    }

    private void onClicked() {
        txtsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (TextUtils.isEmpty(editName.getText().toString())) {
                        Toast.makeText(ProfileActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(editEmail.getText().toString())) {
                        Toast.makeText(ProfileActivity.this, "Please enter Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(editCountry.getText().toString())) {
                        Toast.makeText(ProfileActivity.this, "Please enter Country", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(editAge.getText().toString())) {
                        Toast.makeText(ProfileActivity.this, "Please enter age", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(Gender)) {
                        Toast.makeText(ProfileActivity.this, "Please Specify Gender", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    uploadImage();


                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        imgperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         /*      finish();
                auth = FirebaseAuth.getInstance();
                auth.signOut();*/
                ProfileActivity.this.finish();
            }
        });
        CheckFn();
    }

    private void init() {
        pref = new SharePref(ProfileActivity.this);
        checkmale = findViewById(R.id.checkmale);
        checkfemale = findViewById(R.id.checkfemale);
        if (pref.getGender().equals("Male")) {
            checkfemale.setEnabled(false);
            checkmale.setChecked(true);
        } else {
            checkmale.setEnabled(false);
            checkfemale.setChecked(true);
        }
        imgperson = findViewById(R.id.circleview);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.person);
        Glide.with(ProfileActivity.this).load(pref.getImagePath()).apply(requestOptions).into(imgperson);
        backimg = findViewById(R.id.gobackplease);
        txtsave = findViewById(R.id.txtsave);

        editName = findViewById(R.id.txteditName);
        editName.setText(pref.getName());
        editEmail = findViewById(R.id.editEmail);
        editAge = findViewById(R.id.editage);
        editAge.setText(pref.getAge());
        if (Objects.requireNonNull(getIntent().getExtras()).getString("email") != null) {
            editEmail.setText(getIntent().getExtras().getString("email"));
        } else editEmail.setText(pref.getEmail());
        editCountry = findViewById(R.id.editcountry);
        editCountry.setText(pref.getCountry());
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    public void CheckFn() {
        checkmale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkmale.isChecked()) {
                    Gender = "Male";
                    checkfemale.setEnabled(false);
                } else {
                    checkfemale.setEnabled(true);
                }
            }
        });
        checkfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkfemale.isChecked()) {
                    Gender = "Female";
                    checkmale.setEnabled(false);
                } else {
                    checkmale.setEnabled(true);
                }
            }
        });
    }

    private void writeNewUser(String userId, String name, String country, String email, String age, String gender, String imageUrl) {
        UserData user = new UserData();
        user.setName(name);
        user.setEmail(email);
        user.setCountry(country);
        user.setGender(gender);
        user.setAge(age);
        user.setImagepath(imageUrl);
        mDatabase.child("users").child(userId).setValue(user);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgperson.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            ref = storageReference.child("images/" + auth.getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri dlUri = uri;

                                    writeNewUser(auth.getUid(), editName.getText().toString(), editCountry.getText().toString(), editEmail.getText().toString(), editAge.getText().toString(), Gender, dlUri.toString());
                                    pref.setName(editName.getText().toString());
                                    pref.setGender(Gender);
                                    pref.setAge(editAge.getText().toString());
                                    pref.setCountry(editCountry.getText().toString());
                                    pref.setImagePath(dlUri.toString());
                                    pref.setUserId(auth.getUid());
                                    pref.setEmail(editEmail.getText().toString());
                                    Toast.makeText(ProfileActivity.this, "PROFILE SAVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
    }

}
