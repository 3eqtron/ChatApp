package com.example.chat_bknj;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
   public Activity activity;
  public    AlertDialog dialog;
    public LoadingDialog(Activity myActivity){
        activity = myActivity;
    }
    void StartLoading(){
AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }
    void dissmiss(){
        dialog.dismiss();
    }
}
