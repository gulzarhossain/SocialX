package com.socialx.myapplication.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.socialx.myapplication.FragmentHolder;
import com.socialx.myapplication.Network.ApiClient;
import com.socialx.myapplication.Network.ApiInterface;
import com.socialx.myapplication.Network.ApiResponse;
import com.socialx.myapplication.R;
import com.socialx.myapplication.databinding.ActivityHomeBinding;
import com.socialx.myapplication.models.Articles.ArticleResponse;
import com.socialx.myapplication.models.Articles.ArticlesItem;
import com.socialx.myapplication.utility.AppPreferences;
import com.socialx.myapplication.utility.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class Home extends AppCompatActivity implements ApiResponse {
    ActivityHomeBinding binding;

    private FirebaseAuth mAuth;

    private ArrayList<ArticlesItem>Articles=new ArrayList<>();
    private ArrayList<ArticlesItem>searchList=new ArrayList<>();

    private ArticleAdapter adapter;

    private Map<String,String> ArticleFilterMap=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_home);

        adapter=new ArticleAdapter(Articles,this);
        binding.articleRV.setAdapter(adapter);


        mAuth = FirebaseAuth.getInstance();

        getArticles();
        SearhArticles();

        binding.btLogout.setOnClickListener(view -> {
            LoginManager.getInstance().logOut();
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home.this, FragmentHolder.class));
            AppPreferences.setUserLoginStatus(this,false);
            finish();
        });

    }

    private void SearhArticles() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    Articles.clear();
                    for (ArticlesItem item : searchList) {
                        if (item.getTitle().contains(editable.toString())) {
                            Articles.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    Articles.clear();
                    Articles.addAll(searchList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getArticles() {
        binding.pBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface= ApiClient.getApiInterFace(this);

        ArticleFilterMap.put("q","jobs");
        ArticleFilterMap.put("from","2022-04-20");
        ArticleFilterMap.put("sortBy","publishedAt");
        ArticleFilterMap.put("apiKey","31ab0e3fa2c14a8c8145ecb27e0ad7a1");
        Call<ResponseBody>getarticles=apiInterface.getArticles(ArticleFilterMap);
        ApiClient.callApi(getarticles,this,1);
            }

    @Override
    public void OnResponse(String response, int apiRequest) {
        binding.pBar.setVisibility(View.GONE);
        binding.articleRV.setVisibility(View.VISIBLE);
        ArticleResponse articleResponse=ApiClient.getPayload(ArticleResponse.class,response);

        Articles.addAll(articleResponse.getArticles());
        searchList.addAll(articleResponse.getArticles());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnError(String errorResponse, int apiRequest) {
        Toast.makeText(this, errorResponse, Toast.LENGTH_SHORT).show();
    }
}