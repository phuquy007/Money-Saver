package com.phuquytran_300303518.moneysaver.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.phuquytran_300303518.moneysaver.Entities.User;
import com.phuquytran_300303518.moneysaver.R;

public class ProfileActivity extends AppCompatActivity {
    User user;
    TextView txtFirstName, txtLastName, txtPhoneNumber;
    ImageView imgEditFirstName, imgEditLastName, imgEditPhoneNumber, imgAvatar;
    Button btnDone;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private final int IMAGE_REQUEST = 25;
    private Uri imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtFirstName = findViewById(R.id.txtProfile_FirstName);
        txtLastName = findViewById(R.id.txtProfile_LastName);
        txtPhoneNumber = findViewById(R.id.txtProfile_PhoneNumber);

        imgEditFirstName = findViewById(R.id.imgProfile_EditFirstName);
        imgEditLastName = findViewById(R.id.imgProfile_EditLastName);
        imgEditPhoneNumber = findViewById(R.id.imgProfile_EditPhoneNumber);

        imgAvatar = findViewById(R.id.imgProfile_Avatar);
        imgAvatar.setOnClickListener(view -> {
            chooseAvatar();
        });

        btnDone = findViewById(R.id.btnProfile_Done);
        btnDone.setOnClickListener(view -> {
            finish();
        });

        getUser();

        imgEditFirstName.setOnClickListener(view -> {
            edit_Click("firstName");
        });
        imgEditLastName.setOnClickListener(view -> {
            edit_Click("lastName");
        });
        imgEditPhoneNumber.setOnClickListener(view -> {
            edit_Click("phoneNumber");
        });
    }

    private void getUser(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = User.fromFirebase(snapshot);
                txtFirstName.setText(user.getFirstName());
                txtLastName.setText(user.getLastName());
                txtPhoneNumber.setText(user.getPhoneNumber());

                String avatar = snapshot.child("avatar").getValue(String.class);
                if (avatar == null)
                    return;
//                Toast.makeText(ProfileActivity.this, avatar, Toast.LENGTH_SHORT).show();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference("avatars");
                Glide.with(ProfileActivity.this).load(storageReference.child(avatar))
                        .placeholder(R.drawable.placeholder_square)
                        .into(imgAvatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void edit_Click(String param){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.new_limit_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("New Limit");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final EditText newValue = alertLayout.findViewById(R.id.edt_newValue);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                EditText newValue = alertLayout.findViewById(R.id.edt_newValue);
                String newStringValue = newValue.getText().toString();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                switch (param){
                    case "firstName":
                        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("firstName").setValue(newStringValue);
                        txtFirstName.setText(newStringValue);
                        break;
                    case "lastName":
                        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("lastName").setValue(newStringValue);
                        txtLastName.setText(newStringValue);
                        break;
                    case "phoneNumber":
                        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("phoneNumber").setValue(newStringValue);
                        txtPhoneNumber.setText(newStringValue);
                        break;
                }

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void chooseAvatar(){
        Intent intent = new Intent();
        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
                if(data.getData()!= null){
                    imgPath = data.getData();
                }

                imgAvatar.setImageURI(imgPath);
                String avatarName = FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg";
                StorageReference ref = FirebaseStorage.getInstance().getReference().child("avatars").child(avatarName);
                ref.putFile(imgPath);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("avatar").setValue(avatarName);
            } else {
                Toast.makeText(this, "Not yet choose an avatar ",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}