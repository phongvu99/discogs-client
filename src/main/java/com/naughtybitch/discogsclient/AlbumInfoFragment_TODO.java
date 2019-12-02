package com.naughtybitch.discogsclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AlbumInfoFragment_TODO extends Fragment {
    public AlbumInfoFragment_TODO() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.activity_main, container, false);
//        ImageButton imgbtn = (ImageButton)v.findViewById(R.id.img1);
//        ImageButton imgbtnact = (ImageButton)v.findViewById(R.id.img2);
//        ImageButton imgbtn2 = (ImageButton)v.findViewById(R.id.img3);
//        imgbtn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Click", "Clicked #3 image");
//
//            }
//        });
//        imgbtnact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Click", "Clicked #2 image");
//                startActivity(new Intent(getActivity(), SongsActivity.class));
//            }
//        });
//
//        imgbtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Log.d("Click", "Clicked #1 image");
////                 Create new fragment and transaction
//                Fragment newFragment = new RecommendationFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//// Replace whatever is in the fragment_container view with this fragment,
//// and add the transaction to the back stack
//                transaction.replace(R.id.rl_container, newFragment);
//                transaction.addToBackStack(null);
//
//// Commit the transaction
//                transaction.commit();
//            }
//        });
        return v;
    }
}