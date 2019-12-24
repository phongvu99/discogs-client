package com.naughtybitch.discogsclient.explore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.SearchableActivity;
import com.naughtybitch.recyclerview.GenreAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment implements GenreAdapter.OnGenreListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private RecyclerView lv1, lv2, lv3;
    private String[] genres1, genres2, genres3;
    private Integer[] items1, items2, items3;
    private Integer[] covers1, covers2, covers3;
    private Integer[] icons1, icons2, icons3;
    private GenreAdapter adapter1, adapter2, adapter3;
    private OnFragmentInteractionListener mListener;
    private Pagination pagination;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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

    public static ExploreFragment newInstance() {
        Bundle args = new Bundle();
        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_explore, container, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        lv1 = v.findViewById(R.id.rc_genre1);
        lv2 = v.findViewById(R.id.rc_genre2);
        lv3 = v.findViewById(R.id.rc_genre3);
        lv1.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        lv2.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        lv3.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        genres1 = new String[]{"Rock", "Folk, World, & Country",
                "Classical", "Reggae", "Non-Music"};
        genres2 = new String[]{"Electronic", "Jazz", "Hip Hop",
                "Stage & Screen", "Children's"};
        genres3 = new String[]{"Pop", "Funk / Soul", "Latin",
                "Blues", "Brass & Military"};
        items1 = new Integer[]{4211787, 1335772, 655560, 354870, 214975};
        items2 = new Integer[]{3518015, 998719, 643235, 326361, 94732};
        items3 = new Integer[]{2349623, 909136, 438480, 266159, 34922};
        covers1 = new Integer[]{R.drawable.rock_cover, R.drawable.folk_cover,
                R.drawable.classical_cover, R.drawable.reggae_cover, R.drawable.non_music_cover};
        covers2 = new Integer[]{R.drawable.electronic_cover, R.drawable.jazz_cover,
                R.drawable.hip_hop_cover, R.drawable.stage_screen_cover, R.drawable.childrens_music_cover};
        covers3 = new Integer[]{R.drawable.pop_cover, R.drawable.soul_cover,
                R.drawable.latin_cover, R.drawable.blues_cover, R.drawable.military_cover};
        icons1 = new Integer[]{R.drawable.rock, R.drawable.folk,
                R.drawable.classical, R.drawable.reggae, R.drawable.non_music};
        icons2 = new Integer[]{R.drawable.edm, R.drawable.jazz,
                R.drawable.hip_hop, R.drawable.stage_screen, R.drawable.childrens_music};
        icons3 = new Integer[]{R.drawable.pop, R.drawable.funk,
                R.drawable.latin, R.drawable.blues, R.drawable.military};
        adapter1 = new GenreAdapter(getContext(), genres1, items1, covers1, icons1, this);
        adapter2 = new GenreAdapter(getContext(), genres2, items2, covers2, icons2, this);
        adapter3 = new GenreAdapter(getContext(), genres3, items3, covers3, icons3, this);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
        lv3.setAdapter(adapter3);

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

    @Override
    public void onGenreClick(int position, String genre) {
        Toast.makeText(getContext(), "Genre " + genre, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), SearchableActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("genre", genre);
        intent.putExtras(bundle);
        startActivity(intent);
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
