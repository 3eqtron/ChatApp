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

public class LoginTabfrag extends Fragment {
EditText emaillogin,passwordlogin;
Button login;
FirebaseAuth auth;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup)inflater.inflate(R.layout.login,container,false);
emaillogin=root.findViewById(R.id.emaillogin);
passwordlogin=root.findViewById(R.id.passwordlogin);
login=root.findViewById(R.id.login);
auth=FirebaseAuth.getInstance();
        LoadingDialog loadingDialog=new LoadingDialog(getActivity());
login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String txemail=emaillogin.getText().toString();
        String txpass=passwordlogin.getText().toString();
        if (TextUtils.isEmpty(txemail)||TextUtils.isEmpty(txpass)){
            Toast.makeText(getContext(),"All fields are required...",Toast.LENGTH_SHORT).show();
        }
        else {
            auth.signInWithEmailAndPassword(txemail,txpass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                loadingDialog.StartLoading();
                              showmainactivity();
                              loadingDialog.dissmiss();
                            }
                            else {
                                Toast.makeText(getContext(),"Failed to Authenticated...",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }
});

        return root;
    }
    private void showmainactivity() {

        Intent i=new Intent(getContext(),Dashboard.class);
        startActivity(i);
        Toast.makeText(getContext(),"Authentification Valid",Toast.LENGTH_SHORT).show();
    }


}
