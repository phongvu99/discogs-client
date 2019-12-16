package com.naughtybitch.discogsapi;

import com.naughtybitch.POJO.ArtistReleasesResponse;
import com.naughtybitch.POJO.MasterReleasesResponse;
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
    Call<MasterReleasesResponse> fetchMasterData(@Path("master_id") int master_id);

    @GET("artists/{artist_id}/releases")
    Call<ArtistReleasesResponse> fetchArtistReleases(@Path("artist_id") int artist_id, @Query("sort") String sort, @Query("sort_order") String sort_order
            , @Query("per_page") int per_page, @Query("page") int page);

}
