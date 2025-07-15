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

public class CommentListFragment extends Fragment {

    private static final String ARG_STORE = "store";
    private Store store;
    private TextView tvCommentPlaceholder; // Placeholder for now

    public static CommentListFragment newInstance(Store store) {
        CommentListFragment fragment = new CommentListFragment();
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
        View view = inflater.inflate(R.layout.fragment_comment_list, container, false);
        tvCommentPlaceholder = view.findViewById(R.id.tv_comment_placeholder);
        if (store != null) {
            tvCommentPlaceholder.setText(store.getName() + " 的评论区\n");
        }
        return view;
    }
}