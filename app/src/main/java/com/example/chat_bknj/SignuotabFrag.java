package com.example.chat_bknj;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignuotabFrag extends Fragment {
    EditText name,email,password;
    Button signup;
    FirebaseAuth auth;
    DatabaseReference reference;
    LoadingDialog loadingDialog=new LoadingDialog(getActivity());

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.register, container, false);
name=root.findViewById(R.id.name);
        password=root.findViewById(R.id.password);
        email=root.findViewById(R.id.email);
signup =root.findViewById(R.id.signup);
auth=FirebaseAuth.getInstance();
signup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
String txname= name.getText().toString();
String txemail=email.getText().toString();
String txpassword=password.getText().toString();
if (TextUtils.isEmpty(txname)||TextUtils.isEmpty(txemail)||TextUtils.isEmpty(txpassword)){
    Toast.makeText(getContext(),"Empty Fields please insert text...",Toast.LENGTH_SHORT).show();
}
else if (txpassword.length()<6){
    Toast.makeText(getContext(),"Password should be at least 6 caracters...",Toast.LENGTH_SHORT).show();
}
else {
    loadingDialog.StartLoading();
    register(txname,txemail,txpassword);
    loadingDialog.dissmiss();
}
    }
});
        return root;
    }
    public void register(String name,String email,String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser=auth.getCurrentUser();
                            String userId=firebaseUser.getUid();
                            reference= FirebaseDatabase.getInstance().getReference("users").child(userId);
                            HashMap<String,String>hashMap=new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("name",name);
                            hashMap.put("imageURL","default");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent=new Intent(getContext(),Authentification.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);


                                    }
                                }
                            });
                        }else {
                            Toast.makeText(getContext(),"Error while sign up...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
