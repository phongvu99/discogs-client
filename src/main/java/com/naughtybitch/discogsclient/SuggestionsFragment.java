package com.naughtybitch.discogsclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class SuggestionsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public SuggestionsFragment() {
    }

    static SuggestionsFragment newInstance(String param1, String param2) {
        SuggestionsFragment fragment = new SuggestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_suggestions, container, false);
        buttonOnClickListener(view);
        return view;
    }

    private void buttonOnClickListener(View v) {
        ImageButton img_btn_1 = v.findViewById(R.id.img1);
        ImageButton img_btn_2 = v.findViewById(R.id.img2);
        ImageButton img_btn_3 = v.findViewById(R.id.img3);
        ImageButton img_btn_4 = v.findViewById(R.id.img4);
        ImageButton img_btn_5 = v.findViewById(R.id.img5);
        ImageButton img_btn_6 = v.findViewById(R.id.img6);
        ImageButton img_btn_7 = v.findViewById(R.id.img7);
        ImageButton img_btn_8 = v.findViewById(R.id.img8);
        ImageButton img_btn_9 = v.findViewById(R.id.img9);
        ImageButton img_btn_view_all = v.findViewById(R.id.view_all);
        img_btn_1.setOnClickListener(this);
        img_btn_2.setOnClickListener(this);
        img_btn_3.setOnClickListener(this);
        img_btn_4.setOnClickListener(this);
        img_btn_5.setOnClickListener(this);
        img_btn_6.setOnClickListener(this);
        img_btn_7.setOnClickListener(this);
        img_btn_8.setOnClickListener(this);
        img_btn_9.setOnClickListener(this);
        img_btn_view_all.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), AlbumInfoActivity.class);
        switch (v.getId()) {
            case (R.id.img1):
                Log.i("test", "test");
                startActivity(intent);
                break;
            case (R.id.img2):
                startActivity(intent);
                break;
            case (R.id.img3):
                startActivity(intent);
                break;
            case (R.id.img4):
                startActivity(intent);
                break;
            case (R.id.img5):
                startActivity(intent);
                break;
            case (R.id.img6):
                startActivity(intent);
                break;
            case (R.id.img7):
                startActivity(intent);
                break;
            case (R.id.img8):
                startActivity(intent);
                break;
            case (R.id.img9):
                startActivity(intent);
                break;
            case (R.id.view_all):
                startActivity(intent);
                break;
        }
    }
}
