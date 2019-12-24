package com.naughtybitch.discogsclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.naughtybitch.POJO.Result;
import com.naughtybitch.POJO.SearchResponse;
import com.naughtybitch.POJOlastfm.Artist;
import com.naughtybitch.POJOlastfm.TopArtistsResponse;
import com.naughtybitch.discogsapi.AppController;
import com.naughtybitch.discogsapi.CustomRequest;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.recyclerview.ChartAdapter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private String lastfm_api_key, lastfm_shared_secret_key, username;
    private SharedPreferences sp;
    private RecyclerView rv_chart;
    private ChartAdapter chartAdapter;
    private ArrayList<Artist> artists;
    private Context context = getActivity();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public void buttonOnClickListener(View v) {
        Button send_request = v.findViewById(R.id.send_request);
        Button token_request = v.findViewById(R.id.token_request);
        Button sign_out = v.findViewById(R.id.sign_out);
        sign_out.setOnClickListener(this);
        send_request.setOnClickListener(this);
        token_request.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.send_request):
                Toast.makeText(getActivity(), "Sending request", Toast.LENGTH_SHORT).show();
                sendRequest();
                break;
            case (R.id.token_request):
                Toast.makeText(getActivity(), "Sending request", Toast.LENGTH_SHORT).show();
                checkLoginStatus();
                break;
            case (R.id.sign_out):
                Intent intent = new Intent(getActivity(), SignOutActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void sendRequest() {
        String tag_json_object = "json_object_request";
        final EditText editText = (EditText) getActivity().findViewById(R.id.response);
        final ImageView coverImage = (ImageView) getActivity().findViewById(R.id.album_cover);

        String api_base_url = "https://api.discogs.com";
        String search_url = api_base_url + "/database/search?release_title=everyday+life&artist=coldplay&per_page=3&page=1";
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, search_url, SearchResponse.class, new Response.Listener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse response) {
                Log.i("response", "Response is " + response);
                List<Result> list = response.getResults();
                String coverImage_url = list.get(0).getCoverImage();
                editText.setText("CoverImage url: " + coverImage_url + "\nHave a look at this amazing album cover by Coldplay!", TextView.BufferType.EDITABLE);
                Glide.with(getActivity()).load(coverImage_url).override(600, 600).fitCenter()
                        .placeholder(R.drawable.bigpump)
                        .error(R.drawable.bigpump)
                        .into(coverImage);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                editText.setText("That didn't work!");
                editText.setText("Response is: " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                headers.put("Authorization", "Discogs key=zrNFOdbKoUvMXDxixdPY" + "," + "secret=NgFRwbmvWCwmIiIRjAaiUnWSutmlHDNJ");
                headers.put("Authorization", "Discogs token=mbAGeTxLGrWvJLiEEYOUZSwxkiVJyFYDiqEoNyxt");
                return headers;
            }
        };
        // Add the request to the RequestQueue
        AppController.getInstance().addToRequestQueue(customRequest, tag_json_object);
    }

    private void checkLoginStatus() {
        SharedPreferences user_preferences;
        user_preferences = getActivity().getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        boolean logged_in = user_preferences.getBoolean("logged_in", false);
        String access_token = user_preferences.getString("access_token", null);
        String access_token_secret = user_preferences.getString("access_token_secret", null);
        Log.i("logged_in", "logged_in " + logged_in);
        Log.i("access_token", "access_token " + access_token);
        Log.i("access_token_secret", "access_token_secret " + access_token_secret);
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sp = getActivity().getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        username = sp.getString("user_name", null);
        initView(view);
        buttonOnClickListener(view);
        if (username != null) {
            fetchChart(lastfm_api_key);
        }
        return view;
    }

    private void initView(View v) {
        rv_chart = v.findViewById(R.id.rv_chart);
        rv_chart.setLayoutManager(new LinearLayoutManager(context));
        lastfm_api_key = "89e03a93e5ad3a74913c27f806cf860b";
        lastfm_shared_secret_key = "926be997616ba4e29bcc06494c387750";
    }

    private void updateChart() {
        chartAdapter = new ChartAdapter();
        rv_chart.setAdapter(chartAdapter);
    }

    private void fetchChart(String lastfm_api_key) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<TopArtistsResponse> call = discogsAPI.fetchTopArtist(lastfm_api_key, 20, 1);
        call.enqueue(new Callback<TopArtistsResponse>() {
            @Override
            public void onResponse(Call<TopArtistsResponse> call, retrofit2.Response<TopArtistsResponse> response) {
                if (response.body() != null) {
                    updateChart();
                }
                Log.i("CODE_RESPONSE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<TopArtistsResponse> call, Throwable t) {
                Log.e("CHART_CAT", t.getMessage());
            }
        });
    }

    private DiscogsAPI getDiscogsAPI() {
        final String auth = getCredentials();
        // Define the interceptor, add authentication headers
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request.Builder onGoing = chain.request().newBuilder();
                onGoing.addHeader("Authorization", auth);
                onGoing.addHeader("User-Agent", "Discogsnect/0.1 +http://discogsnect.com");
                return chain.proceed(onGoing.build());
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();

        Retrofit retrofit = RetrofitClient.getRetrofitClient(client);

        return retrofit.create(DiscogsAPI.class);
    }

    private String getCredentials() {
        DiscogsClient instance = DiscogsClient.getInstance();
        Timestamp currentTimestamp = instance.currentTimeStamp();
        ArrayList<String> token = instance.getCredentials(getActivity());
        String auth = "OAuth oauth_consumer_key=\"" + instance.getConsumer_key() + "\", " +
                "oauth_nonce=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_token=\"" + token.get(0) + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_version=\"1.0\", " +
                "oauth_signature=\"" + instance.getConsumer_secret() + token.get(1) + "\"";
        return auth;
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
