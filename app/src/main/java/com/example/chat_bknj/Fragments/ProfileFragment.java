package com.example.chat_bknj.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.chat_bknj.R;
import com.example.chat_bknj.model.users;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

CircleImageView image;
TextView username;
DatabaseReference reference;
FirebaseUser firebaseUser;
StorageReference storageReference;
private static final int IMAGE_REQUEST=1;
private Uri imageuri;
private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        image=view.findViewById(R.id.imageprofile);
        username=view.findViewById(R.id.nameprofile);

        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users user=snapshot.getValue(users.class);
                username.setText(user.getName());
                if (user.getImageURL().equals("default")){
                image.setImageResource(R.mipmap.ic_launcher);
                }
                else {
                    Glide.with(getContext()).load(user.getImageURL()).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
        return view;
    }
    public void openImage(){
        Intent intent=new Intent();intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);


    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadimage(){
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
progressDialog.show();

if (imageuri != null){
    final StorageReference filereference =storageReference.child(System.currentTimeMillis()
    +"."+getFileExtension(imageuri));
    uploadTask=filereference.putFile(imageuri);


     uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
        @Override
        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
if (! task.isSuccessful()){
    throw task.getException();
}
return filereference.getDownloadUrl();
        }
    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
       if (task.isSuccessful()){
           Uri downloadUri=task.getResult();
           String mUri=downloadUri.toString();
           reference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
           HashMap <String,Object>map=new HashMap<>();
           map.put("imageURL",mUri);
           reference.updateChildren(map);
           progressDialog.dismiss();
       }else {
           Toast.makeText(getContext(), "Failed to Upload...", Toast.LENGTH_SHORT).show();
           progressDialog.dismiss();
       }
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    });
}else {
    Toast.makeText(getContext(), "No Image Selected...", Toast.LENGTH_SHORT).show();
}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
        && data !=null && data.getData() != null){
            imageuri = data.getData();
            if (uploadTask != null && uploadTask.isInProgress()){

                Toast.makeText(getContext(), "Upload In Progress...", Toast.LENGTH_SHORT).show();
            }
            else {
                uploadimage();
            }
        }
    }
}
