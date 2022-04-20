package com.socialx.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.socialx.myapplication.Home.Home;
import com.socialx.myapplication.databinding.ActivityFragmentHolderBinding;
import com.socialx.myapplication.utility.AppPreferences;

public class FragmentHolder extends AppCompatActivity {
    ActivityFragmentHolderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_fragment_holder);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(FragmentHolder.this,R.color.red));

        if (AppPreferences.getUserLoginStatus(this)){
            startActivity(new Intent(FragmentHolder.this, Home.class));
        }else {
            loadFragment(new Login_Frag());
            binding.btLogin.setOnClickListener(view -> {
                LoginBackground();
                loadFragment(new Login_Frag());
            });
            binding.btSingUp.setOnClickListener(view -> {
                SingupBackground();
                loadFragment(new SingUp_Frag());
            });
        }
    }

    private void SingupBackground() {
        binding.btSingUp.setTextColor(Color.WHITE);
        binding.btLogin.setTextColor(Color.BLACK);
        binding.btSingUp.setBackgroundResource(R.drawable.buttonbackground);
        binding.btLogin.setBackgroundColor(Color.TRANSPARENT);
    }

    private void LoginBackground() {
        binding.btLogin.setTextColor(Color.WHITE);
        binding.btSingUp.setTextColor(Color.BLACK);
        binding.btLogin.setBackgroundResource(R.drawable.buttonbackground);
        binding.btSingUp.setBackgroundColor(Color.TRANSPARENT);
    }

    void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragholder, fragment);
        transaction.commit();
    }
}