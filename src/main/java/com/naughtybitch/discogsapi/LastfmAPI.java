package com.naughtybitch.discogsapi;

import com.naughtybitch.POJOlastfm.TopArtistsResponse;
import com.naughtybitch.POJOlastfm.TopTracksResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastfmAPI {

    /*
    page (Optional) : The page number to fetch. Defaults to first page.
    limit (Optional) : The number of results to fetch per page. Defaults to 50.
    api_key (Required) : A Last.fm API key.
    */
    @GET("2.0/?method=chart.gettopartists&format=json")
    Call<TopArtistsResponse> fetchTopArtist(@Query("api_key") String api_key, @Query("limit") int per_page, @Query("page") int page);

    /*
    page (Optional) : The page number to fetch. Defaults to first page.
    limit (Optional) : The number of results to fetch per page. Defaults to 50.
    api_key (Required) : A Last.fm API key.
    */

    @GET("2.0/?method=chart.gettoptracks&format=json")
    Call<TopTracksResponse> fetchTopTrack(@Query("api_key") String api_key, @Query("limit") int per_page, @Query("page") int page);

}
