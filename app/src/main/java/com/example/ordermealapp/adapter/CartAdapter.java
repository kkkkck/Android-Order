package com.example.ordermealapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermealapp.R;
import com.example.ordermealapp.activity.ConfirmOrderActivity;
import com.example.ordermealapp.model.Dish;
import com.example.ordermealapp.model.ShoppingCart;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Map.Entry<Dish, Integer>> cartItems;
    private ShoppingCart shoppingCart;

    public CartAdapter(Context context, List<Map.Entry<Dish, Integer>> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        this.shoppingCart = ShoppingCart.getInstance();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Map.Entry<Dish, Integer> entry = cartItems.get(position);
        Dish dish = entry.getKey();
        int quantity = entry.getValue();
        double subtotal = dish.getPrice() * quantity;

        holder.tvItemName.setText(dish.getName());
        holder.tvItemQuantity.setText(String.valueOf(quantity));
        holder.tvItemPrice.setText(String.format("¥ %.2f", subtotal));

        holder.btnIncreaseQuantity.setOnClickListener(v -> {
            shoppingCart.addItem(dish);
            updateCartData(); // Refresh data and notify adapter
            if (context instanceof ConfirmOrderActivity) {
                ((ConfirmOrderActivity) context).updateTotalPrice();
            }
        });

        holder.btnDecreaseQuantity.setOnClickListener(v -> {
            shoppingCart.removeItem(dish);
            updateCartData(); // Refresh data and notify adapter
            if (context instanceof ConfirmOrderActivity) {
                ((ConfirmOrderActivity) context).updateTotalPrice();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // Call this method to update the adapter's data from the ShoppingCart
    public void updateCartData() {
        this.cartItems = shoppingCart.getCartItems();
        notifyDataSetChanged();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemQuantity, tvItemPrice;
        Button btnIncreaseQuantity, btnDecreaseQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_cart_item_name);
            tvItemQuantity = itemView.findViewById(R.id.tv_cart_item_quantity);
            tvItemPrice = itemView.findViewById(R.id.tv_cart_item_price);
            btnIncreaseQuantity = itemView.findViewById(R.id.btn_increase_quantity);
            btnDecreaseQuantity = itemView.findViewById(R.id.btn_decrease_quantity);
        }
    }
}