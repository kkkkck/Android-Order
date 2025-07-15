// In: com.example.ordermealapp.adapter.StoreAdapter.java
package com.example.ordermealapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordermealapp.R;
import com.example.ordermealapp.activity.StoreDetailActivity;
import com.example.ordermealapp.model.Store;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private Context context;
    private List<Store> storeList;

    public StoreAdapter(Context context, List<Store> storeList) {
        this.context = context;
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_store, parent, false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        Store store = storeList.get(position);
        holder.tvStoreName.setText(store.getName());
        holder.tvStoreRating.setText("评分: " + store.getRating());

        Glide.with(context)
                .load(store.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background) // A default image
                .into(holder.ivStoreImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StoreDetailActivity.class);
            intent.putExtra("store", store); // Pass the whole store object
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStoreImage;
        TextView tvStoreName, tvStoreRating;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStoreImage = itemView.findViewById(R.id.iv_store_image);
            tvStoreName = itemView.findViewById(R.id.tv_store_name);
            tvStoreRating = itemView.findViewById(R.id.tv_store_rating);
        }
    }
}