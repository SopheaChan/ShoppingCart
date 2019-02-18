package com.example.dell.myapplication.ui.sale_product;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.dell.myapplication.R;

public class AddCompanyInfoFragment extends Fragment implements View.OnClickListener {
    private EditText etCompanyName;
    private EditText etCompanyTel;
    private EditText etCompanyEmail;
    String companyName, companyTel, companyEmail;
    private Button btnNext;
    private AddProductToStoreMvpPresenter addProductToStoreMvpPresenter =
            new AddProductToStorePresenter();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_company_info, container, false);
        btnNext =view.findViewById(R.id.btnNext);
        etCompanyName = view.findViewById(R.id.etCompanyName);
        etCompanyTel = view.findViewById(R.id.etTelephone);
        etCompanyEmail = view.findViewById(R.id.etEmail);

        btnNext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int buttonID = v.getId();
        switch (buttonID){
            case R.id.btnNext: {
                getButtonNextListener();
                break;
            }
        }
    }

    private void getButtonNextListener() {
        companyName = etCompanyName.getText().toString();
        companyTel = etCompanyTel.getText().toString();
        companyEmail = etCompanyEmail.getText().toString();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        addProductToStoreMvpPresenter.setButtonNextListener(companyName, companyTel, companyEmail,
                etCompanyName, etCompanyTel, etCompanyEmail, getContext(), fragmentManager);
    }
}
