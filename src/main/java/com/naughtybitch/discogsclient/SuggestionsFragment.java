package com.naughtybitch.discogsclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class SuggestionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SuggestionsFragment() {
        // Required empty public constructor
    }

    public static SuggestionsFragment newInstance(String param1, String param2) {
        SuggestionsFragment fragment = new SuggestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_suggestions, container, false);
        ImageButton imgbtn = (ImageButton) v.findViewById(R.id.img1);
        ImageButton imgbtnact = (ImageButton) v.findViewById(R.id.img2);
        ImageButton imgbtn2 = (ImageButton) v.findViewById(R.id.img3);
        imgbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #3 image");
                startActivity(new Intent(getActivity(), SuggestionActivity.class));
            }
        });
        imgbtnact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #2 image");
                startActivity(new Intent(getActivity(), SuggestionActivity.class));
            }
        });

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Click", "Clicked #1 image");
                startActivity(new Intent(getActivity(), SuggestionActivity.class));
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
            }
        });
        return v;
    }

}
