package com.socialx.myapplication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.socialx.myapplication.databinding.FragmentSingUpBinding;
import com.socialx.myapplication.utility.AppPreferences;

public class SingUp_Frag extends Fragment {
    FragmentSingUpBinding binding;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sing_up_, container, false);

        binding.btRegister.setOnClickListener(view -> {
            if (validateForm()) {
                AppPreferences.setUserEmail(context, binding.etEmail.getText().toString().trim());
                AppPreferences.setUserName(context, binding.etName.getText().toString().trim());
                AppPreferences.setUserPhone(context, binding.etPhone.getText().toString().trim());
                AppPreferences.setUserPass(context, binding.etPass.getText().toString().trim());
                Toast.makeText(context, "Account Created /n Log in", Toast.LENGTH_LONG).show();
            }
        });

        binding.btSignIn.setOnClickListener(v -> {
            ((FragmentHolder) context).binding.btLogin.performClick();
        });

        return binding.getRoot();
    }

    private boolean validateForm() {
        boolean val = true;
        if (binding.etEmail.getText().length() < 8) {
            binding.etEmail.setError("Invalid Email ID");
            val = false;
        }
        if (binding.etPass.getText().length() < 4) {
            binding.etPass.setError("Min 4 characters");
            val = false;
        }
        if (binding.etName.getText().length() < 6) {
            binding.etName.setError("Min 6 characters");
            val = false;
        }
        if (binding.etPhone.getText().length() < 10) {
            binding.etPhone.setError("Invalid phone number");
            val = false;
        }
        if (!binding.chekTerm.isChecked()) {
            val = false;
            Toast.makeText(context, "Check Terms and Conditions", Toast.LENGTH_SHORT).show();
        }
        return val;
    }

}