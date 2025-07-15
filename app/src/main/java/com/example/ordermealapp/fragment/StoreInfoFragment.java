package com.example.ordermealapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ordermealapp.R;
import com.example.ordermealapp.model.Store;

public class StoreInfoFragment extends Fragment {

    private static final String ARG_STORE = "store";
    private Store store;
    private TextView tvStoreAddress, tvStorePhone, tvStoreHours; // Placeholder

    public static StoreInfoFragment newInstance(Store store) {
        StoreInfoFragment fragment = new StoreInfoFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_info, container, false);
        tvStoreAddress = view.findViewById(R.id.tv_store_address);
        tvStorePhone = view.findViewById(R.id.tv_store_phone);
        tvStoreHours = view.findViewById(R.id.tv_store_hours);

        if (store != null) {
            tvStoreAddress.setText("门店地址: " + store.getName()); // Placeholder
            tvStorePhone.setText("电话: 123-4567-8901"); // Placeholder
            tvStoreHours.setText("营业时间: 09:00 - 22:00"); // Placeholder
        }
        return view;
    }
}