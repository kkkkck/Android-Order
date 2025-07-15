package com.example.ordermealapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ordermealapp.fragment.CommentListFragment;
import com.example.ordermealapp.fragment.DishListFragment;
import com.example.ordermealapp.fragment.StoreInfoFragment;
import com.example.ordermealapp.model.Store;

public class StoreDetailPagerAdapter extends FragmentStateAdapter {

    private Store store;

    public StoreDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, Store store) {
        super(fragmentActivity);
        this.store = store;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (store == null) {
            // Handle case where store is null, though it shouldn't happen with proper setup
            return new Fragment();
        }
        switch (position) {
            case 0:
                return DishListFragment.newInstance(store);
            case 1:
                return CommentListFragment.newInstance(store);
            case 2:
                return StoreInfoFragment.newInstance(store);
            default:
                return DishListFragment.newInstance(store);
        }
    }

    @Override
    public int getItemCount() {
        return 3; // "点菜", "评价", "商家"
    }
}