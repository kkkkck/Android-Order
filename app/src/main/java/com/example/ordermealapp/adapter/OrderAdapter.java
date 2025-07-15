package com.example.ordermealapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue; // Import TypedValue

import com.example.ordermealapp.R;
import com.example.ordermealapp.model.Order;
import com.example.ordermealapp.model.OrderItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvStoreName.setText(order.getStoreName());
        holder.tvOrderDate.setText("下单时间: " + dateFormat.format(order.getOrderDate()));
        holder.tvOrderStatus.setText("状态: " + order.getStatus());
        holder.tvTotalPrice.setText(String.format("总计: ¥ %.2f", order.getTotalPrice()));

        // Dynamically add order items
        holder.llOrderItemsContainer.removeAllViews(); // Clear previous views
        for (OrderItem item : order.getItems()) {
            TextView itemTextView = new TextView(context);
            itemTextView.setText(String.format("%s x%d (¥%.2f)", item.getDishName(), item.getQuantity(), item.getDishPrice()));
            // Corrected line: Use TypedValue.COMPLEX_UNIT_SP
            itemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            itemTextView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            holder.llOrderItemsContainer.addView(itemTextView);
        }

        // Handle item click for order details (optional)
        holder.itemView.setOnClickListener(v -> {
            // TODO: Implement navigation to OrderDetailActivity if needed
            // Intent intent = new Intent(context, OrderDetailActivity.class);
            // intent.putExtra("order", order);
            // context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvStoreName, tvOrderDate, tvOrderStatus, tvTotalPrice;
        LinearLayout llOrderItemsContainer;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStoreName = itemView.findViewById(R.id.tv_order_store_name);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvTotalPrice = itemView.findViewById(R.id.tv_order_total_price);
            llOrderItemsContainer = itemView.findViewById(R.id.ll_order_items_container);
        }
    }
}