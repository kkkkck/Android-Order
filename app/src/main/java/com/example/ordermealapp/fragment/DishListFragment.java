package com.example.ordermealapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordermealapp.R;
import com.example.ordermealapp.adapter.DishAdapter;
import com.example.ordermealapp.database.AppDatabase;
import com.example.ordermealapp.model.Dish;
import com.example.ordermealapp.model.Store;
import com.example.ordermealapp.model.ShoppingCart; // Ensure this is accessible

import java.util.List;

public class DishListFragment extends Fragment {

    private static final String ARG_STORE = "store";
    private Store store;
    private RecyclerView rvDishes;
    private DishAdapter dishAdapter;
    private AppDatabase db;
    private ShoppingCart shoppingCart;

    public static DishListFragment newInstance(Store store) {
        DishListFragment fragment = new DishListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORE, store);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            store = (Store) getArguments().getSerializable(ARG_STORE);
        }
        db = AppDatabase.getDatabase(requireContext());
        shoppingCart = ShoppingCart.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish_list, container, false);
        rvDishes = view.findViewById(R.id.rv_dishes_fragment);
        rvDishes.setLayoutManager(new LinearLayoutManager(getContext()));
        loadDishes();
        return view;
    }

    private void loadDishes() {
        if (store == null) return;
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Dish> dishes = db.dishDao().getDishesByStoreId(store.getId());
            requireActivity().runOnUiThread(() -> {
                dishAdapter = new DishAdapter(getContext(), dishes);
                rvDishes.setAdapter(dishAdapter);
            });
        });
    }


    public void updateCartDisplay() {
    }
}