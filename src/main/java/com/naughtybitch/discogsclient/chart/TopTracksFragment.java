package com.naughtybitch.discogsclient.chart;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.POJOlastfm.TopTracksResponse;
import com.naughtybitch.POJOlastfm.Track;
import com.naughtybitch.discogsapi.LastfmAPI;
import com.naughtybitch.discogsapi.LastfmClient;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.SearchableActivity;
import com.naughtybitch.recyclerview.TopTrackAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TopTracksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TopTracksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopTracksFragment extends Fragment implements TopTrackAdapter.OnClickItemListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String lastfm_api_key, lastfm_shared_secret_key;
    private RecyclerView rv_chart;
    private TopTrackAdapter chartAdapter;
    private Context context = getActivity();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TopTracksFragment() {
        // Required empty public constructor
    }

    public static TopTracksFragment newInstance() {

        Bundle args = new Bundle();

        TopTracksFragment fragment = new TopTracksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TopTracksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopTracksFragment newInstance(String param1, String param2) {
        TopTracksFragment fragment = new TopTracksFragment();
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
        View view = inflater.inflate(R.layout.fragment_top_tracks, container, false);
        initView(view);
        fetchChart(lastfm_api_key);
        return view;
    }

    private void initView(View v) {
        rv_chart = v.findViewById(R.id.rv_chart);
        rv_chart.setLayoutManager(new LinearLayoutManager(context));
        chartAdapter = new TopTrackAdapter();
        rv_chart.setAdapter(chartAdapter);
        lastfm_api_key = "89e03a93e5ad3a74913c27f806cf860b";
        lastfm_shared_secret_key = "926be997616ba4e29bcc06494c387750";
    }

    private void updateChart(TopTracksResponse topTracksResponse) {
        chartAdapter = new TopTrackAdapter(topTracksResponse.getTracks().getTrack(), this);
        rv_chart.setAdapter(chartAdapter);
    }

    private void fetchChart(String lastfm_api_key) {
        Retrofit retrofit = LastfmClient.getRetrofitClient();
        LastfmAPI lastfmAPI = retrofit.create(LastfmAPI.class);
        Call<TopTracksResponse> call = lastfmAPI.fetchTopTrack(lastfm_api_key, 50, 1);
        call.enqueue(new Callback<TopTracksResponse>() {
            @Override
            public void onResponse(Call<TopTracksResponse> call, Response<TopTracksResponse> response) {
                if (response.body() != null) {
                    TopTracksResponse topTracksResponse = response.body();
                    updateChart(topTracksResponse);
                }
            }

            @Override
            public void onFailure(Call<TopTracksResponse> call, Throwable t) {
                Log.e("TRACK_LOG", t.getMessage());
            }
        });
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
    public void onTrackClick(int position, Track track) {
        Log.d(TAG, "onTrackClick: clicked");
        Intent intent = new Intent(getActivity(), SearchableActivity.class);
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
