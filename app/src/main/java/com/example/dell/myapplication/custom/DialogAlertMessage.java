package com.example.dell.myapplication.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.myapplication.R;

import java.util.Objects;

public class DialogAlertMessage implements View.OnClickListener {
    private Dialog dialog;
    private Context context;
    private ButtonSkipListener mSkip;

    public DialogAlertMessage(Context context, ButtonSkipListener skip) {
        this.context = context;
        this.mSkip = skip;
    }

    public void onDisplayAlertMessage(String alertTitle, String alertMessage) {
        dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_alert_message);
        Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
        dialog.setCancelable(true);
        dialog.show();

        TextView tvAlertTitle = dialog.findViewById(R.id.tvAlertTitle);
        TextView tvAlertMessage = dialog.findViewById(R.id.tvAlertMessage);
        Button btnBack = dialog.findViewById(R.id.btnBack);
        Button btnSkip = dialog.findViewById(R.id.btnSkip);

        tvAlertTitle.setText(alertTitle);
        tvAlertMessage.setText(alertMessage);

        btnBack.setOnClickListener(this);
        btnSkip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack: {
                dialog.dismiss();
                break;
            }
            case R.id.btnSkip: {
                mSkip.onSkipListener(dialog);
                break;
            }
            default:
                break;
        }
    }

    public interface ButtonSkipListener{
        void onSkipListener(Dialog dialog);
    }
}
