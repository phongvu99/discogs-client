package com.naughtybitch.discogsclient.sell;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.buy.BuyMusicFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BuyMusicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuyMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellMusicFragment extends Fragment implements
        View.OnClickListener,
        OrderFragment.OnFragmentInteractionListener,
        OfferFragment.OnFragmentInteractionListener,
        InventoryFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SellMusicFragment() {
        // Required empty public constructor
    }

    private void buttonOnClickListener(View v) {
        Button btn_order = v.findViewById(R.id.order);
        Button btn_offer = v.findViewById(R.id.offer);
        Button btn_inven = v.findViewById(R.id.inventory);
        btn_order.setOnClickListener(this);
        btn_offer.setOnClickListener(this);
        btn_inven.setOnClickListener(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Do smt
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order:
                navigateToFragment(OrderFragment.newInstance());
                break;
            case R.id.offer:
                navigateToFragment(OfferFragment.newInstance());
                break;
            case R.id.inventory:
                navigateToFragment(InventoryFragment.newInstance());
                break;
        }
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyMusicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellMusicFragment newInstance(String param1, String param2) {
        SellMusicFragment fragment = new SellMusicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SellMusicFragment newInstance() {
        Bundle args = new Bundle();
        SellMusicFragment fragment = new SellMusicFragment();
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
        View v = inflater.inflate(R.layout.fragment_sell_music, container, false);
        buttonOnClickListener(v);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
