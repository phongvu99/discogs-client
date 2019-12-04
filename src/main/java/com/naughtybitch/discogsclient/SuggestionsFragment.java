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


public class SuggestionsFragment extends Fragment {
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
        View v = inflater.inflate(R.layout.fragment_suggestions, container, false);
        ImageButton img_btn_1 = v.findViewById(R.id.img1);
        ImageButton img_btn_2 = v.findViewById(R.id.img2);
        ImageButton img_btn_3 = v.findViewById(R.id.img3);
        ImageButton img_btn_4 = v.findViewById(R.id.img4);
        ImageButton img_btn_5 = v.findViewById(R.id.img5);
        ImageButton img_btn_6 = v.findViewById(R.id.img6);
        ImageButton img_btn_7 = v.findViewById(R.id.img7);
        ImageButton img_btn_8 = v.findViewById(R.id.img8);
        ImageButton img_btn_9 = v.findViewById(R.id.img9);
        ImageButton img_btn_10 = v.findViewById(R.id.img10);
        ImageButton img_btn_11 = v.findViewById(R.id.img11);
        ImageButton img_btn_view_all = v.findViewById(R.id.img_view_all);


        img_btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #1 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #2 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #3 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #4 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #5 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #6 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #7 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #8 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #9 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #10 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #11 album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        img_btn_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked view all album");
                startActivity(new Intent(getActivity(), AlbumInfoActivity.class));
            }
        });
        return v;
    }
}
