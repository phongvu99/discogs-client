package com.naughtybitch.discogsclient;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.naughtybitch.POJO.MasterResponse;
import com.naughtybitch.adapter.SliderAdapter;
import com.naughtybitch.discogsapi.DiscogsAPI;
import com.naughtybitch.discogsapi.DiscogsClient;
import com.naughtybitch.discogsapi.RetrofitClient;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AlbumInfoActivity extends AppCompatActivity {

    int master_id;
    SliderAdapter sliderAdapter;
    SliderView sliderView;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_info);
        master_id = getIntent().getExtras().getInt("master_id");
        Log.i("master_id", "master_id " + master_id);
        initView();
        fetchData();
//        ImageAdapter adapter = new ImageAdapter(this);
//        ViewPager pager = (ViewPager) findViewById(R.id.pager);
//        pager.setOffscreenPageLimit(5);
//        pager.setAdapter(adapter);
    }

    public void initView() {
        sliderView = findViewById(R.id.slider_view);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
    }

    public void fetchData() {
        DiscogsClient instance = DiscogsClient.getInstance();
        Timestamp currentTimestamp = instance.currentTimeStamp();
        ArrayList<String> token = instance.getCredentials(this);
        final String auth = "OAuth oauth_consumer_key=\"" + instance.getConsumer_key() + "\", " +
                "oauth_nonce=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_token=\"" + token.get(0) + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_version=\"1.0\", " +
                "oauth_signature=\"" + instance.getConsumer_secret() + token.get(1) + "\"";

        Log.i("header", auth);

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

        DiscogsAPI discogsAPI = retrofit.create(DiscogsAPI.class);

        Call<MasterResponse> call = discogsAPI.fetchData(master_id);

        call.enqueue(new Callback<MasterResponse>() {
            @Override
            public void onResponse(Call<MasterResponse> call, Response<MasterResponse> response) {
                Log.i("status", "response code " + response.code());
                if (response.body() != null) {
                    MasterResponse masterResponse = response.body();
                    sliderAdapter = new SliderAdapter(context, masterResponse);
                    sliderView.setSliderAdapter(sliderAdapter);
                }
            }

            @Override
            public void onFailure(Call<MasterResponse> call, Throwable t) {
                Log.i("status", "response code " + t.getMessage());
            }
        });


    }

}
