package com.socialx.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.socialx.myapplication.Home.Home;
import com.socialx.myapplication.databinding.FragmentLoginBinding;
import com.socialx.myapplication.utility.AppPreferences;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Login_Frag extends Fragment {
    FragmentLoginBinding binding;

    private static final int GGL_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;

    private GoogleSignInClient mGoogleSignInClient;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("712762097411-e4ctda2875q0v6h5st9ugcvsjnhka3b7.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        mAuth = FirebaseAuth.getInstance();

        binding.btGoogle.setOnClickListener(view -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, GGL_SIGN_IN);
        });
        binding.btRegister.setOnClickListener(v->{
            ((FragmentHolder)context).binding.btSingUp.performClick();
        });
        callbackManager = CallbackManager.Factory.create();

        binding.fbloginButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        binding.fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Toast.makeText(context, "Error occured", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btFB.setOnClickListener(view -> {
            binding.fbloginButton.performClick();
        });

        binding.btLogin.setOnClickListener(view -> {
            if (validateForm()){
                startActivity(new Intent((FragmentHolder)context,Home.class));
                AppPreferences.setUserLoginStatus(context,true);
                AppPreferences.setUserLoginStatus((FragmentHolder)context,true);
                getActivity().finish();
            }
        });

        return binding.getRoot();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((FragmentHolder)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent((FragmentHolder)context,Home.class));
                            AppPreferences.setUserLoginStatus((FragmentHolder)context,true);
                            getActivity().finish();
                        } else {
                            startActivity(new Intent((FragmentHolder)context,Home.class));
                            getActivity().finish();                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GGL_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(context, "Singin Failed", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((FragmentHolder)context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent((FragmentHolder)context,Home.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(context, "Singin Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean validateForm() {
        boolean val=true;

        if (AppPreferences.getUserEmail((FragmentHolder)context)==null){
            val=false;
            Toast.makeText(context, "Register First", Toast.LENGTH_SHORT).show();
        }
            if (!binding.etEmail.getText().toString().trim().equals(AppPreferences.getUserEmail((FragmentHolder)context))) {
                binding.etEmail.setError("Invalid Email");
                val = false;
            }
            if (!binding.etPass.getText().toString().trim().equals(AppPreferences.getUserPass((FragmentHolder)context))) {
                binding.etPass.setError("Wrong Password");
                val = false;
            }

        return val;
    }

}