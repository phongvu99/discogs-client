package com.naughtybitch.discogsapi;

import com.naughtybitch.POJO.MasterResponse;
import com.naughtybitch.POJO.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DiscogsAPI {

    /*
    Get request to issue a search query to the Discogs database. This
    endpoint accepts pagination parameters
     */
    @GET("database/search")
    Call<SearchResponse> getSearchResult(@Query("q") String query, @Query("per_page") int per_page, @Query("page") int page);
	
    @GET("masters/{master_id}")
    Call<MasterResponse> fetchData(@Path("master_id") int master_id);
	
}
