package com.example.dell.myapplication.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.dell.myapplication.R;

public class DialogItemDetail implements View.OnClickListener {
    private Context context;
    private Dialog dialog;

    public DialogItemDetail (Context context){
        this.context = context;

        this.dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_view_item_detail);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        dialog.show();
    }

    @Override
    public void onClick(View v) {

    }
}
