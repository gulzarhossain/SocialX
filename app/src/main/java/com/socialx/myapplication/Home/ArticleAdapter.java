package com.socialx.myapplication.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.socialx.myapplication.R;
import com.socialx.myapplication.databinding.FeedViewBinding;
import com.socialx.myapplication.models.Articles.ArticlesItem;
import com.socialx.myapplication.utility.AppPreferences;
import com.socialx.myapplication.utility.AppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.Item> {

    private ArrayList<ArticlesItem> Articles;
    private Context context;

    public ArticleAdapter(ArrayList<ArticlesItem> articles, Context context) {
        Articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.feed_view,parent,false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
      holder.binding.publishTime.setText(AppUtils.FilterTime(Articles.get(position).getPublishedAt()));
        holder.binding.articleTitle.setText(Articles.get(position).getTitle());
        holder.binding.articleDesc.setText(Articles.get(position).getDescription());
        holder.binding.articleSource.setText(Articles.get(position).getSource().getName());
        Glide.with(context).load(Articles.get(position).getUrlToImage()).placeholder(R.drawable.ic_baseline_image_24).into(holder.binding.articleIMG);

    }

    @Override
    public int getItemCount() {
        return Articles.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        FeedViewBinding binding;
        public Item(@NonNull View itemView) {
            super(itemView);
            binding=FeedViewBinding.bind(itemView);
        }
    }
}
