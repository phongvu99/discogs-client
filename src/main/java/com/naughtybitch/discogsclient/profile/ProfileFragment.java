package com.naughtybitch.discogsclient.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.naughtybitch.POJO.CollectionResponse;
import com.naughtybitch.POJO.CollectionValueResponse;
import com.naughtybitch.POJO.ProfileResponse;
import com.naughtybitch.POJO.Release;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.recyclerview.MoreByAdapter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener,
        MoreByAdapter.OnMoreByListener {

    private Context context = getActivity();
    private SharedPreferences sp;
    private ImageView profile_image, profile_banner;
    private TextView profile, profile_name, seller_rating_star, seller_rating, button_sign_out;
    private TextView buyer_rating_star, buyer_rating, profile_location, min_value, med_value, max_value;
    private NestedScrollView profile_container;
    private RecyclerView profile_collection, profile_wishlist;
    private MoreByAdapter collection_wishlist_adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        sp = getActivity().getSharedPreferences("userPreferences", Context.MODE_PRIVATE);
        String username = sp.getString("user_name", null);
        initView(v);
        if (username != null) {
            fetchProfile(username);
            fetchCollection(username, 0);
            fetchCollectionValue(username);
        }
        buttonOnClickListener(v);
        return v;
    }

    private void initView(View v) {
        profile_image = v.findViewById(R.id.profile_image);
        profile_name = v.findViewById(R.id.profile_name);
        profile = v.findViewById(R.id.profile);
        profile_banner = v.findViewById(R.id.profile_banner);
        seller_rating = v.findViewById(R.id.seller_rating);
        seller_rating_star = v.findViewById(R.id.seller_rating_stars);
        buyer_rating = v.findViewById(R.id.buyer_rating);
        buyer_rating_star = v.findViewById(R.id.buyer_rating_stars);
        profile_container = v.findViewById(R.id.profile_container);
        profile_collection = v.findViewById(R.id.rc_view_all_collection);
        profile_wishlist = v.findViewById(R.id.rc_view_all_wishlist);
        collection_wishlist_adapter = new MoreByAdapter();
        profile_collection.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        profile_collection.setAdapter(collection_wishlist_adapter);
        profile_wishlist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        profile_wishlist.setAdapter(collection_wishlist_adapter);
        profile_location = v.findViewById(R.id.location);
        min_value = v.findViewById(R.id.min_value);
        med_value = v.findViewById(R.id.med_value);
        max_value = v.findViewById(R.id.max_value);
        button_sign_out = v.findViewById(R.id.button_sign_out);
    }

    private void buttonOnClickListener(View v) {
        button_sign_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_sign_out) {

        }
    }

    private void updateCollectionValue(CollectionValueResponse collectionValueResponse) {
        min_value.setText(collectionValueResponse.getMinimum());
        med_value.setText(collectionValueResponse.getMedian());
        max_value.setText(collectionValueResponse.getMaximum());
    }

    private void updateProfile(ProfileResponse profileResponse) {
        Glide.with(getActivity())
                .load(profileResponse.getAvatarUrl())
                .error(R.drawable.discogs_vinyl_record_mark)
                .placeholder(R.drawable.discogs_vinyl_record_mark)
                .into(profile_image);
        profile_name.setText(profileResponse.getName());
        profile.setText(profileResponse.getProfile());
        Glide.with(getActivity())
                .load(profileResponse.getBannerUrl())
                .error(R.drawable.discogs_vinyl_record_mark)
                .placeholder(R.drawable.discogs_vinyl_record_mark)
                .into(profile_banner);
        seller_rating.setText(String.valueOf(profileResponse.getSellerNumRatings()));
        buyer_rating.setText(String.valueOf(profileResponse.getBuyerNumRatings()));
        seller_rating_star.setText(String.valueOf(profileResponse.getSellerRatingStars()));
        buyer_rating_star.setText(String.valueOf(profileResponse.getBuyerRatingStars()));
        profile_container.setVisibility(View.VISIBLE);
        profile_location.setText(profileResponse.getLocation());
    }

    private void updateCollection(CollectionResponse collectionResponse) {
        collection_wishlist_adapter = new MoreByAdapter(getActivity(), collectionResponse.getReleases(), 0, this);
        profile_collection.setAdapter(collection_wishlist_adapter);
    }

//    private void updateWishlist(CollectionResponse collectionResponse) {
//        collection_wishlist_adapter = new MoreByAdapter(getActivity(), collectionResponse.getReleases(), 0, this);
//        profile_wishlist.setAdapter(collection_wishlist_adapter);
//    }

    private void fetchCollectionValue(final String username) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<CollectionValueResponse> call = discogsAPI.fetchCollectionValue(username);
        call.enqueue(new Callback<CollectionValueResponse>() {
            @Override
            public void onResponse(Call<CollectionValueResponse> call, Response<CollectionValueResponse> response) {
                if (response.body() != null) {
                    CollectionValueResponse collectionValueResponse = response.body();
                    updateCollectionValue(collectionValueResponse);
                }
            }

            @Override
            public void onFailure(Call<CollectionValueResponse> call, Throwable t) {
                Log.e("VALUE_CAT", t.getMessage());
            }
        });
    }

    private void fetchCollection(String username, int folder_id) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<CollectionResponse> call = discogsAPI.fetchCollection(username, folder_id, 5, 1);
        call.enqueue(new Callback<CollectionResponse>() {
            @Override
            public void onResponse(Call<CollectionResponse> call, Response<CollectionResponse> response) {
                if (response.body() != null) {
                    CollectionResponse collectionResponse = response.body();
                    updateCollection(collectionResponse);
                }
                Log.i("CODE_RESPONSE", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<CollectionResponse> call, Throwable t) {
                Log.e("COLLECTION_CAT", t.getMessage());
            }
        });
    }

    private void fetchProfile(String username) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ProfileResponse> call = discogsAPI.fetchProfile(username);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.body() != null) {
                    ProfileResponse profileResponse = response.body();
                    updateProfile(profileResponse);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Log.e("PROFILE_CAT", t.getMessage());
            }
        });
    }

    private DiscogsAPI getDiscogsAPI() {
        final String auth = getCredentials();
        // Define the interceptor, add authentication headers
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder onGoing = chain.request().newBuilder();
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

    private void navigateToFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
    public void onReleaseClick(int position, Release release, List<Release> releases, int artist_id) {

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
