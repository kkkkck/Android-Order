package com.example.ordermealapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordermealapp.R;
import com.example.ordermealapp.activity.StoreDetailActivity; // Import StoreDetailActivity
import com.example.ordermealapp.model.Dish;
import com.example.ordermealapp.model.ShoppingCart;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private Context context;
    private List<Dish> dishList;
    private ShoppingCart shoppingCart;

    public DishAdapter(Context context, List<Dish> dishList) {
        this.context = context;
        this.dishList = dishList;
        this.shoppingCart = ShoppingCart.getInstance();
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);

        holder.tvDishName.setText(dish.getName());
        holder.tvDishDescription.setText(dish.getDescription());
        holder.tvDishPrice.setText(String.format("¥ %.2f", dish.getPrice()));

        Glide.with(context)
                .load(dish.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivDishImage);

        holder.btnAddToCart.setOnClickListener(v -> {
            shoppingCart.addItem(dish);
            Toast.makeText(context, dish.getName() + " 已加入购物车", Toast.LENGTH_SHORT).show();
            // Notify the parent activity (StoreDetailActivity) to update its cart display
            if (context instanceof StoreDetailActivity) {
                ((StoreDetailActivity) context).updateCartDisplay();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDishImage;
        TextView tvDishName, tvDishDescription, tvDishPrice;
        Button btnAddToCart;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDishImage = itemView.findViewById(R.id.iv_dish_image);
            tvDishName = itemView.findViewById(R.id.tv_dish_name);
            tvDishDescription = itemView.findViewById(R.id.tv_dish_description);
            tvDishPrice = itemView.findViewById(R.id.tv_dish_price);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}