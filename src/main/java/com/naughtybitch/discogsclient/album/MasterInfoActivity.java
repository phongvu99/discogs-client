package com.naughtybitch.discogsclient.album;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.POJO.ArtistReleasesResponse;
import com.naughtybitch.POJO.MasterReleasesResponse;
import com.naughtybitch.POJO.Release;
import com.naughtybitch.adapter.SliderAdapter;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.naughtybitch.discogsclient.R;
import com.naughtybitch.recyclerview.MoreByAdapter;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

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


public class MasterInfoActivity extends AppCompatActivity implements MoreByAdapter.OnMoreByListener {

    private int master_id;
    private List<String> genres, styles;
    private TextView title, artist, year, genre, style;
    private SliderAdapter sliderAdapter;
    private SliderView sliderView;
    private MoreByAdapter adapter;
    private RecyclerView recyclerView;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_info);
        master_id = getIntent().getExtras().getInt("master_id");
        Log.i("master_id", "master_id " + master_id);
        initView();
        fetchData();
    }

    public void initView() {
        genre = findViewById(R.id.master_genre);
        style = findViewById(R.id.master_style);
        title = findViewById(R.id.master_title);
        artist = findViewById(R.id.master_artist);
        year = findViewById(R.id.master_year);
        recyclerView = findViewById(R.id.rc_view);
        adapter = new MoreByAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        sliderView = findViewById(R.id.slider_view);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
    }

    public DiscogsAPI getDiscogsAPI() {
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

    public String getCredentials() {
        DiscogsClient instance = DiscogsClient.getInstance();
        Timestamp currentTimestamp = instance.currentTimeStamp();
        ArrayList<String> token = instance.getCredentials(this);
        String auth = "OAuth oauth_consumer_key=\"" + instance.getConsumer_key() + "\", " +
                "oauth_nonce=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_token=\"" + token.get(0) + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_version=\"1.0\", " +
                "oauth_signature=\"" + instance.getConsumer_secret() + token.get(1) + "\"";

        Log.i("header", auth);
        return auth;
    }

    public void fetchMoreBy(int artist_id) {
        DiscogsAPI discogsAPI = getDiscogsAPI();
        Call<ArtistReleasesResponse> call = discogsAPI.fetchArtistReleases(artist_id, "year", "asc"
                , 9, 1);

        call.enqueue(new Callback<ArtistReleasesResponse>() {
            @Override
            public void onResponse(Call<ArtistReleasesResponse> call, Response<ArtistReleasesResponse> response) {
                Log.i("status", "response code " + response.code());
                if (response.body() != null) {
                    ArtistReleasesResponse artistResponse = response.body();
                    List<Release> releases = artistResponse.getReleases();
                    adapter = new MoreByAdapter(context, releases, MasterInfoActivity.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArtistReleasesResponse> call, Throwable t) {
                Log.i("status", "response code " + t.getMessage());
            }
        });

    }

    public void updateView(MasterReleasesResponse masterResponse) {
        StringBuilder stringBuilder = null;
        try {
            styles = masterResponse.getStyles();

            stringBuilder = new StringBuilder();
            for (String style : styles) {
                stringBuilder.append(style);
                if (styles.indexOf(style) == styles.size() - 1) {
                    break;
                }
                stringBuilder.append(", ");
            }
            style.setText("Style: " + stringBuilder);
        } catch (NullPointerException e) {
            e.printStackTrace();
            style.setText("Style: " + "Freestyle");
            Log.i("damn", "damn");
        }
        stringBuilder.delete(0, stringBuilder.capacity());
        genres = masterResponse.getGenres();
        for (String genre : genres) {
            stringBuilder.append(genre);
            if (genres.indexOf(genre) == genres.size() - 1) {
                break;
            }
            stringBuilder.append(", ");
        }
        genre.setText("Genre: " + stringBuilder);
        title.setText("Title: " + masterResponse.getTitle());
        artist.setText("Artist: " + masterResponse.getArtists().get(0).getName());
        year.setText("Year: " + masterResponse.getYear());
    }

    public void fetchData() {
        DiscogsAPI discogsAPI = getDiscogsAPI();

        Call<MasterReleasesResponse> call = discogsAPI.fetchMasterData(master_id);

        call.enqueue(new Callback<MasterReleasesResponse>() {
            @Override
            public void onResponse(Call<MasterReleasesResponse> call, Response<MasterReleasesResponse> response) {
                Log.i("status", "response code " + response.code());
                if (response.body() != null) {
                    MasterReleasesResponse masterResponse = response.body();
                    int artist_id = masterResponse.getArtists().get(0).getId();
                    updateView(masterResponse);
                    fetchMoreBy(artist_id);
                    sliderAdapter = new SliderAdapter(context, masterResponse);
                    sliderView.setSliderAdapter(sliderAdapter);
                }
            }

            @Override
            public void onFailure(Call<MasterReleasesResponse> call, Throwable t) {
                Log.i("status", "response code " + t.getMessage());
            }
        });


    }

    @Override
    public void onReleaseClick(int position, Release release) {
        Log.i("release", "Release position " + position + "release title " + release.getTitle());
    }
}
